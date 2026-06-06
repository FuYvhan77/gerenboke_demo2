package com.hxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hxy.enums.ChatTypeEnum;
import com.hxy.enums.SSEMsgType;
import com.hxy.pojo.ChattingRecords;
import com.hxy.service.ChattingRecordsService;
import com.hxy.service.OllamaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author zhangyadong
 * @version 1.0
 * @ClassName OllamaServiceImpl
 * @date 2026-04-17 13:55
 */

@Service
@Slf4j
public class OllamaServiceImpl implements OllamaService {


    @Autowired
    private OllamaChatClient ollamaChatClient;

    @Autowired
    private ChattingRecordsService chattingRecordsService;

    /**
     * 在线人数统计：使用AtomicInteger保证原子操作，避免多线程计数误差
     */
    private static final AtomicInteger onlineCounts = new AtomicInteger(0);

    /**
     * 存储所有SSE连接：key=用户ID，value=该用户对应的SSE发射器（用于推送消息）
     * ConcurrentHashMap：保证多线程环境下的线程安全，避免连接管理异常
     */
    private static final Map<String, SseEmitter> sseClients = new ConcurrentHashMap<>();



    //创建链接接口
    @Override
    public SseEmitter connect(String userId) {
        // 初始化SSE发射器，设置超时时间为0L，表示永不过期（避免连接自动断开）
        SseEmitter sseEmitter = new SseEmitter(0L);

        // 注册连接生命周期回调：完成、错误、超时，统一处理连接关闭逻辑
        sseEmitter.onCompletion(completionCallback(userId));
        sseEmitter.onError(errorCallback(userId));
        sseEmitter.onTimeout(timeoutCallback(userId));

        // 将当前用户的SSE连接存入全局集合，便于后续推送消息
        sseClients.put(userId, sseEmitter);

        // ============= 建立连接后必须发一条消息   msg add=============
        sendMessage(userId, "SSE连接建立成功", SSEMsgType.MESSAGE);

        log.info("SSE连接创建成功，用户ID：");

        // 在线人数+1
        onlineCounts.incrementAndGet();

        return sseEmitter;
    }



    //发送消息接口
    private void sendMessage(String userId, String string, SSEMsgType sseMsgType) {
        SseEmitter sseEmitter = sseClients.get(userId);
        if (sseEmitter != null){
            try {
                sseEmitter.send(SseEmitter.event()
                        .id(userId)
                        .data(string)
                        .name(sseMsgType.type)
                        );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            log.error("没有建立链接");
        }
    }


    private Runnable timeoutCallback(String userId) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                System.out.println("用户" + userId + "连接超时");
                //在线人数-1
                onlineCounts.decrementAndGet();
                sseClients.remove(userId);
            }
        };
        return runnable;
    }

    private Consumer<Throwable> errorCallback(String userId) {
        Consumer<Throwable> consumer=new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                System.out.println("用户" + userId + "连接异常");
                //在线人数-1
                onlineCounts.decrementAndGet();
                sseClients.remove(userId);
            }
        };
        return consumer;
    }

    private Runnable completionCallback(String userId) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                System.out.println("用户" + userId + "连接完成");
            }
        };
        return runnable;
    }


    //对话方法
    @Override
    public void sendMsg(String userId, String message) {
        log.info("用户：{}，发送了消息：{}", userId, message);
        chattingRecordsService.add(userId, message, ChatTypeEnum.USER);
        //封装你问的问题
        Prompt prompt = new Prompt(new UserMessage(message));

        //问ai
        Flux<ChatResponse> flux = ollamaChatClient.stream(prompt);
        StringBuffer stringBuffer=new StringBuffer();
        //获取答案
        flux.toStream().map(s->{
            String content = s.getResult().getOutput().getContent();

            sendMessage(userId, content, SSEMsgType.ADD);

            stringBuffer.append(content);

            return content;
        }).collect(Collectors.toList() );
        log.info("用户：{}，AI回答：{}", userId, stringBuffer.toString());
        chattingRecordsService.add(userId, stringBuffer.toString(), ChatTypeEnum.BOT);
    }

    //获取历史记录
    @Override
    public List<ChattingRecords> history(String userId) {
        LambdaQueryWrapper<ChattingRecords> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChattingRecords::getUserName, userId);
        return chattingRecordsService.list(queryWrapper);
    }

    @Override
    public void stop(String userId) {
        SseEmitter sseEmitter = sseClients.get(userId);
        if (sseEmitter!=null) {
            sseEmitter.complete();
            //在线人数-1
            onlineCounts.decrementAndGet();
            sseClients.remove(userId);
        }
    }

    @Override
    public int onlineCount() {
        return onlineCounts.intValue();
    }
}


















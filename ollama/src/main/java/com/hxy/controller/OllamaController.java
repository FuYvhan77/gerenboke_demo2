package com.hxy.controller;


import com.hxy.pojo.ChattingRecords;
import com.hxy.service.OllamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RequestMapping("/ollama")
@RestController
public class OllamaController {

    @Autowired
    private OllamaService ollamaService;

    //TODO:建立链接
    @GetMapping(path = "/connect", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter connect(@RequestParam(value = "userId") String userId){
        return ollamaService.connect(userId);
    }


    //TODO:接收消息
    @GetMapping("/sendMessage")
    public void sendMessage(@RequestParam(value = "userId") String userId,
                            @RequestParam(value = "message") String message){
        ollamaService.sendMsg(userId,message);
    }

    //TODO:历史纪录
    @GetMapping("/history")
    public List<ChattingRecords> history(@RequestParam(value = "userId") String userId){
        return ollamaService.history(userId);
    }

    //TODO:断开连接
    @GetMapping("/stop")
    public void stop(@RequestParam(value = "userId") String userId){
        ollamaService.stop(userId);
    }



    //TODO:在线人数
    @GetMapping("/onlineCount")
    public int onlineCount(){
        return ollamaService.onlineCount();
    }
}

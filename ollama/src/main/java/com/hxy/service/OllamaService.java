package com.hxy.service;

import com.hxy.pojo.ChattingRecords;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface OllamaService {
    SseEmitter connect(String userId);

    void sendMsg(String userId, String message);

    List<ChattingRecords> history(String userId);

    void stop(String userId);

    int onlineCount();
}

package com.hxy.enums;

/**
 * SSE消息类型枚举，统一管理所有SSE事件名称
 * 用于服务端推送消息时指定事件类型，客户端对应监听相同的事件名称
 */
public enum SSEMsgType {

    MESSAGE("message", "单次发送的普通消息"),
    ADD("add", "消息追加，用于流式stream推送（如AI流式回答）"),
    FINISH("finish", "消息推送完成（标记流式推送结束）"),
    CUSTOM_EVENT("customEvent", "自定义消息类型（适配特定业务场景）"),
    DONE("done", "任务完成通知（如后台任务执行完毕）");

    // 事件名称（客户端监听的事件名，必须与服务端一致）
    public final String type;
    // 事件描述（用于代码注释和文档说明）
    public final String value;

    // 构造方法
    SSEMsgType(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
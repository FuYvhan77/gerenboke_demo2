package com.hxy.enums;

/**
 * 会话记录类型枚举
 */
public enum ChatTypeEnum {

    USER("user", "用户发送的内容"),
    BOT("bot", "AI回复的内容");

    public final String type;  // 数据库存储的类型值
    public final String value; // 类型描述

    ChatTypeEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
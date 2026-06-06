package com.hxy.exception;

import com.hxy.enums.AppHttpCodeEnum;

/**
 * @author zhangyadong
 * @version 1.0
 * @ClassName SystemException
 * @date 2026-04-09 9:00
 */


public class SystemException extends RuntimeException{
    private int code;
    private String msg;

    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }

    public int getCode() { return code; }
    public String getMsg() { return msg; }
}

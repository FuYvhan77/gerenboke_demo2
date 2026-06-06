package com.hxy.exception;

import com.hxy.enums.AppHttpCodeEnum;
import com.hxy.result.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zhangyadong
 * @version 1.0
 * @ClassName GlobalExceptionHandler
 * @date 2026-04-09 9:09
 */

@RestControllerAdvice//统一处理异常
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        //打印异常信息
        log.error("出现了异常！{}",e);
        if (e instanceof SystemException){
            SystemException systemException = (SystemException) e;
            return ResponseResult.errorResult(systemException.getCode(),systemException.getMsg());
        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
        }
    }

}

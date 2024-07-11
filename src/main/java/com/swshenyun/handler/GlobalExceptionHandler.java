package com.swshenyun.handler;


import com.swshenyun.common.BaseResponse;
import com.swshenyun.exception.BaseException;
import com.swshenyun.utils.ResultUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public BaseResponse exceptionHandler(BaseException ex) {
        return ResultUtils.error(ex.getCode(), ex.getMessage());
    }

//    @ExceptionHandler
//    public BaseResponse exceptionHandler(SQLIntegrityConstraintViolationException ex) {
//
//        String message = ex.getMessage();
//        if (message.contains("Duplicate entry")) {
//            String username = message.split(" ")[2];
//            String msg = username + MessageConstant.ALREADY_EXISTS;
//            return Result.error(msg);
//        }else {
//            return Result.error(MessageConstant.UNKNOWN_ERROR);
//        }
//    }
}

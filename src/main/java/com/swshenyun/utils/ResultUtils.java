package com.swshenyun.utils;

import com.swshenyun.common.BaseResponse;
import com.swshenyun.common.ErrorCode;

/**
 * 返回工具类
 */
public class ResultUtils {

    /**
     * 成功，无数据
     * @return
     */
    public static BaseResponse success(){
        return new BaseResponse(200, "");
    }

    /**
     * 成功，有数据
     * @param data
     * @return
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<T>(200, data);
    }

    /**
     * 失败，自定义返回内容
     * @param code
     * @param message
     * @return
     */
    public static BaseResponse error(int code, String message){
        return new BaseResponse(0, message);
    }

    /**
     * 失败，错误码
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse(errorCode.getCode(), errorCode.getMessage());
    }
}

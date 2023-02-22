package com.findPartner.domain.R;

import com.findPartner.common.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据通用返回类
 */
@Data
public class ResultData<T> implements Serializable {
    private static final long serialVersionUID = 31527887237995008L;

    private int code;
    private T data;
    private String message;

    /**
     * 当成功的时候，我们可以调用这个构造函数去自定义code，而不是直接调用success函数，防止code的僵化
     */
    public ResultData(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    /**
     * 失败的时候，我们返回一个错误码和失败的信息
     */
    public ResultData(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 当成功的时候，我们可以调用这个构造函数，并且不用返回任何的信息
     */
    public ResultData(int code, T data) {
        this.code = code;
        this.data = data;
    }

    /**
     * 出现异常的时候，ExceptionAdvice 可以调用这个构造方法利用ErrorCode去自定义一个信息返回给前端
     *
     * @param errorCode
     */
    public ResultData(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> ResultData<T> success(T data, String message) {
        return new ResultData<>(0, data, message);
    }

    public static <T> ResultData<T> fail(String message) {
        return new ResultData(10000, message);
    }
}

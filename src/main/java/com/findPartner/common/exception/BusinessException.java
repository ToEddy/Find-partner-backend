package com.findPartner.common.exception;


/**
 * 自定义业务异常类
 * final修饰成员变量：该成员变量必须在其所在类对象创建之前被初始化（且只能被初始化一次）。
 *
 * @author eddy
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -4459376314121780306L;

    private final int code;
    private final String description;

    public BusinessException(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public BusinessException(int code, String message, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

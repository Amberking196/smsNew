package com.server.module.trade.exception;


/**
 * 封装的异常
 *
 * @author yjr
 */
public class MyException extends RuntimeException {

    private Integer code;

    private String message;

    public MyException(ServiceExceptionEnum serviceExceptionEnum) {
        this.code = serviceExceptionEnum.getCode();
        this.message = serviceExceptionEnum.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

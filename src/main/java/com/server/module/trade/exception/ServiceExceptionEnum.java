package com.server.module.trade.exception;

/**
 * 抽象接口
 *
 * @author yjr
 */
public interface ServiceExceptionEnum {

    /**
     * 获取异常编码
     */
    Integer getCode();

    /**
     * 获取异常信息
     */
    String getMessage();
}

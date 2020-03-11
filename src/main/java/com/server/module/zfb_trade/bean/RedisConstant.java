package com.server.module.zfb_trade.bean;

public interface RedisConstant {

	/**
	 * 登录用户
	 */
	String CUSTOMER_ALIUSER = "customer_aliUser";
	
	String TOKEN_PREFIX = "token_";
	/**
	 * 令牌有效期 (半小时)
	 */
	Integer TOKEN_EXPIRE = 1800;
	/**
	 * 手机验证有效时间
	 */
	Integer PHONE_VERIFY_EXPIRE = 60;
	/**
	 * 最大超时时间
	 */
	Integer MAX_TIME_OUT = 15;
}

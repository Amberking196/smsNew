package com.server.module.trade.auth.controller.dto;

import java.io.Serializable;

/**
 * 认证的响应结果
 *
 * @author fengshuonan
 * @Date 2017/8/24 13:58
 */
public class AuthResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    /**
     * jwt token
     */
    private final String token;

    /**
     * 用于客户端混淆md5加密
     */
    private final String randomKey;
    
    private final Integer registerCoupon;

    public AuthResponse(String token, String randomKey,Integer registerCoupon) {
        this.token = token;
        this.randomKey = randomKey;
        this.registerCoupon = registerCoupon;
    }

    public String getToken() {
        return this.token;
    }

    public String getRandomKey() {
        return randomKey;
    }

	public Integer getRegisterCoupon() {
		return registerCoupon;
	}
    
    
}

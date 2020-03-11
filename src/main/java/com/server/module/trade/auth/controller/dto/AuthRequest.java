package com.server.module.trade.auth.controller.dto;

import com.server.module.trade.auth.validator.dto.Credence;

/**
 * 认证的请求dto
 */
public class AuthRequest implements Credence {

    private String userName;
    private String password;
    
    private String openId;
    
    private Integer type;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String getCredenceName() {
        return this.userName;
    }

    @Override
    public String getCredenceCode() {
        return this.password;
    }

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
    
    
}



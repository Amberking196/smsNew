package com.server.module.trade.order.vo;

import java.util.Date;

import lombok.Data;

@Data
public class MessageVo {
	private Integer doorNo;
	private String vmCode;
	private String address;
	private Date createTime;
	private String phone;
	private String principalPhone;
}


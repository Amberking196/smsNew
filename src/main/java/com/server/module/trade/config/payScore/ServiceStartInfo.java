package com.server.module.trade.config.payScore;

import lombok.Data;

@Data
public class ServiceStartInfo {

	private String id;
	private String createTime;
	private String event_type;//通知类型 PAYSCORE.USER_OPEN_SERVICE
	private String resource_type;//资源类型 encrypt-resource
	private ResourceInfo resource;
}

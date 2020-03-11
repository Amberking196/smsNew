package com.server.module.zfb_trade.util.enumparam;

public enum OpenTypeEnum {

	CONSUME(1,"消费开门"),
	OPERATE(2,"运维开门")
	;
	
	private Integer code;
	private String message;

	private OpenTypeEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
	public Integer getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
}

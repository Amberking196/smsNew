package com.server.module.zfb_trade.util.enumparam;

public enum DoorStateEnum {


	AVAILABLE(30001,"货道正常"),
	UNAVAILABLE(30003,"货道不可用")
	;
	
	private Integer code;
	private String message;

	private DoorStateEnum(Integer code, String message) {
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

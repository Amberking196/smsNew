package com.server.module.zfb_trade.util.enumparam;

public enum MachineEventEnum {


	IDLE(0,"机器空闲"),
	OPENING(1,"正在开门"),
	OPENED(2,"门已打开"),
	CLOSED(3,"门已关上"),
	OPEN_FAILED(4,"打开失败"),
	ERROR(5,"错误，置货道为不可用"),
	;
	
	private Integer code;
	private String message;

	private MachineEventEnum(Integer code, String message) {
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

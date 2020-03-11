package com.server.module.zfb_trade.util.enumparam;


public enum DoorOperateEventEnum {


	WAIT(1,"等待开门"),
	OPENED(2,"开门成功"),
	CLOSED(3,"关闭成功"),
	FAILED(4,"开门失败"),
	ERROR(5,"货道错误"),
	;
	
	private Integer code;
	private String message;

	private DoorOperateEventEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
	public static String getMessage(int code) {
		for (DoorOperateEventEnum c : DoorOperateEventEnum.values()) {
			if (c.getCode() == code) {
				return c.getMessage();
			}
		}
		return null;
	}
	public Integer getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
}

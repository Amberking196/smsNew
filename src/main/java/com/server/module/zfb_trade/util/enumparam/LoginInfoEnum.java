package com.server.module.zfb_trade.util.enumparam;

public enum LoginInfoEnum {

	LOGIN_STATUS_INVALID(0,"禁用"),
	LOGIN_STATUS_AVAILABLE(1,"可用"),
	LOGIN_STATUS_DELETE(2,"公司已禁用"),
	
	// 非负责人
	NO_PRINCIPAL(3,"0"),
	// 负责人
	PRINCIPAL(4,"1")
	;
	private Integer code;
	private String message;
	/**
	 * @param code
	 * @param message
	 */
	private LoginInfoEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
}

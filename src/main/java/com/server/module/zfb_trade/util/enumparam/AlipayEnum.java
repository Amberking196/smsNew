package com.server.module.zfb_trade.util.enumparam;

public enum AlipayEnum{
	// 成功
	SUCCESS(1,"T"),
	// 失败
	FAIL(2,"F");
	
	private Integer code;
	private String message;
	/**
	 * @param code
	 * @param message
	 */
	private AlipayEnum(Integer code, String message) {
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

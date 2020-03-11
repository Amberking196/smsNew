package com.server.module.zfb_trade.util.enumparam;

public enum CustomerEnum {

	ACCOUNT_TYPE_WECHAT(1,"微信"),
	ACCOUNT_TYPE_ALI(2,"支付宝"),
	SEX_MALE (1, "M"),
	SEX_FEMALE (2, "F"),
	// 已实名制
	CERTIFIED (3,"T"),
	// 未实名制
    NOT_CERTIFIED(4,"F")
	;
	
	private Integer code;
	private String message;
	/**
	 * @param code
	 * @param message
	 */
	private CustomerEnum(Integer code, String message) {
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

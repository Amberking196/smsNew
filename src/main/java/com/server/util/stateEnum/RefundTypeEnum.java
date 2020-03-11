package com.server.util.stateEnum;

public enum RefundTypeEnum {
	
	REFUND_WX_SUCCESS_PAY_SUCCESS(1,"微信支付成功，后台状态支付成功，退款"),
	REFUND_WX_SUCCESS_PAY_FAIL(2,"微信支付成功，后台状态支付失败，退款"),
	REFUND_WX_SUCCESS_PAY_MULTIPLE(3,"同一订单支付多次成功，退款")
	;

	private Integer state;
	private String name;
	
	RefundTypeEnum(Integer state,String name){
		this.state = state;
		this.name = name;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}

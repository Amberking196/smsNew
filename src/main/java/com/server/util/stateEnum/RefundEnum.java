package com.server.util.stateEnum;

public enum RefundEnum {

	GROUP_BUY_SUCCESS(1,"拼团成功"),
	GROUP_BUY_FAIL(0,"拼团失败"),
	GROUP_BUYING(2,"拼团中")
	
	;
	
	private Integer state;
	private String name;
	RefundEnum(Integer state,String name){
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

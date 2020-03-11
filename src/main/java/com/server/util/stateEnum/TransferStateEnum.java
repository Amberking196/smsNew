package com.server.util.stateEnum;

public enum TransferStateEnum {

	SUCCESS("0","成功"),
	ERROR("1","失败")
	;
	private String state;
	private String stateName;
	
	TransferStateEnum(String state,String stateName){
		this.state = state;
		this.stateName = stateName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	
}

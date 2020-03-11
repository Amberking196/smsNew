package com.server.util.stateEnum;

public enum ModeEnum {

	LOGIN_MODE(1,"登录模式"),
	CHANGE_STOCK_MODE(2,"取/补货模式"),
	REVISE_MODE(3,"校准模式"),
	REVISE_ALL_MODE(4,"校准所有"),
	RESTART_MODE(5,"重启模式"),
	UPDATE_VOLUME_MODE(6,"调整音量"),
	ONCE_REVISE_MODE(7,"一次校准模式"),

	ELSE_MODE(8,"其他模式")
	;
	private Integer state;
	private String name;
	
	ModeEnum(Integer state,String name){
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

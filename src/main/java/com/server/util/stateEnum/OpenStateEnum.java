package com.server.util.stateEnum;

public enum OpenStateEnum {

	REOPEN_FAIL("2","请稍后，当前有用户正在使用"),
	OPEN_FAIL("1","开门失败"),
	OPEN_SUCCESS("0","开门成功"),
	NEW_OPEN_SUCCESS("3","开门成功"),
	NOT_HEART("4","机器无心跳，无法开门"),
	MACHINES_BREAK_DOWN("5","机器故障，无法开门"),
	MACHINES_BUSY("6","机器繁忙，请5秒后重试")
	;
	private String state;
	private String name;
	
	
	
	public String getState() {
		return state;
	}



	public String getName() {
		return name;
	}


	OpenStateEnum(String state,String name){
		this.state = state;
		this.name = name;
	}
}

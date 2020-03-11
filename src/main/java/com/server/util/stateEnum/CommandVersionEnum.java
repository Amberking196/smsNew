package com.server.util.stateEnum;

public enum CommandVersionEnum {

	VER1(1,"Ver.1.001-20180601"),
	VER2(2,"Ver.2.001-20180827");
	
	private Integer state;
	private String version;
	
	
	public Integer getState() {
		return state;
	}


	public String getVersion() {
		return version;
	}


	CommandVersionEnum(Integer state,String version){
		this.state = state;
		this.version = version;
	}
}

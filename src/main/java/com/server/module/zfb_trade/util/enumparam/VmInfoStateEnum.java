package com.server.module.zfb_trade.util.enumparam;

public enum VmInfoStateEnum {

	MACHINES_NORMAL(20001,"售货机正常"),
	MACHINES_UNINITIALIZED(20002,"售货机未初始化"),
	MACHINES_BREAKDOWN(20003,"售货机损坏"),
	MACHINES_NOT_RUNNING(20004,"售卖机未投入使用"),
	MACHINES_RETURN_FACTORY(20005,"售卖机返厂"),
	MACHINES_RUNNING(20006,"售卖机正常运行");
	
	private Integer code;
	private String message;
	
	
	
	public Integer getCode() {
		return code;
	}



	public void setCode(Integer code) {
		this.code = code;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	VmInfoStateEnum(Integer code,String message){
		this.code = code;
		this.message = message;
	}
}

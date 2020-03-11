package com.server.util.stateEnum;

public enum MerchantTypeEnum {

	MERCHANT(0,"普通商户"),
	FACILITATOR(1,"服务商");
	public Integer type;
	public String name;
	
	MerchantTypeEnum(Integer type , String name){
		this.type = type;
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public String getName() {
		return name;
	}
	
}

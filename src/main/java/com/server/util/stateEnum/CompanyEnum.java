package com.server.util.stateEnum;

public enum CompanyEnum {

	ZHONGBU(1,"总部"),
	YOUSHUI(149,"优水"),
	GUANGZHOU_YOUSHUI(76,"优水到家"),
	WUHAN_YOUSHUI(113,"武汉优水"),
	YASHENGHUO(123,"雅生活");
	private Integer companyId;
	private String companyName;
	
	
	
	public Integer getCompanyId() {
		return companyId;
	}



	public String getCompanyName() {
		return companyName;
	}



	CompanyEnum(Integer companyId,String companyName){
		this.companyId = companyId;
		this.companyName = companyName;
	}
}

package com.server.module.zfb_trade.module.vminfo;

public class VminfoDto {

	/**
	 * 售货机编号
	 */
	private String vmCode;
	/**
	 * 售货机位置
	 */
	private String locationName;
	//公司名称
	private String companyName;
	
	private Integer companyId;

	
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
}

package com.server.module.coupon;
/**
 * 机器线路id，区域id，公司id
 * @author hebiting
 *
 */
public class MachinesLAC {

	private Integer companyId;
	private Integer areaId;
	private Integer lineId;
	private String vmCode;
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public Integer getLineId() {
		return lineId;
	}
	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	
	
}

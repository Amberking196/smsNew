package com.server.module.zfb_trade.module.vmbase;

public class VmbaseInfoDto {

	//机器编号
	private String vmCode;
	//出厂编号
	private String factoryNumber;
	//总货道数
	private Integer totalWayNum;
	//机器版本
	private Integer machinesVersion;
	//公司负责人电话
	private String principalPhone;

	private Integer freeMachine;

	public Integer getFreeMachine() {
		return freeMachine;
	}

	public void setFreeMachine(Integer freeMachine) {
		this.freeMachine = freeMachine;
	}

	public String getPrincipalPhone() {
		return principalPhone;
	}
	public void setPrincipalPhone(String principalPhone) {
		this.principalPhone = principalPhone;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public String getFactoryNumber() {
		return factoryNumber;
	}
	public void setFactoryNumber(String factoryNumber) {
		this.factoryNumber = factoryNumber;
	}
	public Integer getTotalWayNum() {
		return totalWayNum;
	}
	public void setTotalWayNum(Integer totalWayNum) {
		this.totalWayNum = totalWayNum;
	}
	public Integer getMachinesVersion() {
		return machinesVersion;
	}
	public void setMachinesVersion(Integer machinesVersion) {
		this.machinesVersion = machinesVersion;
	}
	
	
}

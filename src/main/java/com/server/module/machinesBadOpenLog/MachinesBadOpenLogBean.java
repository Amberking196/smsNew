package com.server.module.machinesBadOpenLog;

import java.util.Date;



public class MachinesBadOpenLogBean {

	private Long id;
	private String vmCode;
	private Long customerId;
	private Date createTime;
	
	public MachinesBadOpenLogBean(){}
	public MachinesBadOpenLogBean(String vmCode,Long customerId,Date createTime){
		this.vmCode = vmCode;
		this.customerId = customerId;
		this.createTime = createTime;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}

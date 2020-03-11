package com.server.module.payRecord;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class PayRecordForm {

	private String payCodeOrName;
	private Integer companyId;
	private String companyIds;
	private String vmCode;
	private Integer state;
	private Long customerId;
	private Long dutyId;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startDate;
	@DateTimeFormat(pattern="yyyy-MM-dd")	
	private Date endDate;
	
	private String orderType;//订单类型
	
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getPayCodeOrName() {
		return payCodeOrName;
	}
	public void setPayCodeOrName(String payCodeOrName) {
		this.payCodeOrName = payCodeOrName;
	}
	public Long getDutyId() {
		return dutyId;
	}
	public void setDutyId(Long dutyId) {
		this.dutyId = dutyId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getCompanyIds() {
		return companyIds;
	}
	public void setCompanyIds(String companyIds) {
		this.companyIds = companyIds;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}

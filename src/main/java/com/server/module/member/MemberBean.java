package com.server.module.member;

import java.util.Date;

public class MemberBean {

	private Long customerId;
	private Integer isMember;
	private Integer memberTypeId;
	private Double discount;
	private Date startTime;
	private Date endTime;
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Integer getIsMember() {
		return isMember;
	}
	public void setIsMember(Integer isMember) {
		this.isMember = isMember;
	}
	public Integer getMemberTypeId() {
		return memberTypeId;
	}
	public void setMemberTypeId(Integer memberTypeId) {
		this.memberTypeId = memberTypeId;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
}

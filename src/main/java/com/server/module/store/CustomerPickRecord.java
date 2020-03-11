package com.server.module.store;

import java.util.Date;

public class CustomerPickRecord {

	private Long id;
	
	private Long customerId;
	
	private Long basicItemId;
	
	private Integer wayNum;
	
	private Integer pickNum;
	
	private String vmCode;
	
	private Date createTime;

	
	public Long getBasicItemId() {
		return basicItemId;
	}

	public void setBasicItemId(Long basicItemId) {
		this.basicItemId = basicItemId;
	}

	public Integer getWayNum() {
		return wayNum;
	}

	public void setWayNum(Integer wayNum) {
		this.wayNum = wayNum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Integer getPickNum() {
		return pickNum;
	}

	public void setPickNum(Integer pickNum) {
		this.pickNum = pickNum;
	}

	public String getVmCode() {
		return vmCode;
	}

	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}

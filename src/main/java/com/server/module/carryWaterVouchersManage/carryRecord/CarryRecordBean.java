package com.server.module.carryWaterVouchersManage.carryRecord;

import java.util.Date;

public class CarryRecordBean {
	
	private Long id;
	private Long carryId;
	private Long customerId;
	private Long orderId;
	private Integer num;
	private Date createTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCarryId() {
		return carryId;
	}
	public void setCarryId(Long carryId) {
		this.carryId = carryId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	

}

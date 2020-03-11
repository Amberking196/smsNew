package com.server.module.carryWaterVouchersManage.carryWaterVouchers;

import java.util.Date;

public class CarryDto {

	private Long carryId;
	private Long carryCustomerId;
	private Integer carryNum;
	private String carryName;
	private Date endTime;
	private Long orderId;
	
	
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getCarryName() {
		return carryName;
	}
	public void setCarryName(String carryName) {
		this.carryName = carryName;
	}
	public Long getCarryCustomerId() {
		return carryCustomerId;
	}
	public void setCarryCustomerId(Long carryCustomerId) {
		this.carryCustomerId = carryCustomerId;
	}
	public Long getCarryId() {
		return carryId;
	}
	public void setCarryId(Long carryId) {
		this.carryId = carryId;
	}
	public Integer getCarryNum() {
		return carryNum;
	}
	public void setCarryNum(Integer carryNum) {
		this.carryNum = carryNum;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	@Override
	public int hashCode() {
		int result = 17;
		result = result*31 + this.carryId.hashCode();
		result = result*31 + this.carryCustomerId.hashCode();
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		// object must be Test at this point
		CarryDto carry = (CarryDto) obj;
		boolean result = this.carryId.equals(carry.getCarryId()) && this.carryCustomerId.equals(carry.getCarryCustomerId());
		return result;
	}
	
	
}

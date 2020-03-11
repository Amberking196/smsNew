package com.server.module.refund;

import java.math.BigDecimal;
import java.util.Date;

public class RefundOrderInfo {

	private Long orderId;
	private BigDecimal price;
	private Integer companyId;
	private String ptCode;
	private String itemName;
	private Long customerId;
	private Long friendCustomerId;
	private Date payTime;
	
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getPtCode() {
		return ptCode;
	}

	public void setPtCode(String ptCode) {
		this.ptCode = ptCode;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getFriendCustomerId() {
		return friendCustomerId;
	}

	public void setFriendCustomerId(Long friendCustomerId) {
		this.friendCustomerId = friendCustomerId;
	}

}

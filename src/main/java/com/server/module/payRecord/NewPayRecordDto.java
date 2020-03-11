package com.server.module.payRecord;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class NewPayRecordDto {

	private Long orderId;
	private Long customerId;
	private String payCode;
	// 支付宝订单号
	private String ptCode;
	// 支付状态
	private String state;
	private String stateName;
	// 订单创建时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	// 总价
	private BigDecimal totalPrice;
	// 数量
	private Integer num;
	
	private String allItemName;
	private String vmCode;
	//余额支付
	private BigDecimal useMoney;
	private List<PayRecordItemDto> itemList = new ArrayList<PayRecordItemDto>();

	
	
	public BigDecimal getUseMoney() {
		return useMoney;
	}

	public void setUseMoney(BigDecimal useMoney) {
		this.useMoney = useMoney;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getAllItemName() {
		return allItemName;
	}

	public void setAllItemName(String allItemName) {
		this.allItemName = allItemName;
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

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getPtCode() {
		return ptCode;
	}

	public void setPtCode(String ptCode) {
		this.ptCode = ptCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}


	public String getVmCode() {
		return vmCode;
	}

	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}

	public List<PayRecordItemDto> getItemList() {
		return itemList;
	}

	public void setItemList(List<PayRecordItemDto> itemList) {
		this.itemList = itemList;
	}

//	@Override
//	public int hashCode() {
//		int result = 17;
//		result = result*31 + this.orderId.hashCode();
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if ((obj == null) || (obj.getClass() != this.getClass()))
//			return false;
//		NewPayRecordDto payRecord = (NewPayRecordDto)obj;
//		boolean result = this.orderId.equals(payRecord.getOrderId());
//		return result;
//	}
//
//
//	@Override
//	public int compareTo(NewPayRecordDto o) {
//		return -this.orderId.compareTo(o.getOrderId());
//	}
//	
}

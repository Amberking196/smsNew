package com.server.module.refund;

import java.math.BigDecimal;
import java.util.List;

public class RefundDto {
	private String adminToken;
	private Integer refundType;//1：支付成功后台状态成功退款，2：支付成功但是后台状态未变退款，3：同一订单多次支付退款
	private Integer orderType;//1:机器普通订单；2：商城普通订单；3:拼团订单
	private String reason;
	private String vmCode;
	private Integer companyId;
	private String payCode;
	private String ptCode;
	private String itemName;
	private BigDecimal price;
	private BigDecimal refundPrice;
	private String phone;
	private String phoneCode;
	private List<RefundItemDto> itemList;
	// 退款类型 0.自动退款 1.拍错了 2.取货不方便
	private Integer refundGenre;
	//退款金额类型 0 全额退款 1部分退款
	private Integer refundMoneyType;
	//退款数量
	private Integer refundNum;
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public String getAdminToken() {
		return adminToken;
	}
	public void setAdminToken(String adminToken) {
		this.adminToken = adminToken;
	}
	public BigDecimal getRefundPrice() {
		return refundPrice;
	}
	public void setRefundPrice(BigDecimal refundPrice) {
		this.refundPrice = refundPrice;
	}
	public Integer getRefundType() {
		return refundType;
	}
	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhoneCode() {
		return phoneCode;
	}
	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public List<RefundItemDto> getItemList() {
		return itemList;
	}
	public void setItemList(List<RefundItemDto> itemList) {
		this.itemList = itemList;
	}
	public Integer getRefundGenre() {
		return refundGenre;
	}
	public void setRefundGenre(Integer refundGenre) {
		this.refundGenre = refundGenre;
	}
	public Integer getRefundMoneyType() {
		return refundMoneyType;
	}
	public void setRefundMoneyType(Integer refundMoneyType) {
		this.refundMoneyType = refundMoneyType;
	}
	public Integer getRefundNum() {
		return refundNum;
	}
	public void setRefundNum(Integer refundNum) {
		this.refundNum = refundNum;
	}
	
	
}

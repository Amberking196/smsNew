package com.server.module.trade.order;

import java.math.BigDecimal;

public class OrderResultBean {

	private String price;
	private String couponId;
	private String couponName;
	private String sumDeductionMoney;
	private String orderId;
	private String carryWaterCouponName;
	private String type;//1优惠券  2提水券
	private Integer follow;
	private BigDecimal memberMoney;
	private String payType;//1  余额  null 其他
	private String payState;//0 数量超过10 1正常
	

	public String getPayState() {
		return payState;
	}
	public void setPayState(String payState) {
		this.payState = payState;
	}
	public BigDecimal getMemberMoney() {
		return memberMoney;
	}
	public void setMemberMoney(BigDecimal memberMoney) {
		this.memberMoney = memberMoney;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public String getSumDeductionMoney() {
		return sumDeductionMoney;
	}
	public void setSumDeductionMoney(String sumDeductionMoney) {
		this.sumDeductionMoney = sumDeductionMoney;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCarryWaterCouponName() {
		return carryWaterCouponName;
	}
	public void setCarryWaterCouponName(String carryWaterCouponName) {
		this.carryWaterCouponName = carryWaterCouponName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getFollow() {
		return follow;
	}
	public void setFollow(Integer follow) {
		this.follow = follow;
	}

	
	
}

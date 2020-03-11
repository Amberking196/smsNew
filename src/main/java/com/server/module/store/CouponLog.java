package com.server.module.store;

import java.util.Date;

public class CouponLog {

	// 自增id
	private Integer id;
	// 优惠券id
	private Long couponId;
	// 优惠券对应的顾客id
	private Long couponCustomerId;
	// 订单id
	private Long orderId;
	// 订单类型
	private Integer type;
	// 订单金额
	private Double money;
	// 减扣金额
	private Double deductionMoney;
	// 创建时间
	private Date createTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getCouponId() {
		return couponId;
	}
	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}
	public Long getCouponCustomerId() {
		return couponCustomerId;
	}
	public void setCouponCustomerId(Long couponCustomerId) {
		this.couponCustomerId = couponCustomerId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Double getDeductionMoney() {
		return deductionMoney;
	}
	public void setDeductionMoney(Double deductionMoney) {
		this.deductionMoney = deductionMoney;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}

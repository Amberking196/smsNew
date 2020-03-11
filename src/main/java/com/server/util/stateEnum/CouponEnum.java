package com.server.util.stateEnum;

public enum CouponEnum {

	PURCHASE_COUPON(1,"购买返券"),
	SELF_COUPON(2,"自助领券"),
	ACTIVITY_COUPON(3,"活动赠券"),
	REGISTER_COUPON(4,"注册赠券"),
	BUY_COUPON(5,"可购买的优惠券"),
	FOLLOW_COUPON(6,"关注赠券"),
	Member_COUPON(7,"会员赠券"),
	INVITE_COUPON(8,"邀请赠券"),
	
	SHOPREGISTER_COUPON(9,"商城注册赠券"),
	
	USE_STORE(2,"商城优惠券"),
	USE_MACHINES(1,"机器优惠券"),
	
	TARGET_COMPANY(1,"公司"),
	TARGET_AREA(2,"区域"),
	TARGET_VMCODE(3,"机器"),
	
	NEW_COUPON(0,"新建未领取"),
	RECEIVE_COUPON(1,"已领取"),
	USED_COUPON(2,"已使用")
	;
	
	
	private Integer state;
	private String name;
	public Integer getState() {
		return state;
	}
	public String getName() {
		return name;
	}
	
	CouponEnum(Integer state ,String name){
		this.state = state;
		this.name = name;
	}
}

package com.server.util.stateEnum;

public enum GamePrizeTypeEnum {

	NOTHING(0,"无奖励","sendNoRewardProcessor"),
	INTEGRAL(1,"积分","sendIntegralProcessor"),
	COUPON(2,"优惠券","sendCouponProcessor"),
	GOODS(3,"商品奖励","sendGoodsProcessor"),
	CASH(4,"现金奖励","sendCashProcessor"),
	STOREGOODS(5,"提水券","sendCarryWaterProcessor");
	
	private Integer state;
	private String name;
	private String beanName;
	
	
	
	public Integer getState() {
		return state;
	}



	public String getName() {
		return name;
	}
	

	public String getBeanName() {
		return beanName;
	}



	GamePrizeTypeEnum(Integer state , String name , String beanName){
		this.state = state;
		this.name = name;
		this.beanName = beanName;
	}
	
	public static GamePrizeTypeEnum getType(Integer state){
		GamePrizeTypeEnum[] values = GamePrizeTypeEnum.values();
		for (GamePrizeTypeEnum type : values) {
			if(type.getState().equals(state)){
				return type;
			}
		}
		return null;
	}
}

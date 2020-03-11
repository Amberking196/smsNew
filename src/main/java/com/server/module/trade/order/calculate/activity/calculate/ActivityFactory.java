package com.server.module.trade.order.calculate.activity.calculate;


public class ActivityFactory {
	
	private static ActivityCalculate buySend = new BuySendActivityCalculate();
	private static ActivityCalculate discount = new DiscountActivityCalculate();
	private static ActivityCalculate percent = new PercentActivityCalculate();
	private static ActivityCalculate xtongYzhe = new XtongYzheActivityCalculate();

	public static ActivityCalculate getActivityCalculate(Integer type){
		ActivityCalculate activityCalculate = null;
		if(type == 5){
			activityCalculate = xtongYzhe;
		}else if(type == 4){
			//买x送y活动计算
			activityCalculate = buySend;
		}else if(type == 3){
			//折扣券计算
			activityCalculate = percent;
		} else {
			//满减券固减券计算
			activityCalculate = discount;
		}
		return activityCalculate;
	}
}

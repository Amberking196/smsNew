package com.server.module.trade.order.calculate.coupon.anTong.calculate;

import org.springframework.stereotype.Component;

@Component
public class AnTongCouponFactory {
	
	private AnTongCouponCalculate discount = new DiscountAnTongCouponCalculate();
	private AnTongCouponCalculate maxDiscount = new MaxDiscountAnTongCouponCalculate();
	private AnTongCouponCalculate percent = new PercentAnTongCouponCalculate();

	
	public AnTongCouponCalculate getAnTongCouponCalcualte(Integer type){
		AnTongCouponCalculate anTongCouponCalculate = null;
		if(type == 4){
			//固定折扣券计算
			anTongCouponCalculate = maxDiscount;
		}else if(type == 3){
			//折扣券计算
			anTongCouponCalculate = percent;
		} else {
			//满减券固减券计算
			anTongCouponCalculate = discount;
		}
		return anTongCouponCalculate;
	}
}

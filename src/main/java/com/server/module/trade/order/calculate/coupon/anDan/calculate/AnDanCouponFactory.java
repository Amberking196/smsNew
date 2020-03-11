package com.server.module.trade.order.calculate.coupon.anDan.calculate;

import org.springframework.stereotype.Component;

@Component
public class AnDanCouponFactory{
	
	private AnDanCouponCalculate discount = new DiscountAnDanCouponCalculate();
	private AnDanCouponCalculate maxDiscount = new MaxDiscountAnDanCouponCalculate();
	private AnDanCouponCalculate percent = new PercentAnDanCouponCalculate();
	
	
	public AnDanCouponCalculate getNormalAnDanCouponCalcualte(Integer type){
		AnDanCouponCalculate anDanCouponCalculate = null;
		if(type == 4){
			//固定折扣券计算
			anDanCouponCalculate = maxDiscount;
		}else if(type == 3){
			//折扣券计算
			anDanCouponCalculate = percent;
		} else {
			//满减券固减券计算
			anDanCouponCalculate = discount;
		}
		return anDanCouponCalculate;
	}
}

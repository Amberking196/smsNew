package com.server.module.trade.order.calculate.coupon.anTong.calculate;

import java.math.BigDecimal;
/**
 * 按桶固减满减券计算
 * @author hebiting
 *
 */
public class DiscountAnTongCouponCalculate implements AnTongCouponCalculate{

	@Override
	public BigDecimal calculate(BigDecimal price, BigDecimal couponMoney, Double maxFavMoney) {
		BigDecimal finalPrice = null;
		//满减券固减券计算
		if(price.compareTo(couponMoney) >= 0){
			finalPrice = price.subtract(couponMoney);
		}else {
			finalPrice = new BigDecimal(0);
		}
		return finalPrice;
	}

}

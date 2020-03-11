package com.server.module.trade.order.calculate.coupon.anTong.calculate;

import java.math.BigDecimal;
/**
 * 按桶固定折扣优惠券计算
 * @author hebiting
 *
 */
public class MaxDiscountAnTongCouponCalculate implements AnTongCouponCalculate{

	@Override
	public BigDecimal calculate(BigDecimal price, BigDecimal couponMoney, Double maxFavMoney) {
		BigDecimal finalPrice = null;
		//固定折扣券计算
		BigDecimal maxMoney = new BigDecimal(maxFavMoney.toString());
		finalPrice = price.multiply(couponMoney.divide(new BigDecimal(10)));
		BigDecimal favourable = price.subtract(finalPrice);
		if(maxMoney.compareTo(favourable)<0){
			finalPrice = price.subtract(maxMoney);
		}
		return finalPrice;
	}

}

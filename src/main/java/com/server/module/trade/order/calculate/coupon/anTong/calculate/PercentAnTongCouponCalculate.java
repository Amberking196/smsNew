package com.server.module.trade.order.calculate.coupon.anTong.calculate;

import java.math.BigDecimal;

/**
 * 按桶折扣优惠券计算
 * @author hebiting
 *
 */
public class PercentAnTongCouponCalculate implements AnTongCouponCalculate{

	@Override
	public BigDecimal calculate(BigDecimal price, BigDecimal couponMoney, Double maxFavMoney) {
		BigDecimal finalPrice = null;
		//折扣券计算
		finalPrice = price.multiply(couponMoney.divide(new BigDecimal(10)));
		return finalPrice;
	}
}

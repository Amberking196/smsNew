package com.server.module.trade.order.calculate.coupon.anTong.calculate;

import java.math.BigDecimal;

public interface AnTongCouponCalculate {

	BigDecimal calculate(BigDecimal price,BigDecimal couponMoney,Double maxFavMoney);
}

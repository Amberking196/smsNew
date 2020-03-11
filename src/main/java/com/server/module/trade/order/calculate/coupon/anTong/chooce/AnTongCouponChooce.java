package com.server.module.trade.order.calculate.coupon.anTong.chooce;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.coupon.CouponBean;
import com.server.module.trade.order.calculate.coupon.anTong.calculate.AnTongCouponCalculate;
import com.server.module.trade.order.calculate.coupon.anTong.calculate.AnTongCouponFactory;

@Component
public class AnTongCouponChooce {
	
	@Autowired
	private AnTongCouponFactory anTongCouponFactory;
	
	public List<CouponBean> chooce(List<CouponBean> canUseCouponList,BigDecimal unitItemPrice){
		Collections.sort(canUseCouponList, new Comparator<CouponBean>() {
			@Override
			public int compare(CouponBean o1, CouponBean o2) {
				AnTongCouponCalculate anTongCouponCalcualte1 = anTongCouponFactory.getAnTongCouponCalcualte(o1.getType());
				AnTongCouponCalculate anTongCouponCalcualte2 = anTongCouponFactory.getAnTongCouponCalcualte(o1.getType());
				BigDecimal calculate1 = anTongCouponCalcualte1.calculate(unitItemPrice, new BigDecimal(o1.getDeductionMoney().toString()), o1.getMaximumDiscount());
				BigDecimal calculate2 = anTongCouponCalcualte2.calculate(unitItemPrice, new BigDecimal(o2.getDeductionMoney().toString()), o2.getMaximumDiscount());
				return calculate2.compareTo(calculate1);
			}
		});
		return canUseCouponList;
	}
}

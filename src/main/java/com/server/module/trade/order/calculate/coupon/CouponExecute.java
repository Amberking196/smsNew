package com.server.module.trade.order.calculate.coupon;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.coupon.CouponBean;
import com.server.module.coupon.MoreGoodsCouponDto;
import com.server.module.trade.order.calculate.coupon.anDan.AnDanCouponExecute;
import com.server.module.trade.order.calculate.coupon.anTong.AnTongCouponExecute;
import com.server.module.trade.order.domain.MachineInfo;
@Component
public class CouponExecute {
	
	@Autowired
	private AnDanCouponExecute anDanCouponExecute;
	@Autowired
	private AnTongCouponExecute anTongCouponExecute;

	public BigDecimal chooceAndCalculate(MoreGoodsCouponDto moreGoodsCoupon, MachineInfo machineInfo,
			Map<CouponBean, Integer> usedCouponMap){
		BigDecimal finalPrice = new BigDecimal(0);
		Map<CouponBean, Integer> couponMap = moreGoodsCoupon.getCouponMap();
		Set<CouponBean> keySet = couponMap.keySet();
		boolean canUseAnTong = false;
		for (CouponBean couponBean : keySet) {
			if (couponBean.getFormulaMode() == 1) {
				canUseAnTong = true;
				break;
			}
		}
		if (canUseAnTong) {
			finalPrice = anTongCouponExecute.chooceAndCalculate(moreGoodsCoupon, machineInfo, usedCouponMap);
		} else {
			finalPrice = anDanCouponExecute.chooceAndCalculate(moreGoodsCoupon, machineInfo, usedCouponMap);
		}
		return finalPrice;
	}
}

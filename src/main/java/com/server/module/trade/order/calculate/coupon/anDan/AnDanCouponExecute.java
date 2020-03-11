package com.server.module.trade.order.calculate.coupon.anDan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.coupon.CouponBean;
import com.server.module.coupon.MoreGoodsCouponDto;
import com.server.module.trade.order.calculate.coupon.anDan.calculate.AnDanCouponCalculate;
import com.server.module.trade.order.calculate.coupon.anDan.calculate.AnDanCouponFactory;
import com.server.module.trade.order.calculate.coupon.anDan.chooce.AnDanCouponChooce;
import com.server.module.trade.order.domain.MachineInfo;

@Component
public class AnDanCouponExecute {
	
	@Autowired
	private AnDanCouponChooce anDanCouponChooce;
	@Autowired
	private AnDanCouponFactory anDanCouponFactory;
	
	public BigDecimal chooceAndCalculate(MoreGoodsCouponDto moreGoodsCoupon, MachineInfo machineInfo,
			Map<CouponBean, Integer> usedCouponMap){
		BigDecimal finalPrice = new BigDecimal(0);
		Map<CouponBean, List<Long>> itemMap = moreGoodsCoupon.getItemMap();
		List<CouponBean> collect = moreGoodsCoupon.getNoBindItemList().stream()
				.filter(coupon -> coupon.getFormulaMode() == 0).collect(Collectors.toList());
		List<CouponBean> canUseCouponList = new ArrayList<CouponBean>(collect);
		for (Map.Entry<CouponBean, List<Long>> entry : itemMap.entrySet()) {
			if (entry.getKey().getFormulaMode() == 0) {
				canUseCouponList.add(entry.getKey());
			}
		}
		CouponBean min = anDanCouponChooce.chooce(canUseCouponList, machineInfo, itemMap);
		usedCouponMap.put(min, 1);
		AnDanCouponCalculate anDanCouponCalcualte = anDanCouponFactory.getNormalAnDanCouponCalcualte(min.getType());
		finalPrice = anDanCouponCalcualte.calculate(machineInfo, new BigDecimal(min.getDeductionMoney().toString()), itemMap.get(min), min.getMaximumDiscount());
		return finalPrice;
	}
}

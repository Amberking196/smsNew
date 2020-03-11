package com.server.module.trade.order.calculate.coupon;

import java.math.BigDecimal;
import java.util.Map;

import com.server.module.coupon.CouponBean;
import com.server.module.coupon.MoreGoodsCouponDto;
import com.server.module.trade.order.domain.MachineInfo;

public interface CouponCalculate{

	BigDecimal anDanCouponCalculate(MoreGoodsCouponDto moreGoodsCoupon,MachineInfo machineInfo,
			Map<CouponBean,Integer> usedCouponMap);
	
}

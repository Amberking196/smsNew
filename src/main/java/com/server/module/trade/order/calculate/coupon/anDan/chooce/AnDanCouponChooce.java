package com.server.module.trade.order.calculate.coupon.anDan.chooce;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.coupon.CouponBean;
import com.server.module.trade.order.calculate.coupon.anDan.calculate.AnDanCouponCalculate;
import com.server.module.trade.order.calculate.coupon.anDan.calculate.AnDanCouponFactory;
import com.server.module.trade.order.domain.MachineInfo;

@Component
public class AnDanCouponChooce {
	@Autowired
	private AnDanCouponFactory anDanCouponFactory;

	
	public CouponBean chooce(List<CouponBean> canUseCouponList,MachineInfo machineInfo,Map<CouponBean, List<Long>> itemMap){
		CouponBean min = Collections.min(canUseCouponList, new Comparator<CouponBean>() {
			@Override
			public int compare(CouponBean o1, CouponBean o2) {
				AnDanCouponCalculate anDanCouponCalcualte = anDanCouponFactory.getNormalAnDanCouponCalcualte(o1.getType());
				AnDanCouponCalculate anDanCouponCalcualte2 = anDanCouponFactory.getNormalAnDanCouponCalcualte(o2.getType());
				BigDecimal calculate1 = anDanCouponCalcualte.calculate(machineInfo, new BigDecimal(o1.getDeductionMoney().toString()), itemMap.get(o1), o1.getMaximumDiscount());
				BigDecimal calculate2 = anDanCouponCalcualte2.calculate(machineInfo, new BigDecimal(o2.getDeductionMoney().toString()), itemMap.get(o2), o2.getMaximumDiscount());
				int code = calculate2.compareTo(calculate1);
				if (code == 0) {
					code = compareCoupon(o1, o2);
				}
				return code;
			}
		});
		return min;
	}
	
	/**
	 * 定义同等优惠力度下的优惠券使用顺序
	 * @author hebiting
	 * @date 2018年9月21日上午10:15:25
	 * @param c1
	 * @param c2
	 * @return
	 */
	public int compareCoupon(CouponBean c1 , CouponBean c2){
		if(!c1.getType().equals(c2.getType())){
			if(c1.getType()!=4 && c2.getType()!=4){
				return c1.getType().compareTo(c2.getType());
			}else if(c1.getType()==3){
				return 1;
			}else if(c2.getType()==3){
				return -1;
			}else{
				if(c1.getType() == 4){
					if(c1.getMaximumDiscount().equals(c2.getDeductionMoney())){
						return -1;
					}else{
						return c1.getMaximumDiscount().compareTo(c2.getDeductionMoney());
					}
				}else{
					if(c1.getDeductionMoney().equals(c2.getMaximumDiscount())){
						return 1;
					}else{
						return c1.getMaximumDiscount().compareTo(c2.getDeductionMoney());
					}
				}
			}
		}else{
			if(c1.getType()==4){
				return c1.getMaximumDiscount().compareTo(c2.getMaximumDiscount());
			}else{
				return c1.getDeductionMoney().compareTo(c2.getDeductionMoney());
			}
		}
	}
}

package com.server.module.trade.order.calculate.coupon.anTong;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.coupon.CouponBean;
import com.server.module.coupon.MoreGoodsCouponDto;
import com.server.module.trade.order.calculate.coupon.anTong.calculate.AnTongCouponCalculate;
import com.server.module.trade.order.calculate.coupon.anTong.calculate.AnTongCouponFactory;
import com.server.module.trade.order.calculate.coupon.anTong.chooce.AnTongCouponChooce;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;

@Component
public class AnTongCouponExecute {
	
	@Autowired
	private AnTongCouponChooce anTongCouponChooce;
	@Autowired
	private AnTongCouponFactory anTongCouponFactory;

	public BigDecimal chooceAndCalculate(MoreGoodsCouponDto moreGoodsCoupon, MachineInfo machineInfo,
			Map<CouponBean, Integer> usedCouponMap){
		BigDecimal endPrice = new BigDecimal(0);
		List<ItemChangeDto> itemChangeList = machineInfo.getItemChangeList();
		Collections.sort(itemChangeList, new Comparator<ItemChangeDto>() {
			@Override
			public int compare(ItemChangeDto o1, ItemChangeDto o2) {
				return o2.getPrice().compareTo(o1.getPrice());
			}
		});
		for (ItemChangeDto itemChangeDto : itemChangeList) {
			if (itemChangeDto.getChangeNum() < 0) {
				Long basicItemId = itemChangeDto.getBasicItemId();
				List<CouponBean> collect = moreGoodsCoupon.getNoBindItemList().stream()
						.filter(coupon -> coupon.getFormulaMode() == 1).collect(Collectors.toList());
				List<CouponBean> temporaryList = new ArrayList<CouponBean>(collect);
				Map<CouponBean, List<Long>> itemMap = moreGoodsCoupon.getItemMap();
				for (Map.Entry<CouponBean, List<Long>> entry : itemMap.entrySet()) {
					if (entry.getKey().getFormulaMode() == 1 && entry.getValue().contains(basicItemId)) {
						temporaryList.add(entry.getKey());
					}
				}
				int couponNum = 0;
				for (CouponBean couponBean : temporaryList) {
					couponNum += couponBean.getQuantity();
				}
				int changeNum = Math.abs(itemChangeDto.getChangeNum());
				BigDecimal unitItemPrice = new BigDecimal(itemChangeDto.getPrice().toString());
				BigDecimal realTotalPrice = new BigDecimal(0);// 单类商品总价
				if (couponNum > changeNum) {
					// 优惠券数量大于商品数量
					//优惠券排序
					temporaryList = anTongCouponChooce.chooce(temporaryList, unitItemPrice);
					int notUseCouponNum = changeNum;
					for (CouponBean couponBean : temporaryList) {
						AnTongCouponCalculate anTongCouponCalcualte = anTongCouponFactory.getAnTongCouponCalcualte(couponBean.getType());
						int couponQuantity = couponBean.getQuantity();
						if (notUseCouponNum > couponQuantity) {
							Integer useNum = usedCouponMap.get(couponBean);
							if (useNum == null) {
								usedCouponMap.put(couponBean, couponQuantity);
							} else {
								usedCouponMap.put(couponBean, useNum + couponQuantity);
							}
							couponBean.setQuantity(0);
							moreGoodsCoupon.getNoBindItemList().remove(couponBean);
							moreGoodsCoupon.getItemMap().remove(couponBean);
							BigDecimal calculate = anTongCouponCalcualte.calculate(unitItemPrice, new BigDecimal(couponBean.getDeductionMoney().toString())
									.multiply(new BigDecimal(couponQuantity)), couponBean.getMaximumDiscount());
							realTotalPrice = realTotalPrice.add(calculate);
						} else {
							BigDecimal calculate = anTongCouponCalcualte.calculate(unitItemPrice, new BigDecimal(couponBean.getDeductionMoney().toString())
									.multiply(new BigDecimal(notUseCouponNum)), couponBean.getMaximumDiscount());
							realTotalPrice = realTotalPrice.add(calculate);
							Integer useNum = usedCouponMap.get(couponBean);
							if (useNum == null) {
								usedCouponMap.put(couponBean, notUseCouponNum);
							} else {
								usedCouponMap.put(couponBean, useNum + notUseCouponNum);
							}
							couponBean.setQuantity(couponQuantity - notUseCouponNum);
						}
						notUseCouponNum -= couponQuantity;
						if (notUseCouponNum <= 0) {
							break;
						}
					}
				} else {
					// 商品数量大于等于优惠券数量
					for (CouponBean couponBean : temporaryList) {
						AnTongCouponCalculate anTongCouponCalcualte = anTongCouponFactory.getAnTongCouponCalcualte(couponBean.getType());
						BigDecimal couponPrice = anTongCouponCalcualte.calculate(unitItemPrice,new BigDecimal(couponBean.getDeductionMoney().toString()),
								couponBean.getMaximumDiscount());
						BigDecimal itemTotalPrice = couponPrice
								.multiply(new BigDecimal(couponBean.getQuantity().toString()));
						realTotalPrice = realTotalPrice.add(itemTotalPrice);
						Integer useNum = usedCouponMap.get(couponBean);
						if (useNum == null) {
							usedCouponMap.put(couponBean, couponBean.getQuantity());
						} else {
							usedCouponMap.put(couponBean, useNum + couponBean.getQuantity());
						}
						couponBean.setQuantity(0);
						moreGoodsCoupon.getNoBindItemList().remove(couponBean);
						moreGoodsCoupon.getItemMap().remove(couponBean);
					}
					realTotalPrice = realTotalPrice.add(new BigDecimal(changeNum - couponNum).multiply(unitItemPrice));
				}
				realTotalPrice = realTotalPrice.setScale(2, BigDecimal.ROUND_DOWN);
				itemChangeDto.setRealTotalPrice(realTotalPrice.doubleValue());
				endPrice = endPrice.add(realTotalPrice);
			}
		}
		return endPrice;
	}
}

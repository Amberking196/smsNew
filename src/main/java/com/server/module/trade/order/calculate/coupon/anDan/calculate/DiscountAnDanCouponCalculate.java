package com.server.module.trade.order.calculate.coupon.anDan.calculate;

import java.math.BigDecimal;
import java.util.List;

import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;

/**
 * 按单满减固减券计算
 * @author hebiting
 *
 */
public class DiscountAnDanCouponCalculate implements AnDanCouponCalculate{

	@Override
	public BigDecimal calculate(MachineInfo machineInfo, BigDecimal couponMoney, List<Long> itemList,
			Double maxFavMoney) {
		BigDecimal finalPrice = new BigDecimal(0);
//		Integer version = machineInfo.getVersion();
		List<ItemChangeDto> itemChangeList = machineInfo.getItemChangeList();
		//满减券固减券计算
		for(int i=0;i<itemChangeList.size();i++ ){
			ItemChangeDto itemChangeDto = itemChangeList.get(i);
			if(itemChangeDto.getChangeNum()<0){
				int num = Math.abs(itemChangeDto.getChangeNum());
				BigDecimal goNum = new BigDecimal(num);//购买数量
				BigDecimal unitPrice = new BigDecimal(itemChangeDto.getPrice().toString());//单价
				BigDecimal itemTotalPrice = unitPrice.multiply(goNum);
				if((itemList == null || itemList.contains(itemChangeDto.getBasicItemId()))
						&& couponMoney.compareTo(new BigDecimal(0))> 0){
					if(couponMoney.compareTo(itemTotalPrice) > 0){
						itemChangeDto.setRealTotalPrice(0d);
						couponMoney = couponMoney.subtract(itemTotalPrice);
					}else{
						BigDecimal realTotalPrice = itemTotalPrice.subtract(couponMoney).setScale(2, BigDecimal.ROUND_DOWN);
						itemChangeDto.setRealTotalPrice(realTotalPrice.doubleValue());
						finalPrice = finalPrice.add(realTotalPrice);
						couponMoney = new BigDecimal(0);
					}
				}else{
					BigDecimal realTotalPrice = itemTotalPrice.setScale(2, BigDecimal.ROUND_DOWN);
					itemChangeDto.setRealTotalPrice(realTotalPrice.doubleValue());
					finalPrice = finalPrice.add(realTotalPrice);
				}
			}
		}
		return finalPrice;
	}

}

package com.server.module.trade.order.calculate.coupon.anDan.calculate;

import java.math.BigDecimal;
import java.util.List;

import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;

/**
 * 按单折扣券计算
 * @author hebiting
 *
 */
public class PercentAnDanCouponCalculate implements AnDanCouponCalculate{

	@Override
	public BigDecimal calculate(MachineInfo machineInfo, BigDecimal couponMoney, List<Long> itemList,
			Double maxFavMoney) {
		BigDecimal finalPrice = new BigDecimal(0);
//		Integer version = machineInfo.getVersion();
		List<ItemChangeDto> itemChangeList = machineInfo.getItemChangeList();
		//折扣券计算
		for(int i=0;i<itemChangeList.size();i++ ){
			ItemChangeDto itemChangeDto = itemChangeList.get(i);
			if(itemChangeDto.getChangeNum()<0){
				int num = Math.abs(itemChangeDto.getChangeNum());
				BigDecimal goNum = new BigDecimal(num);//购买数量
				BigDecimal unitPrice = new BigDecimal(itemChangeDto.getPrice().toString());//单价
				BigDecimal realTotalPrice = null;
				if(itemList == null || itemList.contains(itemChangeDto.getBasicItemId())){
					BigDecimal discount = couponMoney.divide(new BigDecimal(10));//折扣
					realTotalPrice = unitPrice.multiply(goNum).multiply(discount);
				}else{
					realTotalPrice = unitPrice.multiply(goNum);
				}
				realTotalPrice = realTotalPrice.setScale(2, BigDecimal.ROUND_DOWN);
				itemChangeDto.setRealTotalPrice(realTotalPrice.doubleValue());
				finalPrice = finalPrice.add(realTotalPrice);
			}
		}
		return finalPrice;
	}

}

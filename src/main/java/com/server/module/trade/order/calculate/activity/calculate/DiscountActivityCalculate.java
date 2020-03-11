package com.server.module.trade.order.calculate.activity.calculate;

import java.math.BigDecimal;
import java.util.List;

import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;

/**
 * 满减固减活动计算
 * @author hebiting
 *
 */
public class DiscountActivityCalculate implements ActivityCalculate{

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
				BigDecimal realTotalPrice = null;
				if(itemList ==null || itemList.contains(itemChangeDto.getBasicItemId())){
					if(couponMoney.compareTo(new BigDecimal(0))> 0){
						if(couponMoney.compareTo(itemTotalPrice) > 0){
							realTotalPrice = new BigDecimal(0);
							couponMoney = couponMoney.subtract(itemTotalPrice);
						}else{
							realTotalPrice = itemTotalPrice.subtract(couponMoney).setScale(2, BigDecimal.ROUND_DOWN);
							finalPrice = finalPrice.add(realTotalPrice);
							couponMoney = new BigDecimal(0);
						}
					}else{
						realTotalPrice = itemTotalPrice.setScale(2, BigDecimal.ROUND_DOWN);
						finalPrice = finalPrice.add(realTotalPrice);
					}
				}else{
					realTotalPrice = itemTotalPrice.setScale(2, BigDecimal.ROUND_DOWN);
					finalPrice = finalPrice.add(realTotalPrice);
				}
				itemChangeDto.setRealTotalPrice(realTotalPrice.doubleValue());
			}
		}
		return finalPrice;
	}

}

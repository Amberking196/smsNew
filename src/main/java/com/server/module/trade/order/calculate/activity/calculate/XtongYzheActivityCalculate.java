package com.server.module.trade.order.calculate.activity.calculate;

import java.math.BigDecimal;
import java.util.List;

import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;

public class XtongYzheActivityCalculate implements ActivityCalculate{

	@Override
	public BigDecimal calculate(MachineInfo machineInfo, BigDecimal couponMoney, List<Long> itemList,
			Double maxFavMoney) {
		//x桶y折扣活动
		BigDecimal finalPrice = new BigDecimal(0);
		int intValue = couponMoney.intValue();
		String value = intValue+"";
		//以右为起点，第五位为分割位，前面为买 goNum 桶,后面为back 折扣,须除以1000
		String go = value.substring(0, value.length()-5);
		String back = value.substring(value.length()-4,value.length());
		Integer goNum = Integer.valueOf(go);
		BigDecimal discount = new BigDecimal(back).divide(new BigDecimal("1000"));
		List<ItemChangeDto> itemChangeList = machineInfo.getItemChangeList();
		if(itemChangeList!=null && itemChangeList.size()>0){
			for (int i = 0;i<itemChangeList.size();i++) {
				ItemChangeDto itemChangeDto = itemChangeList.get(i);
				if(itemChangeDto.getChangeNum()<0){
					Integer num = Math.abs(itemChangeDto.getChangeNum());
					if(itemList ==null || itemList.contains(itemChangeDto.getBasicItemId())){
						BigDecimal originalPrice = new BigDecimal(itemChangeDto.getPrice().toString());
						int original = (num / goNum)*(goNum - 1) + (num % goNum);
						int willDis = num / goNum;
						BigDecimal realTotalPrice = new BigDecimal(original).
								add(new BigDecimal(willDis).multiply(discount)).
								multiply(originalPrice).setScale(2, BigDecimal.ROUND_DOWN);
						itemChangeDto.setRealTotalPrice(realTotalPrice.doubleValue());
						finalPrice = finalPrice.add(realTotalPrice);
					}else {
						BigDecimal realTotalPrice = new BigDecimal(num).
								multiply(new BigDecimal(itemChangeDto.getPrice().toString())).setScale(2, BigDecimal.ROUND_DOWN);
						itemChangeDto.setRealTotalPrice(realTotalPrice.doubleValue());
						finalPrice = finalPrice.add(realTotalPrice);
					}
				}
			}
		}
		return finalPrice;
	}

}

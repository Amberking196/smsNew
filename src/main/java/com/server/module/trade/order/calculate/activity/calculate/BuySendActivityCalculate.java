package com.server.module.trade.order.calculate.activity.calculate;

import java.math.BigDecimal;
import java.util.List;

import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;

/**
 * 买赠活动计算
 * @author hebiting
 *
 */
public class BuySendActivityCalculate implements ActivityCalculate{

	@Override
	public BigDecimal calculate(MachineInfo machineInfo,BigDecimal couponMoney,
			List<Long> itemList,Double maxFavMoney) {
		BigDecimal finalPrice = new BigDecimal(0);
//		Integer version = machineInfo.getVersion();
		List<ItemChangeDto> itemChangeList = machineInfo.getItemChangeList();
		//买x送y活动计算
		int intValue = couponMoney.intValue();
		String value = intValue+"";
		//以右为起点，第五位为分割位，前面为买 goNum 桶,后面为送 backNum 桶
		String go = value.substring(0, value.length()-5);
		String back = value.substring(value.length()-4,value.length());
		Integer goNum = Integer.valueOf(go);
		Integer backNum = Integer.valueOf(back);
		for(int i=0;i<itemChangeList.size();i++ ){
			ItemChangeDto itemChangeDto = itemChangeList.get(i);
			if(itemChangeDto.getChangeNum()<0){
				Integer num = Math.abs(itemChangeDto.getChangeNum());
				if(itemList ==null || itemList.contains(itemChangeDto.getBasicItemId())){
					Integer realNum =  (num/(goNum+backNum)*goNum) + (num%(goNum+backNum)>=goNum?goNum:num%(goNum+backNum));
					BigDecimal realTotalPrice = new BigDecimal(realNum).
							multiply(new BigDecimal(itemChangeDto.getPrice().toString())).setScale(2, BigDecimal.ROUND_DOWN);
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
		return finalPrice;
	}

}

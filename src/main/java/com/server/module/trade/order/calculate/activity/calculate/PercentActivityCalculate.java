package com.server.module.trade.order.calculate.activity.calculate;

import java.math.BigDecimal;
import java.util.List;

import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;

/**
 * 折扣活动计算
 * @author hebiting
 *
 */
public class PercentActivityCalculate implements ActivityCalculate{

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
				if(itemList ==null || itemList.contains(itemChangeDto.getBasicItemId())){
					BigDecimal discount = couponMoney.divide(new BigDecimal(10));//折扣
					BigDecimal realTotalPrice = unitPrice.multiply(goNum).multiply(discount).setScale(2, BigDecimal.ROUND_DOWN);
					itemChangeDto.setRealTotalPrice(realTotalPrice.doubleValue());
					finalPrice = finalPrice.add(realTotalPrice);
				}else{
					BigDecimal realTotalPrice = unitPrice.multiply(goNum).setScale(2, BigDecimal.ROUND_DOWN);
					itemChangeDto.setRealTotalPrice(realTotalPrice.doubleValue());
					finalPrice = finalPrice.add(unitPrice.multiply(goNum));
				}
			}
		}
		return finalPrice;
	}

}

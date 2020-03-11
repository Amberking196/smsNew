package com.server.module.trade.order.calculate.carry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.server.module.carryWaterVouchersManage.carryWaterVouchers.CarryDto;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;

@Component
public class CarryExecute {

	public BigDecimal chooceAndCalculate(Map<CarryDto, List<Long>> carryMap,Map<CarryDto, Integer> usedCarryMap,
			MachineInfo machineInfo){
		BigDecimal finalPrice = BigDecimal.ZERO;
		List<ItemChangeDto> itemChangeList = machineInfo.getItemChangeList();
		Collections.sort(itemChangeList, new Comparator<ItemChangeDto>() {
			@Override
			public int compare(ItemChangeDto o1, ItemChangeDto o2) {
				return o2.getPrice().compareTo(o1.getPrice());
			}
		});
		for(ItemChangeDto itemChange : itemChangeList){
			if(itemChange.getChangeNum() < 0 ){
				List<CarryDto> canUseList = new ArrayList<CarryDto>();
				int carryNotUseNum = 0;
				int changeNum = Math.abs(itemChange.getChangeNum());
				for (Map.Entry<CarryDto, List<Long>> carryEntry : carryMap.entrySet()) {
					List<Long> itemList = carryEntry.getValue();
					if(itemList == null || itemList.contains(itemChange.getBasicItemId())){
						canUseList.add(carryEntry.getKey());
						carryNotUseNum += carryEntry.getKey().getCarryNum();
					}
				}
				Collections.sort(canUseList,new Comparator<CarryDto>(){
					@Override
					public int compare(CarryDto o1, CarryDto o2) {
						return o1.getEndTime().compareTo(o2.getEndTime());
					}
				});
				if(carryNotUseNum > changeNum){
					//提水券总 数量大于 商品数量
					itemChange.setRealTotalPrice(0D);
					for(CarryDto carry : canUseList){
						if(carry.getCarryNum() > changeNum){
							//提水券 数量大于 商品数量
							int surplus = carry.getCarryNum() - changeNum;
							carry.setCarryNum(surplus);
							Integer useNum = usedCarryMap.get(carry);
							if(useNum == null ){
								usedCarryMap.put(carry, changeNum);
							}else{
								usedCarryMap.put(carry, useNum + changeNum);
							}
							changeNum = 0;
						} else {
							//提水券 数量小于 商品数量		
							Integer carryNum = carry.getCarryNum();
							carry.setCarryNum(0);
							carryMap.remove(carry);
							Integer num = usedCarryMap.get(carry);
							if(num == null){
								usedCarryMap.put(carry, carryNum);
							} else {
								usedCarryMap.put(carry, num + carryNum);
							}
							changeNum -= carryNum;
						}
						if(changeNum <= 0){
							break;
						}
					}
				} else {
					//提水券总 数量小于或等于 商品数量
					int notUseItemNum = changeNum - carryNotUseNum;
					for(CarryDto carry : canUseList){
						Integer carryNum = carry.getCarryNum();
						carry.setCarryNum(0);
						carryMap.remove(carry);
						Integer num = usedCarryMap.get(carry);
						if(num == null){
							usedCarryMap.put(carry, carryNum);
						} else {
							usedCarryMap.put(carry, num + carryNum);
						}
					}
					BigDecimal realTotalPrice = new BigDecimal(itemChange.getPrice().toString())
							.multiply(new BigDecimal(notUseItemNum)).setScale(2, BigDecimal.ROUND_DOWN);
					itemChange.setRealTotalPrice(realTotalPrice.doubleValue());
					finalPrice = finalPrice.add(realTotalPrice);
				}
			}
		}
		return finalPrice;
	}
	
	public static void main(String[] args) {
//		Calendar calendar1 = Calendar.getInstance();
//		Calendar calendar2 = Calendar.getInstance();
//		calendar1.setTime(new Date());
//		calendar1.add(Calendar.MONTH, -1);
//		calendar2.setTime(new Date());
//		System.out.println(calendar1.getTime());
//		System.out.println(calendar2.getTime());
//		System.out.println(calendar1.compareTo(calendar2));
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		try {
//			Date parse1 = format.parse("2018-09-12 00:00:00");
//			Date parse2 = format.parse("2018-09-12 23:00:00");
//			System.out.println(parse1.compareTo(parse2));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
	}
}

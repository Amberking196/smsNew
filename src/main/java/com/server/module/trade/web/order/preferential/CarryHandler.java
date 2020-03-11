package com.server.module.trade.web.order.preferential;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.carryWaterVouchersManage.carryRecord.CarryRecordBean;
import com.server.module.carryWaterVouchersManage.carryRecord.CarryRecordDao;
import com.server.module.carryWaterVouchersManage.carryWaterVouchers.CarryDto;
import com.server.module.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersDao;
import com.server.module.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerDao;
import com.server.module.coupon.MachinesLAC;
import com.server.module.store.StoreOrderDao;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.bean.PayRecordBean;
import com.server.module.trade.order.calculate.carry.CarryExecute;
import com.server.module.trade.order.domain.MachineInfo;
@Component
public class CarryHandler extends PreferentialHandler{
	
	@Autowired
	private CarryWaterVouchersDao carryWaterDao;
	@Autowired
	private CarryExecute carryExecute;
	@Autowired
	private CarryWaterVouchersCustomerDao carryCustomerDao;
	@Autowired
	private CarryRecordDao carryRecordDao;
	@Autowired
	private StoreOrderDao storeOrderDao;

	public CarryHandler(){
		setType(HandlerTypeEnum.PREFERENTIAL);
	}

	public CarryHandler(Handler carryHandler){
		setType(HandlerTypeEnum.PREFERENTIAL);
		setHandler(carryHandler);
	}

	@Override
	public void handler(MachineInfo machineInfo,UserVo userVo,PayRecordBean payRecord,MachinesLAC mlac,String items,List<HandlerTypeEnum> typeList) {
		Map<CarryDto, Integer> usedCarryMap = new HashMap<CarryDto, Integer>();
		if(!hasSimilar(typeList)){
			Map<CarryDto, List<Long>> carryMap = carryWaterDao.getCarryWaterVoucherByCustomer(userVo.getId(), mlac, items);
			if(carryMap != null && carryMap.size() > 0){
				BigDecimal finalPrice = carryExecute.chooceAndCalculate(carryMap, usedCarryMap, machineInfo);
				if(!payRecord.getPrice().equals(finalPrice)){
					typeList.add(getType());
					payRecord.setPrice(finalPrice);
				}
				for(Map.Entry<CarryDto, Integer> carryEntry : usedCarryMap.entrySet()){
					carryCustomerDao.reduceCarry(carryEntry.getKey().getCarryCustomerId(), carryEntry.getValue());
				}
			}
		}
		if(getHandler() != null){
			getHandler().handler(machineInfo, userVo, payRecord,mlac,items, typeList);
		}
		if (usedCarryMap != null && usedCarryMap.size() > 0) {
			Integer sumCarryRecordNum=0;
			for (Map.Entry<CarryDto, Integer> entry : usedCarryMap.entrySet()) {
				CarryRecordBean carryRecord = new CarryRecordBean();
				carryRecord.setCarryId(entry.getKey().getCarryId());
				carryRecord.setCustomerId(userVo.getId());
				carryRecord.setNum(entry.getValue());
				carryRecord.setOrderId(payRecord.getId());
				sumCarryRecordNum=sumCarryRecordNum+entry.getValue();
				//插入提水券使用记录
				carryRecordDao.insertCarryRecord(carryRecord);
				//修改拼团订单状态
				if(entry.getKey().getOrderId()!=null&&entry.getKey().getOrderId()>0) {
					storeOrderDao.paySpellgroupStroeOrder(entry.getKey().getOrderId()+"",0);
				}
			}
		}
	}
	
}

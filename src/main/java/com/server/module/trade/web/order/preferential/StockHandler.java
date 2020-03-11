package com.server.module.trade.web.order.preferential;

import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.coupon.MachinesLAC;
import com.server.module.store.CustomerPickRecord;
import com.server.module.store.CustomerStockBean;
import com.server.module.store.StoreOrderDao;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.bean.PayRecordBean;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;

@Component
public class StockHandler extends PreferentialHandler{
	
	private final static Logger log = LogManager.getLogger(StockHandler.class);
	
	@Autowired
	private StoreOrderDao storeOrderDaoImpl;
	
	public StockHandler(){
		setType(HandlerTypeEnum.ELSE);
	}

	public StockHandler(Handler carryHandler){
		setType(HandlerTypeEnum.ELSE);
		setHandler(carryHandler);
	}
	

	@Override
	public void handler(MachineInfo machineInfo,UserVo userVo,PayRecordBean payRecord,MachinesLAC mlac,String items,List<HandlerTypeEnum> typeList) {
		if(!hasSimilar(typeList)){
			log.info("存水处理器");
			typeList.add(getType());
			Iterator<ItemChangeDto> iterator = machineInfo.getItemChangeList().iterator();
			while(iterator.hasNext()){
				ItemChangeDto itemChange = iterator.next();
				if (itemChange.getChangeNum() < 0) {
					int num = Math.abs(itemChange.getChangeNum());
					CustomerStockBean stock = null;
					if (userVo.getType() == UserVo.USER_CUSTOMER) {
						stock = storeOrderDaoImpl.getStockByBasicItem(itemChange.getBasicItemId(), userVo.getId());
						if (stock != null) {
							int stockNum = stock.getStock() - stock.getPickNum();
							int pickNum = 0;
							if (stockNum >= num) {
								itemChange.setChangeNum(0);
								stock.setPickNum(stock.getPickNum() + num);
								pickNum = num;
							} else {
								itemChange.setChangeNum(stockNum - num);
								stock.setPickNum(stock.getStock());
								pickNum = stockNum;
							}
							storeOrderDaoImpl.update(stock);
							CustomerPickRecord pickRecord = new CustomerPickRecord();
							pickRecord.setBasicItemId(stock.getBasicItemId());
							pickRecord.setCustomerId(userVo.getId());
							pickRecord.setVmCode(machineInfo.getVmCode());
							pickRecord.setWayNum(machineInfo.getCurrWay());
							pickRecord.setPickNum(pickNum);
							storeOrderDaoImpl.insertPickRecord(pickRecord);
						}
					}
				}
			}
		}
		if(getHandler() != null){
			getHandler().handler(machineInfo, userVo, payRecord, mlac,items,typeList);
		}
	}

}

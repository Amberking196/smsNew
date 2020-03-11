package com.server.module.trade.web.order.preferential;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.activeDegree.UserActiveDegreeDao;
import com.server.module.coupon.MachinesLAC;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.bean.PayRecordBean;
import com.server.module.trade.order.dao.PayRecordDao;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.payRecordItem.PayRecordItemBean;
import com.server.module.trade.payRecordItem.PayRecordItemDao;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;
import com.server.util.stateEnum.PayTypeEnum;
@Component
public class CreateOrderHandler extends PreferentialHandler{
	
	@Autowired
	private PayRecordDao payRecordDaoImpl;
	@Autowired
	private UserActiveDegreeDao userActiveDegreeDao;
	@Autowired
	private PayRecordItemDao payRecordItemDao;

	public CreateOrderHandler(){
		setType(HandlerTypeEnum.ELSE);
	}

	public CreateOrderHandler(Handler carryHandler){
		setType(HandlerTypeEnum.ELSE);
		setHandler(carryHandler);
	}
	
	@Override
	public void handler(MachineInfo machineInfo, UserVo userVo, PayRecordBean payRecord,
			MachinesLAC mlac,String items,List<HandlerTypeEnum> typeList) {
		if(!hasSimilar(typeList)){
			typeList.add(getType());
			// HBT  待解决：footmark 的id，记得要写入,现在暂时用null代替
			payRecordDaoImpl.newCreatePayRecordAndReduceStock(payRecord, machineInfo, userVo,null,payRecord.existsOpenBeforeOrder);
			userActiveDegreeDao.update(userVo.getId(),machineInfo.getUnit());
			if(!payRecord.existsOpenBeforeOrder){
				for (ItemChangeDto itemChangeDto : machineInfo.getItemChangeList()) {
					if (itemChangeDto.getRealNum() < 0) {
						int realChangeNum = Math.abs(itemChangeDto.getRealNum());
						PayRecordItemBean recordItem = new PayRecordItemBean();
						recordItem.setBasicItemId(itemChangeDto.getBasicItemId());
						recordItem.setItemName(itemChangeDto.getItemName());
						recordItem.setNum(realChangeNum);
						recordItem.setPayRecordId(payRecord.getId());
						recordItem.setPrice(new BigDecimal(itemChangeDto.getPrice().toString())
								.multiply(new BigDecimal(realChangeNum)).doubleValue());
						if (PayTypeEnum.YS_BALANCE.getIndex() == payRecord.getPayType()) {
							recordItem.setRealTotalPrice(0d);
						} else {
							recordItem.setRealTotalPrice(itemChangeDto.getRealTotalPrice());
						}
						payRecordItemDao.insert(recordItem);
					}
				}
			}else{
				List<PayRecordItemBean> payRecordItemList = payRecordItemDao.getListByPayRecordId(payRecord.getId());
				for (ItemChangeDto itemChangeDto : machineInfo.getItemChangeList()) {
					if(itemChangeDto.getRealNum() < 0){
						for(PayRecordItemBean payRecordItemBean : payRecordItemList){
							if(itemChangeDto.getBasicItemId().equals(payRecordItemBean.getBasicItemId())){
								int realChangeNum = Math.abs(itemChangeDto.getRealNum());
								payRecordItemBean.setNum(realChangeNum);
								payRecordItemBean.setPrice(new BigDecimal(itemChangeDto.getPrice().toString())
										.multiply(new BigDecimal(realChangeNum)).doubleValue());
								if (PayTypeEnum.YS_BALANCE.getIndex() == payRecord.getPayType()) {
									payRecordItemBean.setRealTotalPrice(0d);
								} else {
									payRecordItemBean.setRealTotalPrice(itemChangeDto.getRealTotalPrice());
								}
								payRecordItemDao.updatePayRecordItem(payRecordItemBean);
							}
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

package com.server.module.trade.web.order.preferential;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.activity.ActivityBean;
import com.server.module.activity.ActivityDao;
import com.server.module.coupon.MachinesLAC;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.bean.PayRecordBean;
import com.server.module.trade.order.calculate.activity.ActivityExecute;
import com.server.module.trade.order.domain.MachineInfo;
@Component
public class ActivityHandler extends PreferentialHandler{
	@Autowired
	private ActivityDao activityDao;
	@Autowired
	private ActivityExecute activityExecute;
	
	
	public ActivityHandler(){
		setType(HandlerTypeEnum.PREFERENTIAL);
	}

	public ActivityHandler(Handler carryHandler){
		setType(HandlerTypeEnum.PREFERENTIAL);
		setHandler(carryHandler);
	}
	
	
	@Override
	public void handler(MachineInfo machineInfo,UserVo userVo,PayRecordBean payRecord,MachinesLAC mlac,String items,List<HandlerTypeEnum> typeList) {
		if(!hasSimilar(typeList)){
			Map<ActivityBean, List<Long>> activityMap = activityDao.getMoreGoodsActivity(mlac, machineInfo.getTotalPrice(),
					items);
			Set<ActivityBean> keySet = activityMap.keySet();
			if (keySet != null && keySet.size() > 0) {
				Map<String, Object> result = activityExecute.chooceAndCalculate(activityMap, machineInfo);
				if ((boolean) result.get("canDo")) {
					BigDecimal finalPrice = (BigDecimal) result.get("finalPrice");
					if(!payRecord.getPrice().equals(finalPrice)){
						typeList.add(getType());
						payRecord.setPrice(finalPrice);
					}
				}
			}
		}
		if(getHandler() != null){
			getHandler().handler(machineInfo, userVo, payRecord,mlac,items, typeList);
		}
	}

}

package com.server.module.trade.order.calculate.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.activity.ActivityBean;
import com.server.module.activity.ActivityDao;
import com.server.module.activity.ActivityQuantumBean;
import com.server.module.trade.order.calculate.activity.calculate.ActivityCalculate;
import com.server.module.trade.order.calculate.activity.calculate.ActivityFactory;
import com.server.module.trade.order.calculate.activity.chooce.ActivityChooce;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.util.StringUtil;

@Component
public class ActivityExecute {
	
	@Autowired
	private ActivityDao activityDao;
	@Autowired
	private ActivityChooce activityChooce;

	public Map<String, Object> chooceAndCalculate(Map<ActivityBean, List<Long>> activityMap,
			MachineInfo machineInfo){
		List<ActivityBean> activityList = new ArrayList<ActivityBean>(activityMap.keySet());
		Map<String, Object> result = new HashMap<String, Object>();
		boolean canDo = false;
		BigDecimal finalPrice = new BigDecimal(0);
		ActivityBean minAreaActivity = activityChooce.chooce(activityList);
		if (minAreaActivity.getType() == 1) {
			// 固定活动
			canDo = true;
			BigDecimal deductionMoney = new BigDecimal(minAreaActivity.getDeductionMoney().toString());
			ActivityCalculate activityCalculate = ActivityFactory.getActivityCalculate(minAreaActivity.getDiscountType());
			finalPrice = activityCalculate.calculate(machineInfo,deductionMoney, activityMap.get(minAreaActivity), null);
		} else {
			// 时间段活动
			List<ActivityQuantumBean> timeList = activityDao.getActivityQuantum(minAreaActivity.getTimeQuantumId());
			DateTime now = new DateTime();
			for (int i = 0; i < timeList.size(); i++) {
				String timeSlot = timeList.get(i).getTimeSlot();
				if (StringUtil.isNotBlank(timeSlot)) {
					timeSlot = timeSlot.trim();
					String[] times = timeSlot.split("-");
					if (times.length == 2) {
						String[] front = times[0].split(":");
						String[] back = times[1].split(":");
						DateTime frontTime = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(),
								Integer.valueOf(front[0]), Integer.valueOf(front[1]), 0);
						DateTime backTime = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(),
								Integer.valueOf(back[0]), Integer.valueOf(back[1]), 0);
						if (frontTime.isBeforeNow() && backTime.isAfterNow()) {
							canDo = true;
							BigDecimal deductionMoney = new BigDecimal(timeList.get(i).getRebate());
							ActivityCalculate activityCalculate = ActivityFactory.getActivityCalculate(minAreaActivity.getDiscountType());
							finalPrice = activityCalculate.calculate(machineInfo,deductionMoney, activityMap.get(minAreaActivity), null);
						}
					}
				}
			}
		}
		result.put("canDo", canDo);
		result.put("finalPrice", finalPrice);
		result.put("activity", minAreaActivity);
		return result;
	}
}

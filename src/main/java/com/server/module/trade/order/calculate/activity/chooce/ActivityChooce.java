package com.server.module.trade.order.calculate.activity.chooce;

import java.util.List;

import com.server.module.activity.ActivityBean;

public interface ActivityChooce {
	
	/**
	 * 活动选择
	 * @author hebiting
	 * @date 2018年10月19日下午4:28:01
	 * @param activityList
	 * @return
	 */
	public ActivityBean chooce(List<ActivityBean> activityList);
}

package com.server.module.trade.order.calculate.activity.chooce;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.server.module.activity.ActivityBean;

@Component
public class OptimalPriceActivityChooce implements ActivityChooce{
	
	public ActivityBean chooce(List<ActivityBean> activityList){
		
		ActivityBean minAreaActivity = Collections.max(activityList, new Comparator<ActivityBean>() {
			@Override
			public int compare(ActivityBean a, ActivityBean b) {
				return a.getTarget().compareTo(b.getTarget());
			}
		});
		return minAreaActivity;
	}
}

package com.server.module.activity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.server.module.coupon.MachinesLAC;

public interface ActivityService {
	
	/**
	 * 获取该机器所有活动
	 * @author hebiting
	 * @date 2018年8月28日上午10:51:37
	 * @param vmCode
	 * @return
	 */
	List<ActivityBean> getActivity(MachinesLAC machinesLAC,BigDecimal money,Long productId);

	/**
	 * 获取该机器所有活动
	 * @author hebiting
	 * @date 2018年8月28日上午10:51:37
	 * @param vmCode
	 * @return
	 */
	Map<ActivityBean,List<Long>> getMoreGoodsActivity(MachinesLAC machinesLAC,BigDecimal money,String productIds);


	/**
	 * 获取活动绑定商品
	 * @author hebiting
	 * @date 2018年8月28日上午11:13:32
	 * @param activityId
	 * @return
	 */
	List<ActivityProductBean> getActivityProduct(Long activityId);
	
	/**
	 * 获取活动时间段
	 * @author hebiting
	 * @date 2018年8月28日上午11:13:32
	 * @param quantnumIds
	 * @return
	 */
	List<ActivityQuantumBean> getActivityQuantum(String quantnumIds);
	
	
	/**
	 * 获取该机器活动(暂不可用)
	 * @author hebiting
	 * @date 2018年8月28日上午10:51:37
	 * @param vmCode
	 * @return
	 */
	ActivityBean getLocalActivity(MachinesLAC machinesLAC,String basicItemIds);
}

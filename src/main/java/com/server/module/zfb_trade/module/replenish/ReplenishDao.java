package com.server.module.zfb_trade.module.replenish;

import java.util.List;

public interface ReplenishDao {

	/**
	 * 根据条件查询补货信息
	 * @author hebiting
	 * @date 2018年6月5日上午10:06:07
	 * @param form
	 * @return
	 */
	public List<ReplenishDto> queryReplenish(ReplenishForm form);
	
	/**
	 * 校准补货数量
	 * @author hebiting
	 * @date 2018年10月29日下午3:26:30
	 * @return
	 */
	public boolean adjustReplenishNum(Integer adjustNum,Long Id);
	
	/**
	 * 获取最近的一条补货记录的id
	 * @author hebiting
	 * @date 2018年10月31日上午11:15:33
	 * @param vmCode
	 * @param wayNum
	 * @param basicItemId
	 * @param userId
	 * @return
	 */
	public Long getCurrentReplenish(String vmCode,Integer wayNum,Long basicItemId,Long userId);
	
	/**
	 * 查询上一次关门是否补一桶(废弃)
	 * @author hjc
	 * @date 2019年7月11日上午11:15:33
	 * @param vmCode
	 * @param wayNum
	 * @param basicItemId
	 * @param userId
	 * @return
	 */
	public boolean checkIfReplenishOne(String vmCode,Integer wayNum,Long basicItemId,Long userId);
}
 
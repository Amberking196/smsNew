package com.server.module.footmark;

public interface FootmarkDao {

	/**
	 * 足迹
	 * @author hebiting
	 * @date 2018年11月15日上午10:55:53
	 * @param footmarkDao
	 * @return
	 */
	Long insertFootmark(FootmarkBean footmarkBean);
	/**
	 * 用于旧版校准关门 取消自动发校准
	 * @author hjc
	 * @date 2018年11月15日上午10:55:53
	 * @param footmarkDao
	 * @return
	 */
	FootmarkBean findFinalReplenishRecord(String vmCode,Integer wayNum,Long basicItemId,Long userId);
}

package com.server.module.footmark;

import com.server.module.trade.auth.UserVo;
import com.server.util.stateEnum.ModeEnum;

public interface FootmarkService {

	/**
	 * 足迹
	 * @author hebiting
	 * @date 2018年11月15日上午10:55:53
	 * @param footmarkDao
	 * @return
	 */
	Long insertFootmark(FootmarkBean footmarkBean);
	
	/**
	 * 插入足迹并写入redis
	 * @author hebiting
	 * @date 2018年11月15日上午11:36:20
	 * @param user
	 * @param mode
	 * @param vmCode
	 */
	Long insertFootmarkAndRedis(UserVo user,ModeEnum mode,String vmCode,Long enterId);
	
	/**
	 * 用于旧版校准关门 取消自动发校准
	 * @author hjc
	 * @date 2018年11月15日上午10:55:53
	 * @param footmarkDao
	 * @return
	 */
	FootmarkBean findFinalReplenishRecord(String vmCode,Integer wayNum,Long basicItemId,Long userId);
	
}

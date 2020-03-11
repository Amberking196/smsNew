package com.server.module.zfb_trade.module.dooroperation;

import com.server.module.zfb_trade.bean.DoorOperateHistoryBean;

public interface AliDoorOperationDao {

	/**
	 * @Description: 添加开门记录 
	 */
	public DoorOperateHistoryBean save(DoorOperateHistoryBean doorOperateHistory);
	/**
	 * 更新开关门记录
	 * @author hebiting
	 * @date 2018年6月12日上午10:28:24
	 * @param doorOperateHistory
	 * @return
	 */
	public boolean update(DoorOperateHistoryBean doorOperateHistory);
	
	/**
	 * 仅获取该开关门记录中的货道号
	 * @author hebiting
	 * @date 2018年12月25日下午2:43:09
	 * @param doorOperateId
	 * @param customerId
	 * @return
	 */
	public Integer get(Long doorOperateId,Long customerId);
	
	/**
	 * 根据id获取开门记录信息
	 * @author hebiting
	 * @date 2019年1月7日上午8:42:13
	 * @param id
	 * @return
	 */
	public DoorOperateHistoryBean getById(Long id);
	/**
	 * 根据机器编号获取开门记录信息
	 * @author hebiting
	 * @date 2019年2月1日上午10:16:47
	 * @param vmCode
	 * @param wayNumber
	 * @return
	 */
	public DoorOperateHistoryBean getByVmCode(String vmCode);
	
}

package com.server.module.activeDegree;

import com.server.util.stateEnum.RegisterFlowEnum;

public interface UserActiveDegreeDao {

	/**
	 * 插入注册流程表
	 * @author hebiting
	 * @date 2018年12月11日下午2:31:24
	 * @param registerBean
	 * @return
	 */
	public RegisterFlowBean save(RegisterFlowBean registerBean);
	
	/**
	 * 更新注册流程表
	 * @author hebiting
	 * @date 2018年12月11日下午3:00:03
	 * @param customerId
	 * @param event
	 * @return
	 */
	public boolean update(Long customerId,RegisterFlowEnum event);
	
	/**
	 * 购买完成后更新注册流程表
	 * @author hebiting
	 * @date 2018年12月11日下午4:42:25
	 * @param customerId
	 * @param buyNum
	 * @return
	 */
	public boolean update(Long customerId,Integer buyNum);
}

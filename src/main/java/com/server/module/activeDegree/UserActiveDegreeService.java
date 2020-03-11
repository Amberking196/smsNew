package com.server.module.activeDegree;

import com.server.util.stateEnum.RegisterFlowEnum;

public interface UserActiveDegreeService {


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
	 * 更新注册流程表
	 * @author hebiting
	 * @date 2018年12月11日下午3:44:37
	 * @param openId
	 * @param event
	 * @return
	 */
	public boolean update(String openId,RegisterFlowEnum event);
}

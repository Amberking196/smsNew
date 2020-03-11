package com.server.module.zfb_trade.module.vmbase;



public interface AliVendingMachinesService {

	/**
	 * 根据机器编码查询售货机基础信息
	 * @author hebiting
	 * @date 2018年5月27日下午8:42:49
	 * @param vmCode
	 * @return
	 */
	public VendingMachinesBaseBean queryOneByVmCode(String vmCode);
	
	/**
	 * 获取机器基础信息
	 * @author hebiting
	 * @date 2018年11月20日下午2:43:06
	 * @return
	 */
	public VmbaseInfoDto getBaseInfo(String vmCode);
}

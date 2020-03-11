package com.server.module.machine;

public interface MachineInfoService {

	/**
	 * 获取机器所属公司id
	 * @author hebiting
	 * @date 2018年9月28日上午10:33:39
	 * @param vmCode
	 * @return
	 */
	public Integer getCompanyByVmcode(String vmCode);

	/**
	 * 根据售卖机code查询售卖机信息
	 * @param id
	 * @return  VendingMachinesBean
	 */
	VendingMachinesInfoBean findVendingMachinesByCode(String code);
}

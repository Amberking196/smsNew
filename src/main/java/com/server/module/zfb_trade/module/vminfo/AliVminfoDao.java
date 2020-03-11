package com.server.module.zfb_trade.module.vminfo;

import java.math.BigDecimal;
import java.util.List;

import com.server.module.zfb_trade.module.replenish.ReplenishForm;

public interface AliVminfoDao {

	/**
	 * 根据条件查询售货机信息
	 * @author hebiting
	 * @date 2018年5月27日下午8:44:15
	 * @param condition
	 * @param companyIds
	 * @param machineType
	 * @param state
	 * @return
	 */
	List<VminfoDto> queryVMList(String condition,String companyIds,Integer machineType ,Integer state);
	/**
	 * 根据线路负责人，查询所管辖的所有机器
	 * @author hebiting
	 * @date 2018年5月27日下午8:44:18
	 * @param condition
	 * @param dutyId
	 * @param machineType
	 * @param state
	 * @return
	 */
	List<VminfoDto> queryOwnVMList(String condition,Long dutyId,Integer machineType ,Integer state);
	
	/**
	 * 查询补货机器
	 * @author hebiting
	 * @date 2018年6月4日下午2:40:33
	 * @return
	 */
	List<VminfoDto> queryReplenishVMList(ReplenishForm replenishForm);
	
	/**
	 * 查询所有机器总数
	 * @author hebiting
	 * @date 2018年6月4日下午3:36:26
	 * @return
	 */
	Integer queryAllMachinesNum(Integer companyId,String companyIds);
	
	/**
	 * 根据vmCode查询机器信息
	 * @author hebiting
	 * @date 2018年6月5日上午8:33:54
	 * @param vmCode
	 * @return
	 */
	VminfoDto queryByVmCode(String vmCode);
	
}

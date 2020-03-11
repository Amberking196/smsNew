package com.server.module.zfb_trade.module.vmway;

import java.util.List;

import com.server.module.commonBean.ItemInfoDto;
import com.server.module.trade.vendingMachinesWayItem.WayValidatorDto;

public interface AliVmwayDao {

	/**
	 * 查询当前货道信息
	 * @author hebiting
	 * @date 2018年4月26日下午6:53:36
	 * @param vmCode
	 * @param wayNo
	 * @return
	 */
	public VendingMachinesWayBean queryByWayAndVmcode(String vmCode , Integer wayNo);
	/**
	 * 根据售货机编码查询所有货道信息
	 * @author hebiting
	 * @date 2018年5月27日下午8:46:17
	 * @param vmCode
	 * @return
	 */
	public List<VendingMachinesWayBean> queryByWayAndVmcode(String vmCode);
	
	
	/**
	 * 获取一个货道的商品信息
	 * @author hebiting
	 * @date 2018年9月29日下午4:03:14
	 * @param vmCode
	 * @param wayNum
	 * @return
	 */
	WayValidatorDto getOneWayInfo(String vmCode,Integer wayNum);
	/**
	 * 获取该货道的商品信息
	 * @author hebiting
	 * @date 2018年12月5日上午9:04:09
	 * @param vmCode
	 * @param wayNum
	 * @return
	 */
	List<ItemInfoDto> getOneWayItemInfo(String vmCode,Integer wayNum);
}

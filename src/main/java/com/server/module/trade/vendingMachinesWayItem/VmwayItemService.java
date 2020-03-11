package com.server.module.trade.vendingMachinesWayItem;

import java.util.List;
import java.util.Map;

import com.server.module.commonBean.ItemInfoDto;

public interface VmwayItemService {

	/**
	 * 获取改变的商品信息
	 * @author hebiting
	 * @date 2018年9月4日下午5:06:23
	 * @param itemChangeDto
	 * @return
	 */
	ItemChangeDto getChangeInfo(ItemChangeDto itemChangeDto);
	
	/**
	 * 获取多货道商品信息
	 * @author hebiting
	 * @date 2018年9月12日下午4:33:16
	 * @param vmCode
	 * @return
	 */
	List<MoreGoodsWayDto> getWayInfo(String vmCode,Integer type);
	
	/**
	 * 更新货道信息
	 * @author hebiting
	 * @date 2018年9月21日上午11:40:28
	 * @param vmCode
	 * @param wayNum
	 * @param orderNum
	 * @param num
	 * @return
	 */
	boolean updateWayInfo(String vmCode,Integer wayNum,Integer orderNum,Integer num);
	
	
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
	 * 仅获取商品信息
	 * @author hebiting
	 * @date 2018年12月3日上午10:48:17
	 * @param vmCode
	 * @param wayNum
	 * @return
	 */
	List<ItemInfoDto> getOneWayItmeInfo(String vmCode,Integer wayNum);
	
	/**
	 * 根据出厂编号获取同层可放多类型商品的机器的商品规格命令
	 * @author hebiting
	 * @date 2018年9月4日上午10:27:22
	 * @param factnum
	 * @return
	 */
	public String getMultilayerCommand(String factnum);
}

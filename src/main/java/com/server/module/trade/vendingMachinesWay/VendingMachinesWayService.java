package com.server.module.trade.vendingMachinesWay;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.server.module.trade.vendingMachinesWayItem.MoreGoodsWayDto;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-04-12 14:04:38
 */
public interface VendingMachinesWayService {

	public ReturnDataUtil listPage(VendingMachinesWayCondition condition);

	public List<VendingMachinesWayBean> list(VendingMachinesWayCondition condition);

	public boolean update(VendingMachinesWayBean entity);

	public boolean del(Object id);

	public VendingMachinesWayBean get(Object id);

	public VendingMachinesWayBean add(VendingMachinesWayBean entity);
    
	public List<WayDto> listAll(String vmCode,int type);
	
	public ReturnDataUtil editWayAndItem(BindItemDto dto);
	
	public ReturnDataUtil statisticsWayNum(StatisticsWayNumCondition condition);
	
	public ReturnDataUtil checkAdd(VendingMachinesWayBean entity);
	
	/**
	 * 获取售货机 温度
	 * @param vmCode
	 * @return
	 */
	public String findTemperature(String vmCode);
	/**
	 * 获取售货机 音量
	 * @param vmCode
	 * @return
	 */
	public String findVolume(String vmCode);
	
	/**
	 * 获取多货道商品信息
	 * @author hebiting
	 * @date 2018年9月12日下午4:33:16
	 * @param vmCode
	 * @return
	 */
	List<MoreGoodsWayDto> getWayInfo(String vmCode,Integer type);
	
	/**
	 * 根据售货机出厂编号 得到对应机器货道的商品规格 组成一个字符串 返回
	 */
	public String findItemStandard(String factnum);
}

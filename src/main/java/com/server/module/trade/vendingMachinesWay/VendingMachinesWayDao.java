package com.server.module.trade.vendingMachinesWay;
import java.util.List;
import java.util.Map;

import com.server.module.trade.vendingMachiesItem.VendingMachinesItemBean;
import com.server.module.trade.vendingMachinesWayItem.MoreGoodsWayDto;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-04-12 14:04:38
 */
public interface VendingMachinesWayDao {

	public ReturnDataUtil listPage(VendingMachinesWayCondition condition);

	public List<VendingMachinesWayBean> list(VendingMachinesWayCondition condition);

	public boolean update(VendingMachinesWayBean entity);

	public boolean delete(Object id);

	public VendingMachinesWayBean get(Object id);

	public VendingMachinesWayBean insert(VendingMachinesWayBean entity);

    public List<WayDto> listAll(String vmCode);
    
	public boolean editWayAndItem(BindItemDto dto);
	
	public ReturnDataUtil statisticsWayNum(StatisticsWayNumCondition condition);
	
	public long selectCount(String sql,List<Object> list);

	/**
	 * 获取多货道商品信息
	 * @author hebiting
	 * @date 2018年9月12日下午4:33:16
	 * @param vmCode
	 * @return
	 */
	List<MoreGoodsWayDto> getWayInfo(String vmCode);
	
	/**
	 * 更新货道商品数量
	 * @author hebiting
	 * @date 2018年11月9日上午11:45:37
	 * @param vmCode
	 * @param wayNum
	 * @param replenishNum
	 * @return
	 */
	public boolean updateWayNum(String vmCode , Integer wayNum,Integer replenishNum);

	/**
	 * 根据售货机出厂编号 得到对应机器货道的商品规格 组成一个字符串 返回
	 */
	public List<String> findItemStandard(String factnum);
}

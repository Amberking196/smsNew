package com.server.module.trade.order;

import com.server.module.footmark.FootmarkRedisBean;
import com.server.module.store.OrderBean;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.util.ResultBean;
import com.server.util.ReturnDataUtil;

import java.util.List;
import java.util.Map;

public interface OrderService {
	//关门
	public ResultBean<?>  closeDoor(MachineInfo machineInfo,String params);

	/**
	 * 新机器关门调用
	 * @author hebiting
	 * @date 2018年9月18日上午9:06:13
	 * @return
	 */
	public ResultBean<?> moreGoodsOrder(String param);
	
	//发送请求开门  远程
	public String sendCommandToOpenDoor1(Map<String, Object> result , String vmCode,int way);
	//我的订单列表  分页
	public ReturnDataUtil myOrderList(int curPage,Long customerId);
	public Map<String,Object> getOrder(Long orderId);
	public void paySuccessOrder(String outTradeNo,String transactionId);
	public void insertEntrustInfo(Map map);
	public boolean isEntrust(Integer companyId , String openid);
	public boolean jieyue(Map map);
	public boolean isHaveNotPayOrder(Long id);
	
	public ResultBean<String> resetWaysNum(String params);
	/**
	 * 根据payCode查询订单的部分信息
	 * @author hhh
	 * @date 2019年3月16日 上午11:31:36
	 * @param payCode
	 * @return
	 */
	public OrderBean getMessageByPayCode(String payCode);
	/**
	 * 微信完成商城订单后的回调，用以确认订单是否完成支付，并更新状态
	 * @param type 0 普通订单  其他为团购订单
	 * @return
	 */
	public void paySuccessStroeOrder(Integer distributionModel,String outTradeNo, String transactionId,Integer Type);
	
	/**
	 * 根据product查itemName
	 * @param xml
	 * @return
	 */
	public List<String> findItemName(String product);
	
	String getFactoryNumberByVmCode(String vmCode);
	
	Integer getVersionByFactoryNum(String factoryNum);
	
	Integer getVersionByVmCode(String vmCode);

	/**
	 * 支付失败进行短信提醒
	 * @author hebiting
	 * @date 2018年10月19日上午9:26:14
	 * @param customerId
	 */
	void payFailSendMsg(Long customerId);

	
	/**
	 * 根据根据product查shoppingGoogsName
	 * @author why
	 * @date 2018年11月10日 上午11:53:30 
	 * @param product
	 * @return
	 */
	public String findShoppingGoogsName(String product);

	
	/**
	 * 获取机器基础信息
	 * @author hebiting
	 * @date 2018年11月8日下午2:45:45
	 * @param vmCode
	 * @return
	 */
	public Map<String,Object> getBaseInfoByVmCode(String vmCode);


	public void payRecord(MachineInfo machineInfo, UserVo userVo, Long operateHistoryId, String items,FootmarkRedisBean footmark,String remark) ;

	public void payRecordForFree(MachineInfo machineInfo, UserVo userVo, Long operateHistoryId, String items,FootmarkRedisBean footmark,String remark) ;

	/**
	 * 修改拼团订单状态
	 * @author why
	 * @date 2019年1月15日 下午2:12:03 
	 * @param outTradeNo
	 */
	public void paySpellgroupStroeOrder(String outTradeNo,Integer type);

	/**
	 * 根据payCode查询订单的配送方式
	 * @author hhh
	 * @date 2019年12月30日 上午11:31:36
	 * @param payCode
	 * @return
	 */
	public Integer getDistributionModelByPayCode(String payCode);

	/**
	 *  更新用户订单配送状态
	 * @return
	 */
	public boolean delivering(Long orderId);

	/**
	 * 支付宝完成商城订单后的回调，用以确认订单是否完成支付，并更新状态
	 * type 0 普通订单  其他为团购订单
	 * @return
	 */
	public void updatePayState(String ptCode, String payCode, Integer state, Integer Type);
}

package com.server.module.payRecord;

import java.util.List;

import com.server.module.zfb_trade.bean.MsgDto;
import com.server.util.ReturnDataUtil;

public interface AliPayRecordService {

	/**
	 * 查询是否有未支付订单
	 * @author hebiting
	 * @date 2018年4月26日下午3:30:28
	 * @param alipayUserId
	 * @return
	 */
	public boolean booleanIsArrearage(Long customerId);
	/**
	 * 更新
	 * @author hebiting
	 * @date 2018年4月27日下午5:35:34
	 * @param payCode
	 * @param ptCode
	 * @return
	 */
	boolean updatePayRecord(String payCode,String ptCode);
	
	/**
	 * 查询消费信息
	 * @author hebiting
	 * @date 2018年4月27日下午9:29:56
	 * @param payCode
	 * @return
	 */
	MsgDto findMsgDto(String payCode);
	/**
	 * 查询订单信息
	 * @author hebiting
	 * @date 2018年4月28日下午1:58:49
	 * @param vmCode
	 * @param customerId
	 * @param state
	 * @return
	 */
	List<PayRecordDto> findPayRecord(PayRecordForm payRecordForm);
	/**
	 * 根据id查询订单信息
	 * @author hebiting
	 * @date 2018年5月8日下午4:19:54
	 * @param payRecordId
	 * @return
	 */
	PayRecordDto findPayRecordById(Long payRecordId);
	/**
	 * 修改订单状态
	 * @author hebiting
	 * @date 2018年5月8日下午10:02:53
	 * @param payRecordId
	 * @param state
	 * @return
	 */
	boolean updatePayState(String ptCode,String payCode,Integer state);
	
	/**
	 * 根据支付编码查询交易记录
	 * @author hebiting
	 * @date 2018年4月27日下午5:43:48
	 * @param payCode
	 * @return
	 */
	PayRecordBean findPayRecordByPayCode(String payCode);
	/**
	 * 更新销售记录信息
	 * @author hebiting
	 * @date 2018年5月10日上午10:34:42
	 * @return
	 */
	boolean update(PayRecordBean payRecordBean);
	

	/**
	 * 查询记录总条数
	 * @param payRecordForm
	 * @return SumPayRecordDto记录数
	 */
	SumPayRecordDto findPayRecordNum(PayRecordForm payRecordForm);
	
	/**
	 * 获取待支付订单信息
	 * @author hebiting
	 * @date 2018年10月26日下午2:14:56
	 * @param id
	 * @return
	 */
	public PayRecordDto getOrderInfo(Long id);
	
	/**
	 * 获取用户所有订单
	 * @author hebiting
	 * @date 2018年10月26日下午3:43:44
	 * @param customerId
	 * @param state
	 * @return
	 */
	public List<PayRecordDto> findUserAllOrder(Long customerId , Integer state);
	
	
	/**
	 * 获取用户订单信息
	 * @author hebiting
	 * @date 2018年11月14日下午4:20:28
	 * @param customerId
	 * @param state
	 */
	public List<NewPayRecordDto> getCustomerOrder(Long customerId , Integer state,Integer isShowAll);
	
	/**
	 * 获取用户订单详情信息
	 * @author hjc
	 * @date 2018年12月18日下午4:20:28
	 * @param payCodeOrName
	 */
	public ReturnDataUtil findMachineHistory(PayRecordForm customerMachineForm);
	
}

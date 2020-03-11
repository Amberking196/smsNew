package com.server.module.refund;

import java.math.BigDecimal;
import java.util.List;


public interface RefundService {

	/**
	 * 是否为退款负责人
	 * @author hebiting
	 * @date 2018年10月22日下午2:54:34
	 * @param operateId
	 * @param phone
	 * @return
	 */
	public boolean isPrincipal(Long operateId,String phone);
	
	/**
	 * 插入，生成退款记录
	 * @author hebiting
	 * @date 2018年10月22日下午3:07:13
	 * @param refundBean
	 * @return
	 */
	public Long insertRefundRecord(RefundRecordBean refundBean);
	
	/**
	 * 根据订单类型查找订单信息
	 * @author hebiting
	 * @date 2018年10月23日上午10:08:15
	 * @param payCode
	 * @param orderType 1:机器订单;2:商城普通订单;3:团购订单
	 */
	public RefundOrderInfo findOrder(String payCode,Integer orderType);
	
	/**
	 * 查询团购退款信息
	 * @author hebiting
	 * @date 2018年10月24日上午11:02:35
	 * @param groupId
	 * @return
	 */
	public List<RefundDto> findGroupBuyOrder(Integer groupId);
	
	/**
	 * 更新退款后订单状态
	 * @author hebiting
	 * @date 2018年10月24日上午11:28:53
	 * @param payCode
	 * @return
	 */
	public boolean updateOrderRefundState(List<String> payCodeList,Integer orderType);
	
	/**
	 * 更新拼团状态
	 * @author hebiting
	 * @date 2018年10月25日上午8:39:22
	 * @param groupId
	 * @return
	 */
	public boolean updateGroupBuyState(Integer groupId,Integer state);
	
	/**
	 * 查询负责人电话
	 * @author hebiting
	 * @date 2018年10月25日下午3:20:35
	 * @param loginInfoId
	 * @return
	 */
	public String getPrincipalInfoById(Long loginInfoId);
	
	/**
	 * 退款申请
	 * @author hebiting
	 * @date 2018年10月31日上午8:58:46
	 * @return
	 */
	public Long insertRefundApplication(RefundApplicationBean refundApp);
	
	/**
	 * 获取退款申请信息
	 * @author hebiting
	 * @date 2018年10月31日下午1:36:42
	 * @param payCode
	 * @return
	 */
	public List<RefundApplicationBean> getRefundApplication(String payCode);
	
	/**
	 * 根据订单号获取用户手机号
	 * @author hebiting
	 * @date 2018年10月31日下午5:27:10
	 * @param payCode
	 * @return
	 */
	public String getPhoneByPayCode(String payCode);
	
	/**
	 * 判断订单是否可退款
	 * @author hebiting
	 * @date 2018年11月27日上午11:34:54
	 * @param payCode
	 * @param refundPrice
	 * @return
	 */
	public RefundApplicationDto judgeOrderRefund(String payCode,BigDecimal refundPrice);

	/**
	 * 删除用户提水券
	 * @param:
	 * @return:
	 * @auther: why
	 * @date: 2019/1/17 16:29
	 */
	public Long deleteCarry(Long orderId);
}

package com.server.module.trade.memberOrder;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-09-26 14:36:43
 */
public interface MemberOrderService {

	/**
	 * 微信完成商城购买会员订单后的回调，用以确认订单是否完成支付，并更新状态
	 * @author why
	 * @date 2018年9月26日 下午3:32:53 
	 * @param outTradeNo
	 * @param transactionId
	 * @return
	 */
	public boolean paySuccessMemberOrder(String outTradeNo, String transactionId);
	
	/**
	 * 添加为会员
	 * @author why
	 * @date 2018年9月26日 下午3:50:52 
	 * @param entity
	 * @return
	 */
	public boolean addMember(MemberOrderBean entity);
	
	/**
	 * 获取订单信息
	 * @author why
	 * @date 2018年9月26日 下午3:58:44 
	 * @param payCode
	 * @return
	 */
	public MemberOrderBean getMemberOrder(String payCode);
	
	/**
	 * 修改用户余额
	 * @author why
	 * @date 2018年10月8日 下午2:58:33 
	 * @param entity
	 * @return
	 */
	public boolean updateCustomerBalance(MemberOrderBean entity);

	/**
	 * 增加用户充值订单
	 * @param: entity
	 * @return:
	 * @auther: why
	 * @date: 2018/12/1 16:57
	 */
	public ReturnDataUtil add(MemberOrderBean entity);

}


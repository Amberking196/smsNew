package com.server.module.member;

import java.math.BigDecimal;

public interface MemberService {

	
	/**
	 * 获取用户是否为会员信息
	 * @author hebiting
	 * @date 2018年9月29日上午9:06:44
	 * @param customerId
	 * @return
	 */
	public MemberBean getMemberInfo(Long customerId);
	
	/**
	 * 获取会员余额
	 * @author hebiting
	 * @date 2018年10月10日下午3:43:26
	 * @param customerId
	 * @return
	 */
	public BigDecimal getMemberMoney(Long customerId);
	
	/**
	 * 使用余额扣款后更新余额
	 * @author hebiting
	 * @date 2018年10月10日下午3:54:45
	 * @param customerId
	 * @param money
	 * @return
	 */
	public boolean updateMemberMoney(Long customerId,BigDecimal money);
	
	/**
	 * 插入使用会员余额记录
	 * @author hebiting
	 * @date 2018年11月14日上午10:35:27
	 * @param memberLog
	 * @return
	 */
	public Long insertMemberUseLog(MemberUseLog memberLog);
}

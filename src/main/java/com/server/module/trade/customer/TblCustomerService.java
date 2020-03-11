package com.server.module.trade.customer;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-04-16 15:06:05
 */
public interface TblCustomerService {

	public ReturnDataUtil listPage(TblCustomerCondition condition);

	public List<TblCustomerBean> list(TblCustomerCondition condition);

	public boolean update(TblCustomerBean entity);

	public boolean del(Object id);

	public TblCustomerBean get(Object id);

	public TblCustomerBean add(TblCustomerBean entity);

	/**
	 * 顾客是否第一次购买
	 * 
	 * @author hebiting
	 * @date 2018年5月29日上午9:03:16
	 * @param customerId
	 * @return
	 */
	public Integer isFirstBuy(Long customerId);

	/**
	 * 顾客是否商城第一次购买
	 * 
	 * @author hjc
	 * @date 2018年10月26日上午9:03:16
	 * @param customerId
	 * @return
	 */
	public Integer isStoreFirstBuy(Long customerId);
	
	/**
	 * 更新用户vmCode
	 * 
	 * @author hebiting
	 * @date 2018年8月13日下午5:47:08
	 * @param customerId
	 * @param vmCode
	 * @return
	 */
	public boolean updateCusVmCode(Long customerId, String vmCode);

	/**
	 * 根据公司筛选用户列表
	 * 
	 * @author hjc
	 * @date 2018年9月25日下午5:10:15
	 * @param companyId
	 * @return
	 */
	public List<TblCustomerBean> getCustomerByCompanyId(Integer companyId);

	/**
	 * 查询会员信息
	 * @author why
	 * @date 2018年10月9日 下午2:14:41 
	 * @param customerId
	 * @return
	 */
	public TblCustomerBean findBen(Long customerId);

	/**
 	* 根据id获取用户
 	* @author hjc
 	* @date 2018年10月8日下午5:10:15
 	* @param customerId
 	* @return
 	*/
	public TblCustomerBean getCustomerById(Long customerId);
	
	/**
 	* 根据id发送公众号客服信息
 	* @author hjc
 	* @date 2018年10月19日下午5:10:15
 	* @param openId
 	* @return
 	*/
	public void sendWechatMessage(String openId,String message);


	/**
	 * 我的邀请奖励
	 * @return:
	 * @auther: why
	 * @date: 2018/12/1 15:23
	 */
	public List<TblCustomerBean> myInviteRewards();
	
	/**
	 * 获取用户信息 用于判断是否可以进行余额充值
	 * @author why
	 * @date 2019年1月23日 下午2:33:35 
	 * @param customerId
	 * @return
	 */
	public TblCustomerBean  getCustomerBeanById(Long customerId);
	
	/**
	 * 游戏中奖后 更新用户积分或者余额
	 * @author why
	 * @date 2019年3月4日 上午10:07:16 
	 * @param customerId
	 * @param rewards
	 * @param type 1 积分  2 余额
	 * @return
	 */
	public boolean updateCustomerBean(Long customerId,Long rewards,Integer type);

}

package com.server.module.trade.customer;

import java.util.List;

import com.server.module.trade.customer.wxcustomer.WxCustomerBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-04-16 15:06:05
 */
public interface TblCustomerDao {

	public ReturnDataUtil listPage(TblCustomerCondition condition);

	public List<TblCustomerBean> list(TblCustomerCondition condition);

	public boolean update(TblCustomerBean entity);

	public boolean delete(Object id);

	public TblCustomerBean get(Object id);

	public TblCustomerBean insert(TblCustomerBean entity);

	public TblCustomerBean getCustomer(Integer type, String openId);

	public TblCustomerBean getCustomerByPhone(String phone);
	
	public boolean deleteUser(Long id);

	/**
	 * 顾客是否第一次购买
	 * @author hebiting
	 * @date 2018年5月29日上午9:03:16
	 * @param customerId
	 * @return
	 */
	public Integer isFirstBuy(Long customerId);
	
	/**
	 * 顾客是否商城第一次购买
	 * @author hjc
	 * @date 2018年10月26日上午9:03:16
	 * @param customerId
	 * @return
	 */
	public Integer isStoreFirstBuy(Long customerId);
	
	/**
	 * 更新用户vmCode
	 * @author hebiting
	 * @date 2018年8月13日下午5:10:15
	 * @param customerId
	 * @param vmCode
	 * @return
	 */
	public boolean updateCusVmCode(Long customerId,String vmCode);
	
	
	/**
	 * 更新用户vmCode
	 * @author hebiting
	 * @date 2018年8月13日下午5:10:15
	 * @param customerId
	 * @param vmCode
	 * @return
	 */
	public boolean cusVmCodeIsNull(Long customerId);
	
	/**
	 * 根据公司筛选用户列表
	 * @author hjc
	 * @date 2018年9月25日下午5:10:15
	 * @param companyId
	 * @return
	 */
	public List<TblCustomerBean> getCustomerByCompanyId(Integer companyId);
	/**
	 * 更新在公众号注册的支付宝用户
	 * @author hjc
	 * @date 2018年9月25日下午5:10:15
	 * @param TblCustomerBean WxCustomerBean
	 * @return
	 */
	public boolean updateAliUser(TblCustomerBean entity,WxCustomerBean wxCustomerBean);

	/**
	 * 更新用户关注公众号状态(未使用)
	 * @author hjc
	 * @date 2018年9月25日下午5:10:15
	 * @param openIdList
	 * @return
	 */
	public boolean updateUserFollow(List<String> openIdList);
	

	/**
	 * 根据id获取用户
	 * @author hjc
	 * @date 2018年10月8日下午5:10:15
	 * @param customerId
	 * @return
	 */
	public TblCustomerBean getCustomerById(Long customerId);
	

	/**
	 * 查询会员信息
	 * @author why
	 * @date 2018年10月8日 上午10:58:11 
	 * @param customerId
	 * @return
	 */
	public TblCustomerBean findBen(Long customerId);
	
	/**
	 * 获取用户手机
	 * @author hebiting
	 * @date 2018年10月20日上午11:50:52
	 * @param customerId
	 * @return
	 */
	public String getPhoneByCustomerId(Long customerId);

	/**
	 * 我的邀请奖励
	 * @return:
	 * @auther: why
	 * @date: 2018/12/1 15:04
	 */
	public List<TblCustomerBean> myInviteRewards();

	/**
	 * 获取用户信息 用于判断是否可以进行余额充值
	 * @author why
	 * @date 2019年1月23日 下午2:29:44 
	 * @param customerId
	 * @return
	 */
	public TblCustomerBean  getCustomerBeanById(Long customerId);
	/**
	 * 获取用户最后一次购买商品所在的售货机
	 * @author hjc
	 * @date 2019年3月8日 下午11:29:44 
	 * @param customerId
	 * @return
	 */
	public String getCustomerLastVmcode(Long customerId);
	
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

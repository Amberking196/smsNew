package com.server.module.coupon;

import java.util.List;

import com.server.module.zfb_trade.module.customer.CustomerBean;

public interface CouponService {

	/**
	 * 获取该用户优惠券
	 * @author hebiting
	 * @date 2018年7月16日上午10:01:47
	 * @param customerId
	 * @param vmCode
	 * @return
	 */
	public List<CouponBean> getCoupon(Long customerId,String vmCode,Double money,Long productId);
	
	/**
	 * 使用优惠券后更新状态
	 * @author hebiting
	 * @date 2018年7月16日下午3:11:36
	 * @param couponId
	 * @return
	 */
	public boolean updateCouponCustomer(Long couponCustomerId);
	
	/**
	 * 赠券插入
	 * @author hebiting
	 * @date 2018年7月23日上午9:03:50
	 * @param couCusBean
	 * @return
	 */
	public Integer insertCouponCustomer(CouponCustomerBean couCusBean);
	
	
	/**
	 * 赠券插入  购买优惠券
	 * @author hjc
	 * @date 2018年8月31日上午9:03:50
	 * @param couCusBean
	 * @return
	 */
	public Integer insertCouponCustomerWithSumQuantity(CouponCustomerBean couCusBean);
	
	/**
	 * 根据机器编码获取该机器线路，区域，公司归属
	 * @author hebiting
	 * @date 2018年7月16日上午10:20:29
	 * @param vmCode
	 * @return
	 */
	public MachinesLAC getMachinesLAC(String vmCode);
	
	/**
	 * 获取优惠券信息
	 * @author hebiting
	 * @date 2018年7月23日上午11:25:41
	 * @param couponForm
	 * @return
	 */
	public List<CouponBean> getPresentCoupon(CouponForm couponForm);
	
	/**
	 * 该优惠券是否已领 
	 * @author hebiting
	 * @date 2018年7月24日上午10:59:45
	 * @param customerId
	 * @param couponId
	 * @return boolean 已领：true;未领：false;
	 */
	public boolean isReceive(Long customerId,Long couponId);

	
	/**
	 * 更新用户购水积分
	 * @author hjc
	 * @date 2018年8月17日上午10:59:45
	 * @param customerId
	 * @param num 购买数量
	 * @return boolean 成功：true;失败：false;
	 */
	public boolean updateIntegral(Long customerId,Long num);

	
	/**
	 * 顾客可用优惠券
	 * @author hebiting
	 * @date 2018年8月16日上午11:51:36
	 * @param customerid
	 * @param mlac
	 * @return
	 */
	public List<CouponDto> getAvailableCoupon(Long customerId,MachinesLAC mlac);
	
	/**
	 * 根据优惠券Id获取优惠券信息
	 * @author hebiting
	 * @date 2018年8月22日上午10:08:01
	 * @param couponId
	 * @return
	 */
	public CouponBean getCouponInfo(Long couponId);
	
	/**
	 * 根据优惠券Ids获取优惠券信息
	 * @author hjc
	 * @date 2018年11月6日上午10:08:01
	 * @param couponIds
	 * @return
	 */
	public List<CouponBean> getCouponInfos(String couponIds);
	
	/**
	 * 更新优惠券数量(邀请赠券数增加)
	 * @author hjc
	 * @date 2018年10月8日下午3:11:36
	 * @param couponId customer quantity
	 * @return
	 */
	public boolean updateCouponCustomer(Long couponId,Long customer,Integer quantity);


	/**
	 * 我的优惠券
	 * @param:couponForm
	 * @return:
	 * @auther: why
	 * @date: 2018/12/1 10:32
	 */
	public List<CouponBean> myCoupon(CouponForm couponForm);


	/**
	 * 支付前获取该用户可用优惠券(多商品)
	 * @author hebiting
	 * @date 2018年7月16日上午10:01:47
	 * @param customerId
	 * @param vmCode
	 * @return
	 */
	public MoreGoodsCouponDto getMoreGoodsCoupon(Long customerId,MachinesLAC mlac,Double money, String productIds);

	/**
	 * 发放邀请赠券
	 * @author hebiting
	 * @date 2019年1月29日上午11:12:43
	 * @param customer
	 */
	public void sendInviteCoupon(CustomerBean customer);
}

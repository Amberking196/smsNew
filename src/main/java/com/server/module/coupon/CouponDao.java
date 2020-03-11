package com.server.module.coupon;

import java.util.List;

import com.server.module.product.ShoppingGoodsBean;

public interface CouponDao {

	/**
	 * 支付前获取该用户可用优惠券
	 * @author hebiting
	 * @date 2018年7月16日上午10:01:47
	 * @param customerId
	 * @param vmCode
	 * @return
	 */
	public List<CouponBean> getCoupon(Long customerId,MachinesLAC mlac,Double money,Long productId);
	
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
	 * 顾客可用优惠券
	 * @author hebiting
	 * @date 2018年8月16日上午11:51:36
	 * @param customerid
	 * @param mlac
	 * @return
	 */
	public List<CouponDto> getAvailableCoupon(Long customerId,MachinesLAC mlac);
	
	/**
	 * 根据机器编码获取该机器线路，区域，公司归属
	 * @author hebiting
	 * @date 2018年7月16日上午10:20:29
	 * @param vmCode
	 * @return
	 */
	public MachinesLAC getMachinesLAC(String vmCode);
	
	/**
	 * 使用优惠券后更新状态
	 * @author hebiting
	 * @date 2018年7月16日下午3:11:36
	 * @param couponId
	 * @return
	 */
	public boolean updateCouponCustomer(Long couponCustomerId);
	
	/**
	 * 更新优惠券数量(邀请赠券数增加)
	 * @author hjc
	 * @date 2018年10月8日下午3:11:36
	 * @param couponId customer quantity
	 * @return
	 */
	public boolean updateCouponCustomer(Long couponId,Long customer,Integer quantity);
	
	/**
	 * 更新用户优惠券使用数量
	 * @author hebiting
	 * @date 2018年9月12日上午10:31:00
	 * @param couponId
	 * @param num
	 * @param customer
	 * @return
	 */
	public boolean reduceCoupon(Long couponCustomerId,Integer num);
	
	/**
	 * 赠券插入
	 * @author hebiting
	 * @date 2018年7月23日上午9:03:50
	 * @param couCusBean
	 * @return
	 */
	public Integer insertCouponCustomer(CouponCustomerBean couCusBean);
	
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
	public boolean updateIntegral(Long customerId, Long num);
	
	/**
	 * 根据优惠券Id获取优惠券信息
	 * @author hebiting
	 * @date 2018年8月22日上午10:08:01
	 * @param couponId
	 * @return
	 */
	public CouponBean getCouponInfo(Long couponId);
	
	/**
	 * 根据优惠券Id获取优惠券信息
	 * @author hjc
	 * @date 2018年11月6日上午10:08:01
	 * @param couponIds
	 * @return
	 */
	public List<CouponBean> getCouponInfos(String couponId);
	
	/**
	 * 根据商品名称获取优惠券信息
	 * @author hjc
	 * @date 2018年8月25日上午10:08:01
	 * @param shoppingGoodsBean
	 * @return
	 */
	public CouponBean getCouponInfoByProduct(ShoppingGoodsBean shoppingGoodsBean);
	
	/**
	 * 获取绑定优惠券信息
	 * @author hjc
	 * @date 2018年8月25日上午10:08:01
	 * @param Long customerId, Long couponId
	 * @return
	 */
	public CouponCustomerBean getCouponCustomerBean(Long customerId, Long couponId);
	
	/**
	 * 更新绑定用户的优惠券数量(仅限于购买优惠券的用户)
	 * @author hjc
	 * @date 2018年8月25日上午10:08:01
	 * @param entity
	 * @return
	 */
	Boolean updateCouponCustomerBean(CouponCustomerBean entity);
	
	/**
	 * 赠券插入  购买优惠券
	 * @author hjc
	 * @date 2018年8月31日上午9:03:50
	 * @param couCusBean
	 * @return
	 */
	public Integer insertCouponCustomerWithSumQuantity(CouponCustomerBean couCusBean);


	/**
	 * 我的优惠券
	 * @param:couponForm
	 * @return:
	 * @auther: why
	 * @date: 2018/12/1 10:19
	 */
	public List<CouponBean> myCoupon(CouponForm couponForm);

	
}

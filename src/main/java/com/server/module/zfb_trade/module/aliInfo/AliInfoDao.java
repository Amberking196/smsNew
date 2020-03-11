package com.server.module.zfb_trade.module.aliInfo;

import java.util.List;

public interface AliInfoDao {

	/**
	 * 根据公司id获取生活号信息
	 * @author hebiting
	 * @date 2018年7月7日上午10:56:40
	 * @param companyId
	 * @return
	 */
	public AliInfoBean getAliInfo(Integer companyId);
	
	/**
	 * 获取所有的生活号信息
	 * @author hebiting
	 * @date 2018年7月7日上午10:57:04
	 * @return
	 */
	public List<AliInfoBean> getAllAliInfo();
	
	/**
	 * 根据公司id获取商户信息
	 * @author hebiting
	 * @date 2018年7月7日上午11:02:06
	 * @param companyId
	 * @return
	 */
	public AliMerchantInfoBean getAliMerchantInfo(Integer companyId);
	
	/**
	 * 获取所有商户信息
	 * @author hebiting
	 * @date 2018年7月7日上午11:02:21
	 * @return
	 */
	public List<AliMerchantInfoBean> getAllAliMerchantInfo();
}

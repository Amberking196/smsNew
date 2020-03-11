package com.server.module.trade.config.pay;

import java.util.List;

public interface MerchantInfoDao {
	
	/**
	 * 根据type查询相应的配置信息
	 * @author hebiting
	 * @date 2018年6月29日下午5:02:17
	 * @param type（1：服务商，0：普通商户）
	 * @return
	 */
	List<MerchantInfo> findMerchantInfo(Integer type);
	
	/**
	 * 根据公司id获取用户信息
	 * @author hebiting
	 * @date 2018年11月12日下午4:21:37
	 * @param companyId
	 * @return
	 */
	MerchantInfo findMerchantInfoByCompanyId(Integer companyId);
}

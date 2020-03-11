package com.server.module.trade.submerchant;

public interface SubMerchantDao {

	/**
	 * 根据vmCode查询子商户信息
	 * @author hebiting
	 * @date 2018年5月25日下午2:55:11
	 * @param vmCode
	 * @return
	 */
	public SubMerchantBean queryByVmCode(String vmCode);
}

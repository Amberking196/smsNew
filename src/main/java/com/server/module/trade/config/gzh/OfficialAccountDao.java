package com.server.module.trade.config.gzh;

import java.util.List;

public interface OfficialAccountDao {

	/**
	 * 从数据库中获取公众号配置信息
	 * @author hebiting
	 * @date 2018年7月5日下午2:40:19
	 * @return
	 */
	List<OfficialAccountInfo> findOfficialAccountInfo();
	
	/**
	 * 根据公司id获取公众号信息
	 * @author hebiting
	 * @date 2018年11月12日下午5:06:32
	 * @param companyId
	 * @return
	 */
	OfficialAccountInfo findOfficialAccountInfoByCompanyId(Integer companyId);
}

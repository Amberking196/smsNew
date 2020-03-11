package com.server.module.zfb_trade.module.company;

import java.util.List;

public interface AliCompanyService {
	

	/**
	 * 查询该公司及所有的子公司信息
	 * @author hebiting
	 * @date 2018年5月27日下午8:32:00
	 * @param companyId
	 * @return
	 */
	List<CompanyBean> findAllSonCompany(Integer companyId);
	
	/**
	 * 查询该公司及子公司所有的companyId
	 * @param parentId
	 * @return
	 */
	List<Integer> findAllSonCompanyId(Integer parentId);
}

package com.server.company;

import java.util.List;

public interface MachinesService {

	/**
	 * 根据机器编码获取公司id
	 * @author hebiting
	 * @date 2018年7月10日下午3:18:56
	 * @return
	 */
	public Integer getCompanyIdByVmCode(String vmCode);
	
	/**
	 * 根据payCode查询公司id
	 * @author hebiting
	 * @date 2018年7月10日下午5:01:57
	 * @param payCode
	 * @return
	 */
	public Integer getCompanyIdByPayCode(String payCode);
	
	/**
	 * 是否为子公司或者本公司
	 * @author hebiting
	 * @date 2018年8月31日上午9:51:09
	 * @param sonCompany
	 * @param faCompany
	 * @return
	 */
	public boolean isSonCompany(Integer sonCompany,Integer faCompany);
	/**
	 * 是否为补水
	 * @author hebiting
	 * @date 2018年9月20日上午9:51:09
	 * @param sonCompany
	 * @return
	 */
	public List<Integer> getOtherCompanyIdByVmCode(String payCode);
	/**
	 * 是否是补水公司人员
	 * @author hebiting
	 * @date 2018年9月20日下午5:15:09
	 * @param vmCode
	 * @param companyId
	 * @return
	 */
	public boolean isReplenishCompanyMan(String vmCode,Integer companyId);
	
	/**
	 * 商城 根据payCode查询公司id
	 * @author why
	 * @date 2019年5月21日 下午3:51:29 
	 * @param payCode
	 * @return
	 */
	public Integer getCompanyIdByPayCodeStore(String payCode);
	
}

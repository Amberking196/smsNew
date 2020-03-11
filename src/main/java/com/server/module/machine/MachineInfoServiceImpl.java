package com.server.module.machine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MachineInfoServiceImpl implements MachineInfoService{

	private final static Logger log = LogManager.getLogger(MachineInfoServiceImpl.class);
	@Autowired
	private MachineInfoDao machineInfoDao;

	@Override
	public Integer getCompanyByVmcode(String vmCode) {
		//log.info("<MachineInfoServiceImpl--getCompanyByVmcode--start>");
		Integer companyId = machineInfoDao.getCompanyByVmcode(vmCode);
		//log.info("<MachineInfoServiceImpl--getCompanyByVmcode--end>");
		return companyId;
	}

	/**
	 * 根据售卖机code查询售卖机信息
	 */
	@Override
	public VendingMachinesInfoBean findVendingMachinesByCode(String code) {
		return machineInfoDao.findVendingMachinesByCode(code);
	}

}

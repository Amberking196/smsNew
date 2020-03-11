package com.server.module.zfb_trade.module.vmbase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("aliVendingMachinesService")
public class AliVendingMachinesServiceImpl implements AliVendingMachinesService{

	public static Logger log = LogManager.getLogger(AliVendingMachinesServiceImpl.class); 
	@Autowired
	@Qualifier("aliVendingMachinesDao")
	private AliVendingMachinesDao vendingMachinesDao;
	
	@Override
	public VendingMachinesBaseBean queryOneByVmCode(String vmCode) {
		//log.info("<AliVendingMachinesServiceImpl--queryOneByVmCode--start>");
		VendingMachinesBaseBean queryOneByVmCode = vendingMachinesDao.queryOneByVmCode(vmCode);
		//log.info("<AliVendingMachinesServiceImpl--queryOneByVmCode--end>");
		return queryOneByVmCode;
	}

	@Override
	public VmbaseInfoDto getBaseInfo(String vmCode) {
		//log.info("<AliVendingMachinesServiceImpl--getBaseInfo--start>");
		VmbaseInfoDto baseInfo = vendingMachinesDao.getBaseInfo(vmCode);
		//log.info("<AliVendingMachinesServiceImpl--getBaseInfo--end>");
		return baseInfo;
	}

}

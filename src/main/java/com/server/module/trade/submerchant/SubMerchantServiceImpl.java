package com.server.module.trade.submerchant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubMerchantServiceImpl implements SubMerchantService{

	private static final Logger log = LogManager.getLogger(SubMerchantServiceImpl.class);
	
	@Autowired
	private SubMerchantDao subMerchantDao;

	@Override
	public SubMerchantBean queryByVmCode(String vmCode) {
		log.info("<SubMerchantServiceImpl--queryByVmCode--start>");
		SubMerchantBean queryByVmCode = subMerchantDao.queryByVmCode(vmCode);
		log.info("<SubMerchantServiceImpl--queryByVmCode--end>");
		return queryByVmCode;
	}
}

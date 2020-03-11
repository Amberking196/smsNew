package com.server.module.zfb_trade.module.aliInfo;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AliInfoServiceImpl implements AliInfoService{

	private Logger log = LogManager.getLogger(AliInfoServiceImpl.class);
	
	@Autowired
	private AliInfoDao aliInfoDao;

	@Override
	public AliInfoBean getAliInfo(Integer companyId) {
		log.info("<AliInfoServiceImpl--getAliInfo--start>");
		AliInfoBean aliInfo = aliInfoDao.getAliInfo(companyId);
		log.info("<AliInfoServiceImpl--getAliInfo--end>");
		return aliInfo;
	}

	@Override
	public List<AliInfoBean> getAllAliInfo() {
		log.info("<AliInfoServiceImpl--getAllAliInfo--start>");
		List<AliInfoBean> allAliInfo = aliInfoDao.getAllAliInfo();
		log.info("<AliInfoServiceImpl--getAllAliInfo--end>");
		return allAliInfo;
	}

	@Override
	public AliMerchantInfoBean getAliMerchantInfo(Integer companyId) {
		log.info("<AliInfoServiceImpl--getAliMerchantInfo--start>");
		AliMerchantInfoBean aliMerchantInfo = aliInfoDao.getAliMerchantInfo(companyId);
		log.info("<AliInfoServiceImpl--getAliMerchantInfo--end>");
		return aliMerchantInfo;
	}

	@Override
	public List<AliMerchantInfoBean> getAllAliMerchantInfo() {
		log.info("<AliInfoServiceImpl--getAllAliMerchantInfo--start>");
		List<AliMerchantInfoBean> allAliMerchantInfo = aliInfoDao.getAllAliMerchantInfo();
		log.info("<AliInfoServiceImpl--getAllAliMerchantInfo--end>");
		return allAliMerchantInfo;
	}
	
	
}

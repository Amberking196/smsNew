package com.server.module.zfb_trade.module.company;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("aliCompanyService")
public class AliCompanyServiceImpl implements AliCompanyService{

	public static Logger log = LogManager.getLogger(AliCompanyServiceImpl.class); 
	
	@Autowired
	@Qualifier("aliCompanyDao")
	private AliCompanyDao companyDao;
	@Override
	public List<CompanyBean> findAllSonCompany(Integer companyId) {
		log.info("AliCompanyServiceImpl--findAllSonCompany--start");
		List<CompanyBean> sonCompanyList = companyDao.findAllSonCompany(companyId);
		log.info("AliCompanyServiceImpl--findAllSonCompany--end");
		return sonCompanyList;
		
	}
	@Override
	public List<Integer> findAllSonCompanyId(Integer parentId) {
		log.info("AliCompanyServiceImpl--findAllSonCompanyId--start");
		List<Integer> findAllSonCompanyId = companyDao.findAllSonCompanyId(parentId);
		log.info("AliCompanyServiceImpl--findAllSonCompanyId--end");
		return findAllSonCompanyId;
	}
	
}

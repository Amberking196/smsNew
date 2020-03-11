package com.server.module.trade.customer.wxcustomer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WxCustomerServiceImpl implements WxCustomerService{

	private static Logger log = LogManager.getLogger(WxCustomerServiceImpl.class);
	@Autowired
	private WxCustomerDao wxCustomerDao;

	@Override
	public boolean update(WxCustomerBean wxCustomer) {
		log.info("<WxCustomerServiceImpl--update--start>");
		boolean update = wxCustomerDao.update(wxCustomer);
		log.info("<WxCustomerServiceImpl--update--end>");
		return update;
	}

	@Override
	public boolean updateUnionid(WxCustomerBean wxCustomer) {
		log.info("<WxCustomerServiceImpl--updateUnionid--start>");
		boolean update = wxCustomerDao.updateUnionid(wxCustomer);
		log.info("<WxCustomerServiceImpl--updateUnionid--end>");
		return update;
	}
	
	@Override
	public boolean needUpdate(String openId) {
		log.info("<WxCustomerServiceImpl--needUpdate--start>");
		boolean needUpdate = wxCustomerDao.needUpdate(openId);
		log.info("<WxCustomerServiceImpl--needUpdate--end>");
		return needUpdate;
	}
	

	@Override
	public WxCustomerBean insert(WxCustomerBean wxCustomer) {
		log.info("<WxCustomerServiceImpl--insert--start>");
		WxCustomerBean wxCustomerBean = wxCustomerDao.insert(wxCustomer);
		log.info("<WxCustomerServiceImpl--insert--end>");
		return wxCustomerBean;
	}
	@Override
	public WxCustomerBean selectByOpenId(String openId, Integer companyId) {
		log.info("<WxCustomerServiceImpl--selectByOpenId--start>");
		WxCustomerBean wxCustomerBean = wxCustomerDao.selectByOpenId(openId,companyId);
		log.info("<WxCustomerServiceImpl--selectByOpenId--end>");
		return wxCustomerBean;
	}
}

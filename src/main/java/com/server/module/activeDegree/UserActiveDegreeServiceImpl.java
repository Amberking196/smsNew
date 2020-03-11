package com.server.module.activeDegree;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.trade.customer.wxcustomer.WxCustomerBean;
import com.server.module.trade.customer.wxcustomer.WxCustomerDao;
import com.server.util.stateEnum.RegisterFlowEnum;

@Service
public class UserActiveDegreeServiceImpl implements UserActiveDegreeService{
	
	private final static Logger log = LogManager.getLogger(UserActiveDegreeServiceImpl.class);
	
	@Autowired
	private UserActiveDegreeDao userActiveDegreeDao;
	@Autowired
	private WxCustomerDao wxCustomerDao;

	@Override
	public RegisterFlowBean save(RegisterFlowBean registerBean) {
		//log.info("UserActiveDegreeServiceImpl--save--start");
		RegisterFlowBean save = userActiveDegreeDao.save(registerBean);
		//log.info("UserActiveDegreeServiceImpl--save--end");
		return save;
	}

	@Override
	public boolean update(Long customerId, RegisterFlowEnum event) {
		//log.info("UserActiveDegreeServiceImpl--update--start");
		boolean update = userActiveDegreeDao.update(customerId, event);
		//log.info("UserActiveDegreeServiceImpl--update--end");
		return update;
	}

	@Override
	public boolean update(String openId, RegisterFlowEnum event) {
		//log.info("UserActiveDegreeServiceImpl--update--start");
		boolean update = false;
		WxCustomerBean wxCus = wxCustomerDao.selectByOpenId(openId, null);
		if(wxCus!=null){
			update = userActiveDegreeDao.update(wxCus.getCustomerId(), event);
		}
		//log.info("UserActiveDegreeServiceImpl--update--start");
		return update;
	}
	
}

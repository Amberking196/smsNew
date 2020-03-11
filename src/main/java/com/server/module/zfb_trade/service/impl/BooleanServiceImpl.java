package com.server.module.zfb_trade.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.trade.auth.UserVo;
import com.server.module.trade.util.UserUtil;
import com.server.module.zfb_trade.bean.AdminConstant;
import com.server.module.zfb_trade.module.logininfo.LoginInfoBean;
import com.server.module.zfb_trade.module.customer.AliCustomerService;
import com.server.module.zfb_trade.module.customer.CustomerBean;
import com.server.module.zfb_trade.module.logininfo.AliLoginInfoService;
import com.server.module.zfb_trade.service.BooleanService;
import com.server.module.zfb_trade.util.JsonUtil;

@Service
public class BooleanServiceImpl implements BooleanService {

	public static Logger log = LogManager.getLogger(BooleanServiceImpl.class);
	@Autowired
	private AliLoginInfoService loginInfoService;
	@Autowired
	private AliCustomerService customerService;

// HBT  待解决：暂时封印,等待有缘人
//	@Override
//	public LoginInfoBean isBindLoginInfo(HttpServletRequest request) {
//		log.info("<BooleanServiceImpl--isBindLoginInfo--start>");
//		UserVo user = UserUtil.getUser();
//		log.info("user:"+JsonUtil.toJson(user));
//		if (user != null) {
//			Long id = user.getId();
//			CustomerBean queryById = customerService.queryById(id);
//			if (queryById != null) {
//				LoginInfoBean findLoginInfo = loginInfoService.queryByPhone(queryById.getPhone());
//				log.info("<BooleanServiceImpl--isBindLoginInfo--end>");
//				return findLoginInfo;
//			}
//		}
//		log.info("<BooleanServiceImpl--isBindLoginInfo--end>");
//		return null;
//	}
	
	
	@Override
	public LoginInfoBean isBindLoginInfo(HttpServletRequest request) {
		log.info("<BooleanServiceImpl--isBindLoginInfo--start>");
		UserVo user = UserUtil.getUser();
		log.info("user:"+JsonUtil.toJson(user));
		if (user != null) {
			Long id = user.getId();
			LoginInfoBean queryById = loginInfoService.queryById(id);
			return queryById;
		}
		log.info("<BooleanServiceImpl--isBindLoginInfo--end>");
		return null;
	}

}

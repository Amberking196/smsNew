package com.server.module.zfb_trade.convert;

import java.util.Date;

import com.server.module.zfb_trade.module.customer.CustomerBean;

public class AliCustomerConvert {

	public static CustomerBean convertAliCustomerToWx(CustomerBean aliCustomer,CustomerBean wxCustomer){
		wxCustomer.setAlipayUserId(aliCustomer.getAlipayUserId());
		wxCustomer.setCity(aliCustomer.getCity());
		wxCustomer.setCountry(aliCustomer.getCountry());
		wxCustomer.setHeadImgUrl(aliCustomer.getHeadImgUrl());
		wxCustomer.setNickname(aliCustomer.getNickname());
		wxCustomer.setUpdateTime(new Date());
		wxCustomer.setProvince(aliCustomer.getProvince());
		wxCustomer.setSexId(aliCustomer.getSexId());
		wxCustomer.setIsCertified(aliCustomer.getIsCertified());
		wxCustomer.setIsStudentCertified(aliCustomer.getIsStudentCertified());
		wxCustomer.setLatitude(aliCustomer.getLatitude());
		wxCustomer.setLongitude(aliCustomer.getLongitude());
		wxCustomer.setUserType(aliCustomer.getUserType());
		wxCustomer.setUserStatus(aliCustomer.getUserStatus());
		return wxCustomer;
	}
}

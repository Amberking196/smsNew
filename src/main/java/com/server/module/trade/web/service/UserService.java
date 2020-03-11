package com.server.module.trade.web.service;

import java.util.Map;

import com.server.module.trade.config.gzh.WechatUser;
import com.server.module.trade.customer.TblCustomerBean;
import com.server.module.trade.exception.MyException;

public interface UserService {
	/**
	 * 检测用户是否注册
	 * @author yjr
	 * @param type(1:微信,2:支付宝)
	 * @param openId
	 * @return
	 */
	public boolean checkUser(Integer type,String openId);
	/**
	 * 根据类型及openId查询客户信息
	 * @author yjr
	 * @param type(1:微信,2:支付宝)
	 * @param openId
	 * @return
	 */
	public TblCustomerBean getCustomer(Integer type, String openId);
	/**
	 * 用户注册
	 * @author yjr
	 * @param type
	 * @param openId
	 * @param phone
	 * @param smsCode
	 * @param vmCode
	 * @return
	 * @throws MyException
	 */
	public  Map<String,Object>  register(Integer type,String openId, String phone, String smsCode,String vmCode,String inviteId,WechatUser wechatUser) throws MyException ;

	/**
	 * 授权后用户注册
	 * @author hjc
	 * @param  tblCustomerBean
	 */
	public  TblCustomerBean  insert(TblCustomerBean tblCustomerBean);
	
	/**
	 * 商城用户注册
	 * @author hjc
	 * @param type
	 * @param openId
	 * @param phone
	 * @param smsCode
	 * @param vmCode
	 * @return
	 * @throws MyException
	 */
	public Map<String, Object> shopRegister(Integer type, String openId, String phone, String smsCode, String vmCode ,WechatUser wechatUser)throws MyException;

	
	/**
	 * 商城用户注册
	 * @author hjc
	 * @param type
	 * @param openId
	 * @param phone
	 * @param smsCode
	 * @param vmCode
	 * @return
	 * @throws MyException
	 */
	public Map<String, Object> appRegister(Integer type, String openId, String phone, String smsCode, String vmCode ,WechatUser wechatUser)throws MyException;
}

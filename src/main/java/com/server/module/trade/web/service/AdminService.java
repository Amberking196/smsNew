package com.server.module.trade.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.trade.web.dao.LoginInfoDao;

@Service
public class AdminService {
	@Autowired
	private LoginInfoDao loginInfoDao;

	/**
	 * 根据电话号码查询后台用户信息
	 * 
	 * @author yjr
	 * @param phone
	 * @return
	 */
	public List<Map<String, Object>> listByPhone(String phone) {
		List<Map<String, Object>> list = loginInfoDao.listByPhone(phone);
		return list;
	}

	/**
	 * 绑定支付宝或者微信 aliUserId 与 openId 只能传一个
	 * 
	 * @author hebiting
	 * @date 2018年5月24日上午9:18:50
	 * @param aliUserId
	 * @param openId
	 * @param id
	 * @return
	 */
	public boolean update(String aliUserId, String openId, Long id) {
		return loginInfoDao.update(aliUserId, openId, id);
	}

	/**
	 * 根据openId查询后台用户信息
	 * 
	 * @author yjr
	 * @param openId
	 * @return
	 */
	public List<Map<String, Object>> listByOpenId(String openId) {
		List<Map<String, Object>> list = loginInfoDao.listByOpenId(openId);
		return list;
	}
	
	
}

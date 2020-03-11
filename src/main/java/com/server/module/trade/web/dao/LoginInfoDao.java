package com.server.module.trade.web.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.server.common.persistence.BaseDao;
import com.server.module.trade.order.dao.SimpleBaseDao;
import com.server.util.StringUtil;
@Repository
public class LoginInfoDao extends BaseDao{
	
	@Autowired
	private SimpleBaseDao baseDao;
	/**
	 * 根据电话号码查询后台用户信息
	 * @author yjr
	 * @param phone
	 * @return
	 */
	public List<Map<String,Object>> listByPhone(String phone){
		String sql="select id,companyId,loginCode,name,status,openId,aliUserId from login_info where phone='"+phone+"'";
		List<Map<String,Object>> listmap=(ArrayList<Map<String,Object>>) baseDao.baseSelectMap(sql, null);
		return listmap;
	}
	/**
	 * 根据openId查询后台用户信息
	 * @author yjr
	 * @param openId
	 * @return
	 */
	public List<Map<String,Object>> listByOpenId(String openId){
		String sql="select id,companyId,loginCode,name,status,openId,aliUserId from login_info where openId='"+openId+"'";
		List<Map<String,Object>> listmap=(ArrayList<Map<String,Object>>) baseDao.baseSelectMap(sql, null);
		return listmap;
	}
	/**
	 * 绑定支付宝或微信，aliUserId和openId只能传一个
	 * @author hebiting
	 * @param aliUserId
	 * @param openId
	 * @param id
	 * @return
	 */
	public boolean update(String aliUserId,String openId,Long id){
		StringBuffer sql = new StringBuffer();
		sql.append("update login_info set");
		if(StringUtil.isNotBlank(aliUserId)){
			sql.append(" aliUserId='"+aliUserId+"'");
		}else if(StringUtil.isNotBlank(openId)){
			sql.append(" openId='"+openId+"'");
		}
		sql.append(" where id="+id);
		int upate = upate(sql.toString());
		if(upate==1){
			return true;
		}
		return false;
	}

}

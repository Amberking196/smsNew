package com.server.module.trade.customer.wxcustomer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.DateUtil;
import com.server.util.EmojiUtil;
import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class WxCustomerDaoImpl extends MySqlFuns implements WxCustomerDao{

	private static Logger log = LogManager.getLogger(WxCustomerDaoImpl.class);

	@Override
	public WxCustomerBean insert(WxCustomerBean wxCustomer) {
		//log.info("<WxCustomerDaoImpl--insert--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO `tbl_customer_wx`(companyId,customerId,openId,nickname,");
		sql.append(" sex,province,city,country,headimgurl,");
		sql.append(" unionid,createTime,updateTime)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(wxCustomer.getCompanyId());
		param.add(wxCustomer.getCustomerId());
		param.add(wxCustomer.getOpenId());
		param.add(EmojiUtil.getString(wxCustomer.getNickname()));
		param.add(wxCustomer.getSex());
		param.add(wxCustomer.getProvince());
		param.add(wxCustomer.getCity());
		param.add(wxCustomer.getCountry());
		param.add(wxCustomer.getHeadimgurl());
//		param.add(wxCustomer.getPrivilege());
		param.add(wxCustomer.getUnionid());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(wxCustomer.getCreateTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(wxCustomer.getUpdateTime()));
		int id = insertGetID(sql.toString(), param);
		//log.info("<WxCustomerDaoImpl--insert--end>");
		if(id!=-1){
			wxCustomer.setId((long)id);
			return wxCustomer;
		}
		return null;
	}

	@Override
	public boolean update(WxCustomerBean wxCustomer) {
		//log.info("<WxCustomerDaoImpl--update--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE `tbl_customer_wx` SET nickname=?,sex=?,");
		sql.append(" province=?,city=?,country=?,headimgurl=?,");
		sql.append(" unionid=?,updateTime=?,appOpenId=?");
		sql.append(" WHERE openId = ?");
		List<Object> param = new ArrayList<Object>();
		param.add(EmojiUtil.getString(wxCustomer.getNickname()));
		param.add(wxCustomer.getSex());
		param.add(wxCustomer.getProvince());
		param.add(wxCustomer.getCity());
		param.add(wxCustomer.getCountry());
		param.add(wxCustomer.getHeadimgurl());
		param.add(wxCustomer.getUnionid());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(wxCustomer.getUpdateTime()));
		param.add(wxCustomer.getAppOpenId());
		param.add(wxCustomer.getOpenId());
		int upate = upate(sql.toString(),param);
		//log.info("<WxCustomerDaoImpl--update--end>");
		if(upate>0){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean updateUnionid(WxCustomerBean wxCustomer) {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE `tbl_customer_wx` SET unionid=? ");
		sql.append(" WHERE openId = ?");
		List<Object> param = new ArrayList<Object>();
		param.add(wxCustomer.getUnionid());
		param.add(wxCustomer.getOpenId());
		int upate = upate(sql.toString(),param);
		if(upate>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean delete(Long id) {
		//log.info("<WxCustomerDaoImpl--delete--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM `tbl_customer_wx` WHERE id = "+id);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = -1;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		//log.info("<WxCustomerDaoImpl--delete--end>");
		if(rs>0){
			return true;
		}
		return false;
	}

	@Override
	public WxCustomerBean selectByOpenId(String openId, Integer companyId) {
		//log.info("<WxCustomerDaoImpl--selectByOpenId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,companyId,customerId,openId,nickname,");
		sql.append(" sex,province,city,country,headimgurl,");
		sql.append(" unionid,createTime,updateTime");
		sql.append(" FROM `tbl_customer_wx` WHERE openId='"+openId+"' ");
		if(companyId!=null) {
			sql.append(" and companyId = "+companyId);
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		WxCustomerBean wxCustomer = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				wxCustomer = new WxCustomerBean();
				wxCustomer.setCity(rs.getString("city"));
				wxCustomer.setCompanyId(rs.getInt("companyId"));
				wxCustomer.setCountry(rs.getString("country"));
				wxCustomer.setCreateTime(rs.getTimestamp("createTime"));
				wxCustomer.setCustomerId(rs.getLong("customerId"));
				wxCustomer.setHeadimgurl(rs.getString("headimgurl"));
				wxCustomer.setId(rs.getLong("id"));
				wxCustomer.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
				wxCustomer.setOpenId(rs.getString("openId"));
//				wxCustomer.setPrivilege(rs.getString("privilege"));
				wxCustomer.setProvince(rs.getString("province"));
				wxCustomer.setSex(rs.getInt("sex"));
				wxCustomer.setUnionid(rs.getString("unionid"));
				wxCustomer.setUpdateTime(rs.getTimestamp("updateTime"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<WxCustomerDaoImpl--selectByOpenId--end>");
		return wxCustomer;
	}

	@Override
	public WxCustomerBean selectByCustomerId(Long customerId, Integer companyId) {
		//log.info("<WxCustomerDaoImpl--selectByCustomerId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,companyId,customerId,openId,nickname,");
		sql.append(" sex,province,city,country,headimgurl,");
		sql.append(" unionid,createTime,updateTime");
		sql.append(" FROM `tbl_customer_wx` WHERE customerId="+customerId+" and companyId = "+companyId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		WxCustomerBean wxCustomer = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				wxCustomer = new WxCustomerBean();
				wxCustomer.setCity(rs.getString("city"));
				wxCustomer.setCompanyId(rs.getInt("companyId"));
				wxCustomer.setCountry(rs.getString("country"));
				wxCustomer.setCreateTime(rs.getTimestamp("createTime"));
				wxCustomer.setCustomerId(rs.getLong("customerId"));
				wxCustomer.setHeadimgurl(rs.getString("headimgurl"));
				wxCustomer.setId(rs.getLong("id"));
				wxCustomer.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
				wxCustomer.setOpenId(rs.getString("openId"));
//				wxCustomer.setPrivilege(rs.getString("privilege"));
				wxCustomer.setProvince(rs.getString("province"));
				wxCustomer.setSex(rs.getInt("sex"));
				wxCustomer.setUnionid(rs.getString("unionid"));
				wxCustomer.setUpdateTime(rs.getTimestamp("updateTime"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<WxCustomerDaoImpl--selectByCustomerId--end>");
		return wxCustomer;
	}

	@Override
	public boolean needUpdate(String openId) {
		//log.info("<WxCustomerDaoImpl--needUpdate--start>");
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" select 1 from tbl_customer_wx where openId = '"+openId+"' and headimgurl is null");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<WxCustomerDaoImpl--needUpdate--end>");
		return result;
	}
	
	
}

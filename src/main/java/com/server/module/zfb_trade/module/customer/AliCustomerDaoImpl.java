package com.server.module.zfb_trade.module.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.DateUtil;
import com.server.util.EmojiUtil;
import com.server.util.sqlUtil.MySqlFuns;
import com.sun.org.apache.bcel.internal.generic.Select;
@Repository("aliCustomerDao")
public class AliCustomerDaoImpl extends MySqlFuns implements AliCustomerDao{

	public static Logger log = LogManager.getLogger(AliCustomerDaoImpl.class); 
	@Override
	public CustomerBean queryByAliId(CustomerBean customer) {
		log.info("<AliCustomerDaoImpl--queryByAliId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tc.id,tcw.openId,tc.`type`,tc.alipayUserId,tc.phone,tc.nickname,tc.userName,");
		sql.append(" tc.sexId,tc.city,tc.province,tc.country,tc.headImgUrl,tc.latitude,tc.longitude,");
		sql.append(" tc.isCertified,tc.isStudentCertified,tc.userStatus,tc.userType,tc.createTime,");
		sql.append(" tc.createId,tc.updateTime,tc.lastUpdateId,tc.deleteFlag FROM tbl_customer as tc");
		sql.append(" LEFT JOIN tbl_customer_wx AS tcw ON tc.id = tcw.customerId");
		sql.append(" where tc.alipayUserId = "+customer.getAlipayUserId());
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CustomerBean cus = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				cus = new CustomerBean();
				cus.setAlipayUserId(rs.getString("alipayUserId"));
				cus.setCity(rs.getString("city"));
				cus.setCountry(rs.getString("country"));
				cus.setCreateId(rs.getString("createId"));
				cus.setCreateTime(rs.getDate("createTime"));
				cus.setDeleteFlag(rs.getByte("deleteFlag"));
				cus.setHeadImgUrl(rs.getString("headImgUrl"));
				cus.setId(rs.getLong("id"));
				cus.setIsCertified(rs.getString("isCertified"));
				cus.setIsStudentCertified(rs.getString("isStudentCertified"));
				cus.setLastUpdateId(rs.getString("lastUpdateId"));
				cus.setLatitude(rs.getDouble("latitude"));
				cus.setLongitude(rs.getDouble("longitude"));
				cus.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
				cus.setOpenId(rs.getString("openId"));
				cus.setPhone(rs.getString("phone"));
				cus.setProvince(rs.getString("province"));
				cus.setSexId(rs.getInt("sexId"));
				cus.setType(rs.getInt("type"));
				cus.setUpdateTime(rs.getDate("updateTime"));
				cus.setUserName(rs.getString("userName"));
				cus.setUserStatus(rs.getString("userStatus"));
				cus.setUserType(rs.getString("userType"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliCustomerDaoImpl--queryByAliId--end>");
		return cus;
	}

	@Override
	public Long saveCustomer(CustomerBean customer) {
		log.info("<AliCustomerDaoImpl--save--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into tbl_customer(openId,`type`,alipayUserId,phone,");
		sql.append(" nickname,userName,sexId,city,province,country,headImgUrl,");
		sql.append(" latitude,longitude,isCertified,isStudentCertified,userStatus,userType,");
		sql.append(" createTime,createId,updateTime,lastUpdateId,deleteFlag,vmCode) ");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(customer.getOpenId());
		param.add(customer.getType());
		param.add(customer.getAlipayUserId());
		param.add(customer.getPhone());
		param.add(EmojiUtil.getString(customer.getNickname()));
		param.add(customer.getUserName());
		param.add(customer.getSexId());
		param.add(customer.getCity());
		param.add(customer.getProvince());
		param.add(customer.getCountry());
		param.add(customer.getHeadImgUrl());
		param.add(customer.getLatitude());
		param.add(customer.getLongitude());
		param.add(customer.getIsCertified());
		param.add(customer.getIsStudentCertified());
		param.add(customer.getUserStatus());
		param.add(customer.getUserType());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
		param.add(customer.getCreateId());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(customer.getUpdateTime()));
		param.add(customer.getLastUpdateId());
		param.add(customer.getDeleteFlag());
		param.add(customer.getVmCode());
		long result = insertGetID(sql.toString(), param);
		log.info("<AliCustomerDaoImpl--save--end>");
		return result;
	}

	@Override
	public boolean updateCustomer(CustomerBean customer) {
		log.info("<AliCustomerDaoImpl--update--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" update tbl_customer set");
		sql.append(" openId = ?,type=?,alipayUserId=?,phone=?,nickname=?,");
		sql.append(" userName=?,sexId=?,city=?,province=?,country=?,headImgUrl=?,");
		sql.append(" latitude=?,isCertified=?,isStudentCertified=?,userStatus=?,");
		sql.append(" userType=?,createTime=?,createId=?,updateTime=?,lastUpdateId=?,");
		sql.append(" deleteFlag=?,isLoginUser=? where id =?");
		List<Object> param = new ArrayList<Object>();
		param.add(customer.getOpenId());
		param.add(customer.getType());
		param.add(customer.getAlipayUserId());
		param.add(customer.getPhone());
		param.add(EmojiUtil.getString(customer.getNickname()));
		param.add(customer.getUserName());
		param.add(customer.getSexId());
		param.add(customer.getCity());
		param.add(customer.getProvince());
		param.add(customer.getCountry());
		param.add(customer.getHeadImgUrl());
		param.add(customer.getLatitude());
		param.add(customer.getIsCertified());
		param.add(customer.getIsStudentCertified());
		param.add(customer.getUserStatus());
		param.add(customer.getUserType());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(customer.getCreateTime()));
		param.add(customer.getCreateId());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(customer.getUpdateTime()));
		param.add(customer.getLastUpdateId());
		param.add(customer.getDeleteFlag());
		param.add(customer.getIsLoginUser());
		param.add(customer.getId());
		int result = upate(sql.toString(),param);
		log.info("<AliCustomerDaoImpl--update--end>");
		if(result ==1){
			return true;
		}
		return false;
	}

	@Override
	public CustomerBean queryById(Long id) {
		log.info("<AliCustomerDaoImpl--queryById--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tc.id,tcw.openId,tc.`type`,tc.alipayUserId,tc.phone,tc.nickname,tc.UserName,");
		sql.append(" tc.sexId,tc.city,tc.province,tc.country,tc.headImgUrl,tc.latitude,tc.longitude,");
		sql.append(" tc.isCertified,tc.isStudentCertified,tc.userStatus,tc.userType,tc.createTime,");
		sql.append(" tc.createId,tc.updateTime,tc.lastUpdateId,tc.deleteFlag,tc.inviterId FROM tbl_customer as tc");
		sql.append(" LEFT JOIN tbl_customer_wx AS tcw ON tc.id = tcw.customerId");
		sql.append(" where tc.id = "+id);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CustomerBean cus = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				cus = new CustomerBean();
				cus.setAlipayUserId(rs.getString("alipayUserId"));
				cus.setCity(rs.getString("city"));
				cus.setCountry(rs.getString("country"));
				cus.setCreateId(rs.getString("createId"));
				cus.setCreateTime(rs.getTimestamp("createTime"));
				cus.setDeleteFlag(rs.getByte("deleteFlag"));
				cus.setHeadImgUrl(rs.getString("headImgUrl"));
				cus.setId(rs.getLong("id"));
				cus.setIsCertified(rs.getString("isCertified"));
				cus.setIsStudentCertified(rs.getString("isStudentCertified"));
				cus.setLastUpdateId(rs.getString("lastUpdateId"));
				cus.setLatitude(rs.getDouble("latitude"));
				cus.setLongitude(rs.getDouble("longitude"));
				cus.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
				cus.setOpenId(rs.getString("openId"));
				cus.setPhone(rs.getString("phone"));
				cus.setProvince(rs.getString("province"));
				cus.setSexId(rs.getInt("sexId"));
				cus.setType(rs.getInt("type"));
				cus.setUpdateTime(rs.getDate("updateTime"));
				cus.setUserName(rs.getString("UserName")==null?"":rs.getString("UserName"));
				cus.setUserStatus(rs.getString("userStatus"));
				cus.setUserType(rs.getString("userType"));
				cus.setInviterId(rs.getLong("inviterId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliCustomerDaoImpl--queryById--end>");
		return cus;
	}

	@Override
	public List<CustomerBean> queryCustomer(String customerIdList) {
		log.info("<AliCustomerDaoImpl--queryCustomer--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tc.id,tcw.openId,tc.`type`,tc.alipayUserId,tc.phone,tc.nickname,tc.userName,");
		sql.append(" tc.sexId,tc.city,tc.province,tc.country,tc.headImgUrl,tc.latitude,tc.longitude,");
		sql.append(" tc.isCertified,tc.isStudentCertified,tc.userStatus,tc.userType,tc.createTime,");
		sql.append(" tc.createId,tc.updateTime,tc.lastUpdateId,tc.deleteFlag FROM tbl_customer as tc");
		sql.append(" LEFT JOIN tbl_customer_wx AS tcw ON tc.id = tcw.customerId");
		sql.append(" where tc.id in ("+customerIdList+")");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CustomerBean cus = null;
		List<CustomerBean> cusList = new ArrayList<CustomerBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				cus = new CustomerBean();
				cus.setAlipayUserId(rs.getString("alipayUserId"));
				cus.setCity(rs.getString("city"));
				cus.setCountry(rs.getString("country"));
				cus.setCreateId(rs.getString("createId"));
				cus.setCreateTime(rs.getDate("createTime"));
				cus.setDeleteFlag(rs.getByte("deleteFlag"));
				cus.setHeadImgUrl(rs.getString("headImgUrl"));
				cus.setId(rs.getLong("id"));
				cus.setIsCertified(rs.getString("isCertified"));
				cus.setIsStudentCertified(rs.getString("isStudentCertified"));
				cus.setLastUpdateId(rs.getString("lastUpdateId"));
				cus.setLatitude(rs.getDouble("latitude"));
				cus.setLongitude(rs.getDouble("longitude"));
				cus.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
				cus.setOpenId(rs.getString("openId"));
				cus.setPhone(rs.getString("phone"));
				cus.setProvince(rs.getString("province"));
				cus.setSexId(rs.getInt("sexId"));
				cus.setType(rs.getInt("type"));
				cus.setUpdateTime(rs.getDate("updateTime"));
				cus.setUserName(rs.getString("userName"));
				cus.setUserStatus(rs.getString("userStatus"));
				cus.setUserType(rs.getString("userType"));
				cusList.add(cus);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliCustomerDaoImpl--queryCustomer--end>");
		return cusList;
	}

	@Override
	public CustomerBean queryByPhone(String phone) {
		log.info("<AliCustomerDaoImpl--queryByPhone--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tc.id,tcw.openId,tc.`type`,tc.alipayUserId,tc.phone,tc.nickname,tc.UserName,");
		sql.append(" tc.sexId,tc.city,tc.province,tc.country,tc.headImgUrl,tc.latitude,tc.longitude,");
		sql.append(" tc.isCertified,tc.isStudentCertified,tc.userStatus,tc.userType,tc.createTime,");
		sql.append(" tc.createId,tc.updateTime,tc.lastUpdateId,tc.deleteFlag FROM tbl_customer as tc");
		sql.append(" LEFT JOIN tbl_customer_wx AS tcw ON tc.id = tcw.customerId");
		sql.append(" where tc.phone = "+ phone);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CustomerBean cus = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				cus = new CustomerBean();
				cus.setAlipayUserId(rs.getString("alipayUserId"));
				cus.setCity(rs.getString("city"));
				cus.setCountry(rs.getString("country"));
				cus.setCreateId(rs.getString("createId"));
				cus.setCreateTime(rs.getDate("createTime"));
				cus.setDeleteFlag(rs.getByte("deleteFlag"));
				cus.setHeadImgUrl(rs.getString("headImgUrl"));
				cus.setId(rs.getLong("id"));
				cus.setIsCertified(rs.getString("isCertified"));
				cus.setIsStudentCertified(rs.getString("isStudentCertified"));
				cus.setLastUpdateId(rs.getString("lastUpdateId"));
				cus.setLatitude(rs.getDouble("latitude"));
				cus.setLongitude(rs.getDouble("longitude"));
				cus.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
				cus.setOpenId(rs.getString("openId"));
				cus.setPhone(rs.getString("phone"));
				cus.setProvince(rs.getString("province"));
				cus.setSexId(rs.getInt("sexId"));
				cus.setType(rs.getInt("type"));
				cus.setUpdateTime(rs.getDate("updateTime"));
				cus.setUserName(rs.getString("UserName")==null?"":rs.getString("UserName"));
				cus.setUserStatus(rs.getString("userStatus"));
				cus.setUserType(rs.getString("userType"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliCustomerDaoImpl--queryByPhone--end>");
		return cus;
	}

	@Override
	public Integer delCustomer(Long id) {
		log.info("<AliCustomerDaoImpl--delCustomer--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM tbl_customer WHERE id = "+ id);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int executeUpdate = -1;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			executeUpdate = ps.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliCustomerDaoImpl--delCustomer--end>");
		return executeUpdate;
	}
	
	@Override
	public boolean queryIsLoginUserByPhone(String phone) {
		log.info("<AliCustomerDaoImpl--queryIsLoginUserByPhone--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" select phone from login_info WHERE phone = "+ phone);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				return true;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliCustomerDaoImpl--queryIsLoginUserByPhone--end>");
		return false;

	}

	@Override
	public boolean queryIfReplenishRecent(String code) {
		log.info("<AliCustomerDaoImpl--queryIfReplenishRecent--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from replenish_record where ");
		sql.append(" vmcode='"+code+"' and ");
		sql.append(" createTime >= DATE_SUB(NOW(),INTERVAL 15 MINUTE) ");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				return true;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliCustomerDaoImpl--queryIfReplenishRecent--end>");
		return false;

	}


}

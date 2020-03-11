package com.server.module.zfb_trade.module.logininfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;
@Repository("aliLoginInfoDao")
public class AliLoginInfoDaoImpl extends MySqlFuns implements AliLoginInfoDao{

	public static Logger log = LogManager.getLogger(AliLoginInfoDaoImpl.class); 

	

	@Override
	public boolean isDuty(Long dutyId, String vmCode) {
		log.info("<AliLoginInfoDaoImpl--isDuty--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT 1 AS num FROM vending_machines_info WHERE `code`= '"+vmCode+"'");
		sql.append(" AND lineId IN (SELECT id FROM vending_line WHERE FIND_IN_SET("+dutyId+",dutyId))");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			log.info("<AliLoginInfoDaoImpl--isDuty--end>");
			if(rs!=null && rs.next()){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		return false;
	}


	@Override
	public LoginInfoBean findLoginInfo(String alipayUserId) {
		log.info("<AliLoginInfoDaoImpl--findLoginInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,loginCode,`PASSWORD`,`STATUS`,`NAME`,hireDate,codeNo,role,");
		sql.append(" companyId,phone,loginModel,mail,founder,updateDate,extraMenu,");
		sql.append(" rightsCompany,rightsMachines,rightsLine,rightsButton,validataCode,");
		sql.append(" outDate,banVmCode,openId,aliUserId,isPrincipal,post,departMent");
		sql.append(" FROM login_info where status = 1 and ( aliUserId = '"+alipayUserId+"'");
		sql.append(" or openId = '"+alipayUserId+"')");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		LoginInfoBean loginInfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				loginInfo = new LoginInfoBean();
				loginInfo.setAliUserId(rs.getString("aliUserId"));
				loginInfo.setCodeNo(rs.getString("codeNo"));
				loginInfo.setCompanyId(rs.getInt("companyId"));
				loginInfo.setHireDate(rs.getDate("hireDate"));
				loginInfo.setId(rs.getLong("id"));
				loginInfo.setIsPrincipal(rs.getString("isPrincipal"));
				loginInfo.setLoginCode(rs.getString("loginCode"));
				loginInfo.setMail(rs.getString("mail"));
				loginInfo.setName(rs.getString("name"));
				loginInfo.setOpenId(rs.getString("openId"));
				loginInfo.setPhone(rs.getString("phone"));
				loginInfo.setRole(rs.getString("role"));
				loginInfo.setStatus(rs.getInt("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliLoginInfoDaoImpl--findLoginInfo--end>");
		return loginInfo;
	}


	@Override
	public boolean update(LoginInfoBean loginInfo) {
		log.info("<AliLoginInfoDaoImpl--update--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" update login_info set aliUserId='"+loginInfo.getAliUserId()+"'");
		sql.append(" ,updateDate='"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"'");
		sql.append(" where id = "+loginInfo.getId());
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliLoginInfoDaoImpl--update--end>");
		if(result==1){
			return true;
		}
		return false;
	}


	@Override
	public LoginInfoBean login(String loginCode, String password) {
		log.info("<AliLoginInfoDaoImpl--login--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,loginCode,`PASSWORD`,`STATUS`,`NAME`,hireDate,codeNo,role,");
		sql.append(" companyId,phone,loginModel,mail,founder,updateDate,extraMenu,");
		sql.append(" rightsCompany,rightsMachines,rightsLine,rightsButton,validataCode,");
		sql.append(" outDate,banVmCode,openId,aliUserId,isPrincipal,post,departMent");
		sql.append(" FROM login_info where  status = 1 and loginCode = '"+loginCode+"' and password = '"+password+"'");
		log.info("sql语句:"+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		LoginInfoBean loginInfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				loginInfo = new LoginInfoBean();
				loginInfo.setAliUserId(rs.getString("aliUserId"));
				loginInfo.setCodeNo(rs.getString("codeNo"));
				loginInfo.setCompanyId(rs.getInt("companyId"));
				loginInfo.setHireDate(rs.getDate("hireDate"));
				loginInfo.setId(rs.getLong("id"));
				loginInfo.setIsPrincipal(rs.getString("isPrincipal"));
				loginInfo.setLoginCode(rs.getString("loginCode"));
				loginInfo.setMail(rs.getString("mail"));
				loginInfo.setName(rs.getString("name"));
				loginInfo.setOpenId(rs.getString("openId"));
				loginInfo.setPhone(rs.getString("phone"));
				loginInfo.setRole(rs.getString("role"));
				loginInfo.setStatus(rs.getInt("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliLoginInfoDaoImpl--login--end>");
		return loginInfo;
	}


	@Override
	public LoginInfoBean queryByPhone(String phone) {
		log.info("<AliLoginInfoDaoImpl--queryByPhone--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,loginCode,`PASSWORD`,`STATUS`,`NAME`,hireDate,codeNo,role,");
		sql.append(" companyId,phone,loginModel,mail,founder,updateDate,extraMenu,");
		sql.append(" rightsCompany,rightsMachines,rightsLine,rightsButton,validataCode,");
		sql.append(" outDate,banVmCode,openId,aliUserId,isPrincipal,post,departMent");
		sql.append(" FROM login_info where status = 1 and phone = '"+phone+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		LoginInfoBean loginInfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				loginInfo = new LoginInfoBean();
				loginInfo.setAliUserId(rs.getString("aliUserId"));
				loginInfo.setCodeNo(rs.getString("codeNo"));
				loginInfo.setCompanyId(rs.getInt("companyId"));
				loginInfo.setHireDate(rs.getDate("hireDate"));
				loginInfo.setId(rs.getLong("id"));
				loginInfo.setIsPrincipal(rs.getString("isPrincipal"));
				loginInfo.setLoginCode(rs.getString("loginCode"));
				loginInfo.setMail(rs.getString("mail"));
				loginInfo.setName(rs.getString("name"));
				loginInfo.setOpenId(rs.getString("openId"));
				loginInfo.setPhone(rs.getString("phone"));
				loginInfo.setRole(rs.getString("role"));
				loginInfo.setStatus(rs.getInt("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliLoginInfoDaoImpl--queryByPhone--end>");
		return loginInfo;
	}


	@Override
	public LoginInfoBean queryById(Long id) {
		log.info("<AliLoginInfoDaoImpl--queryById--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,loginCode,`PASSWORD`,`STATUS`,`NAME`,hireDate,codeNo,role,");
		sql.append(" companyId,phone,loginModel,mail,founder,updateDate,extraMenu,");
		sql.append(" rightsCompany,rightsMachines,rightsLine,rightsButton,validataCode,");
		sql.append(" outDate,banVmCode,openId,aliUserId,isPrincipal,post,departMent");
		sql.append(" FROM login_info where  status = 1 and id = "+id);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		LoginInfoBean loginInfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				loginInfo = new LoginInfoBean();
				loginInfo.setAliUserId(rs.getString("aliUserId"));
				loginInfo.setCodeNo(rs.getString("codeNo"));
				loginInfo.setCompanyId(rs.getInt("companyId"));
				loginInfo.setHireDate(rs.getDate("hireDate"));
				loginInfo.setId(rs.getLong("id"));
				loginInfo.setIsPrincipal(rs.getString("isPrincipal"));
				loginInfo.setLoginCode(rs.getString("loginCode"));
				loginInfo.setMail(rs.getString("mail"));
				loginInfo.setName(rs.getString("name"));
				loginInfo.setOpenId(rs.getString("openId"));
				loginInfo.setPhone(rs.getString("phone"));
				loginInfo.setRole(rs.getString("role"));
				loginInfo.setStatus(rs.getInt("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliLoginInfoDaoImpl--queryById--end>");
		return loginInfo;
	}


}

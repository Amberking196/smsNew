package com.server.module.member;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.sqlUtil.MySqlFuns;
@Repository
public class MemberDaoImpl extends MySqlFuns implements MemberDao{

	private final static Logger log = LogManager.getLogger(MemberDaoImpl.class);
	
	
	@Override
	public MemberBean getMemberInfo(Long customerId) {
		log.info("<MemberDaoImpl--getMemberInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tc.`id`,tc.`memberTypeId`,tc.`startTime`,tc.`endTime`,mt.`discount`,tc.isMember");
		sql.append(" FROM tbl_customer AS tc ");
		sql.append(" INNER JOIN member_type AS mt ON mt.`id` = tc.`memberTypeId`");
		sql.append(" WHERE tc.`id` = '"+customerId+"' AND isMember = 1 AND startTime <= now() AND endTime >= now()");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MemberBean member = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				member = new MemberBean();
				member.setCustomerId(rs.getLong("id"));
				member.setDiscount(rs.getDouble("discount"));
				member.setEndTime(rs.getTimestamp("endTime"));
				member.setIsMember(rs.getInt("isMember"));
				member.setMemberTypeId(rs.getInt("memberTypeId"));
				member.setStartTime(rs.getTimestamp("startTime"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<MemberDaoImpl--getMemberInfo--end>");
		return member;
	}


	@Override
	public BigDecimal getMemberMoney(Long customerId) {
		log.info("<MemberDaoImpl--getMemberMoney--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT userBalance FROM tbl_customer WHERE id = '"+customerId+"'");
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BigDecimal userBalance = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				userBalance = rs.getBigDecimal("userBalance");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<MemberDaoImpl--getMemberMoney--end>");
		return userBalance;
	}


	@Override
	public boolean updateMemberMoney(Long customerId, BigDecimal money) {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE tbl_customer SET userBalance = userBalance - "+money+" WHERE id = '"+customerId+"'");
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			result = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		if(result>0){
			return true;
		}
		return false;
	}


	@Override
	public Long insertMemberUseLog(MemberUseLog memberLog) {
		log.info("<MemberDaoImpl--insertMemberUseLog--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO member_use_log(customerId,orderId,useMoney,createTime)");
		sql.append(" VALUES(?,?,?,NOW())");
		List<Object> param = new ArrayList<Object>();
		param.add(memberLog.getCustomerId());
		param.add(memberLog.getOrderId());
		param.add(memberLog.getUseMoney());
		int id = insertGetID(sql.toString(), param);
		log.info("<MemberDaoImpl--insertMemberUseLog--end>");
		if(id>0){
			return Long.valueOf(id);
		}
		return null;
	}

}

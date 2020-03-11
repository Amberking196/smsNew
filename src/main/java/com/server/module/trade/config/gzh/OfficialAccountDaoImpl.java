package com.server.module.trade.config.gzh;

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
public class OfficialAccountDaoImpl extends MySqlFuns implements OfficialAccountDao{

	private static final Logger log = LogManager.getLogger(OfficialAccountDaoImpl.class);

	@Override
	public List<OfficialAccountInfo> findOfficialAccountInfo() {
		log.info("<OfficialAccountDaoImpl--findOfficialAccountInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id,appId,secret,token,companyId,appAppId,appSecret FROM `official_accounts_info` WHERE deleteFlag = 0");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		OfficialAccountInfo oaInfo = null;
		List<OfficialAccountInfo> oaList = new ArrayList<OfficialAccountInfo>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				oaInfo = new OfficialAccountInfo();
				oaInfo.setAppId(rs.getString("appId"));
				oaInfo.setId(rs.getInt("id"));
				oaInfo.setSecret(rs.getString("secret"));
				oaInfo.setToken(rs.getString("token"));
				oaInfo.setCompanyId(rs.getInt("companyId"));
				oaInfo.setAppAppId(rs.getString("appAppId"));
				oaInfo.setAppSecret(rs.getString("appSecret"));
				oaList.add(oaInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OfficialAccountDaoImpl--findOfficialAccountInfo--end>");
		return oaList;
	}

	@Override
	public OfficialAccountInfo findOfficialAccountInfoByCompanyId(Integer companyId) {
		log.info("<OfficialAccountDaoImpl--findOfficialAccountInfoByCompanyId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,appId,secret,token,companyId FROM `official_accounts_info` WHERE deleteFlag = 0 AND companyId = '"+companyId+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		OfficialAccountInfo oaInfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				oaInfo = new OfficialAccountInfo();
				oaInfo.setAppId(rs.getString("appId"));
				oaInfo.setId(rs.getInt("id"));
				oaInfo.setSecret(rs.getString("secret"));
				oaInfo.setToken(rs.getString("token"));
				oaInfo.setCompanyId(rs.getInt("companyId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OfficialAccountDaoImpl--findOfficialAccountInfoByCompanyId--end>");
		return oaInfo;
	}
	
}

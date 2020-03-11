package com.server.module.trade.config.pay;

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
public class MerchantInfoDaoImpl extends MySqlFuns implements MerchantInfoDao{

	private static final Logger log = LogManager.getLogger(MerchantInfoDaoImpl.class);
	
	@Override
	public List<MerchantInfo> findMerchantInfo(Integer type) {
		log.info("<MerchantInfoDaoImpl--findMerchantInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id,appId,mchId,mchKey,keyPath,`type`,companyId,planId,usingCompanyConfig,appKey FROM `merchant_info` WHERE deleteFlag = 0 and `type` = "+type);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MerchantInfo> merchantList = new ArrayList<MerchantInfo>();
		MerchantInfo merchant = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				merchant = new MerchantInfo();
				merchant.setAppId(rs.getString("appId"));
				merchant.setId(rs.getInt("id"));
				merchant.setKeyPath(rs.getString("keyPath"));
				merchant.setMchId(rs.getString("mchId"));
				merchant.setMchKey(rs.getString("mchKey"));
				merchant.setType(rs.getInt("type"));
				merchant.setCompanyId(rs.getInt("companyId"));
				merchant.setPlanId(rs.getString("planId"));
				merchant.setUsingCompanyConfig(rs.getInt("usingCompanyConfig"));
				merchant.setAppKey(rs.getString("appKey"));
				merchantList.add(merchant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<MerchantInfoDaoImpl--findMerchantInfo--end>");
		return merchantList;
	}

	@Override
	public MerchantInfo findMerchantInfoByCompanyId(Integer companyId) {
		log.info("<MerchantInfoDaoImpl--findMerchantInfoByCompanyId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,appId,mchId,mchKey,keyPath,`type`,companyId,planId,usingCompanyConfig ");
		sql.append(" FROM merchant_info WHERE companyId = '"+companyId+"' AND deleteFlag = 0 AND `type` = 0");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MerchantInfo merchant = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				merchant = new MerchantInfo();
				merchant.setAppId(rs.getString("appId"));
				merchant.setId(rs.getInt("id"));
				merchant.setKeyPath(rs.getString("keyPath"));
				merchant.setMchId(rs.getString("mchId"));
				merchant.setMchKey(rs.getString("mchKey"));
				merchant.setType(rs.getInt("type"));
				merchant.setCompanyId(rs.getInt("companyId"));
				merchant.setPlanId(rs.getString("planId"));
				merchant.setUsingCompanyConfig(rs.getInt("usingCompanyConfig"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<MerchantInfoDaoImpl--findMerchantInfoByCompanyId--end>");
		return merchant;
	}
	
}

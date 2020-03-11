package com.server.module.zfb_trade.module.aliInfo;

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
public class AliInfoDaoImpl extends MySqlFuns implements AliInfoDao{

	private Logger log = LogManager.getLogger(AliInfoDaoImpl.class);

	@Override
	public AliInfoBean getAliInfo(Integer companyId) {
		log.info("<AliInfoDaoImpl--getAliInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,companyId,appId,app_private_key,app_public_key,alipay_public_key,partner");
		sql.append(" FROM ali_info WHERE companyId = "+companyId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AliInfoBean aliInfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				aliInfo = new AliInfoBean();
				aliInfo.setAlipayPublicKey(rs.getString("alipay_public_key"));
				aliInfo.setAppId(rs.getString("appId"));
				aliInfo.setAppPrivateKey(rs.getString("app_private_key"));
				aliInfo.setAppPublicKey(rs.getString("app_public_key"));
				aliInfo.setCompanyId(rs.getInt("companyId"));
				aliInfo.setId(rs.getInt("id"));
				aliInfo.setPartner(rs.getString("partner"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliInfoDaoImpl--getAliInfo--end>");
		return aliInfo;
	}

	@Override
	public List<AliInfoBean> getAllAliInfo() {
		log.info("<AliInfoDaoImpl--getAllAliInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,companyId,appId,app_private_key,app_public_key,alipay_public_key,partner");
		sql.append(" FROM ali_info");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AliInfoBean aliInfo = null;
		List<AliInfoBean> aliInfoList = new ArrayList<AliInfoBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				aliInfo = new AliInfoBean();
				aliInfo.setAlipayPublicKey(rs.getString("alipay_public_key"));
				aliInfo.setAppId(rs.getString("appId"));
				aliInfo.setAppPrivateKey(rs.getString("app_private_key"));
				aliInfo.setAppPublicKey(rs.getString("app_public_key"));
				aliInfo.setCompanyId(rs.getInt("companyId"));
				aliInfo.setId(rs.getInt("id"));
				aliInfo.setPartner(rs.getString("partner"));
				aliInfoList.add(aliInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliInfoDaoImpl--getAllAliInfo--end>");
		return aliInfoList;
	}

	@Override
	public AliMerchantInfoBean getAliMerchantInfo(Integer companyId) {
		log.info("<AliInfoDaoImpl--getAliMerchantInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,companyId,partner,`key`,template_id");
		sql.append(" FROM ali_merchant_info WHERE companyId = "+companyId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AliMerchantInfoBean merchant = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				merchant = new AliMerchantInfoBean();
				merchant.setCompanyId(rs.getInt("companyId"));
				merchant.setId(rs.getInt("id"));
				merchant.setKey(rs.getString("key"));
				merchant.setPartner(rs.getString("partner"));
				merchant.setTemplateId(rs.getString("template_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliInfoDaoImpl--getAliMerchantInfo--end>");
		return merchant;
	}

	@Override
	public List<AliMerchantInfoBean> getAllAliMerchantInfo() {
		log.info("<AliInfoDaoImpl--getAllAliMerchantInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,companyId,partner,`key`,template_id");
		sql.append(" FROM ali_merchant_info");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AliMerchantInfoBean merchant = null;
		List<AliMerchantInfoBean> merchantList = new ArrayList<AliMerchantInfoBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				merchant = new AliMerchantInfoBean();
				merchant.setCompanyId(rs.getInt("companyId"));
				merchant.setId(rs.getInt("id"));
				merchant.setKey(rs.getString("key"));
				merchant.setPartner(rs.getString("partner"));
				merchant.setTemplateId(rs.getString("template_id"));
				merchantList.add(merchant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliInfoDaoImpl--getAllAliMerchantInfo--end>");
		return merchantList;
	}
	
	
}

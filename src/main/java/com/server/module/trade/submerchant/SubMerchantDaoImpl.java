package com.server.module.trade.submerchant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class SubMerchantDaoImpl extends MySqlFuns implements SubMerchantDao{

	private static final Logger log = LogManager.getLogger(SubMerchantDaoImpl.class);

	@Override
	public SubMerchantBean queryByVmCode(String vmCode) {
		log.info("<SubMerchantDaoImpl--queryByVmCode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT sm.id,sm.`companyId`,sm.`createTime`,sm.`createUser`,sm.`deleteFlag`,");
		sql.append("sm.`subAppid`,sm.`subMchId`,sm.`updateTime`,sm.`updateUser`");
		sql.append("FROM sub_merchant AS sm ");
		sql.append("INNER JOIN vending_machines_info AS vmi ON sm.`companyId`=vmi.`companyId`");
		sql.append("WHERE vmi.`code` = '"+vmCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SubMerchantBean merchantBean = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				merchantBean = new SubMerchantBean();
				merchantBean.setCompanyId(rs.getInt("companyId"));
				merchantBean.setCreateTime(rs.getDate("createTime"));
				merchantBean.setCreateUser(rs.getLong("createUser"));
				merchantBean.setDeleteFlag(rs.getInt("deleteFlag"));
				merchantBean.setId(rs.getInt("id"));
				merchantBean.setSubAppid(rs.getString("subAppid"));
				merchantBean.setSubMchId(rs.getString("subMchId"));
				merchantBean.setUpdateTime(rs.getDate("updateTime"));
				merchantBean.setUpdateUser(rs.getLong("updateUser"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		log.info("<SubMerchantDaoImpl--queryByVmCode--end>");
		return merchantBean;
	}
}

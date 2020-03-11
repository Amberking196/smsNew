package com.server.module.footmark;

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
public class FootmarkDaoImpl extends MySqlFuns implements FootmarkDao{

	private Logger log = LogManager.getLogger(FootmarkDaoImpl.class);
	
	@Override
	public Long insertFootmark(FootmarkBean footmarkBean) {
		//log.info("<FootmarkDaoImpl--insertFootmark--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO footmark(userType,userId,vmCode,`mode`,createTime,enterId)");
		sql.append(" VALUES(?,?,?,?,NOW(),?)");
		List<Object> param = new ArrayList<Object>();
		param.add(footmarkBean.getUserType());
		param.add(footmarkBean.getUserId());
		param.add(footmarkBean.getVmCode());
		param.add(footmarkBean.getMode());
		param.add(footmarkBean.getEnterId());
		int id = insertGetID(sql.toString(), param);
		//log.info("<FootmarkDaoImpl--insertFootmark--end>");
		return Long.valueOf(id);
	}

	
	public FootmarkBean findFinalReplenishRecord(String vmCode,Integer wayNum,Long basicItemId,Long userId) {
		log.info("<ReplenishDaoImpl--findFinalReplenishRecord--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM `footmark` ");  
		sql.append(" where vmCode = "+vmCode );
		sql.append(" and wayNum = "+wayNum);
		sql.append(" and userType = 2 ");
		sql.append(" ORDER BY id desc ");
		sql.append(" LIMIT 1 ");
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		FootmarkBean fbBean= new FootmarkBean();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				fbBean.setMode(rs.getInt("mode"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<ReplenishDaoImpl--findFinalReplenishRecord--end>");
		return fbBean;
		
	}
}

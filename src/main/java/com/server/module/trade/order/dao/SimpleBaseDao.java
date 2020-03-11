package com.server.module.trade.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.server.dbpool.DBPool;
import com.server.util.sqlUtil.MySqlFuns;

@Component
public class SimpleBaseDao extends MySqlFuns{
	private static Log log = LogFactory.getLog(SimpleBaseDao.class);
	@Value("${db.show.sql}")
	public  Boolean showSql;
	public Object baseSelectMap(String sql,List<Object> plist) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			if (showSql) {
				log.info(sql);
				if(plist!=null)
				log.info(plist.toString());
			}
			conn = openConnection();
			pst=conn.prepareStatement(sql);
			if (plist != null && plist.size() > 0){
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			}
			rs = pst.executeQuery();
			List<Map<String,?>> list = Lists.newArrayList();
			while (rs.next()) {
				ResultSetMetaData md=rs.getMetaData();
				int ccount=md.getColumnCount();
				Map<String,Object> map=Maps.newHashMap();
				for(int i=1;i<=ccount;i++){
					String fieldName=md.getColumnName(i);
					map.put(fieldName, rs.getObject(fieldName));
				}
				list.add(map);
				
			}
			
			
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return Lists.newArrayList();
		} finally {
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	 
}

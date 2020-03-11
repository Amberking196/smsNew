package com.server.module.event;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.util.sqlUtil.MySqlFuns;
import com.server.util.stateEnum.RefundApplicationEnum;
import com.server.util.stateEnum.RefundTypeEnum;

@Repository
public class EventDaoImpl extends MySqlFuns implements EventDao{
	
	private final static Logger log = LogManager.getLogger(EventDaoImpl.class);

	@Override
	public List<EventBean> eventList(){
		log.info("<EventDaoImpl--eventList--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM wechat_event ");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<EventBean> list=Lists.newArrayList();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				EventBean eb=new EventBean();
				eb.setCode(rs.getString("code"));
				eb.setContent(rs.getString("content"));
				eb.setIsPic(rs.getString("isPic"));
				eb.setType(rs.getString("type"));
				list.add(eb);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<EventDaoImpl--eventList--end>");
		return list;
	}

	
	@Override
	public boolean updateEvent(EventBean eventBean) {
		log.info("<EventDaoImpl--updateEvent--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" update `wechat_event` set content ='"+eventBean.getContent()+"' where id = "+ eventBean.getId());
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = 0;
		List<EventBean> list=Lists.newArrayList();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeUpdate();
			if(rs>0) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<EventDaoImpl--updateEvent--end>");
		return false;
	}

	@Override
	public boolean insertEvent(EventBean eventBean) {
		log.info("<EventDaoImpl--insertEvent--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("insert  into `wechat_event`(type,code,content,isPic) values ("+eventBean.getType()+","+eventBean.getCode()+","+eventBean.getContent()+","+eventBean.getIsPic()+")");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeUpdate();
			if(rs>0) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<EventDaoImpl--insertEvent--end>");
		return false;
	}
	
	@Override
	public boolean delEvent(EventBean eventBean) {
		log.info("<EventDaoImpl--delEvent--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("delete from `wechat_event` where id = "+eventBean.getId());
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeUpdate();
			if(rs>0) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<EventDaoImpl--delEvent--end>");
		return false;
	}
}

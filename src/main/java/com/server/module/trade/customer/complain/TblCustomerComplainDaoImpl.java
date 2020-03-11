package com.server.module.trade.customer.complain;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.trade.customer.complainReply.TblCustomerComplainReplyBean;
import com.server.module.trade.customer.complainReply.TblCustomerComplainReplyDao;
import com.server.module.trade.util.UserUtil;
import com.server.util.EmojiUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * author name: why create time: 2018-08-17 08:48:16
 */
@Repository
public class TblCustomerComplainDaoImpl extends BaseDao<TblCustomerComplainBean> implements TblCustomerComplainDao {

	private static Logger log = LogManager.getLogger(TblCustomerComplainDaoImpl.class);

	@Autowired
	private TblCustomerComplainReplyDao tblCustomerComplainReplyDaoImpl;

	/**
	 * 故障申报增加
	 */
	public TblCustomerComplainBean insert(TblCustomerComplainBean entity) {
		log.info("<CustomerMessageDaoImpl>----<insert>-------start");
		TblCustomerComplainBean tblCustomerComplainBean = super.insert(entity);
		log.info("<CustomerMessageDaoImpl>----<insert>-------end");
		return tblCustomerComplainBean;
	}

	/**
	 *我的故障申报
	 */
	public List<TblCustomerComplainBean> myDeclaration(TblCustomerComplainForm tblCustomerComplainForm) {
		log.info("<TblCustomerComplainDaoImpl>----<myDeclaration>-------start");
		StringBuilder sql = new StringBuilder();
		List<TblCustomerComplainBean> list = Lists.newArrayList();
		Long customerId = UserUtil.getUser().getId();
		sql.append(" select tcc.id,tcc.customerId,tcc.phone,tcc.content,tcc.picName,tcc.state,tcc.type,tcc.createTime,tcw.nickname from tbl_customer_complain  tcc ");
		sql.append("  left join tbl_customer_wx tcw on tcc.customerId=tcw.customerId where tcc.deleteFlag=0 and tcc.customerId='"+customerId+"' ");
		//state  故障申报回复状态  0 未回复 1 已回复
		if (tblCustomerComplainForm.getState()!=null) {
			sql.append(" and tcc.state='"+tblCustomerComplainForm.getState()+"'");
		}
		sql.append(" order by tcc.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		TblCustomerComplainBean bean = null;
		log.info("我的故障申报 SQL:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean = new TblCustomerComplainBean();
				long id = rs.getLong("id");
				bean.setId(id);
				bean.setPhone(rs.getString("phone"));
				bean.setContent(EmojiUtil.getString(rs.getString("content")));
				bean.setPicName(rs.getString("picName"));
				bean.setState(rs.getInt("state"));
				bean.setType(rs.getInt("type"));
				bean.setCreateTimes(rs.getString("createTime").substring(0, rs.getString("createTime").indexOf(".")));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setNickName(EmojiUtil.getEmoji(rs.getString("nickname")));
				//查询回复信息
				List<TblCustomerComplainReplyBean> list1 = tblCustomerComplainReplyDaoImpl.list(id);
				bean.setList(list1);
				list.add(bean);
			}
			log.info("<TblCustomerComplainDaoImpl>----<listPage>-------end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<TblCustomerComplainDaoImpl>----<listPage>-------end");
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public boolean update(Long  id){
		log.info("<TblCustomerComplainDaoImpl>-----<update>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  tbl_customer_complain set state= 0 where id in ("+id+") ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("修改故障申报状态sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if(executeUpdate>0) {
				log.info("<TblCustomerComplainDaoImpl>-----<update>-----end");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return false;
	}

	public  Integer findComplaintsNumberById(){
		log.info("<TblCustomerComplainDaoImpl>-----<findComplaintsNumberById>-----start");
		StringBuilder sql = new StringBuilder();
		Long customerId = UserUtil.getUser().getId();
		sql.append(" select *  from  tbl_customer_complain where date(createTime) = curdate() and customerId='"+customerId+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("用户每天申诉次数sql语句："+sql.toString());
		int number=0;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			while (rs.next())  {
				number++;
			}
			log.info("<TblCustomerComplainDaoImpl>-----<findComplaintsNumberById>-----end");
			return number;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<TblCustomerComplainDaoImpl>-----<findComplaintsNumberById>-----end");
		return number;
	}
}

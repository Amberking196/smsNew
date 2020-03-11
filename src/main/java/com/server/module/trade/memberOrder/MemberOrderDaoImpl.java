package com.server.module.trade.memberOrder;

import com.server.common.persistence.BaseDao;
import com.server.util.StringUtil;
import com.server.util.stateEnum.PayStateEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * author name: why create time: 2018-09-26 14:36:43
 */
@Repository
public class MemberOrderDaoImpl extends BaseDao<MemberOrderBean> implements MemberOrderDao {

	private static Logger log = LogManager.getLogger(MemberOrderDaoImpl.class);


	/**
	 * 微信完成商城购买会员订单后的回调，用以确认订单是否完成支付，并更新状态
	 */
	@Override
	public boolean paySuccessMemberOrder(String outTradeNo, String transactionId) {
		log.info("<MemberOrderDaoImpl>-----<paySuccessMemberOrder>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update member_order set ptCode='" + transactionId+"' ,payTime=current_timestamp(),state=" + PayStateEnum.PAY_SUCCESS.getState()+"  where payCode='" + outTradeNo + "' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("更新会员订单状态 sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if(executeUpdate>0) {
				log.info("<MemberOrderDaoImpl>-----<paySuccessMemberOrder>-----end");
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

	/**
	 * 添加为会员
	 */
	@Override
	public boolean addMember(MemberOrderBean entity) {
		log.info("<MemberOrderDaoImpl>-----<addMember>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  tbl_customer set isMember= 1 ,startTime='"+entity.getStartTime()+"',endTime='"+entity.getEndTime()+"',");
		sql.append(" userBalance=userBalance+"+entity.getPrice()+" where id='"+entity.getCustomerId()+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("添加为会员  sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if(executeUpdate>0) {
				log.info("<MemberOrderDaoImpl>-----<addMember>-----end");
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

	/**
	 * 获取订单信息
	 */
	@Override
	public MemberOrderBean getMemberOrder(String payCode) {
		log.info("<MemberOrderDaoImpl>-----<getMemberOrder>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select mo.wayNumber,mo.vmCode,mo.type,mo.id,mo.customerId,mo.price,mo.state,mo.ptCode,mo.payCode,mo.createTime,mo.payTime,mo.updateTime,mo.friendCustomerId,mo.friendPhone from member_order mo ");
		sql.append(" where payCode= '"+payCode+"'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("获取订单信息sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			MemberOrderBean bean=null;
			while(rs.next()) {
				bean = new MemberOrderBean();
				bean.setType(rs.getInt("type"));
				bean.setId(rs.getLong("id"));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setPrice(rs.getBigDecimal("price"));
				bean.setState(rs.getLong("state"));
				bean.setPtCode(rs.getString("ptCode"));
				bean.setPayCode(rs.getString("payCode"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setPayTime(rs.getDate("payTime"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setFriendCustomerId(rs.getLong("friendCustomerId"));
				bean.setFriendPhone(rs.getString("friendPhone"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setWayNumber(rs.getString("wayNumber"));
			}
			log.info("<MemberOrderDaoImpl>-----<getMemberOrder>-----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return null;
	}

	/**
	 * 修改用户余额
	 */
	@Override
	public boolean updateCustomerBalance(MemberOrderBean entity) {
		log.info("<MemberOrderDaoImpl>-----<updateCustomerBalance>-----start");
		//得到充值金额  计算本次充值最终金额
		BigDecimal price = entity.getPrice();
		BigDecimal sumPrice=new BigDecimal(0);
		// 金额单次  50<=price<100
/*		if(price.compareTo(BigDecimal.valueOf(50)) >= 0 && price.compareTo(BigDecimal.valueOf(100)) < 0){
			sumPrice=price.add(price.multiply(BigDecimal.valueOf(0.04)));
		// 金额单次  100<=price<200
		} else if (price.compareTo(BigDecimal.valueOf(100)) >= 0 && price.compareTo(BigDecimal.valueOf(200)) < 0) {
			sumPrice=price.add(price.multiply(BigDecimal.valueOf(0.06)));
		// 金额单次  200<=price<500
		}else if(price.compareTo(BigDecimal.valueOf(200)) >= 0 && price.compareTo(BigDecimal.valueOf(500)) < 0){
			sumPrice=price.add(price.multiply(BigDecimal.valueOf(0.08)));
		//金额单次  price>=500
		}else if(price.compareTo(BigDecimal.valueOf(500)) >= 0){
			sumPrice=price.add(price.multiply(BigDecimal.valueOf(0.1)));
		}else {*/
			sumPrice=price;
		//}
		StringBuilder sql = new StringBuilder();
		if(StringUtil.isNotBlank(entity.getFriendPhone())){
			sql.append(" update  tbl_customer set userBalance=userBalance+"+sumPrice+" where id='"+entity.getFriendCustomerId()+"' ");
		}else{
			sql.append(" update  tbl_customer set userBalance=userBalance+"+sumPrice+" where id='"+entity.getCustomerId()+"' ");
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("修改用户余额  sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if(executeUpdate>0) {
				log.info("<MemberOrderDaoImpl>-----<updateCustomerBalance>-----end");
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

	/**
	 * 增加用户充值订单
	 */
	public MemberOrderBean insert(MemberOrderBean entity) {
		log.info("<MemberOrderDaoImpl>-----<insert>------start");
		MemberOrderBean memberOrderBean = super.insert(entity);
		log.info("<MemberOrderDaoImpl>-----<insert>------end");
		return memberOrderBean;
	}
	

	

	
}

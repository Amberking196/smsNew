package com.server.module.carryWaterVouchersManage.carryWaterVouchersCustomer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.server.module.trade.util.UserUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: why create time: 2018-11-03 16:14:12
 */
@Repository
public class CarryWaterVouchersCustomerDaoImpl extends BaseDao<CarryWaterVouchersCustomerBean>
		implements CarryWaterVouchersCustomerDao {

	private static Logger log = LogManager.getLogger(CarryWaterVouchersCustomerDaoImpl.class);

	
	/**
	 * 更新用户提水券
	 */
	public boolean update(CarryWaterVouchersCustomerBean entity) {
		log.info("<CarryWaterVouchersCustomerDaoImpl>------<update>----start>");
		StringBuffer sql = new StringBuffer();
		Date date = entity.getEndTime();
		String endTime = DateUtil.formatYYYYMMDDHHMMSS(date);
		sql.append("update carry_water_vouchers_customer set quantity ="+entity.getQuantity()+" , endTime='"+endTime+"'  where id="+entity.getId());
		log.info("更新用户提水券sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		Integer rs = null;
		boolean updateCouponCustomer= false;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeUpdate();
			if(rs!=null && rs>0){
				updateCouponCustomer = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<CarryWaterVouchersCustomerDaoImpl>------<update>----end>");
		return updateCouponCustomer;
	}

	/**
	 * 提水券绑定用户
	 */
	public CarryWaterVouchersCustomerBean insert(CarryWaterVouchersCustomerBean entity) {
		log.info("<CarryWaterVouchersCustomerDaoImpl>-------<insert>------start");
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into carry_water_vouchers_customer (customerId,startTime,endTime,carryId,quantity,createUser,orderId) ");
		sql.append(" values(?,?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(entity.getCustomerId());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(entity.getStartTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(entity.getEndTime()));
		param.add(entity.getCarryId());
		param.add(entity.getQuantity());
		param.add(entity.getCreateUser());
		param.add(entity.getOrderId());
		insertGetID(sql.toString(),param);
		log.info("<CarryWaterVouchersCustomerDaoImpl>-------<insert>------end");
		return entity;
	}

	

	/**
	 * 获取用户绑定提水券信息
	 */
	@Override
	public CarryWaterVouchersCustomerBean getCarryWaterVouchersCustomerBean(Long customerId, Long carryId) {
		log.info("<CarryWaterVouchersCustomerDaoImpl>-------<getCarryWaterVouchersCustomerBean>-----start");
		StringBuilder sql = new StringBuilder();
		CarryWaterVouchersCustomerBean bean=null;
		sql.append("select c.id,c.carryId,c.customerId,c.quantity,c.startTime,c.endTime,c.createTime,c.createUser,c.updateTime,c.updateUser,c.deleteFlag ");
		sql.append(" from carry_water_vouchers_customer c where c.carryId='"+carryId+"' and c.customerId='"+customerId+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		if (showSql) {
			log.info("获取用户绑定提水券信息sql语句："+sql);
		}
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean = new CarryWaterVouchersCustomerBean();
				bean.setId(rs.getLong("id"));
				bean.setCarryId(rs.getLong("carryId"));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setStartTime(rs.getTimestamp("startTime"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getTimestamp("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
			}
			log.info("<CarryWaterVouchersCustomerDaoImpl>-------<getCarryWaterVouchersCustomerBean>-----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<CarryWaterVouchersCustomerDaoImpl>-------<getCarryWaterVouchersCustomerBean>-----end");
			return bean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public boolean reduceCarry(Long id, Integer useNum) {
		log.info("<CarryWaterVouchersCustomerDaoImpl>-------<reduceCarry>-----start");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE `carry_water_vouchers_customer` SET ");
		sql.append(" useQuantity = useQuantity +  '"+useNum+"',updateTime = NOW()");
		sql.append(" WHERE id = '"+id+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int result = -1;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<CarryWaterVouchersCustomerDaoImpl>-------<reduceCarry>-----end");
		if(result>0){
			return true;
		}
		return false;
	}

	@Override
	public List<CarryWaterVouchersCustomerDto> myCarryWaterVouchers(CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm) {
		log.info("<CarryWaterVouchersCustomerDaoImpl>-------<myCarryWaterVouchers>-----start");
		List<CarryWaterVouchersCustomerDto> list = Lists.newArrayList();
		Long customerId = UserUtil.getUser().getId();
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		// state  1 未使用   2 已使用   3  已过期
		sql.append("select w.name carryName,w.remark,c.id,c.carryId,c.customerId,c.quantity,c.useQuantity,c.startTime,c.endTime  from carry_water_vouchers_customer  c  ");
		sql.append(" inner join carry_water_vouchers w on c.carryId=w.id where c.deleteFlag=0  and c.customerId='" + customerId + "'");
		if (carryWaterVouchersCustomerForm.getState() == 1) {//未使用
			sql.append(" and c.endTime > now() and useQuantity<quantity  ");
		}
		if (carryWaterVouchersCustomerForm.getState() == 2) {//已使用
			sql.append(" and c.endTime >now() and c.useQuantity>0  ");
		}
		if (carryWaterVouchersCustomerForm.getState() == 3) {//已过期
			sql.append(" and c.endTime < now() ");
		}
		sql.append(" order by c.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("我的提水券 SQL 语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				int id = 0;
				CarryWaterVouchersCustomerDto bean = new CarryWaterVouchersCustomerDto();
				bean.setId(id);
				bean.setCarryId(rs.getLong("carryId"));
				bean.setCarryName(rs.getString("carryName"));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setStartTime(rs.getDate("startTime"));
				bean.setEndTime(rs.getDate("endTime"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setUseQuantity(rs.getLong("useQuantity"));
				bean.setSurplusQuantity(rs.getLong("quantity") - rs.getLong("useQuantity"));
				bean.setRemark(rs.getString("remark"));
				bean.setIsShow(1);
				list.add(bean);
				id++;
			}
			log.info("<CarryWaterVouchersCustomerDaoImpl>-------<myCarryWaterVouchers>-----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<CarryWaterVouchersCustomerDaoImpl>-------<myCarryWaterVouchers>-----end");
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public boolean delete(Long orderId) {
		log.info("<CarryWaterVouchersCustomerDaoImpl>------<delete>----start>");
		StringBuffer sql = new StringBuffer();
		sql.append("update carry_water_vouchers_customer set deleteFlag=1  where orderId="+orderId);
		log.info("更新用户提水券sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		boolean flag= false;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			Integer rs = ps.executeUpdate();
			if(rs!=null && rs>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<CarryWaterVouchersCustomerDaoImpl>------<delete>----end>");
		return flag;
	}
	
}

package com.server.module.activity;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.module.coupon.MachinesLAC;
import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class ActivityDaoImpl extends MySqlFuns implements ActivityDao {

	private final static Logger log = LogManager.getLogger(ActivityDaoImpl.class);

	@Override
	public List<ActivityBean> getActivity(MachinesLAC machinesLAC,BigDecimal money,Long productId) {
		//log.info("<ActivityDaoImpl--getActivity--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT pa.`id`, pa.`name`,pa.`type`,pa.`timeQuantumId`,pa.`timeFrame`,");
		sql.append(" pa.`startTime`,pa.`endTime`,pa.`money`,pa.`deductionMoney`,pa.`target`,");
		sql.append(" pa.`discountType`");
		sql.append(" FROM promotion_activity AS pa");
		sql.append(" LEFT JOIN activity_product AS ap ON pa.`id` = ap.`activityId`");
		sql.append(" LEFT JOIN item_basic AS ib ON ap.`productId` = ib.`id` WHERE 1=1 ");
		sql.append(" AND pa.startTime<= now() AND pa.endTime> now() ");
		sql.append(" AND pa.deleteFlag = 0 AND pa.useWhere = 1 ");
		sql.append(" AND ( CASE");
		sql.append("     WHEN pa.target = 1 AND pa.companyId = '"+machinesLAC.getCompanyId()+"' THEN 1");
		sql.append("     WHEN pa.target = 2 AND pa.areaId = '"+machinesLAC.getAreaId()+"' THEN 1");
		sql.append("     WHEN pa.target = 3 AND pa.vmCode = '"+machinesLAC.getVmCode()+"' THEN 1");
		sql.append("     ELSE 0 END ) = 1");
		sql.append(" AND ( CASE ");
		sql.append("     WHEN pa.`type` = 1 AND pa.money<= '"+money.toString()+"' THEN 1");
		sql.append("     WHEN pa.`type` = 2 THEN 1");
		sql.append("     ELSE 0 END) = 1");
		sql.append(" AND ( CASE ");
		sql.append("     WHEN pa.bindProduct = 0 THEN 1");
		sql.append("     WHEN pa.bindProduct = 1 AND ap.deleteFlag = 0 AND ap.`productId` = '"+productId+"' THEN 1");
		sql.append("     ELSE 0 END) = 1");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ActivityBean> activityList = new ArrayList<ActivityBean>();
		ActivityBean activity = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				activity = new ActivityBean();
				activity.setId(rs.getLong("id"));
				activity.setName(rs.getString("name"));
				activity.setType(rs.getInt("type"));
				activity.setTimeQuantumId(rs.getString("timeQuantumId"));
				activity.setTimeFrame(rs.getString("timeFrame"));
				activity.setStartTime(rs.getTimestamp("startTime"));
				activity.setEndTime(rs.getTimestamp("endTime"));
				activity.setMoney(rs.getDouble("money"));
				activity.setDeductionMoney(rs.getDouble("deductionMoney"));
				activity.setTarget(rs.getInt("target"));
				activity.setDiscountType(rs.getInt("discountType"));
				activityList.add(activity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<ActivityDaoImpl--getActivity--end>");
		return activityList;
	}

	@Override
	public ActivityBean getLocalActivity(MachinesLAC machinesLAC,String basicItemIds) {
		//log.info("<ActivityDaoImpl--getLocalActivity--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT pa.`id`, pa.`name`,pa.`type`,pa.`timeQuantumId`,pa.`timeFrame`, ");
		sql.append(" pa.`startTime`,pa.`endTime`,pa.`money`,pa.`deductionMoney`,pa.`target`,");
		sql.append(" pa.`discountType` FROM promotion_activity AS pa");
		sql.append(" LEFT JOIN activity_product AS ap ON ap.`activityId` = pa.`id`");
		sql.append(" WHERE pa.`startTime` <= NOW() AND pa.`endTime` >= NOW()");
		sql.append(" AND pa.deleteFlag = 0 AND pa.useWhere = 1 ");
		sql.append(" AND ( CASE WHEN pa.`target` = 1 AND pa.`companyId` = '"+machinesLAC.getCompanyId()+"' THEN 1");
		sql.append(" 	WHEN pa.`target` = 2 AND pa.`areaId` = '"+machinesLAC.getAreaId()+"' THEN 1");
		sql.append(" 	WHEN pa.`target` = 3 AND pa.`vmCode` = '"+machinesLAC.getVmCode()+"' THEN 1");
		sql.append(" 	ELSE 0 END)=1");
		sql.append(" AND (CASE WHEN pa.`bindProduct` = 0 THEN 1");
		sql.append("    WHEN pa.`bindProduct` = 1 AND ap.deleteFlag = 0 AND FIND_IN_SET(ap.`productId`,'"+basicItemIds+"') THEN 1");
		sql.append("    ELSE 0 END) = 1");
		
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<ActivityDaoImpl--getLocalActivity--end>");
		return null;
	}

	@Override
	public List<ActivityProductBean> getActivityProduct(Long activityId) {
		//log.info("<ActivityDaoImpl--getActivityProduct--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,activityId,productId,deleteFlag FROM `activity_product` ");
		sql.append(" WHERE deleteFlag = 0 AND activityId = "+activityId);
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ActivityProductBean> productList = new ArrayList<ActivityProductBean>();
		ActivityProductBean product = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				product = new ActivityProductBean();
				product.setActivityId(rs.getLong("activityId"));
				product.setDeleteFlag(rs.getInt("deleteFlag"));
				product.setId(rs.getLong("id"));
				product.setProductId(rs.getLong("productId"));
				productList.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<ActivityDaoImpl--getActivityProduct--end>");
		return productList;
	}

	@Override
	public List<ActivityQuantumBean> getActivityQuantum(String quantnumIds) {
		//log.info("<ActivityDaoImpl--getActivityQuantum--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,companyId,timeSlot,rebate,deleteFlag FROM `time_quantum` ");
		sql.append(" WHERE deleteFlag = 0 AND id IN("+quantnumIds+")");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ActivityQuantumBean> quantumList = new ArrayList<ActivityQuantumBean>();
		ActivityQuantumBean quantum = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				quantum = new ActivityQuantumBean();
				quantum.setCompanyId(rs.getLong("companyId"));
				quantum.setDeleteFlag(rs.getInt("deleteFlag"));
				quantum.setId(rs.getLong("id"));
				quantum.setRebate(rs.getDouble("rebate"));
				quantum.setTimeSlot(rs.getString("timeSlot"));
				quantumList.add(quantum);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<ActivityDaoImpl--getActivityQuantum--end>");
		return quantumList;
	}

	@Override
	public Map<ActivityBean, List<Long>> getMoreGoodsActivity(MachinesLAC machinesLAC, BigDecimal money, String productIds) {
		//log.info("<ActivityDaoImpl--getMoreGoodsActivity--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT pa.`id`, pa.`remark`,pa.`type`,pa.`timeQuantumId`,pa.`timeFrame`,");
		sql.append(" pa.`startTime`,pa.`endTime`,pa.`money`,pa.`deductionMoney`,pa.`target`,");
		sql.append(" pa.`discountType`,ap.`productId`,pa.bindProduct");
		sql.append(" FROM promotion_activity AS pa");
		sql.append(" LEFT JOIN activity_product AS ap ON pa.`id` = ap.`activityId`");
		sql.append(" LEFT JOIN item_basic AS ib ON ap.`productId` = ib.`id` WHERE 1=1 ");
		sql.append(" AND pa.startTime<= now() AND pa.endTime> now() ");
		sql.append(" AND pa.deleteFlag = 0 AND pa.useWhere = 1 ");
		sql.append(" AND ( CASE");
		sql.append("     WHEN pa.target = 1 AND pa.companyId = '"+machinesLAC.getCompanyId()+"' THEN 1");
		sql.append("     WHEN pa.target = 2 AND pa.areaId = '"+machinesLAC.getAreaId()+"' THEN 1");
		sql.append("     WHEN pa.target = 3 AND pa.vmCode = '"+machinesLAC.getVmCode()+"' THEN 1");
		sql.append("     ELSE 0 END ) = 1");
		sql.append(" AND ( CASE ");
		sql.append("     WHEN pa.`type` = 1 AND pa.money<= '"+money.toString()+"' THEN 1");
		sql.append("     WHEN pa.`type` = 2 THEN 1");
		sql.append("     ELSE 0 END) = 1");
		sql.append(" AND ( CASE ");
		sql.append("     WHEN pa.bindProduct = 0 THEN 1");
		sql.append("     WHEN pa.bindProduct = 1 AND ap.deleteFlag = 0 AND ap.`productId` IN ("+productIds+") THEN 1");
		sql.append("     ELSE 0 END) = 1");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<ActivityBean, List<Long>> activityMap = new HashMap<ActivityBean, List<Long>>();
		ActivityBean activity = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				activity = new ActivityBean();
				activity.setId(rs.getLong("id"));
				activity.setRemark(rs.getString("remark"));
				activity.setType(rs.getInt("type"));
				activity.setTimeQuantumId(rs.getString("timeQuantumId"));
				activity.setTimeFrame(rs.getString("timeFrame"));
				activity.setStartTime(rs.getTimestamp("startTime"));
				activity.setEndTime(rs.getTimestamp("endTime"));
				activity.setMoney(rs.getDouble("money"));
				activity.setDeductionMoney(rs.getDouble("deductionMoney"));
				activity.setTarget(rs.getInt("target"));
				activity.setDiscountType(rs.getInt("discountType"));
				if(rs.getInt("bindProduct")==0){
					activityMap.put(activity, null);
				}else{
					List<Long> list = activityMap.get(activity);
					if(list == null){
						list = new ArrayList<Long>();
						activityMap.put(activity, list);
					}
					list.add(rs.getLong("productId"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<ActivityDaoImpl--getMoreGoodsActivity--end>");
		return activityMap;
	}

}

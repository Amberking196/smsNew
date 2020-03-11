package com.server.module.coupon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.server.module.trade.util.UserUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.module.product.ShoppingGoodsBean;
import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class CouponDaoImpl extends MySqlFuns implements CouponDao{

	private static final Logger log = LogManager.getLogger(CouponDaoImpl.class);
	
	public List<CouponBean> getCoupon(Long customerId,MachinesLAC mlac,Double money,Long productId){
		//log.info("<CouponDaoImpl--getCoupon--start>");
		StringBuffer sql = new StringBuffer();
		Date date = new Date();
		String now = DateUtil.formatYYYYMMDDHHMMSS(date);
		sql.append(" SELECT c.deductionMoney,c.id,c.formulaMode,c.type,cc.quantity,c.maximumDiscount,cc.id as couponCustomerId");
		sql.append(" FROM `coupon` AS c");
		sql.append(" INNER JOIN coupon_customer AS cc ON c.`id` = cc.`couponId`");
		sql.append(" LEFT JOIN coupon_product AS cp ON cp.`couponId` = c.`id`");
		sql.append(" WHERE c.useWhere = 1 AND c.money <= "+ money);
		sql.append(" AND  cc.customerId = "+customerId +" AND cc.state = 1  AND cc.quantity > 0");
		sql.append(" AND cc.`startTime`<= '"+now+"' AND cc.`endTime`>= '"+now+"'");
		sql.append(" AND ( CASE ");
		
		sql.append(" WHEN c.target = 1 AND FIND_IN_SET("+mlac.getCompanyId()+",getChildList(c.companyId)) THEN 1");
//		sql.append(" WHEN c.target = 1 AND c.companyId = "+mlac.getCompanyId()+" THEN 1");
//		sql.append(" WHEN target = 2 AND areaId = "+mlac.getAreaId()+" THEN 1");
//		sql.append(" WHEN target = 3 AND vmCode = '"+mlac.getVmCode()+"' THEN 1");
		sql.append(" WHEN c.target = 2 AND  EXISTS (");
//		sql.append(" SELECT va.id FROM vending_area AS va WHERE va.`id`= c.`areaId` AND va.companyId = "+mlac.getCompanyId());
		sql.append(" SELECT va.id FROM vending_area AS va WHERE va.`id`= c.`areaId` AND FIND_IN_SET("+mlac.getCompanyId()+",getChildList(va.companyId))");
		sql.append(" ) THEN 1");
		sql.append(" WHEN c.target = 3 AND  EXISTS (");
//		sql.append(" SELECT vmi.`code` FROM vending_machines_info AS vmi WHERE vmi.`code` = c.`vmCode` AND vmi.companyId = "+mlac.getCompanyId());
		sql.append(" SELECT vmi.`code` FROM vending_machines_info AS vmi WHERE vmi.`code` = c.`vmCode` AND FIND_IN_SET("+mlac.getCompanyId()+",getChildList(vmi.companyId))");
		sql.append(" ) THEN 1");
		sql.append(" ELSE 0");
		sql.append(" END ) = 1");
		sql.append(" AND (CASE ");
		sql.append(" WHEN c.bindProduct = 0 THEN 1");
		sql.append(" WHEN c.bindProduct = 1 AND cp.`deleteFlag` = 0 AND cp.`productId` = "+productId+" THEN 1");
		sql.append(" ELSE 0 ");
		sql.append(" END) = 1");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CouponBean> couponList = new ArrayList<CouponBean>();
		CouponBean coupon = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				coupon = new CouponBean();
				coupon.setId(rs.getLong("id"));
				coupon.setDeductionMoney(rs.getDouble("deductionMoney"));
				coupon.setFormulaMode(rs.getInt("formulaMode"));
				coupon.setType(rs.getInt("type"));
				coupon.setQuantity(rs.getInt("quantity"));
				coupon.setMaximumDiscount(rs.getDouble("maximumDiscount"));
				coupon.setCouponCustomerId(rs.getLong("couponCustomerId"));
				couponList.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<CouponDaoImpl--getCoupon--end>");
		return couponList;
	}

	@Override
	public MachinesLAC getMachinesLAC(String vmCode) {
		//log.info("<CouponDaoImpl--getMachinesLAC--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmi.`code` AS vmCode,vmi.`lineId`,vmi.`companyId`,vmi.`areaId` ");
		sql.append(" FROM `vending_machines_info` AS vmi");
		sql.append(" LEFT JOIN `vending_line` AS vl ON vmi.`lineId` =  vl.`id`");
		sql.append(" WHERE vmi.`code` = '"+vmCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MachinesLAC mlac = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				mlac = new MachinesLAC();
				mlac.setAreaId(rs.getInt("areaId"));
				mlac.setCompanyId(rs.getInt("companyId"));
				mlac.setLineId(rs.getInt("lineId"));
				mlac.setVmCode(vmCode);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<CouponDaoImpl--getMachinesLAC--end>");
		return mlac;
	}

	@Override
	public boolean updateCouponCustomer(Long couponCustomerId) {
		//log.info("<CouponDaoImpl--updateCouponCustomer--start>");
		StringBuffer sql = new StringBuffer();
//		sql.append(" UPDATE `coupon_customer` SET quantity = quantity - 1 ");
//		sql.append(" WHERE couponId = "+couponId+" AND customerId = "+customer);
		
		sql.append(" UPDATE coupon_customer SET quantity = quantity - 1, ");
		sql.append(" state = (CASE WHEN quantity >0 THEN 1 ELSE  2 END)");
		sql.append(" WHERE state = 1 AND id = "+couponCustomerId);
		
//		sql.append(" UPDATE `coupon_customer` SET state = 2,");
//		sql.append(" updateTime = '"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"'");
//		sql.append(" WHERE id IN (");
//		sql.append(" SELECT id FROM (");
//		sql.append(" SELECT MAX(id) AS id FROM coupon_customer");
//		sql.append(" WHERE couponId = "+couponId+" AND customerId = "+customer+" AND state = 1 ) AS c)");
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
		//log.info("<CouponDaoImpl--updateCouponCustomer--end>");
		if(result>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean updateCouponCustomer(Long couponId, Long customer,Integer quantity) {
		//log.info("<CouponDaoImpl--updateCouponCustomer--start>");
		StringBuffer sql = new StringBuffer();	
		sql.append(" UPDATE coupon_customer SET quantity = quantity + "+quantity+" ");
		sql.append(" WHERE state = 1 AND couponId = "+couponId+" AND customerId = "+customer);

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
		//log.info("<CouponDaoImpl--updateCouponCustomer--end>");
		if(result>0){
			return true;
		}
		return false;
	}

	
	@Override
	public Integer insertCouponCustomer(CouponCustomerBean couCusBean) {
		//log.info("<CouponDaoImpl--insertCouponCustomer--end>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO `coupon_customer`(couponId,customerId,state,startTime,");
		sql.append(" endTime,createTime,createUser,updateTime,updateUser,deleteFlag,quantity)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?)");
		String now = DateUtil.formatYYYYMMDDHHMMSS(new Date());
		List<Object> param = new ArrayList<Object>();
		param.add(couCusBean.getCouponId());
		param.add(couCusBean.getCustomerId());
		param.add(couCusBean.getState());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(couCusBean.getStartTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(couCusBean.getEndTime()));
		param.add(now);
		param.add(couCusBean.getCustomerId());
		param.add(now);
		param.add(couCusBean.getCustomerId());
		param.add(0);//1 删除，0 未删除
		param.add(couCusBean.getQuantity());
		int insertGetID = insertGetID(sql.toString(),param);
		//log.info("<CouponDaoImpl--insertCouponCustomer--end>");
		return insertGetID;
	}

	
	@Override
	public Integer insertCouponCustomerWithSumQuantity(CouponCustomerBean couCusBean) {
		//log.info("<CouponDaoImpl--insertCouponCustomer--end>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO `coupon_customer`(couponId,customerId,state,startTime,");
		sql.append(" endTime,createTime,createUser,updateTime,updateUser,deleteFlag,quantity,sumQuantity)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
		String now = DateUtil.formatYYYYMMDDHHMMSS(new Date());
		List<Object> param = new ArrayList<Object>();
		param.add(couCusBean.getCouponId());
		param.add(couCusBean.getCustomerId());
		param.add(couCusBean.getState());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(couCusBean.getStartTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(couCusBean.getEndTime()));
		param.add(now);
		param.add(couCusBean.getCustomerId());
		param.add(now);
		param.add(couCusBean.getCustomerId());
		param.add(0);//1 删除，0 未删除
		param.add(couCusBean.getQuantity());
		param.add(couCusBean.getSumQuantity());
		int insertGetID = insertGetID(sql.toString(),param);
		//log.info("<CouponDaoImpl--insertCouponCustomer--end>");
		return insertGetID;
	}
	
	@Override
	public List<CouponBean> getPresentCoupon(CouponForm couponForm) {
		//log.info("<CouponDaoImpl--getCoupon--start>");
		String now = DateUtil.formatYYYYMMDDHHMMSS(new Date());
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT c.`id`,c.`name`,c.`useWhere`,c.`target`,c.`companyId`,c.`areaId`,");
		sql.append(" c.`vmCode`,c.`bindProduct`,c.`periodType`,c.`periodDay`,c.`canSend`,");
		sql.append(" c.`type`,c.`way`,c.`money`,c.`deductionMoney`,c.`sendMax`,c.`startTime`,c.`endTime`,");
		sql.append(" c.`deleteFlag`,c.`remark`,c.`pic`,c.formulaMode,c.maximumDiscount FROM coupon AS c ");
		sql.append(" WHERE c.`way` = "+couponForm.getWay()+" AND c.deleteFlag = 0");
		if(couponForm.getUseWhere()!=null){
			sql.append("  AND c.useWhere = "+couponForm.getUseWhere());
		}
		sql.append(" AND c.canSend = 0");
		sql.append(" AND c.`createTime` <= '"+now+"' AND c.`endTime` >= '"+now+"'");
//		sql.append(" AND (CASE WHEN periodType = 1 THEN 1");
//		sql.append(" WHEN periodType = 0 AND c.`createTime` <= '"+now+"' AND c.`endTime` >= '"+now+"' THEN 1");
//		sql.append(" ELSE 0 END) = 1");
		if(couponForm.getLimitRange()){
			sql.append(" AND (CASE WHEN c.useWhere = 2 THEN 1");
			sql.append(" WHEN c.useWhere = 1 AND c.target = 1 AND FIND_IN_SET("+couponForm.getCompanyId()+",getChildList(c.companyId)) THEN 1");
			sql.append(" WHEN c.useWhere = 1 AND c.target = 2 AND c.areaId = "+couponForm.getAreaId()+" THEN 1");
			sql.append(" WHEN c.useWhere = 1 AND c.target = 3 AND c.vmCode = "+couponForm.getVmCode()+" THEN 1");
			sql.append(" ELSE 0 END) = 1");
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CouponBean coupon = null;
		List<CouponBean> couponList = new ArrayList<CouponBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				coupon = new CouponBean();
				coupon.setAreaId(rs.getLong("areaId"));
				coupon.setBindProduct(rs.getInt("bindProduct"));
				coupon.setCanSend(rs.getInt("canSend"));
				coupon.setCompanyId(rs.getLong("companyId"));
				coupon.setDeductionMoney(rs.getDouble("deductionMoney"));
				coupon.setDeleteFlag(rs.getInt("deleteFlag"));
				coupon.setId(rs.getLong("id"));
				coupon.setMoney(rs.getDouble("money"));
				coupon.setName(rs.getString("name"));
				coupon.setPeriodDay(rs.getInt("periodDay"));
				coupon.setPeriodType(rs.getInt("periodType"));
				coupon.setPic(rs.getString("pic"));
				coupon.setSendMax(rs.getInt("sendMax"));
				coupon.setUseWhere(rs.getInt("useWhere"));
				coupon.setTarget(rs.getInt("target"));
				coupon.setVmCode(rs.getString("vmCode"));
				coupon.setType(rs.getInt("type"));
				coupon.setWay(rs.getInt("way"));
				coupon.setStartTime(rs.getTimestamp("startTime"));
				coupon.setEndTime(rs.getTimestamp("endTime"));
				coupon.setRemark(rs.getString("remark"));
				coupon.setFormulaMode(rs.getInt("formulaMode"));
				coupon.setMaximumDiscount(rs.getDouble("maximumDiscount"));
				couponList.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<CouponDaoImpl--getCoupon--end>");
		return couponList;
	}

	@Override
	public boolean isReceive(Long customerId, Long couponId) {
		//log.info("<CouponDaoImpl--receiveNum--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT 1 AS times FROM `coupon_customer` WHERE customerId = "+customerId+" AND couponId = "+couponId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isReceive = false;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				isReceive = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<CouponDaoImpl--receiveNum--end>");
		return isReceive;
	}


	@Override
	public boolean updateIntegral(Long customerId, Long num) {
		//log.info("<CouponDaoImpl--updateIntegral--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("update tbl_customer set integral = integral + "+num+" WHERE id = "+customerId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		Integer rs = null;
		boolean updateIntegral = false;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeUpdate();
			if(rs!=null && rs>0){
				updateIntegral = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		//log.info("<CouponDaoImpl--updateIntegral--end>");
		return updateIntegral;
	}


	@Override
	public List<CouponDto> getAvailableCoupon(Long customerId, MachinesLAC mlac) {
		//log.info("<CouponDaoImpl--getCoupon--start>");
		StringBuffer sql = new StringBuffer();
		Date date = new Date();
		String now = DateUtil.formatYYYYMMDDHHMMSS(date);
		sql.append(" SELECT c.`id`,c.`name`,c.`type`,c.`formulaMode`,c.`useWhere`,c.`target`,");
		sql.append(" c.`companyId`,c.`areaId`,c.`vmCode`,c.`money`,c.`deductionMoney`,");
		sql.append(" c.`pic`,c.`bindProduct`,cc.`quantity`,cc.`startTime`,cc.`endTime`, ");
		sql.append(" c.useWhere,c.areaName,c.companyName,c.maximumDiscount FROM coupon AS c ");
		sql.append(" INNER JOIN coupon_customer AS cc ON c.`id` = cc.`couponId`");
		sql.append(" WHERE c.useWhere = 1");
		sql.append(" AND  cc.customerId = "+customerId +" AND cc.state = 1  AND cc.quantity > 0");
		sql.append(" AND cc.`startTime`<= '"+now+"' AND cc.`endTime`>= '"+now+"'");
		sql.append(" AND ( CASE ");
		sql.append(" WHEN c.target = 1 AND FIND_IN_SET("+mlac.getCompanyId()+",getChildList(c.companyId)) THEN 1");
//		sql.append(" WHEN target = 2 AND areaId = "+mlac.getAreaId()+" THEN 1");
//		sql.append(" WHEN target = 3 AND vmCode = '"+mlac.getVmCode()+"' THEN 1");
		sql.append(" WHEN c.target = 2 AND  EXISTS (");
		sql.append(" SELECT va.id FROM vending_area AS va WHERE va.`id`= c.`areaId` AND FIND_IN_SET("+mlac.getCompanyId()+",getChildList(va.companyId)) " );
		sql.append(" ) THEN 1");
		sql.append(" WHEN c.target = 3 AND  EXISTS (");
		sql.append(" SELECT vmi.`code` FROM vending_machines_info AS vmi WHERE vmi.`code` = c.`vmCode` AND FIND_IN_SET("+mlac.getCompanyId()+",getChildList(vmi.companyId)) ");
		sql.append(" ) THEN 1");
		sql.append(" ELSE 0");
		sql.append(" END ) = 1");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CouponDto> couponList = new ArrayList<CouponDto>();
		CouponDto coupon = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				coupon = new CouponDto();
				coupon.setAreaId(rs.getLong("areaId"));
				coupon.setAreaName(rs.getString("areaName"));
				coupon.setBindProduct(rs.getInt("bindProduct"));
				coupon.setCompanyId(rs.getLong("companyId"));
				coupon.setCompanyName(rs.getString("companyName"));
				coupon.setDeductionMoney(rs.getDouble("deductionMoney"));
				coupon.setEndTime(rs.getString("endTime"));
				coupon.setFormulaMode(rs.getInt("formulaMode"));
				coupon.setId(rs.getLong("id"));
				coupon.setMoney(rs.getDouble("money"));
				coupon.setName(rs.getString("name"));
				coupon.setPic(rs.getString("pic"));
				coupon.setQuantity(rs.getInt("quantity"));
				coupon.setStartTime(rs.getString("startTime"));
				coupon.setTarget(rs.getInt("target"));
				coupon.setType(rs.getInt("type"));
				coupon.setUseWhere(rs.getInt("useWhere"));
				coupon.setVmCode(rs.getString("vmCode"));
				coupon.setMaximumDiscount(rs.getDouble("maximumDiscount"));
				couponList.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<CouponDaoImpl--getCoupon--end>");
		return couponList;
	}

	@Override
	public CouponBean getCouponInfo(Long couponId) {
		//log.info("<CouponDaoImpl--getCoupon--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT c.`id`,c.`name`,c.`useWhere`,c.`target`,c.`companyId`,c.`areaId`,");
		sql.append(" c.`vmCode`,c.`bindProduct`,c.`periodType`,c.`periodDay`,c.`canSend`,");
		sql.append(" c.`type`,c.`way`,c.`money`,c.`deductionMoney`,c.`sendMax`,c.`startTime`,c.`endTime`,");
		sql.append(" c.`deleteFlag`,c.`remark`,c.`pic`,c.formulaMode,c.maximumDiscount FROM coupon AS c ");
		sql.append(" WHERE c.id = "+couponId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CouponBean coupon = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				coupon = new CouponBean();
				coupon.setAreaId(rs.getLong("areaId"));
				coupon.setBindProduct(rs.getInt("bindProduct"));
				coupon.setCanSend(rs.getInt("canSend"));
				coupon.setCompanyId(rs.getLong("companyId"));
				coupon.setDeductionMoney(rs.getDouble("deductionMoney"));
				coupon.setDeleteFlag(rs.getInt("deleteFlag"));
				coupon.setId(rs.getLong("id"));
				coupon.setMoney(rs.getDouble("money"));
				coupon.setName(rs.getString("name"));
				coupon.setPeriodDay(rs.getInt("periodDay"));
				coupon.setPeriodType(rs.getInt("periodType"));
				coupon.setPic(rs.getString("pic"));
				coupon.setSendMax(rs.getInt("sendMax"));
				coupon.setUseWhere(rs.getInt("useWhere"));
				coupon.setTarget(rs.getInt("target"));
				coupon.setVmCode(rs.getString("vmCode"));
				coupon.setType(rs.getInt("type"));
				coupon.setWay(rs.getInt("way"));
				coupon.setStartTime(rs.getTimestamp("startTime"));
				coupon.setEndTime(rs.getTimestamp("endTime"));
				coupon.setRemark(rs.getString("remark"));
				coupon.setFormulaMode(rs.getInt("formulaMode"));
				coupon.setMaximumDiscount(rs.getDouble("maximumDiscount"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<CouponDaoImpl--getCoupon--end>");
		return coupon;
	}

	@Override
	public List<CouponBean> getCouponInfos(String couponId) {
		//log.info("<CouponDaoImpl--getCouponInfos--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT c.`id`,c.`name`,c.`useWhere`,c.`target`,c.`companyId`,c.`areaId`,");
		sql.append(" c.`vmCode`,c.`bindProduct`,c.`periodType`,c.`periodDay`,c.`canSend`,");
		sql.append(" c.`type`,c.`way`,c.`money`,c.`deductionMoney`,c.`sendMax`,c.`startTime`,c.`endTime`,");
		sql.append(" c.`deleteFlag`,c.`remark`,c.`pic`,c.formulaMode,c.maximumDiscount FROM coupon AS c ");
		sql.append(" WHERE c.id in ("+couponId+")");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CouponBean coupon = null;
		List<CouponBean> list=Lists.newArrayList();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				coupon = new CouponBean();
				coupon.setAreaId(rs.getLong("areaId"));
				coupon.setBindProduct(rs.getInt("bindProduct"));
				coupon.setCanSend(rs.getInt("canSend"));
				coupon.setCompanyId(rs.getLong("companyId"));
				coupon.setDeductionMoney(rs.getDouble("deductionMoney"));
				coupon.setDeleteFlag(rs.getInt("deleteFlag"));
				coupon.setId(rs.getLong("id"));
				coupon.setMoney(rs.getDouble("money"));
				coupon.setName(rs.getString("name"));
				coupon.setPeriodDay(rs.getInt("periodDay"));
				coupon.setPeriodType(rs.getInt("periodType"));
				coupon.setPic(rs.getString("pic"));
				coupon.setSendMax(rs.getInt("sendMax"));
				coupon.setUseWhere(rs.getInt("useWhere"));
				coupon.setTarget(rs.getInt("target"));
				coupon.setVmCode(rs.getString("vmCode"));
				coupon.setType(rs.getInt("type"));
				coupon.setWay(rs.getInt("way"));
				coupon.setStartTime(rs.getTimestamp("startTime"));
				coupon.setEndTime(rs.getTimestamp("endTime"));
				coupon.setRemark(rs.getString("remark"));
				coupon.setFormulaMode(rs.getInt("formulaMode"));
				coupon.setMaximumDiscount(rs.getDouble("maximumDiscount"));
				list.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<CouponDaoImpl--getCouponInfos--end>");
		return list;
	}
	
	@Override
	public CouponBean getCouponInfoByProduct(ShoppingGoodsBean shoppingGoodsBean) {
		//log.info("<CouponDaoImpl--getCoupon--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT c.`id`,c.`name`,c.`useWhere`,c.`target`,c.`companyId`,c.`areaId`,");
		sql.append(" c.`vmCode`,c.`bindProduct`,c.`periodType`,c.`periodDay`,c.`canSend`,");
		sql.append(" c.`type`,c.`way`,c.`money`,c.`deductionMoney`,c.`sendMax`,c.`startTime`,c.`endTime`,");
		sql.append(" c.`deleteFlag`,c.`remark`,c.`pic`,c.formulaMode,c.maximumDiscount FROM coupon AS c where 1=1 ");
		if(shoppingGoodsBean.getTarget()==0) {
			sql.append(" and name = '" +shoppingGoodsBean.getName() +"'");
		}
		else if(shoppingGoodsBean.getTarget()==1){
			sql.append(" and areaId=0 and name = '" + shoppingGoodsBean.getName() + "'  and companyId = '"
					+ shoppingGoodsBean.getCompanyId() + "' ");
		}
		else if(shoppingGoodsBean.getTarget()==2){
			sql.append(" and name = '" + shoppingGoodsBean.getName() + "'  and companyId = '"
					+ shoppingGoodsBean.getCompanyId() + "' and areaId = '" + shoppingGoodsBean.getAreaId() + "' ");
		}
		else if(shoppingGoodsBean.getTarget()==3) {
			sql.append(" and name = '" + shoppingGoodsBean.getName() + "'  and vmCode ="+shoppingGoodsBean.getVmCode());
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CouponBean coupon = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				coupon = new CouponBean();
				coupon.setAreaId(rs.getLong("areaId"));
				coupon.setBindProduct(rs.getInt("bindProduct"));
				coupon.setCanSend(rs.getInt("canSend"));
				coupon.setCompanyId(rs.getLong("companyId"));
				coupon.setDeductionMoney(rs.getDouble("deductionMoney"));
				coupon.setDeleteFlag(rs.getInt("deleteFlag"));
				coupon.setId(rs.getLong("id"));
				coupon.setMoney(rs.getDouble("money"));
				coupon.setName(rs.getString("name"));
				coupon.setPeriodDay(rs.getInt("periodDay"));
				coupon.setPeriodType(rs.getInt("periodType"));
				coupon.setPic(rs.getString("pic"));
				coupon.setSendMax(rs.getInt("sendMax"));
				coupon.setUseWhere(rs.getInt("useWhere"));
				coupon.setTarget(rs.getInt("target"));
				coupon.setVmCode(rs.getString("vmCode"));
				coupon.setType(rs.getInt("type"));
				coupon.setWay(rs.getInt("way"));
				coupon.setStartTime(rs.getTimestamp("startTime"));
				coupon.setEndTime(rs.getTimestamp("endTime"));
				coupon.setRemark(rs.getString("remark"));
				coupon.setFormulaMode(rs.getInt("formulaMode"));
				coupon.setMaximumDiscount(rs.getDouble("maximumDiscount"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<CouponDaoImpl--getCoupon--end>");
		return coupon;
	}

	
	@Override
	public CouponCustomerBean getCouponCustomerBean(Long customerId, Long couponId) {
		//log.info("<CouponDaoImpl--getCouponCustomerBean--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM `coupon_customer` WHERE customerId = "+customerId+" AND couponId = "+couponId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
//		boolean isReceive = false;
        CouponCustomerBean bean = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				bean = new CouponCustomerBean();
                bean.setId(rs.getLong("id"));
                bean.setCouponId(rs.getLong("couponId"));
                bean.setCustomerId(rs.getLong("customerId"));
                bean.setState(rs.getInt("state"));
                bean.setCreateTime(rs.getDate("createTime"));
                bean.setCreateUser(rs.getLong("createUser"));
                bean.setUpdateTime(rs.getDate("updateTime"));
                bean.setUpdateUser(rs.getLong("updateUser"));
                bean.setQuantity(rs.getInt("quantity"));
                bean.setSumQuantity(rs.getInt("sumQuantity"));
                bean.setDeleteFlag(rs.getInt("deleteFlag"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<CouponDaoImpl--getCouponCustomerBean--end>");
		return bean;
	}
	@Override
    public Boolean updateCouponCustomerBean(CouponCustomerBean entity) {
		//log.info("<CouponDaoImpl--CouponCustomerBean--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("update coupon_customer set quantity ="+entity.getQuantity()+" ,sumQuantity = "+entity.getSumQuantity()+" where id="+entity.getId());
		log.info("sql语句："+sql);
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
		//log.info("<CouponDaoImpl--CouponCustomerBean--end>");
		return updateCouponCustomer;
    }

	@Override
	public MoreGoodsCouponDto getMoreGoodsCoupon(Long customerId, MachinesLAC mlac, Double money, String productIds) {
		//log.info("<CouponDaoImpl--getMoreGoodsCoupon--start>");
		StringBuffer sql = new StringBuffer();
		Date date = new Date();
		String now = DateUtil.formatYYYYMMDDHHMMSS(date);
		sql.append(" SELECT c.deductionMoney,c.id,c.name,c.formulaMode,c.type,cc.quantity,cp.productId,c.bindProduct,c.maximumDiscount,cc.id as couponCustomerId");
		sql.append(" FROM `coupon` AS c");
		sql.append(" INNER JOIN coupon_customer AS cc ON c.`id` = cc.`couponId`");
		sql.append(" LEFT JOIN coupon_product AS cp ON cp.`couponId` = c.`id`");
		sql.append(" WHERE c.useWhere = 1 AND c.money <= "+ money);
		sql.append(" AND  cc.customerId = "+customerId +" AND cc.state = 1  AND cc.quantity > 0 AND cc.deleteFlag=0 ");
		sql.append(" AND cc.`startTime`<= '"+now+"' AND cc.`endTime`>= '"+now+"'");
		sql.append(" AND ( CASE ");
//		sql.append(" WHEN c.target = 1 AND c.companyId = "+mlac.getCompanyId()+" THEN 1");
		sql.append(" WHEN c.target = 1 AND FIND_IN_SET("+mlac.getCompanyId()+",getChildList(c.companyId)) THEN 1");
//		sql.append(" WHEN target = 2 AND areaId = "+mlac.getAreaId()+" THEN 1");
//		sql.append(" WHEN target = 3 AND vmCode = '"+mlac.getVmCode()+"' THEN 1");
		sql.append(" WHEN c.target = 2 AND  EXISTS (");
//		sql.append(" SELECT va.id FROM vending_area AS va WHERE va.`id`= c.`areaId` AND va.companyId = "+mlac.getCompanyId());
		sql.append(" SELECT va.id FROM vending_area AS va WHERE va.`id`= c.`areaId` AND FIND_IN_SET("+mlac.getCompanyId()+",getChildList(va.companyId))");
		sql.append(" ) THEN 1");
		sql.append(" WHEN c.target = 3 AND  EXISTS (");
//		sql.append(" SELECT vmi.`code` FROM vending_machines_info AS vmi WHERE vmi.`code` = c.`vmCode` AND vmi.companyId = "+mlac.getCompanyId());
		sql.append(" SELECT vmi.`code` FROM vending_machines_info AS vmi WHERE vmi.`code` = c.`vmCode` AND FIND_IN_SET("+mlac.getCompanyId()+",getChildList(vmi.companyId))");
		sql.append(" ) THEN 1");
		sql.append(" ELSE 0");
		sql.append(" END ) = 1");
		sql.append(" AND (CASE ");
		sql.append(" WHEN c.bindProduct = 0 THEN 1");
		sql.append(" WHEN c.bindProduct = 1 AND cp.`deleteFlag` = 0 AND cp.`productId` IN ("+productIds+") THEN 1");
		sql.append(" ELSE 0 ");
		sql.append(" END) = 1");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CouponBean coupon = null;
		Map<CouponBean,Integer> couponMap = new HashMap<CouponBean,Integer>();
		Map<CouponBean,List<Long>> itemMap = new HashMap<CouponBean,List<Long>>();
		Set<CouponBean> noBindItemList = new HashSet<CouponBean>();
		MoreGoodsCouponDto couponDto = new MoreGoodsCouponDto(couponMap,itemMap,noBindItemList);
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				coupon = new CouponBean();
				coupon.setId(rs.getLong("id"));
				coupon.setDeductionMoney(rs.getDouble("deductionMoney"));
				coupon.setFormulaMode(rs.getInt("formulaMode"));
				coupon.setName(rs.getString("name"));
				coupon.setType(rs.getInt("type"));
				coupon.setQuantity(rs.getInt("quantity"));
				coupon.setBindProduct(rs.getInt("bindProduct"));
				coupon.setMaximumDiscount(rs.getDouble("maximumDiscount"));
				coupon.setCouponCustomerId(rs.getLong("couponCustomerId"));
				couponMap.put(coupon, coupon.getQuantity());
				if(coupon.getBindProduct() == 1){
					Long productId = rs.getLong("productId");
					if(itemMap.get(coupon) == null){
						List<Long> itemList = new ArrayList<Long>();
						itemMap.put(coupon,itemList);
					}
					List<Long> list = itemMap.get(coupon);
					list.add(productId);
				}else{
					noBindItemList.add(coupon);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<CouponDaoImpl--getMoreGoodsCoupon--end>");
		return couponDto;
	}

	@Override
	public boolean reduceCoupon(Long couponCustomerId,Integer num) {
		//log.info("<CouponDaoImpl--updateCouponCustomer--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE coupon_customer SET");
		sql.append(" state = (CASE WHEN quantity > "+num+" THEN 1 ELSE  2 END) ,");
		sql.append(" quantity = quantity - " + num);
		sql.append(" WHERE state = 1 AND  id = "+couponCustomerId);
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
		//log.info("<CouponDaoImpl--updateCouponCustomer--end>");
		if(result>0){
			return true;
		}
		return false;
	}

	@Override
	public List<CouponBean> myCoupon(CouponForm couponForm) {
		//log.info("<CouponDaoImpl>----<CouponBean>----start>");
		//得到当前用户id
		Long customerId= UserUtil.getUser().getId();
		StringBuilder sql = new StringBuilder();
		List<CouponBean> list = Lists.newArrayList();
		// state  2 未使用  3 已使用   4 已过期
		if(couponForm.getState()==2){//c.useWhere=2 and
			sql.append("select c.id,c.name,c.useWhere,c.target,c.companyId,c.areaId,c.areaName,c.companyName,c.vmCode,c.type,c.way,c.money,c.deductionMoney,c.sendMax,cc.startTime,cc.endTime,c.pic,c.bindProduct,c.periodType,c.periodDay,cc.quantity,sumQuantity,c.maximumDiscount from coupon c inner join coupon_customer cc on c.id=cc.couponId where ");
			sql.append(" cc.state<2 and cc.endTime>now() and cc.`customerId`="+customerId+" order by cc.createTime desc ");
		}
		if(couponForm.getState()==3){
			sql.append("select c.id,c.name,c.useWhere,c.target,c.companyId,c.areaId,c.areaName,c.companyName,c.vmCode,c.type,c.way,c.money,c.deductionMoney,c.sendMax,cc.startTime,cc.endTime,c.pic,c.bindProduct,c.periodType,c.periodDay,cc.quantity,sumQuantity,c.maximumDiscount from coupon c inner join coupon_customer cc on c.id=cc.couponId where ");
			sql.append("  cc.`customerId`="+customerId+"  and c.sendMax > cc.quantity order by cc.createTime desc " );
		}
		if(couponForm.getState()==4){
			sql.append("select c.id,c.name,c.useWhere,c.target,c.companyId,c.areaId,c.areaName,c.companyName,c.vmCode,c.type,c.way,c.money,c.deductionMoney,c.sendMax,cc.startTime,cc.endTime,c.pic,c.bindProduct,c.periodType,c.periodDay,cc.quantity,sumQuantity,c.maximumDiscount from coupon c inner join coupon_customer cc on c.id=cc.couponId where ");
			sql.append("  cc.state<2 and cc.endTime <now() and cc.`customerId`="+customerId+" order by cc.createTime desc " );
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("我的优惠券 SQL 语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				CouponBean bean = new CouponBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setUseWhere(rs.getInt("useWhere"));
				bean.setTarget(rs.getInt("target"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setAreaId(rs.getLong("areaId"));
				bean.setType(rs.getInt("type"));
				bean.setWay(rs.getInt("way"));
				bean.setMoney(rs.getDouble("money"));
				bean.setPeriodType(rs.getInt("periodType"));
				bean.setPeriodDay(rs.getInt("periodDay"));
				bean.setDeductionMoney(rs.getDouble("deductionMoney"));
				bean.setSendMax(rs.getInt("sendMax"));
				bean.setStartTime(rs.getTimestamp("startTime"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setPic(rs.getString("pic"));
				bean.setBindProduct(rs.getInt("bindProduct"));
				bean.setMaximumDiscount(rs.getDouble("maximumDiscount"));
				if(couponForm.getState()!=1) {
					bean.setQuantity(rs.getInt("quantity"));
					bean.setUseQuantity(rs.getInt("sendMax")-rs.getInt("quantity"));
					if(bean.getWay().equals(5))
					{
						bean.setUseQuantity(rs.getInt("sumQuantity")-rs.getInt("quantity"));
					}
				}
				list.add(bean);
			}
			//log.info("<CouponDaoImpl>----<CouponBean>----end>");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<CouponDaoImpl>----<CouponBean>----end>");
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
}

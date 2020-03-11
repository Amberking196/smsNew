package com.server.module.carryWaterVouchersManage.carryWaterVouchers;

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

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;

import com.server.module.coupon.MachinesLAC;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: why create time: 2018-11-03 09:02:25
 */
@Repository
public class CarryWaterVouchersDaoImpl extends BaseDao<CarryWaterVouchersBean> implements CarryWaterVouchersDao {

	private static Logger log = LogManager.getLogger(CarryWaterVouchersDaoImpl.class);

	
	/**
	 *  获取提水券信息
	 */
	public CarryWaterVouchersBean get(Long id) {
		log.info("<CarryWaterVouchersDaoImpl>-------<get>------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select w.id,w.name,w.target,w.companyId,w.areaId,w.areaName,w.companyName,w.vmCode,w.bindProduct,w.sendMax,w.startTime, ");
		sql.append(" w.endTime,w.createTime,w.createUser,w.updateTime,w.updateUser,w.deleteFlag,w.pic,w.remark,w.periodType,w.periodDay ");
		sql.append(" from  carry_water_vouchers  w  where w.id='"+id+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		CarryWaterVouchersBean bean=null;
		log.info("获取提水券信息sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean=new CarryWaterVouchersBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setTarget(rs.getInt("target"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setAreaId(rs.getLong("areaId"));
				bean.setAreaName(rs.getString("areaName"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setBindProduct(rs.getInt("bindProduct"));
				bean.setSendMax(rs.getInt("sendMax"));
				bean.setStartTime(rs.getTimestamp("startTime"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getTimestamp("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setPic(rs.getString("pic"));
				bean.setRemark(rs.getString("remark"));
				bean.setPeriodDay(rs.getInt("periodDay"));
				bean.setPeriodType(rs.getInt("periodType"));
			}
			log.info("<CarryWaterVouchersDaoImpl>-------<get>----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<CarryWaterVouchersDaoImpl>-------<get>----end");
			return bean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}


	/**
	 * 提水券列表
	 */
	@Override
	public ReturnDataUtil listPage(CarryWaterVouchersForm carryWaterVouchersForm) {
		log.info("<CarryWaterVouchersDaoImpl>-------<listPage>------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select w.id,w.name,w.target,w.companyId,w.areaId,w.areaName,w.companyName,w.vmCode,w.bindProduct,w.sendMax,w.startTime,w.periodType,w.periodDay, ");
		sql.append(" w.endTime,w.createTime,w.createUser,w.updateTime,w.updateUser,w.deleteFlag,w.pic,w.remark, ");
		sql.append("s.state,s.quantity,s.isHomeShow,s.id shoppingGoodsId,s.quantity,s.costPrice,s.salesPrice,s.details,s.commodityParameters,s.purchaseNotes");
		sql.append(" from carry_water_vouchers  w left join carry_record cr on cr.carryId=w.id  left join shopping_goods s on w.id=s.vouchersId where w.deleteFlag=0 ");
		
		if(StringUtil.isNotBlank(carryWaterVouchersForm.getOrderId())) {
			sql.append(" and  cr.orderId in ("+carryWaterVouchersForm.getOrderId()+") ");
		}

		sql.append(" order by w.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		if (showSql) {
			log.info("提水券列表SQL语句：" + sql.toString());
		}
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<CarryWaterVouchersBean> list = Lists.newArrayList();
			while (rs.next()) {
				CarryWaterVouchersBean bean = new CarryWaterVouchersBean();
				bean.setId(rs.getLong("id"));
				bean.setBindProduct(rs.getInt("bindProduct"));
				bean.setSendMax(rs.getInt("sendMax"));
				bean.setStartTime(rs.getTimestamp("startTime"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getTimestamp("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setPic(rs.getString("pic"));
				bean.setRemark(rs.getString("remark"));
				bean.setState(rs.getLong("state"));
				bean.setIsHomeShow(rs.getInt("isHomeShow"));
				bean.setTarget(rs.getInt("target"));
				bean.setPeriodType(rs.getInt("periodType"));
				bean.setPeriodDay(rs.getInt("periodDay"));
				bean.setName(rs.getString("name"));
				list.add(bean);
			}
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<CarryWaterVouchersDaoImpl>-------<listPage>------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<CarryWaterVouchersDaoImpl>-------<listPage>------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}


	@Override
	public Map<CarryDto,List<Long>> getCarryWaterVoucherByCustomer(Long customerId, MachinesLAC mlac, String productIds) {
		log.info("<CarryWaterVouchersDaoImpl>-------<getCarryWaterVoucherByCustomer>------start");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT a.`id` AS carryId,a.`name` as carryName,b.`quantity`,b.useQuantity,c.`itemId`,b.id AS carryCustomerId,a.bindProduct,b.endTime,b.orderId  FROM `carry_water_vouchers` AS a");
		sql.append(" INNER JOIN `carry_water_vouchers_customer` AS b ON b.`carryId` = a.`id`");
		sql.append(" LEFT JOIN `carry_water_vouchers_product` AS c ON c.`carryId` = a.`id`");
		sql.append(" WHERE b.`customerId` = '"+customerId+"'");
		sql.append(" AND b.`startTime` <= NOW() AND b.`endTime` >= NOW()");
		sql.append(" AND b.`deleteFlag` = 0  AND b.quantity > b.useQuantity ");
		sql.append(" AND (");
		sql.append(" CASE WHEN a.`target` = 1 AND FIND_IN_SET('"+mlac.getCompanyId()+"',getChildList(a.`companyId`)) THEN 1");
		sql.append(" WHEN a.`target` = 2 AND a.`areaId` = '"+mlac.getAreaId()+"' THEN 1");
		sql.append(" WHEN a.`target` = 3 AND FIND_IN_SET('"+mlac.getVmCode()+"',a.`vmCode`) THEN 1");
		sql.append(" ELSE 0 END ) = 1");
		sql.append(" AND (");
		sql.append(" CASE WHEN a.`bindProduct` = 0 THEN 1 ");
		sql.append(" WHEN a.`bindProduct` = 1 AND c.deleteFlag = 0 AND FIND_IN_SET(c.`itemId`,'"+productIds+"') THEN 1");
		sql.append(" ELSE 0 END ) = 1;");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CarryDto carry = null;
		Map<CarryDto,List<Long>> carryMap = new HashMap<CarryDto,List<Long>>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				carry = new CarryDto();
				carry.setCarryId(rs.getLong("carryId"));
				carry.setCarryName(rs.getString("carryName"));
				carry.setCarryCustomerId(rs.getLong("carryCustomerId"));
				carry.setCarryNum(rs.getInt("quantity") - rs.getInt("useQuantity"));
				carry.setEndTime(rs.getTimestamp("endTime"));
				carry.setOrderId(rs.getLong("orderId"));
				int bindProduct = rs.getInt("bindProduct");
				if(bindProduct == 1){
					List<Long> itemList = carryMap.get(carry);
					if(itemList == null){
						itemList = new ArrayList<Long>();
						carryMap.put(carry, itemList);
					}
					itemList.add(rs.getLong("itemId"));
				}else{
					carryMap.put(carry, null);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<CarryWaterVouchersDaoImpl>-------<getCarryWaterVoucherByCustomer>------end");
		return carryMap;
	}


}

package com.server.module.zfb_trade.module.vmway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.module.commonBean.ItemInfoDto;
import com.server.module.trade.vendingMachinesWayItem.WayValidatorDto;
import com.server.util.sqlUtil.MySqlFuns;
@Repository("aliVmwayDao")
public class AliVmwayDaoImpl extends MySqlFuns implements AliVmwayDao{

	public static Logger log = LogManager.getLogger(AliVmwayDaoImpl.class); 

	@Override
	public VendingMachinesWayBean queryByWayAndVmcode(String vmCode, Integer wayNo) {
		log.info("<AliVmwayDaoImpl--queryByWayAndVmcode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,vendingMachinesCode,wayNumber,itemId,");
		sql.append(" state,num,fullNum,updateTime,createTime FROM `vending_machines_way`");
		sql.append(" WHERE vendingMachinesCode= '"+vmCode+"'");
		sql.append(" AND wayNumber = "+wayNo);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VendingMachinesWayBean vmWay = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				vmWay = new VendingMachinesWayBean();
				vmWay.setCreateTime(rs.getDate("createTime"));
				vmWay.setFullNum(rs.getInt("fullNum"));
				vmWay.setId(rs.getLong("id"));
				vmWay.setItemId(rs.getLong("itemId"));
				vmWay.setNum(rs.getInt("num"));
				vmWay.setState(rs.getInt("state"));
				vmWay.setUpdateTime(rs.getDate("updateTime"));
				vmWay.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				vmWay.setWayNumber(rs.getInt("wayNumber"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		log.info("<AliVmwayDaoImpl--queryByWayAndVmcode--end>");
		return vmWay;
	}

	@Override
	public List<VendingMachinesWayBean> queryByWayAndVmcode(String vmCode) {
		log.info("<AliVmwayDaoImpl--queryByWayAndVmcode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,vendingMachinesCode,wayNumber,itemId,");
		sql.append(" state,num,fullNum,updateTime,createTime FROM `vending_machines_way`");
		sql.append(" WHERE vendingMachinesCode= '"+vmCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<VendingMachinesWayBean> vmwList = new ArrayList<VendingMachinesWayBean>();
		VendingMachinesWayBean vmWay = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				vmWay = new VendingMachinesWayBean();
				vmWay.setCreateTime(rs.getDate("createTime"));
				vmWay.setFullNum(rs.getInt("fullNum"));
				vmWay.setId(rs.getLong("id"));
				vmWay.setItemId(rs.getLong("itemId"));
				vmWay.setNum(rs.getInt("num"));
				vmWay.setState(rs.getInt("state"));
				vmWay.setUpdateTime(rs.getDate("updateTime"));
				vmWay.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				vmWay.setWayNumber(rs.getInt("wayNumber"));
				vmwList.add(vmWay);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		log.info("<AliVmwayDaoImpl--queryByWayAndVmcode--end>");
		return vmwList;
	}

	@Override
	public WayValidatorDto getOneWayInfo(String vmCode, Integer wayNum) {
		log.info("<AliVmwayDaoImpl--getOneWayInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT vmw.state,vmw.num,vmi.`price` AS minUnitPrice,ib.`standard` AS minStandard FROM `vending_machines_way`  AS vmw ");
		sql.append("  INNER JOIN vending_machines_item AS vmi ON vmw.`itemId` = vmi.`id`");
		sql.append("  INNER JOIN item_basic AS ib ON ib.`id` = vmi.`basicItemId`");
		sql.append(" where vmw.vendingMachinesCode = '"+vmCode+"' and vmw.`wayNumber` = '"+wayNum+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		WayValidatorDto validator = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				validator = new WayValidatorDto();
				validator.setNum(rs.getInt("num"));
				validator.setState(rs.getInt("state"));
				validator.setMinUnitPrice(rs.getBigDecimal("minUnitPrice"));
				validator.setMinStandard(rs.getInt("minStandard"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<VmwayItemDaoImpl--getOneWayInfo--end>");
		return validator;
	}

	@Override
	public List<ItemInfoDto> getOneWayItemInfo(String vmCode, Integer wayNum) {
		//log.info("<VmwayItemDaoImpl--getOneWayItemInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmi.basicItemId,vmi.price AS unitPrice,ib.`standard` AS unitWeight,ib.pic,ib.`name` AS itemName,vmwi.num");
		sql.append(" FROM vending_machines_way AS vmwi");
		sql.append(" INNER JOIN vending_machines_item AS vmi ON vmi.id = vmwi.`itemId`");
		sql.append(" INNER JOIN item_basic AS ib ON ib.`id` = vmi.`basicItemId`");
		sql.append(" WHERE vmwi.`vendingMachinesCode` = '"+vmCode+"' AND vmwi.`wayNumber` = '"+wayNum+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ItemInfoDto> itemInfoList = new ArrayList<ItemInfoDto>();
		ItemInfoDto itemInfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				itemInfo = new ItemInfoDto();
				itemInfo.setBasicItemId(rs.getLong("basicItemId"));
				itemInfo.setItemName(rs.getString("itemName"));
				itemInfo.setPic(rs.getString("pic"));
				itemInfo.setUnitPrice(rs.getBigDecimal("unitPrice"));
				itemInfo.setUnitWeight(rs.getInt("unitWeight"));
				itemInfo.setNum(rs.getInt("num"));
				itemInfoList.add(itemInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<VmwayItemDaoImpl--getOneWayItemInfo--end>");
		return itemInfoList;
	}
}

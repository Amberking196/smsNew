package com.server.module.zfb_trade.module.vmitem;

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

@Repository("aliVmitemDao")
public class AliVmitemDaoImpl extends MySqlFuns implements AliVmitemDao{

	public static Logger log = LogManager.getLogger(AliVmitemDaoImpl.class); 

	@Override
	public Long queryBasicItemId(String vmCode, Integer wayNum) {
		log.info("<AliVmitemDaoImpl--queryBasicItemId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ib.id AS basicItemId FROM vending_machines_way AS vmw ");
		sql.append(" INNER JOIN vending_machines_item AS vmi ON vmw.`itemId`=vmi.`id`");
		sql.append(" INNER JOIN item_basic AS ib ON vmi.`basicItemId` = ib.`id`");
		sql.append(" where vmw.vendingMachinesCode= '"+vmCode+"' and vmw.wayNumber = "+wayNum);
		log.info("sql语句:"+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long basicItemId = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				basicItemId = rs.getLong("basicItemId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		log.info("<AliVmitemDaoImpl--queryBasicItemId--end>");
		return basicItemId;
	}

	@Override
	public List<ItemDto> queryVmitem(String vmCode) {
		log.info("<AliVmitemDaoImpl--queryVmitem--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ib.NAME AS itemName,vmi.price AS price,vmw.state as state,");
		sql.append(" ib.pic AS imageUrl,vmw.wayNumber AS doorNO,vmw.num as num,vmw.fullNum as fullNum");
		sql.append(" FROM vending_machines_way vmw");
		sql.append(" INNER JOIN vending_machines_item vmi ON vmw.itemId = vmi.id");
		sql.append(" INNER JOIN item_basic ib ON ib.id = vmi.basicItemId");
		sql.append(" WHERE vmw.vendingMachinesCode = '"+vmCode+"' ORDER BY vmw.wayNumber ASC");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ItemDto item = null;
		List<ItemDto> itemList = new ArrayList<ItemDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				item = new ItemDto();
				item.setDoorNO(rs.getInt("doorNO"));
				item.setImageUrl(rs.getString("imageUrl"));
				item.setItemName(rs.getString("itemName"));
				item.setPrice(rs.getBigDecimal("price"));
				item.setNum(rs.getInt("num"));
				item.setFullNum(rs.getInt("fullNum"));
				item.setState(rs.getInt("state"));
				itemList.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliVmitemDaoImpl--queryVmitem--end>");
		return itemList;
	}
}

package com.server.module.zfb_trade.module.replenish;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.StringUtil;
import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class ReplenishDaoImpl extends MySqlFuns implements ReplenishDao{

	private static Logger log = LogManager.getLogger(ReplenishDaoImpl.class);

	@Override
	public List<ReplenishDto> queryReplenish(ReplenishForm form) {
		log.info("<ReplenishDaoImpl--queryReplenish--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT (SUM(vmw2.`fullNum`)-SUM(num)) AS replenishNum,ib.`id` AS basicItemId,ib.`name` AS itemName,");
		sql.append(" ib.pic as pic FROM `vending_machines_way` AS vmw2");
		sql.append(" INNER JOIN `vending_machines_item` AS vmi2 ON vmi2.`id`=vmw2.`itemId`");
		sql.append(" INNER JOIN `item_basic` AS ib ON ib.`id` = vmi2.`basicItemId`");
		sql.append(" WHERE vmw2.`vendingMachinesCode` IN (");
		sql.append(" SELECT temp.vendingMachinesCode AS vmCode FROM (");
		sql.append(" SELECT vmw.num,vmw.fullNum,vmw.vendingMachinesCode");
		sql.append(" FROM `vending_machines_way` AS vmw");
		sql.append(" INNER JOIN `vending_machines_info` AS vmi ON vmi.code = vmw.vendingMachinesCode");
		sql.append(" INNER JOIN company AS c ON vmi.`companyId` = c.`id`");
		sql.append(" LEFT JOIN vending_line AS vl ON vmi.lineId = vl.id ");
		sql.append(" WHERE 1=1 AND vmi.state = 20001");
		if(form.getCompanyId()!=null){
			sql.append(" AND c.`id`="+form.getCompanyId());
		}else if(StringUtil.isNotBlank(form.getCompanyIds())){
			sql.append(" and c.`id` in ("+form.getCompanyIds()+")");
		}
		if(form.getDutyId()!=null){
			sql.append(" and FIND_IN_SET("+form.getDutyId() +",vl.dutyId)");
		}
		if(StringUtil.isNotBlank(form.getVmCode())){
			sql.append(" and vmi.code = "+form.getVmCode());
		}
		if(form.getRate()==null){
			form.setRate(0.3D);
		}
		sql.append(" ) AS temp GROUP BY temp.vendingMachinesCode HAVING SUM(temp.num)/SUM(temp.fullNum)<="+form.getRate());
		sql.append(" ) GROUP BY ib.`id`");
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ReplenishDto> replenishList = new ArrayList<ReplenishDto>();
		ReplenishDto replenishDto = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				replenishDto = new ReplenishDto();
				replenishDto.setBasicItemId(rs.getInt("basicItemId"));
				replenishDto.setItemName(rs.getString("itemName"));
				replenishDto.setReplenishNum(rs.getInt("replenishNum"));
				replenishDto.setPic(rs.getString("pic"));
				replenishList.add(replenishDto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<ReplenishDaoImpl--queryReplenish--end>");
		return replenishList;
	}

	@Override
	public boolean adjustReplenishNum(Integer adjustNum,Long Id) {
		log.info("<ReplenishDaoImpl--adjustReplenishNum--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE vending_waynum_log SET adjustNum = '"+adjustNum+"' WHERE id = '"+Id+"'");
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
		log.info("<ReplenishDaoImpl--adjustReplenishNum--end>");
		if(result>0){
			return true;
		}
		return false;
	}

	@Override
	public Long getCurrentReplenish(String vmCode, Integer wayNum, Long basicItemId, Long userId) {
		log.info("<ReplenishDaoImpl--getCurrentReplenish--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT MAX(id) AS logId FROM vending_waynum_log");
		sql.append(" WHERE vmCode = '"+vmCode+"' AND wayNumber = '"+wayNum+"' AND basicItemId = '"+basicItemId+"' ");
		sql.append(" AND userId = '"+userId+"' AND TIMESTAMPDIFF(MINUTE,createTime,NOW()) < 10 ");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long id = -1L;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				id  = rs.getLong("logId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<ReplenishDaoImpl--getCurrentReplenish--end>");
		return id;
	}

	public boolean checkIfReplenishOne(String vmCode,Integer wayNum,Long basicItemId,Long userId) {
		log.info("<ReplenishDaoImpl--checkIfReplenishOne--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM `footmark` ");  
		sql.append(" where vmCode = "+vmCode );
		sql.append(" and wayNum = "+wayNum);
		sql.append(" ORDER BY id desc ");
		sql.append(" LIMIT 1 ");
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				result  = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<ReplenishDaoImpl--checkIfReplenishOne--end>");
		return result;
		
	}
}

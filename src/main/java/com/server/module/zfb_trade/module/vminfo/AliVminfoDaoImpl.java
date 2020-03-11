package com.server.module.zfb_trade.module.vminfo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.module.zfb_trade.module.replenish.ReplenishForm;
import com.server.util.StringUtil;
import com.server.util.sqlUtil.MySqlFuns;

@Repository("aliVminfoDao")
public class AliVminfoDaoImpl extends MySqlFuns implements AliVminfoDao{

	public static Logger log = LogManager.getLogger(AliVminfoDaoImpl.class); 

	@Override
	public List<VminfoDto> queryVMList(String condition,String companyIds,Integer machineType,Integer state) {
		log.info("<AliVminfoDaoImpl--queryVMList--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmi.`code` AS vmCode,vmi.locatoinName AS locationName,c.id as companyId,c.name as companyName");
		sql.append(" FROM vending_machines_info vmi INNER JOIN vending_machines_base vmb ON");
		sql.append(" vmi.machinesBaseId = vmb.id");
		sql.append(" inner join company as c on c.id = vmi.companyId");
		sql.append(" WHERE vmi.companyId IN ("+companyIds+")");
		if(StringUtil.isNotBlank(condition)){
			sql.append(" and (vmi.`code` like CONCAT('%','"+condition+"','%')");
			sql.append(" OR vmi.locatoinName like CONCAT('%','"+condition+"','%'))");
		}
		if(machineType!=null){
			sql.append(" AND vmb.machinesTypeId = "+machineType);
		}
		if(state!=null){
			sql.append(" and vmi.state="+state);
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<VminfoDto> vminfoList = new ArrayList<VminfoDto>();
		VminfoDto vminfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				vminfo = new VminfoDto();
				vminfo.setLocationName(rs.getString("locationName"));
				vminfo.setVmCode(rs.getString("vmCode"));
				vminfo.setCompanyId(rs.getInt("companyId"));
				vminfo.setCompanyName(rs.getString("companyName"));
				vminfoList.add(vminfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliVminfoDaoImpl--queryVMList--end>");
		return vminfoList;
	}

	@Override
	public List<VminfoDto> queryOwnVMList(String condition, Long dutyId, Integer machineType,Integer state) {
		log.info("<AliVminfoDaoImpl--queryOwnVMList--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmi.`code` AS vmCode,vmi.locatoinName AS locationName");
		sql.append(" FROM vending_machines_info vmi INNER JOIN vending_machines_base vmb ON");
		sql.append(" vmi.machinesBaseId = vmb.id");
		sql.append(" inner join vending_line as vl on vl.id = vmi.lineId");
		sql.append(" where FIND_IN_SET("+dutyId + ",vl.dutyId)");
		if(StringUtil.isNotBlank(condition)){
			sql.append(" AND (vmi.`code` like CONCAT('%','"+condition+"','%')");
			sql.append(" OR vmi.locatoinName like CONCAT('%','"+condition+"','%'))");
		}
		if(machineType!=null){
			sql.append(" and vmb.machinesTypeId = "+machineType);
		}
		if(state!=null){
			sql.append(" and vmi.state="+state);
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<VminfoDto> vminfoList = new ArrayList<VminfoDto>();
		VminfoDto vminfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				vminfo = new VminfoDto();
				vminfo.setLocationName(rs.getString("locationName"));
				vminfo.setVmCode(rs.getString("vmCode"));
				vminfoList.add(vminfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliVminfoDaoImpl--queryOwnVMList--end>");
		return null;
	}

	@Override
	public List<VminfoDto> queryReplenishVMList(ReplenishForm replenishForm) {
		log.info("<AliVminfoDaoImpl--queryReplenishVMList--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT temp.vendingMachinesCode AS vmCode,temp.companyName,temp.locatoinName FROM (");
		sql.append(" SELECT vmw.num,vmw.fullNum,vmw.vendingMachinesCode,c.`name` AS companyName,vmi.locatoinName");
		sql.append(" FROM `vending_machines_way` AS vmw");
		sql.append(" INNER JOIN `vending_machines_info` AS vmi ON vmi.code = vmw.vendingMachinesCode");
		sql.append(" INNER JOIN company AS c ON vmi.`companyId` = c.`id`");
		sql.append(" INNER JOIN vending_line AS vl ON vmi.lineId = vl.id ");
		sql.append(" where 1=1 and vmi.state = 20001 ");
		if(StringUtil.isNotBlank(replenishForm.getVmCode())){
			sql.append(" and vmi.code = '"+replenishForm.getVmCode()+"'");
		}
		if(replenishForm.getCompanyId()!=null){
			sql.append(" and c.id = "+replenishForm.getCompanyId());
		}else if(StringUtil.isNotBlank(replenishForm.getCompanyIds())){
			sql.append(" and c.id in ("+replenishForm.getCompanyIds()+")");
		}
		if(replenishForm.getDutyId()!=null){
			sql.append(" and FIND_IN_SET("+replenishForm.getDutyId() +",vl.dutyId)");
		}
		if(replenishForm.getRate() == null){
			replenishForm.setRate(0.3D);
		}
		sql.append(" ) AS temp GROUP BY temp.vendingMachinesCode HAVING SUM(temp.num)/SUM(temp.fullNum)<="+replenishForm.getRate());
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VminfoDto vminfo  = null;
		List<VminfoDto> vminfoList = new ArrayList<VminfoDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				vminfo = new VminfoDto();
				vminfo.setLocationName(rs.getString("locatoinName"));
				vminfo.setVmCode(rs.getString("vmCode"));
				vminfo.setCompanyName(rs.getString("companyName"));
				vminfoList.add(vminfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliVminfoDaoImpl--queryReplenishVMList--end>");
		return vminfoList;
	}

	@Override
	public Integer queryAllMachinesNum(Integer companyId, String companyIds) {
		log.info("<AliVminfoDaoImpl--queryAllMachinesNum--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COUNT(1) AS num FROM vending_machines_info AS vmi WHERE vmi.`companyId` IN (");
		if(companyId!=null){
			sql.append(companyId + ")");
		}else{
			sql.append(companyIds +")");
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer num = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				num = rs.getInt("num");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliVminfoDaoImpl--queryAllMachinesNum--end>");
		return num;
	}

	@Override
	public VminfoDto queryByVmCode(String vmCode) {
		log.info("<AliVminfoDaoImpl--queryByVmCode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT `code`,companyId FROM `vending_machines_info` WHERE `code` ='"+vmCode+"'");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VminfoDto vminfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				vminfo = new VminfoDto();
				vminfo.setCompanyId(rs.getInt("companyId"));
				vminfo.setVmCode(rs.getString("code"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliVminfoDaoImpl--queryByVmCode--end>");
		return vminfo;
	}

}

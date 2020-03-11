package com.server.module.zfb_trade.module.vmbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.sqlUtil.MySqlFuns;

@Repository("aliVendingMachinesDao")
public class AliVendingMachinesDaoImpl extends MySqlFuns implements AliVendingMachinesDao{

	public static Logger log = LogManager.getLogger(AliVendingMachinesDaoImpl.class); 
	@Override
	public VendingMachinesBaseBean queryOneByVmCode(String vmCode) {
		//log.info("<AliVendingMachinesDaoImpl--queryOneByVmCode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,machinesTypeId,aisleConfiguration,factoryNumber,");
		sql.append(" mainProgramVersion,ipcNumber,liftingGearNumber,electricCabinetNumber,");
		sql.append(" caseNumber,doorNumber,airCompressorNumber,keyStr,remark FROM `vending_machines_base`");
		sql.append(" where id = (select machinesBaseId from vending_machines_info where code = '"+vmCode+"')");
		log.info("sql语句:"+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VendingMachinesBaseBean vmBase = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				vmBase = new VendingMachinesBaseBean();
				vmBase.setAirCompressorNumber(rs.getString("airCompressorNumber"));
				vmBase.setAisleConfiguration(rs.getString("aisleConfiguration"));;
				vmBase.setCaseNumber(rs.getString("caseNumber"));;
				vmBase.setDoorNumber(rs.getString("doorNumber"));;
				vmBase.setElectricCabinetNumber(rs.getString("electricCabinetNumber"));;
				vmBase.setFactoryNumber(rs.getString("factoryNumber"));;
				vmBase.setId(rs.getLong("id"));;
				vmBase.setIpcNumber(rs.getString("ipcNumber"));;
				vmBase.setKeyStr(rs.getString("keyStr"));;
				vmBase.setLiftingGearNumber(rs.getString("liftingGearNumber"));;
				vmBase.setMachinesTypeId(rs.getInt("machinesTypeId"));;
				vmBase.setMainProgramVersion(rs.getString("mainProgramVersion"));;
				vmBase.setRemark(rs.getString("remark"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		//log.info("<AliVendingMachinesDaoImpl--queryOneByVmCode--end>");
		return vmBase;
	}

	@Override
	public VmbaseInfoDto getBaseInfo(String vmCode) {
		//log.info("<VminfoDaoImpl--getBaseInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmi.freeMachine,c.phone as principalPhone,vmb.factoryNumber,vmb.aisleConfiguration AS totalWayNum,vmi.`machineVersion`");
		sql.append(" FROM vending_machines_info AS vmi ");
		sql.append(" INNER JOIN company AS c ON vmi.companyId = c.`id`");
		sql.append(" INNER JOIN vending_machines_base AS vmb ON vmi.`machinesBaseId` = vmb.`id`");
		sql.append(" WHERE vmi.`code` = '"+vmCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VmbaseInfoDto baseInfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				baseInfo = new VmbaseInfoDto();
				baseInfo.setFreeMachine(rs.getInt("freeMachine"));
				baseInfo.setPrincipalPhone(rs.getString("principalPhone"));
				baseInfo.setFactoryNumber(rs.getString("factoryNumber"));
				baseInfo.setMachinesVersion(rs.getInt("machineVersion"));
				baseInfo.setTotalWayNum(rs.getInt("totalWayNum"));
				baseInfo.setVmCode(vmCode);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<VminfoDaoImpl--getBaseInfo--end>");
		return baseInfo;
	}
	
}

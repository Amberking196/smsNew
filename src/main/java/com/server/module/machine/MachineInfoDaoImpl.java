package com.server.module.machine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.sqlUtil.MySqlFuns;
@Repository
public class MachineInfoDaoImpl extends MySqlFuns implements MachineInfoDao{

	private final static Logger log = LogManager.getLogger(MachineInfoDaoImpl.class);
	
	@Override
	public Integer getCompanyByVmcode(String vmCode) {
		//log.info("<MachineInfoDaoImpl--getCompanyByVmcode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT companyId FROM vending_machines_info WHERE `code` = '"+vmCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer companyId = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				companyId = rs.getInt("companyId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<MachineInfoDaoImpl--getCompanyByVmcode--end>");
		return companyId;
	}

	/**
	 * 根据售卖机code查询售卖机信息
	 */
	@Override
	public VendingMachinesInfoBean findVendingMachinesByCode(String code) {
		//log.info("<VendingMachinesInfoDaoImpl--findVendingMachinesByCode--start>");
		StringBuffer sql = new StringBuffer();

		sql.append ( " select startUsingTime,c.name as companyName,banWayNumber,code,companyId,errorWarn,isShowH5,itemType,lat,linkman,"
				+ " locatoinDate,locatoinName,lon,machinesBaseId,vmi.name,respManId,vmi.state,vmi.areaId,machineVersion "
				+ " from vending_machines_info vmi left join company c on c.id = vmi.companyId where  code=" + code );
		log.info("根据售卖机code查询售卖机信息--sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VendingMachinesInfoBean vmInfoBean = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				vmInfoBean = new VendingMachinesInfoBean();
				vmInfoBean.setBanWayNumber(rs.getString("banWayNumber"));
				vmInfoBean.setCode(rs.getString("code"));
				vmInfoBean.setCompanyId(rs.getInt("companyId"));
				vmInfoBean.setErrorWarn(rs.getInt("errorWarn"));
				vmInfoBean.setIsShowH5(rs.getInt("isShowH5"));
				vmInfoBean.setItemType(rs.getString("itemType"));
				vmInfoBean.setLat(rs.getDouble("lat"));
				vmInfoBean.setLinkman(rs.getString("linkman"));
				vmInfoBean.setLocatoinDate(rs.getDate("locatoinDate"));
				vmInfoBean.setLocatoinName(rs.getString("locatoinName"));
				vmInfoBean.setLon(rs.getDouble("lon"));
				vmInfoBean.setMachinesBaseId(rs.getInt("machinesBaseId"));
				vmInfoBean.setName(rs.getString("name"));
				vmInfoBean.setRespManId(rs.getLong("respManId"));
				vmInfoBean.setState(rs.getInt("state"));
				vmInfoBean.setAreaId(rs.getInt("areaId"));
				vmInfoBean.setMachineVersion(rs.getInt("machineVersion"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection(rs, ps, conn);
		}
		// log.info("<VendingMachinesInfoDaoImpl--findVendingMachinesByCode--end>");
		return vmInfoBean;
	}
}

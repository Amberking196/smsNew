package com.server.company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.util.sqlUtil.MySqlFuns;


@Repository
public class MachinesDaoImpl extends MySqlFuns implements MachinesDao{

	private Logger log = LogManager.getLogger(MachinesDaoImpl.class);
	
	@Override
	public Integer getCompanyIdByVmCode(String vmCode) {
		//log.info("<MachinesDaoImpl--getCompanyIdByVmCode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT companyId FROM `vending_machines_info` WHERE `code` = '"+vmCode+"'");
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
		//log.info("<MachinesDaoImpl--getCompanyIdByVmCode--end>");
		return companyId;
	}

	@Override
	public Integer getCompanyIdByPayCode(String payCode) {
		//log.info("<MachinesDaoImpl--getCompanyIdByPayCode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmi.`companyId` FROM pay_record AS pr ");
		sql.append(" LEFT JOIN vending_machines_info AS vmi");
		sql.append(" ON pr.`vendingMachinesCode` = vmi.`code`");
		sql.append(" WHERE pr.`payCode` = '"+payCode+"'");
		
		sql.append(" union ");
		
		sql.append(" SELECT vmi.`companyId` FROM pay_record_vision AS pr ");
		sql.append(" LEFT JOIN vending_machines_info AS vmi");
		sql.append(" ON pr.`vendingMachinesCode` = vmi.`code`");
		sql.append(" WHERE pr.`payCode` = '"+payCode+"'");
		
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
		//log.info("<MachinesDaoImpl--getCompanyIdByPayCode--end>");
		return companyId;
	}

	@Override
	public boolean isSonCompany(Integer sonCompany, Integer faCompany) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT FIND_IN_SET("+sonCompany+",getChildList("+faCompany+")) AS location");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer location = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				location = rs.getInt("location");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		if(location!= null && location>0){
			return true;
		}
		return false;
	}

	@Override
	public List<Integer> getOtherCompanyIdByVmCode(String vmCode) {
		//log.info("<MachinesDaoImpl--getOtherCompanyIdByVmCode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT companyId FROM `replenish_company_machines` WHERE `code` = '"+vmCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer companyId = null;
		List<Integer> companyIds= Lists.newArrayList();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				companyId = rs.getInt("companyId");
				companyIds.add(companyId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<MachinesDaoImpl--getOtherCompanyIdByVmCode--end>");
		return companyIds;
	}

	@Override
	public boolean isReplenishCompanyMan(String vmCode, Integer companyId) {
		//log.info("<MachinesDaoImpl--isReplenishCompanyMan--start>");
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT 1 FROM `replenish_company_machines` WHERE companyId = "+companyId+" AND `code` = '"+vmCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				result = true;
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<MachinesDaoImpl--isReplenishCompanyMan--end>");
		return result;
	}

	@Override
	public Integer getPidById(Integer companyId) {
		//log.info("<MachinesDaoImpl--getPidById--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT parentId FROM company WHERE id = '"+companyId+"'");
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer parentId = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				if(rs.getObject("parentId")!=null){
					parentId = rs.getInt("parentId");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<MachinesDaoImpl--getPidById--end>");
		return parentId;
	}

	@Override
	public Integer getCompanyIdByPayCodeStore(String payCode) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select companyId from store_order where payCode='" + payCode + "' ");
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
		return companyId;
	}
}

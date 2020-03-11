package com.server.module.trade.customer.address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.DateUtil;
import com.server.util.StringUtil;
import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class AddressDaoImpl extends MySqlFuns implements AddressDao{

	private static final Logger log = LogManager.getLogger(AddressDaoImpl.class);
	@Override
	public AddressBean insert(AddressBean address) {
		log.info("<AddressDaoImpl--insert--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO `tbl_customer_address`(customerId,`name`,defaultFlag,`receiver`,`phone`,`sex`)");
		sql.append(" VALUES(?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(address.getCustomerId());
		param.add(address.getName());
		param.add(address.getDefaultFlag());
		param.add(address.getReceiver());
		param.add(address.getPhone());
		param.add(address.getSex());
		int id = insertGetID(sql.toString(),param);
		log.info("<AddressDaoImpl--insert--end>");
		if(id!=-1){
			address.setId(Long.valueOf(id));
			return address;
		}else{
			return null;
		}
	}

	@Override
	public boolean update(AddressBean address) {
		log.info("<AddressDaoImpl--update--start>");
		StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		sql.append(" UPDATE `tbl_customer_address` SET ");
		if(StringUtil.isNotBlank(address.getName())){
			sql.append(" `name`=?,");
			param.add(address.getName());
		}
		if(StringUtil.isNotBlank(address.getReceiver())){
			sql.append(" `receiver`=?,");
			param.add(address.getReceiver());
		}
		if(StringUtil.isNotBlank(address.getPhone())){
			sql.append(" `phone`=?,");
			param.add(address.getPhone());
		}
		if(address.getDefaultFlag()!=null){
			sql.append(" defaultFlag=?,");
			param.add(address.getDefaultFlag());
		}
		if(address.getDeleteFlag()!=null){
			sql.append(" deleteFlag=?,");
			param.add(address.getDeleteFlag());
		}
		if(address.getSex() != null){
			sql.append(" sex=?,");
			param.add(address.getSex());
		}
		if(address.getUpdateTime()!=null){
			sql.append(" updateTime=?,");
			param.add(DateUtil.formatYYYYMMDDHHMMSS(address.getUpdateTime()));
		}
		sql = sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(" where id = ?");
		param.add(address.getId());
		int upate = upate(sql.toString(), param);
		log.info("<AddressDaoImpl--update--end>");
		if(upate==1){
			return true;
		}
		return false;
	}

	@Override
	public List<AddressBean> select(Long customerId) {
		log.info("<AddressDaoImpl--select--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT `id`,`name`,`customerId`,`defaultFlag`,`receiver`,`phone`,`sex` from tbl_customer_address WHERE deleteFlag = 0 and customerId = "+customerId);
		sql.append(" order by defaultFlag desc");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AddressBean address = null;
		List<AddressBean> addressList = new ArrayList<AddressBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				address = new AddressBean();
				address.setId(rs.getLong("id"));
				address.setName(rs.getString("name"));
				address.setCustomerId(rs.getLong("customerId"));
				address.setDefaultFlag(rs.getInt("defaultFlag"));
				address.setPhone(rs.getString("phone"));
				address.setReceiver(rs.getString("receiver"));
				address.setSex(rs.getInt("sex"));
				addressList.add(address);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AddressDaoImpl--select--end>");
		return addressList;
	}

	@Override
	public boolean delete(Long addressId) {
		log.info("<AddressDaoImpl--delete--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM `tbl_customer_address` WHERE id = "+addressId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int executeUpdate =-1;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			executeUpdate = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<AddressDaoImpl--delete--end>");
		if(executeUpdate==1){
			return true;
		}
		return false;
	}

	@Override
	public boolean updateDefaultFlag(Long customerId) {
		log.info("<AddressDaoImpl--updateDefaultFlag--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE `tbl_customer_address` SET defaultFlag = 0 WHERE customerId = "+customerId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int executeUpdate = -1;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			executeUpdate = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<AddressDaoImpl--updateDefaultFlag--end>");
		if(executeUpdate>0){
			return true;
		}
		return false;
	}

	@Override
	public AddressBean selectById(Long customerId, Integer id) {
		log.info("<AddressDaoImpl--selectById--end>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT `id`,`name`,`customerId`,`defaultFlag`,`receiver`,`phone`,`sex` from tbl_customer_address WHERE deleteFlag = 0 and customerId = "+customerId+" and id = "+id);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AddressBean address = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				address = new AddressBean();
				address.setId(rs.getLong("id"));
				address.setName(rs.getString("name"));
				address.setCustomerId(rs.getLong("customerId"));
				address.setDefaultFlag(rs.getInt("defaultFlag"));
				address.setPhone(rs.getString("phone"));
				address.setReceiver(rs.getString("receiver"));
				address.setSex(rs.getInt("sex"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AddressDaoImpl--selectById--end>");
		return address;
	}

}

package com.server.module.zfb_trade.module.dooroperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.module.zfb_trade.bean.DoorOperateHistoryBean;
import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;


@Repository("aliDoorOperationDao")
public class AliDoorOperationDaoImpl extends MySqlFuns implements AliDoorOperationDao{

	public static Logger log = LogManager.getLogger(AliDoorOperationDaoImpl.class); 
	@Override
	public DoorOperateHistoryBean save(DoorOperateHistoryBean doorOperateHistory) {
		//log.info("<AliDoorOperationDaoImpl--save--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO `tbl_door_operate_history`(referenceId,basicItemId,`status`,vmCode,");
		sql.append(" openType,doorNO,preWeight,posWeight,createTime,openedTime,closedTime,openFailedTime,remark)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(doorOperateHistory.getReferenceId());
		param.add(doorOperateHistory.getBasicItemId());
		param.add(doorOperateHistory.getStatus());
		param.add(doorOperateHistory.getVmCode());
		param.add(doorOperateHistory.getOpenType());
		param.add(doorOperateHistory.getDoorNO());
		param.add(doorOperateHistory.getPreWeight());
		param.add(doorOperateHistory.getPosWeight());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(doorOperateHistory.getCreateTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(doorOperateHistory.getOpenedTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(doorOperateHistory.getClosedTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(doorOperateHistory.getOpenFailedTime()));
		param.add(doorOperateHistory.getRemark());
		int id = insertGetID(sql.toString(), param);
		//log.info("<AliDoorOperationDaoImpl--save--end>");
		if(id!= -1){
			doorOperateHistory.setId((long)id);
			return doorOperateHistory;
		}
		return doorOperateHistory;
	}
	@Override
	public boolean update(DoorOperateHistoryBean doorOperateHistory) {
		//log.info("<AliDoorOperationDaoImpl--update--start>");
		StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		sql.append(" update `tbl_door_operate_history` set `status`=?,");
		sql.append(" posWeight=?,openedTime=?,closedTime=?,openFailedTime=?,remark=?,normalFlag=?");
		sql.append(" where id = ? ");
		param.add(doorOperateHistory.getStatus());
		param.add(doorOperateHistory.getPosWeight());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(doorOperateHistory.getOpenedTime()));

		param.add(DateUtil.formatYYYYMMDDHHMMSS(doorOperateHistory.getClosedTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(doorOperateHistory.getOpenFailedTime()));
		param.add(doorOperateHistory.getRemark());
		param.add(doorOperateHistory.getNormalFlag());
		param.add(doorOperateHistory.getId());
		if(doorOperateHistory.getReferenceId() != null){
			sql.append(" and referenceId = ?");
			param.add(doorOperateHistory.getReferenceId());
		}
		int upate = upate(sql.toString(), param);
		//log.info("<AliDoorOperationDaoImpl--update--end>");
		if(upate>0){
			return true;
		}
		return false;
	}
	@Override
	public Integer get(Long doorOperateId, Long customerId) {
		//log.info("<AliDoorOperationDaoImpl--get--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT doorNO FROM tbl_door_operate_history WHERE id = '"+doorOperateId+"' AND referenceId = '"+customerId+"'");
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer doorNo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				doorNo = rs.getInt("doorNO");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<AliDoorOperationDaoImpl--get--end>");
		return doorNo;
	}
	@Override
	public DoorOperateHistoryBean getById(Long id) {
		//log.info("<AliDoorOperationDaoImpl--getById--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tdoh.`id`,tdoh.`referenceId`,tdoh.`status`,tdoh.`vmCode`,");
		sql.append(" tdoh.`openType`,tdoh.`doorNO`,tdoh.`preWeight`,tdoh.`posWeight`,");
		sql.append(" tdoh.`createTime`,tdoh.`openedTime`,tdoh.`closedTime`,tdoh.`openFailedTime`,");
		sql.append(" tdoh.`remark`,tdoh.`normalFlag`");
		sql.append(" FROM tbl_door_operate_history AS tdoh");
		sql.append(" WHERE id = '"+id+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DoorOperateHistoryBean his= null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				his = new DoorOperateHistoryBean();
				his.setClosedTime(rs.getTimestamp("closedTime"));
				his.setCreateTime(rs.getTimestamp("createTime"));
				his.setDoorNO(rs.getInt("doorNO"));
				his.setId(id);
				his.setNormalFlag(rs.getInt("normalFlag"));
				his.setOpenedTime(rs.getTimestamp("openedTime"));
				his.setOpenFailedTime(rs.getTimestamp("openFailedTime"));
				his.setOpenType(rs.getInt("openType"));
				his.setPosWeight(rs.getInt("posWeight"));
				his.setPreWeight(rs.getInt("preWeight"));
				his.setReferenceId(rs.getLong("referenceId"));
				his.setRemark(rs.getString("remark"));
				his.setStatus(rs.getInt("status"));
				his.setVmCode(rs.getString("vmCode"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<AliDoorOperationDaoImpl--getById--end>");
		return his;
	}
	@Override
	public DoorOperateHistoryBean getByVmCode(String vmCode) {
		//log.info("<AliDoorOperationDaoImpl--getByVmCodeWay--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tdoh.`id`,tdoh.`referenceId`,tdoh.`status`,tdoh.`vmCode`,");
		sql.append(" tdoh.`openType`,tdoh.`doorNO`,tdoh.`preWeight`,tdoh.`posWeight`,");
		sql.append(" tdoh.`createTime`,tdoh.`openedTime`,tdoh.`closedTime`,tdoh.`openFailedTime`,");
		sql.append(" tdoh.`remark`,tdoh.`normalFlag`");
		sql.append(" FROM tbl_door_operate_history AS tdoh");
		sql.append(" WHERE tdoh.vmCode = '"+vmCode+"'");
		sql.append(" order by tdoh.createTime desc limit 1");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DoorOperateHistoryBean his= null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				his = new DoorOperateHistoryBean();
				his.setClosedTime(rs.getTimestamp("closedTime"));
				his.setCreateTime(rs.getTimestamp("createTime"));
				his.setDoorNO(rs.getInt("doorNO"));
				his.setId(rs.getLong("id"));
				his.setNormalFlag(rs.getInt("normalFlag"));
				his.setOpenedTime(rs.getTimestamp("openedTime"));
				his.setOpenFailedTime(rs.getTimestamp("openFailedTime"));
				his.setOpenType(rs.getInt("openType"));
				if(rs.getObject("posWeight")!=null){
					his.setPosWeight(rs.getInt("posWeight"));
				}
				his.setPreWeight(rs.getInt("preWeight"));
				his.setReferenceId(rs.getLong("referenceId"));
				his.setRemark(rs.getString("remark"));
				his.setStatus(rs.getInt("status"));
				his.setVmCode(rs.getString("vmCode"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<AliDoorOperationDaoImpl--getByVmCodeWay--end>");
		return his;
	}

}

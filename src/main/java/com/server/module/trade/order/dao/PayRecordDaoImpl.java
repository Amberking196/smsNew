package com.server.module.trade.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.OrderServiceImpl;
import com.server.module.trade.order.bean.PayRecordBean;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.order.vo.MessageVo;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.CommandVersionEnum;

/**
 * author name: yjr 
 * create time: 2018-04-27 09:29:08
 */
@Repository
public class PayRecordDaoImpl extends BaseDao<PayRecordBean> implements PayRecordDao {

	public static Logger log = LogManager.getLogger( PayRecordDaoImpl.class);    

	public ReturnDataUtil listPage(int curPage,Long customerId) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select id,customerId,itemExtractId,basicItemId,itemId,payCode,vendingMachinesCode,payType,price,state,num,pickupNum,ptCode,createTime,payTime,refundTime,finishTime,costPrice,refundName,remark,itemName,itemTypeId,wayNumber from pay_record where customerId="+customerId+" order by createTime desc");
		List<Object> plist = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (curPage - 1) * 10;
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + 10);
			rs = pst.executeQuery();
			List<PayRecordBean> list = Lists.newArrayList();
			while (rs.next()) {
				PayRecordBean bean = new PayRecordBean();
				bean.setId(rs.getLong("id"));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setItemExtractId(rs.getLong("itemExtractId"));
				bean.setBasicItemId(rs.getLong("basicItemId"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setPayCode(rs.getString("payCode"));
				bean.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				bean.setPayType(rs.getInt("payType"));
				bean.setPrice(rs.getBigDecimal("price"));
				bean.setState(rs.getLong("state"));
				String stateLable="";
				if(bean.getState()==10001){
					stateLable="已支付";
				}else if(bean.getState()==10002){
					stateLable="未支付";
				}
//				else if(bean.getState()==10003){
//					stateLable="通知出货";
//				}else if(bean.getState()==10004){
//					stateLable="支付失败";
//				}else if(bean.getState()==10006){
//					stateLable="取消订单";
//				}else if(bean.getState()==10007){
//					stateLable="退款完成";
//				}else if(bean.getState()==10008){
//					stateLable="退款失败";
//				}else if(bean.getState()==10101){
//					stateLable="支付完成";
//				}else if(bean.getState()==10102){
//					stateLable="订单完成";
//				}
				bean.setStateLabel(stateLable);
				bean.setNum(rs.getLong("num"));
				bean.setPickupNum(rs.getLong("pickupNum"));
				bean.setPtCode(rs.getString("ptCode"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setPayTime(rs.getDate("payTime"));
				bean.setRefundTime(rs.getDate("refundTime"));
				bean.setFinishTime(rs.getDate("finishTime"));
				bean.setCostPrice(rs.getBigDecimal("costPrice"));
				bean.setRefundName(rs.getString("refundName"));
				bean.setRemark(rs.getString("remark"));
				bean.setItemName(rs.getString("itemName"));
				bean.setItemTypeId(rs.getLong("itemTypeId"));
				bean.setWayNumber(rs.getLong("wayNumber"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
				log.info(plist.toString());
			}
			data.setCurrentPage(curPage);
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public PayRecordBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		PayRecordBean entity = new PayRecordBean();
		return super.del(entity);
	}

	public boolean update(PayRecordBean entity) {
		return super.update(entity);
	}

	public PayRecordBean insert(PayRecordBean entity) {
	
		return super.insert(entity);
	}
	//生成订单并扣除库存
	public void createPayRecordAndReduceStock(PayRecordBean entity,Long wayId,Integer reduceNum,Integer preNum,String vmCode,int way,Long customerId,Long basicItemId,Long itemId,Long footmarkId){
		Connection conn=null;
		Statement st=null;
		try {
			conn = openConnection();
			conn.setAutoCommit(false);
			super.insert(conn, entity);
			int n=preNum-reduceNum;
            String sql="update vending_machines_way set num="+n+" ,updateTime=CURRENT_TIMESTAMP() where id="+wayId;			
			 st=conn.createStatement();
			st.execute(sql);
			StringBuilder sqlLog = new StringBuilder("insert into vending_waynum_log (wayId,vmCode,wayNumber,preNum,num, opType, customerId,itemId,basicItemId, createTime,footmarkId) values(");
			sqlLog.append(wayId+",'" + vmCode + "'," + way + "," + preNum + "," + n + ",1," + customerId + ","+itemId+","+basicItemId+",CURRENT_TIMESTAMP(),"+footmarkId+" )");
			log.info(sqlLog.toString());
			st.execute(sqlLog.toString());
			
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			if(st!=null)
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   closeConnection(conn);
		}
		
	}
	//新机器生成订单0
	public void newCreatePayRecordAndReduceStock(PayRecordBean entity,MachineInfo machineInfo,UserVo userVo,Long footmarkId,boolean existsOpenBeforeOrder){
		Connection conn=null;
		Statement st=null;
		try {
			conn = openConnection();
			conn.setAutoCommit(false);
			if(existsOpenBeforeOrder){
				update(entity);
			}else{
				super.insert(conn, entity);
			}
			for (ItemChangeDto itemChangeDto : machineInfo.getItemChangeList()) {
				if(itemChangeDto.getChangeNum()<0){
					int absRealNum = Math.abs(itemChangeDto.getRealNum());
					StringBuffer sql = new StringBuffer();
					if(CommandVersionEnum.VER2.getState().equals(machineInfo.getVersion())){
						sql.append(" UPDATE vending_machines_way_item SET num = num - "+absRealNum+",updateTime=CURRENT_TIMESTAMP()");
						sql.append(" where vmCode = '"+itemChangeDto.getVmCode()+"' and wayNumber = '"+itemChangeDto.getWayNum()+"' and orderNumber = '"+itemChangeDto.getOrderNum()+"'");
					} else {
						sql.append(" UPDATE vending_machines_way SET num = num - '"+absRealNum+"' ,updateTime=CURRENT_TIMESTAMP()");
						sql.append(" where vendingMachinesCode = '"+itemChangeDto.getVmCode()+"' and wayNumber = '"+itemChangeDto.getWayNum()+"'");
					}
					st=conn.createStatement();
					st.execute(sql.toString());
					StringBuilder sqlLog = new StringBuilder("insert into vending_waynum_log (wayId,vmCode,wayNumber,preNum,num, opType, userId,basicItemId, createTime,footmarkId) values(");
					sqlLog.append(itemChangeDto.getMachineWayId()+",'" + itemChangeDto.getVmCode() + "'," + itemChangeDto.getWayNum() + "," + itemChangeDto.getNum() + "," + (itemChangeDto.getNum()+itemChangeDto.getRealNum()) + ","+userVo.getType()+"," + userVo.getId() +","+itemChangeDto.getBasicItemId()+",CURRENT_TIMESTAMP(),"+footmarkId+" )");
					log.info(sqlLog.toString());
					st.execute(sqlLog.toString());
				}
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			if(st!=null)
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   closeConnection(conn);
		}
		
	}

	@Override
	public PayRecordBean findPayRecordByPtCode(String ptCode) {
		//log.info("<PayRecordDaoImpl--findPayRecordByPayCode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,customerId,basicItemId,itemId,payCode,vendingMachinesCode,");
		sql.append(" payType,price,state,num,pickupNum,ptCode,createTime,payTime,refundTime,finishTime,");
		sql.append(" costPrice,refundName,remark,itemName,itemTypeId,wayNumber");
		sql.append(" FROM pay_record where ptCode = '"+ptCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordBean payRecord = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				payRecord = new PayRecordBean();
				payRecord.setBasicItemId(rs.getLong("basicItemId"));
				payRecord.setCostPrice(rs.getBigDecimal("costPrice"));
				payRecord.setCreateTime(rs.getDate("createTime"));
				payRecord.setCustomerId(rs.getLong("customerId"));
				payRecord.setFinishTime(rs.getDate("finishTime"));
				payRecord.setId(rs.getLong("id"));
				payRecord.setItemId(rs.getLong("itemId"));
				payRecord.setItemName(rs.getString("itemName"));
				payRecord.setItemTypeId(rs.getLong("itemTypeId"));
				payRecord.setNum(rs.getLong("num"));
				payRecord.setPayCode(rs.getString("payCode"));
				payRecord.setPayTime(rs.getDate("payTime"));
				payRecord.setPayType(rs.getInt("payType"));
				payRecord.setPickupNum(rs.getLong("pickupNum"));
				payRecord.setPrice(rs.getBigDecimal("price"));
				payRecord.setPtCode(rs.getString("ptCode"));
				payRecord.setRefundName(rs.getString("refundName"));
				payRecord.setRefundTime(rs.getDate("refundTime"));
				payRecord.setRemark(rs.getString("remark"));
				payRecord.setState(rs.getLong("state"));
				payRecord.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				payRecord.setWayNumber(rs.getLong("wayNumber"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		//log.info("<PayRecordDaoImpl--findPayRecordByPayCode--end>");
		return payRecord;
	}

	@Override
	public PayRecordBean findPayRecordByPayCode(String payCode) {
		//log.info("<PayRecordDaoImpl--findPayRecordByPayCode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,customerId,basicItemId,itemId,payCode,vendingMachinesCode,");
		sql.append(" payType,price,state,num,pickupNum,ptCode,createTime,payTime,refundTime,finishTime,");
		sql.append(" costPrice,refundName,remark,itemName,itemTypeId,wayNumber");
		sql.append(" FROM pay_record where payCode = '"+payCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordBean payRecord = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				payRecord = new PayRecordBean();
				payRecord.setBasicItemId(rs.getLong("basicItemId"));
				payRecord.setCostPrice(rs.getBigDecimal("costPrice"));
				payRecord.setCreateTime(rs.getDate("createTime"));
				payRecord.setCustomerId(rs.getLong("customerId"));
				payRecord.setFinishTime(rs.getDate("finishTime"));
				payRecord.setId(rs.getLong("id"));
				payRecord.setItemId(rs.getLong("itemId"));
				payRecord.setItemName(rs.getString("itemName"));
				payRecord.setItemTypeId(rs.getLong("itemTypeId"));
				payRecord.setNum(rs.getLong("num"));
				payRecord.setPayCode(rs.getString("payCode"));
				payRecord.setPayTime(rs.getDate("payTime"));
				payRecord.setPayType(rs.getInt("payType"));
				payRecord.setPickupNum(rs.getLong("pickupNum"));
				payRecord.setPrice(rs.getBigDecimal("price"));
				payRecord.setPtCode(rs.getString("ptCode"));
				payRecord.setRefundName(rs.getString("refundName"));
				payRecord.setRefundTime(rs.getDate("refundTime"));
				payRecord.setRemark(rs.getString("remark"));
				payRecord.setState(rs.getLong("state"));
				payRecord.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				payRecord.setWayNumber(rs.getLong("wayNumber"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		//log.info("<PayRecordDaoImpl--findPayRecordByPayCode--end>");
		return payRecord;
	}
	
	@Override
	public PayRecordBean getById(Long id) {
		//log.info("<PayRecordDaoImpl--getById--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,customerId,basicItemId,itemId,payCode,vendingMachinesCode,payType,price,");
		sql.append(" state,num,pickupNum,ptCode,createTime,payTime,refundTime,finishTime,costPrice,refundName,");
		sql.append(" remark,itemName,itemTypeId,wayNumber,srcType,operateHistoryId,couponId");
		sql.append(" FROM pay_record WHERE id = '"+id+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordBean payRecord = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				payRecord = new PayRecordBean();
				Long basicItemId = rs.getLong("basicItemId");
				payRecord.setBasicItemId(basicItemId==0L?null:basicItemId);
				payRecord.setCostPrice(rs.getBigDecimal("costPrice"));
				payRecord.setCouponId(rs.getString("couponId"));
				payRecord.setCreateTime(rs.getTimestamp("createTime"));
				payRecord.setCustomerId(rs.getLong("customerId"));
				payRecord.setFinishTime(rs.getTimestamp("finishTime"));
				payRecord.setId(rs.getLong("id"));
				Long itemId = rs.getLong("itemId");
				payRecord.setItemId(itemId==0L?null:itemId);
				payRecord.setItemName(rs.getString("itemName"));
				payRecord.setItemTypeId(rs.getLong("itemTypeId"));
				payRecord.setNum(rs.getLong("num"));
				payRecord.setOperateHistoryId(rs.getLong("operateHistoryId"));
				payRecord.setPayCode(rs.getString("payCode"));
				payRecord.setPayTime(rs.getTimestamp("payTime"));
				payRecord.setPayType(rs.getInt("payType"));
				payRecord.setPickupNum(rs.getLong("pickupNum"));
				payRecord.setPrice(rs.getBigDecimal("price"));
				payRecord.setPtCode(rs.getString("ptCode"));
				payRecord.setRefundName(rs.getString("refundName"));
				payRecord.setRefundTime(rs.getTimestamp("refundTime"));
				payRecord.setRemark(rs.getString("remark"));
				payRecord.setSrcType(rs.getInt("srcType"));
				payRecord.setState(rs.getLong("state"));
				payRecord.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				payRecord.setWayNumber(rs.getLong("wayNumber"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<PayRecordDaoImpl--getById--end>");
		return payRecord;
	}
	
	@Override
	public PayRecordBean getByOperateId(Long operateId) {
		//log.info("<PayRecordDaoImpl--getById--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,customerId,basicItemId,itemId,payCode,vendingMachinesCode,payType,price,");
		sql.append(" state,num,pickupNum,ptCode,createTime,payTime,refundTime,finishTime,costPrice,refundName,");
		sql.append(" remark,itemName,itemTypeId,wayNumber,srcType,operateHistoryId,couponId");
		sql.append(" FROM pay_record WHERE operateHistoryId = '"+operateId+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordBean payRecord = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				payRecord = new PayRecordBean();
				Long basicItemId = rs.getLong("basicItemId");
				payRecord.setBasicItemId(basicItemId==0L?null:basicItemId);
				payRecord.setCostPrice(rs.getBigDecimal("costPrice"));
				payRecord.setCouponId(rs.getString("couponId"));
				payRecord.setCreateTime(rs.getTimestamp("createTime"));
				payRecord.setCustomerId(rs.getLong("customerId"));
				payRecord.setFinishTime(rs.getTimestamp("finishTime"));
				payRecord.setId(rs.getLong("id"));
				Long itemId = rs.getLong("itemId");
				payRecord.setItemId(itemId==0L?null:itemId);
				payRecord.setItemName(rs.getString("itemName"));
				payRecord.setItemTypeId(rs.getLong("itemTypeId"));
				payRecord.setNum(rs.getLong("num"));
				payRecord.setOperateHistoryId(rs.getLong("operateHistoryId"));
				payRecord.setPayCode(rs.getString("payCode"));
				payRecord.setPayTime(rs.getTimestamp("payTime"));
				payRecord.setPayType(rs.getInt("payType"));
				payRecord.setPickupNum(rs.getLong("pickupNum"));
				payRecord.setPrice(rs.getBigDecimal("price"));
				payRecord.setPtCode(rs.getString("ptCode"));
				payRecord.setRefundName(rs.getString("refundName"));
				payRecord.setRefundTime(rs.getTimestamp("refundTime"));
				payRecord.setRemark(rs.getString("remark"));
				payRecord.setSrcType(rs.getInt("srcType"));
				payRecord.setState(rs.getLong("state"));
				payRecord.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				payRecord.setWayNumber(rs.getLong("wayNumber"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<PayRecordDaoImpl--getById--end>");
		return payRecord;
	}

	@Override
	public MessageVo getFinalOrder(String factNum) {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select c.phone as principalPhone,td.doorNO,tc.phone,vmi.code,vmi.locatoinName,pr.createTime from pay_record pr ");
		sql.append(" LEFT JOIN vending_machines_info vmi ON pr.vendingMachinesCode=vmi.code ");
		sql.append(" left join vending_machines_base vmb on vmb.id=vmi.machinesBaseId ");
		sql.append(" LEFT JOIN company c ON c.id=vmi.companyId ");
		sql.append(" left join tbl_door_operate_history td on td.id=pr.operateHistoryId ");
		sql.append(" left join tbl_customer tc on tc.id=pr.customerId ");
		sql.append(" where vmb.factoryNumber = '"+factNum+"' ");
		sql.append(" and pr.state = 10008 ");
		sql.append(" and ISNULL(td.posWeight) ");
		sql.append(" and pr.createTime >= DATE_SUB(NOW(),INTERVAL 2 day)");
		sql.append(" ORDER BY PR.createTime DESC limit 1");
		log.info("sql=:"+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MessageVo messageVo = new MessageVo();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				messageVo.setAddress(rs.getString("locatoinName"));
				messageVo.setPhone(rs.getString("phone"));
				messageVo.setDoorNo(rs.getInt("doorNo"));
				messageVo.setVmCode(rs.getString("code"));
				messageVo.setCreateTime(rs.getTimestamp("createTime"));
				messageVo.setPrincipalPhone(rs.getString("principalPhone"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		return messageVo;
	}
	
	@Override
	public boolean updateOrderTicket(String outTradeNo,String ticket) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update pay_record set ticket=? where payCode=?");
		List<Object> param = new ArrayList<Object>();
		param.add(ticket);
		param.add(outTradeNo);
		int result = upate(sql.toString(), param);
		if(result ==1){
			return true;
		}
		return false;
	}
}

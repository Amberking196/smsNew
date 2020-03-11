package com.server.module.refund;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.sqlUtil.MySqlFuns;
import com.server.util.stateEnum.RefundApplicationEnum;
import com.server.util.stateEnum.RefundTypeEnum;

@Repository
public class RefundDaoImpl extends MySqlFuns implements RefundDao{
	
	private final static Logger log = LogManager.getLogger(RefundDaoImpl.class);

	@Override
	public boolean isPrincipal(Long operateId, String phone) {
		log.info("<RefundDaoImpl--isPrincipal--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT 1 FROM refund_principal WHERE deleteFlag = 0 AND loginInfoId = '"+operateId+"' AND phone = '"+phone+"'");
		
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
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RefundDaoImpl--isPrincipal--end>");
		return result;
	}

	@Override
	public Long insertRefundRecord(RefundRecordBean refundBean) {
		log.info("<RefundDaoImpl--insertRefundRecord--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO refund_record(state,type,refundPlatform,outRefundNo,platformNo,payCode,");
		sql.append(" ptCode,price,refundPrice,reason,createUser,companyId,itemName,createTime,refundGenre,refundMoneyType,refundNum,payTime)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?)");
		log.info("sql语句："+sql);
		List<Object> param = new ArrayList<Object>();
		param.add(refundBean.getState());
		param.add(refundBean.getType());
		param.add(refundBean.getRefundPlatform());
		param.add(refundBean.getOutRefundNo());
		param.add(refundBean.getPlatformNo());
		param.add(refundBean.getPayCode());
		param.add(refundBean.getPtCode());
		param.add(refundBean.getPrice());
		param.add(refundBean.getRefundPrice());
		param.add(refundBean.getReason());
		param.add(refundBean.getCreateUser());
		param.add(refundBean.getCompanyId());
		param.add(refundBean.getItemName());
		param.add(refundBean.getRefundGenre());
		param.add(refundBean.getRefundMoneyType());
		param.add(refundBean.getRefundNum());
		param.add(refundBean.getPayTime());
		int id = insertGetID(sql.toString(), param);
		log.info("<RefundDaoImpl--insertRefundRecord--end>");
		if(id>0){
			return Long.valueOf(id);
		}
		return null;
	}

	@Override
	public RefundOrderInfo findOrder(String payCode, Integer orderType) {
		log.info("<RefundDaoImpl--findOrder--start>");
		StringBuffer sql = new StringBuffer();
		if(orderType == 1){
			sql.append(" SELECT pr.payTime,pr.id orderId,pr.ptCode,pr.price,vmi.`companyId`,pr.`itemName`");
			sql.append(" FROM pay_record AS pr");
			sql.append(" INNER JOIN `vending_machines_info` AS vmi ON vmi.`code` = pr.`vendingMachinesCode`");
			sql.append(" WHERE pr.state = 10001 AND pr.ptCode IS NOT NULL AND pr.payCode = '"+payCode+"'");
		} else if(orderType == 2){
			sql.append(" SELECT so.payTime,so.id orderId,so.ptCode,so.nowprice AS price,so.companyId,sod.`itemName`");
			sql.append(" FROM store_order AS so INNER JOIN store_order_detile AS sod ON sod.`orderId` = so.`id`");
			sql.append(" WHERE so.state = 10001 AND so.ptCode IS NOT NULL AND so.payCode = '"+payCode+"'");
		} else if(orderType == 3){
			sql.append(" SELECT so.payTime,so.id orderId,so.ptCode,so.nowprice AS price,so.companyId,sod.`itemName`");
			sql.append(" FROM store_order AS so INNER JOIN store_order_detile AS sod ON sod.`orderId` = so.`id`");
			sql.append(" WHERE so.state = 10001 AND so.ptCode IS NOT NULL AND so.payCode = '"+payCode+"'");
		} else if(orderType == 4){
			sql.append(" SELECT payTime,id orderId,ptCode,price,76 as companyId,'余额退款' as itemName ,customerId,friendCustomerId");
			sql.append(" FROM member_order WHERE state = 10001 AND ptCode IS NOT NULL AND payCode = '"+payCode+"' ");
		} else if(orderType == 6){
			sql.append(" SELECT pr.payTime,pr.id orderId,pr.ptCode,pr.price,vmi.`companyId`,pr.`itemName`");
			sql.append(" FROM pay_record_vision AS pr");
			sql.append(" INNER JOIN `vending_machines_info` AS vmi ON vmi.`code` = pr.`vendingMachinesCode`");
			sql.append(" WHERE pr.state = 10001 AND pr.ptCode IS NOT NULL AND pr.payCode = '"+payCode+"'");
		}else {
			return null;
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RefundOrderInfo orderInfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				if(orderInfo == null){
					orderInfo = new RefundOrderInfo();
					orderInfo.setOrderId(rs.getLong("orderId"));
					orderInfo.setPrice(rs.getBigDecimal("price"));
					orderInfo.setPtCode(rs.getString("ptCode"));
					orderInfo.setCompanyId(rs.getInt("companyId"));
					orderInfo.setItemName(rs.getString("itemName"));
					orderInfo.setPayTime(rs.getTimestamp("payTime"));
					if(orderType == 4) {
						orderInfo.setCustomerId(rs.getLong("customerId"));
						orderInfo.setFriendCustomerId(rs.getLong("friendCustomerId"));
					}
				} else {
					orderInfo.setItemName(orderInfo.getItemName().concat(",").concat(rs.getString("itemName")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RefundDaoImpl--findOrder--end>");
		return orderInfo;
	}

	@Override
	public List<RefundDto> findGroupBuyOrder(Integer groupId) {
		log.info("<RefundDaoImpl--findGroupBuyOrder--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT so.ptCode,so.price,so.payCode,so.companyId,sod.`itemName` FROM store_order AS so");
		sql.append(" INNER JOIN tbl_customer_spellgroup AS tcs ON so.`customerGroupId` = tcs.`id`");
		sql.append(" LEFT JOIN store_order_detile AS sod ON sod.`orderId` = so.`id`");
		sql.append(" WHERE so.`state` = 10001 AND so.ptCode IS NOT NULL AND tcs.`state` = 2");
		sql.append(" AND tcs.endTime < now()");
		sql.append(" AND so.customerGroupId = '"+groupId+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String,RefundDto> refundMap = new HashMap<String,RefundDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				String payCode = rs.getString("payCode");
				RefundDto refund = refundMap.get(payCode);
				if(refund == null){
					refund = new RefundDto();
					refund.setPayCode(payCode);
					refund.setPtCode(rs.getString("ptCode"));
					BigDecimal price = rs.getBigDecimal("price");
					refund.setPrice(price);
					refund.setRefundPrice(price);
					refund.setCompanyId(rs.getInt("companyId"));
					refund.setReason("拼团失败退款");
					refund.setOrderType(3);
					refund.setItemName(rs.getString("itemName"));
					refund.setRefundType(RefundTypeEnum.REFUND_WX_SUCCESS_PAY_SUCCESS.getState());
					refundMap.put(payCode, refund);
				}else{
					refund.setItemName(refund.getItemName()+","+rs.getString("itemName"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RefundDaoImpl--findGroupBuyOrder--end>");
		return new ArrayList<RefundDto>(refundMap.values());
	}

	@Override
	public boolean updateOrderRefundState(List<String> payCodeList,Integer orderType) {
		log.info("<RefundDaoImpl--updateOrderRefundState--start>");
		if(payCodeList == null || payCodeList.size()<=0){
			return false;
		}
		StringBuffer sql = new StringBuffer();
		String payCodes = StringUtils.join(payCodeList,",");
		if(orderType == 1){
			sql.append(" UPDATE pay_record SET state = 10001,refundTime = NOW() WHERE state = 10001 AND FIND_IN_SET(payCode,'"+payCodes+"')");
		}else if(orderType == 2 ){
			sql.append(" UPDATE store_order SET state = 10006,refundTime = NOW() WHERE state = 10001 AND FIND_IN_SET(payCode,'"+payCodes+"')");
		}else if(orderType == 3){
			sql.append(" UPDATE store_order SET state = 10006,spellgroupState=3,refundTime = NOW()  WHERE state = 10001 AND FIND_IN_SET(payCode,'"+payCodes+"')");
		}else if(orderType == 4){
			sql.append(" UPDATE member_order SET state = 10006,refundTime = NOW()  WHERE state = 10001 AND FIND_IN_SET(payCode,'"+payCodes+"')");
		}else if(orderType == 5){
			sql.append(" UPDATE pay_record SET state = 10006,refundTime = NOW() WHERE state = 10001 AND FIND_IN_SET(payCode,'"+payCodes+"')");
		}else if(orderType == 6){
			sql.append(" UPDATE pay_record_vision SET state = 10001,refundTime = NOW() WHERE state = 10001 AND FIND_IN_SET(payCode,'"+payCodes+"')");
		}else if(orderType == 7){
			sql.append(" UPDATE pay_record_vision SET state = 10006,refundTime = NOW() WHERE state = 10001 AND FIND_IN_SET(payCode,'"+payCodes+"')");
		}else {
			return false;
		}
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
		log.info("<RefundDaoImpl--updateOrderRefundState--end>");
		if(result >0){
			return true;
		}
		return false;
	}

	@Override
	public boolean updateGroupBuyState(Integer groupId,Integer state) {
		log.info("<RefundDaoImpl--updateGroupBuyState--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE tbl_customer_spellgroup SET state = '"+state+"',updateTime = NOW() WHERE id = '"+groupId+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int result = -1;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			result = ps.executeUpdate(sql.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<RefundDaoImpl--updateGroupBuyState--end>");
		if(result>0){
			return true;
		}
		return false;
	}

	@Override
	public String getPrincipalInfoById(Long loginInfoId) {
		log.info("<RefundDaoImpl--getPrincipalInfoById--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT phone FROM refund_principal WHERE deleteFlag = 0 AND  loginInfoId = '"+loginInfoId+"'");
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String phone = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				phone = rs.getString("phone");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RefundDaoImpl--getPrincipalInfoById--end>");
		return phone;
	}

	@Override
	public Long insertRefundApplication(RefundApplicationBean refundApp) {
		log.info("<RefundDaoImpl--insertRefundApplication--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO refund_application(orderType,payCode,ptCode,phone,companyId,reason,state,refundPrice,createTime)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,NOW())");
		List<Object> param = new ArrayList<Object>();
		param.add(refundApp.getOrderType());
		param.add(refundApp.getPayCode());
		param.add(refundApp.getPtCode());
		param.add(refundApp.getPhone());
		param.add(refundApp.getCompanyId());
		param.add(refundApp.getReason());
		param.add(refundApp.getState());
		param.add(refundApp.getRefundPrice());
		int insert = insertGetID(sql.toString(), param);
		log.info("<RefundDaoImpl--insertRefundApplication--end>");
		return Long.valueOf(insert);
	}

	@Override
	public List<RefundApplicationBean> getRefundApplication(String payCode) {
		log.info("<RefundDaoImpl--getRefundApplication--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,orderType,payCode,phone,reason,state,refundPrice,createTime");
		sql.append(" FROM refund_application WHERE payCode = '"+payCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RefundApplicationBean> applicationList = new ArrayList<RefundApplicationBean>();
		RefundApplicationBean application = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				application = new RefundApplicationBean();
				application.setCreateTime(rs.getTime("createTime"));
				application.setId(rs.getLong("id"));
				application.setOrderType(rs.getInt("orderType"));
				application.setPayCode(rs.getString("payCode"));
				application.setPhone(rs.getString("phone"));
				application.setReason(rs.getString("reason"));
				application.setRefundPrice(rs.getBigDecimal("refundPrice"));
				int state = rs.getInt("state");
				application.setState(state);
				application.setStateName(RefundApplicationEnum.getName(state));
				applicationList.add(application);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RefundDaoImpl--getRefundApplication--end>");
		return applicationList;
	}

	@Override
	public String getPhoneByPayCode(String payCode) {
		log.info("<RefundDaoImpl--getPhoneByPayCode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tc.`phone` FROM pay_record AS pr");
		sql.append(" INNER JOIN tbl_customer AS tc ON tc.id = pr.`customerId`");
		sql.append(" WHERE pr.`payCode` = '"+payCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String phone = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				phone = rs.getString("phone");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RefundDaoImpl--getPhoneByPayCode--end>");
		return phone;
	}

	@Override
	public RefundApplicationDto judgeOrderRefund(String payCode,BigDecimal refundPrice) {
		log.info("<RefundDaoImpl--judgeOrderRefund--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT pr.orderType as prOrderType,tc.`phone`,pr.`ptCode`,vmi.companyId FROM pay_record AS pr");
		sql.append(" INNER JOIN vending_machines_info AS vmi ON pr.`vendingMachinesCode` = vmi.`code`");
		sql.append(" INNER JOIN tbl_customer AS tc ON tc.`id` = pr.`customerId`");
		sql.append(" WHERE pr.payCode = '"+payCode+"'");
		sql.append(" AND pr.state = 10001 AND pr.price>='"+refundPrice+"'");
		sql.append(" AND NOT EXISTS (SELECT 1 FROM refund_application AS ra WHERE ra.`payCode` = pr.`payCode`)");
		sql.append(" union ");
		sql.append(" SELECT pr.orderType as prOrderType,tc.`phone`,pr.`ptCode`,vmi.companyId FROM pay_record_vision AS pr");
		sql.append(" INNER JOIN vending_machines_info AS vmi ON pr.`vendingMachinesCode` = vmi.`code`");
		sql.append(" INNER JOIN tbl_customer AS tc ON tc.`id` = pr.`customerId`");
		sql.append(" WHERE pr.payCode = '"+payCode+"'");
		sql.append(" AND pr.state = 10001 AND pr.price>='"+refundPrice+"'");
		sql.append(" AND NOT EXISTS (SELECT 1 FROM refund_application AS ra WHERE ra.`payCode` = pr.`payCode`)");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RefundApplicationDto applicationDto = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				applicationDto = new RefundApplicationDto();
				applicationDto.setPhone(rs.getString("phone"));
				applicationDto.setPtCode(rs.getString("ptCode"));
				applicationDto.setCompanyId(rs.getInt("companyId"));
				applicationDto.setOrderType(rs.getInt("prOrderType"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<RefundDaoImpl--judgeOrderRefund--end>");
		return applicationDto;
	}

	public Long deleteCarry(Long orderId){
		log.info("<RefundDaoImpl>-----<deleteCarry>----start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" DELETE FROM carry_water_vouchers_customer WHERE orderId ='"+orderId+"' ");
		int insert = delete(sql.toString());
		log.info("<<RefundDaoImpl>-----<deleteCarry>------end>");
		return Long.valueOf(insert);
	}
}

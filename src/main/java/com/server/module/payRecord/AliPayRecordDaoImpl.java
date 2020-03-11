package com.server.module.payRecord;

import com.server.module.zfb_trade.bean.MsgDto;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.sqlUtil.MySqlFuns;
import com.server.util.stateEnum.PayStateEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
@Repository("aliPayRecordDao")
public class AliPayRecordDaoImpl extends MySqlFuns implements AliPayRecordDao{

	public static Logger log = LogManager.getLogger(AliPayRecordDaoImpl.class); 
	@Override
	public boolean booleanIsArrearage(Long customerId) {
		log.info("<AliPayRecordDaoImpl--booleanIsArrearage--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" select 1 from pay_record as pr inner join ");
		sql.append(" tbl_customer as tc on pr.customerId = tc.id where ");
		sql.append(" tc.id = "+customerId);
		sql.append(" and pr.state = "+PayStateEnum.NOT_PAY.getState());
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			if(rs!=null && rs.next()){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		log.info("<AliPayRecordDaoImpl--booleanIsArrearage--end>");
		return false;
	}
	@Override
	public PayRecordBean findPayRecordByPayCode(String payCode) {
		log.info("<AliPayRecordDaoImpl--findPayRecordByPayCode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,customerId,basicItemId,itemId,payCode,vendingMachinesCode,");
		sql.append(" payType,price,state,num,pickupNum,ptCode,createTime,payTime,refundTime,finishTime,");
		sql.append(" costPrice,refundName,remark,itemName,itemTypeId,wayNumber");
		sql.append(" FROM pay_record where payCode = '"+payCode+"'");
		
		sql.append(" union ");
		sql.append(" SELECT id,customerId,basicItemId,itemId,payCode,vendingMachinesCode,");
		sql.append(" payType,price,state,num,pickupNum,ptCode,createTime,payTime,refundTime,finishTime,");
		sql.append(" costPrice,refundName,remark,itemName,itemTypeId,wayNumber");
		sql.append(" FROM pay_record_vision where payCode = '"+payCode+"'");
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
				payRecord.setItemTypeId(rs.getInt("itemTypeId"));
				payRecord.setNum(rs.getInt("num"));
				payRecord.setPayCode(rs.getString("payCode"));
				payRecord.setPayTime(rs.getDate("payTime"));
				payRecord.setPayType(rs.getInt("payType"));
				payRecord.setPickupNum(rs.getInt("pickupNum"));
				payRecord.setPrice(rs.getBigDecimal("price"));
				payRecord.setPtCode(rs.getString("ptCode"));
				payRecord.setRefundName(rs.getString("refundName"));
				payRecord.setRefundTime(rs.getDate("refundTime"));
				payRecord.setRemark(rs.getString("remark"));
				payRecord.setState(rs.getInt("state"));
				payRecord.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				payRecord.setWayNumber(rs.getInt("wayNumber"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		log.info("<AliPayRecordDaoImpl--findPayRecordByPayCode--end>");
		return payRecord;
	}
	@Override
	public boolean updatePayRecord(PayRecordBean payRecord) {
		log.info("<AliPayRecordDaoImpl--updatePayRecord--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" update pay_record set state=?,payTime=?,refundTime=?,");
		sql.append(" finishTime=?,refundName=?,ptCode=?,remark=? where payCode=?");
		List<Object> param = new ArrayList<Object>();
		param.add(payRecord.getState());
		param.add(payRecord.getPayTime());
		param.add(payRecord.getRefundTime());
		param.add(payRecord.getFinishTime());
		param.add(payRecord.getRefundName());
		param.add(payRecord.getPtCode());
		param.add(payRecord.getRemark());
		param.add(payRecord.getPayCode());
		int result = upate(sql.toString(), param);
		log.info("<AliPayRecordDaoImpl--updatePayRecord--end>");
		if(result ==1){
			return true;
		}
		return false;
	}
	@Override
	public MsgDto findMsgDto(String payCode) {
		log.info("<AliPayRecordDaoImpl--findMsgDto--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT pr.price,pr.itemName,tc.alipayUserId,pr.vendingMachinesCode AS vmCode ");
		sql.append(" FROM pay_record AS pr");
		sql.append(" INNER JOIN tbl_customer AS tc ON pr.customerId = tc.`id`");
		sql.append(" WHERE payCode = '"+payCode+"'");
		sql.append(" union ");
		sql.append(" SELECT pr.price,pr.itemName,tc.alipayUserId,pr.vendingMachinesCode AS vmCode ");
		sql.append(" FROM pay_record_vision AS pr");
		sql.append(" INNER JOIN tbl_customer AS tc ON pr.customerId = tc.`id`");
		sql.append(" WHERE payCode = '"+payCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MsgDto msgDto = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				msgDto = new MsgDto();
				msgDto.setAlipayUserId(rs.getString("alipayUserId"));
				msgDto.setItemName(rs.getString("itemName"));
				msgDto.setPrice(rs.getBigDecimal("price"));
				msgDto.setVmCode(rs.getString("vmCode"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliPayRecordDaoImpl--findMsgDto--end>");
		return msgDto;
	}
	@Override
	public List<PayRecordDto> findPayRecord(PayRecordForm payRecordForm) {
		log.info("<AliPayRecordDaoImpl--findPayRecord--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  pr.id AS orderId,pr.customerId as customerId, pr.ptCode,pr.state,pr.createTime,ib.`name` AS itemName,");
		sql.append(" (pr.price / pr.num) AS price,pr.price AS totalPrice,pr.num,pr.payCode as payCode,tc.phone,c.`name` as companyName,pr.vendingMachinesCode as vmCode");
		sql.append(" FROM pay_record pr INNER JOIN vending_machines_item vmi ON pr.itemId = vmi.id");
		sql.append(" INNER JOIN item_basic ib ON vmi.basicItemId = ib.id");
		sql.append(" inner join vending_machines_info as vmi2 on vmi2.code = pr.vendingMachinesCode");
		sql.append(" inner join company as c on c.id = vmi2.companyId");
		sql.append(" left join vending_line as vl on vl.id = vmi2.lineId");
		sql.append(" LEFT JOIN tbl_customer AS tc ON pr.customerId = tc.`id`");
		sql.append(" where 1=1");
		if(StringUtil.isNotBlank(payRecordForm.getPayCodeOrName())){
			sql.append(" and (payCode like '%"+payRecordForm.getPayCodeOrName()+"%' or c.name like '%"+payRecordForm.getPayCodeOrName()+"%')");
		}
		if(payRecordForm.getCompanyId()!=null){
			sql.append(" and vmi.companyId = "+payRecordForm.getCompanyId());
		}else if(StringUtil.isNotBlank(payRecordForm.getCompanyIds())){
			sql.append(" and vmi.companyId in ("+payRecordForm.getCompanyIds()+")");
		}
		if(payRecordForm.getDutyId()!=null){
			sql.append(" and FIND_IN_SET("+payRecordForm.getDutyId()+",vl.dutyId)");
		}
		if(StringUtil.isNotBlank(payRecordForm.getVmCode())){
			sql.append(" and pr.vendingMachinesCode = '"+payRecordForm.getVmCode()+"'");
		}
		if(payRecordForm.getCustomerId()!=null){
			sql.append(" and pr.customerId = "+payRecordForm.getCustomerId());
		}
		if(payRecordForm.getState() != null){
			sql.append(" and pr.state = "+payRecordForm.getState());
		}
		if(payRecordForm.getStartDate()!=null){
			sql.append(" and pr.createTime >= '"+DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate())+"'");
		}
		if(payRecordForm.getEndDate()!=null){
			sql.append(" and pr.createTime < '"+DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate())+"'");
		}
		sql.append(" order by pr.createTime desc");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordDto payRecord = null;
		List<PayRecordDto> payRecordList = new ArrayList<PayRecordDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				payRecord = new PayRecordDto();
				payRecord.setVmCode(rs.getString("vmCode"));
				payRecord.setCreateTime(rs.getTimestamp("createTime"));
				payRecord.setItemName(rs.getString("itemName"));
				payRecord.setNum(rs.getInt("num"));
				payRecord.setOrderId(rs.getLong("orderId"));
				payRecord.setPrice(rs.getBigDecimal("price"));
				payRecord.setPayCode(rs.getString("payCode"));
				payRecord.setPtCode(rs.getString("ptCode"));
				payRecord.setState(rs.getString("state"));
				payRecord.setTotalPrice(rs.getBigDecimal("totalPrice"));
				payRecord.setCustomerId(rs.getLong("customerId"));
				payRecord.setPhone(rs.getString("phone"));
				payRecord.setCompanyName(rs.getString("companyName"));
				payRecordList.add(payRecord);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliPayRecordDaoImpl--findPayRecord--end>");
		return payRecordList;
	}
	@Override
	public PayRecordDto findPayRecordById(Long payRecordId) {
		log.info("<AliPayRecordDaoImpl--findPayRecordById--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  pr.id AS orderId,pr.customerId as customerId, pr.ptCode,pr.state,pr.createTime,ib.`name` AS itemName,");
		sql.append(" (pr.price / pr.num) AS price,pr.price AS totalPrice,pr.num,pr.payCode as payCode");
		sql.append(" FROM pay_record pr INNER JOIN vending_machines_item vmi ON pr.itemId = vmi.id");
		sql.append(" INNER JOIN item_basic ib ON vmi.basicItemId = ib.id");
		sql.append(" where pr.id="+payRecordId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordDto payRecord = null;
		List<PayRecordDto> payRecordList = new ArrayList<PayRecordDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				payRecord = new PayRecordDto();
				payRecord.setCreateTime(rs.getDate("createTime"));
				payRecord.setItemName(rs.getString("itemName"));
				payRecord.setNum(rs.getInt("num"));
				payRecord.setOrderId(rs.getLong("orderId"));
				payRecord.setPrice(rs.getBigDecimal("price"));
				payRecord.setPtCode(rs.getString("ptCode"));
				payRecord.setState(rs.getString("state"));
				payRecord.setTotalPrice(rs.getBigDecimal("totalPrice"));
				payRecord.setPayCode(rs.getString("payCode"));
				payRecord.setCustomerId(rs.getLong("customerId"));
				payRecordList.add(payRecord);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliPayRecordDaoImpl--findPayRecordById--end>");
		return payRecord;
	}
	@Override
	public boolean updatePayState(String ptCode,String payCode, Integer state) {
		log.info("<AliPayRecordDaoImpl--updatePayState--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" update pay_record set payType=2,state = "+state);
		if(state.equals(PayStateEnum.PAY_SUCCESS.getState())){
			sql.append(" ,payTime = '"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"'");
			sql.append(" ,ptCode = '"+ptCode+"'");
		}else if (state.equals(PayStateEnum.Delivering.getState())){
			sql.append(" ,payTime = '"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"'");
			sql.append(" ,ptCode = '"+ptCode+"'");
		}else if (state.equals(PayStateEnum.PAY_FINISHED.getState())) {
			sql.append(" ,finishTime = '"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"'");
		}
		sql.append(" where payCode = '"+payCode+"'");
		sql.append(" and state <> "+state);
		log.info("sql语句："+sql);
		int result = upate(sql.toString());
		log.info("<AliPayRecordDaoImpl--updatePayState--end>");
		if(result == 1){
			return true;
		}else{
			StringBuffer sql2 = new StringBuffer();
			sql2.append(" update pay_record_vision set payType=2,state = "+state);
			if(state.equals(PayStateEnum.PAY_SUCCESS.getState())){
				sql2.append(" ,payTime = '"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"'");
				sql2.append(" ,ptCode = '"+ptCode+"'");
			}else if (state.equals(PayStateEnum.Delivering.getState())) {
				sql.append(" ,payTime = '"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"'");
				sql.append(" ,ptCode = '"+ptCode+"'");
			}
			else if (state.equals(PayStateEnum.PAY_FINISHED.getState())) {
				sql2.append(" ,finishTime = '"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"'");
			}
			sql2.append(" where payCode = '"+payCode+"'");
			sql2.append(" and state <> "+state);
			log.info("sql语句："+sql2);
			int result2 = upate(sql2.toString());
			if(result2==1) {
				return true;
			}
		}
		return false;
	}
	@Override
	public SumPayRecordDto findPayRecordNum(PayRecordForm payRecordForm) {
		log.info("<AliPayRecordDaoImpl--findPayRecordNum--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select count(1) as total,SUM(price) as sumPrice, SUM(costPrice) as sumCostPrice,SUM(num) as sumNum from pay_record pr ");
		sql.append(" LEFT JOIN tbl_customer AS tc ON pr.customerId = tc.`id`");
		sql.append(" LEFT JOIN vending_machines_info as vmi ON vmi.`code`=pr.`vendingMachinesCode`");
		sql.append(" left join vending_line as vl on vl.id = vmi.lineId");
		sql.append(" LEFT JOIN company c ON vmi.`companyId` = c.`id`  where 1=1");
		if(payRecordForm.getDutyId()!=null){
			sql.append(" and FIND_IN_SET("+payRecordForm.getDutyId()+",vl.dutyId)");
		}
		if (payRecordForm.getCompanyId() != null) {
			sql.append(" and vmi.companyId =" + payRecordForm.getCompanyId());
		} else if (StringUtils.isNotBlank(payRecordForm.getCompanyIds())) {
			sql.append(" and vmi.companyId in(" + payRecordForm.getCompanyIds() + ")");
		}
		if (payRecordForm.getState() != null) {
			sql.append(" and pr.state like '%" + payRecordForm.getState() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getVmCode())) {
			sql.append(" and pr.vendingMachinesCode like '%" + payRecordForm.getVmCode() + "%'");
		}
		if (payRecordForm.getStartDate() != null) {
			sql.append(" and pr.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate()) + "'");
		}
		if (payRecordForm.getEndDate() != null) {
			sql.append(" and pr.createTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()) + "'");
		}
		log.info("查询语句：" + sql.toString());
		SumPayRecordDto sumPayRecordDto = new SumPayRecordDto();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				sumPayRecordDto.setTotal(rs.getLong("total"));
				sumPayRecordDto.setSumPrice(rs.getBigDecimal("sumPrice"));
				sumPayRecordDto.setSumCostPrice(rs.getBigDecimal("sumCostPrice"));
				sumPayRecordDto.setSumNum(rs.getLong("sumNum"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliPayRecordDaoImpl--findPayRecordNum--end>");
		return sumPayRecordDto;
	}
	@Override
	public PayRecordDto getOrderInfo(Long id) {
		log.info("<AliPayRecordDaoImpl--getOrderInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT itemName,num,price as totalPrice,payCode,state,customerId FROM pay_record WHERE id = '"+id+"'");
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordDto payRecord = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				payRecord = new PayRecordDto();
				payRecord.setItemName(rs.getString("itemName"));
				payRecord.setNum(rs.getInt("num"));
				payRecord.setOrderId(id);
				payRecord.setState(rs.getString("state"));
				payRecord.setTotalPrice(rs.getBigDecimal("totalPrice"));
				payRecord.setPayCode(rs.getString("payCode"));
				payRecord.setCustomerId(rs.getLong("customerId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliPayRecordDaoImpl--getOrderInfo--end>");
		return payRecord;
	}
	
	@Override
	public PayRecordDto getOrderInfoVision(Long id) {
		log.info("<AliPayRecordDaoImpl--getOrderInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT itemName,num,price as totalPrice,payCode,state,customerId FROM pay_record_vision WHERE id = '"+id+"'");
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordDto payRecord = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				payRecord = new PayRecordDto();
				payRecord.setItemName(rs.getString("itemName"));
				payRecord.setNum(rs.getInt("num"));
				payRecord.setOrderId(id);
				payRecord.setState(rs.getString("state"));
				payRecord.setTotalPrice(rs.getBigDecimal("totalPrice"));
				payRecord.setPayCode(rs.getString("payCode"));
				payRecord.setCustomerId(rs.getLong("customerId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliPayRecordDaoImpl--getOrderInfo--end>");
		return payRecord;
	}
	@Override
	public List<PayRecordDto> findUserAllOrder(Long customerId, Integer state) {
		log.info("<AliPayRecordDaoImpl--findUserAllOrder--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,itemName,vendingMachinesCode AS vmCode,createTime,num,");
		sql.append(" payCode,price as totalPrice,state FROM pay_record");
		sql.append(" WHERE customerId = '"+customerId+"'");
		if(state != null){
			sql.append(" AND state = '"+state+"'");
		}
		sql.append(" ORDER BY createTime DESC");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordDto payRecord = null;
		List<PayRecordDto> recordList = new ArrayList<PayRecordDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				payRecord = new PayRecordDto();
				payRecord.setCreateTime(rs.getDate("createTime"));
				payRecord.setItemName(rs.getString("itemName"));
				payRecord.setNum(rs.getInt("num"));
				payRecord.setOrderId(rs.getLong("id"));
				payRecord.setState(PayStateEnum.findStateName(rs.getInt("state")));
				payRecord.setTotalPrice(rs.getBigDecimal("totalPrice"));
				payRecord.setPayCode(rs.getString("payCode"));
				payRecord.setVmCode(rs.getString("vmCode"));
				payRecord.setCustomerId(customerId);
				recordList.add(payRecord);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliPayRecordDaoImpl--findUserAllOrder--end>");
		return recordList;
	}
	


	@Override
	public List<NewPayRecordDto> getCustomerOrder(Long customerId, Integer state,Integer isShowAll) {
		log.info("<AliPayRecordDaoImpl--getCustomerOrder--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT pr.`payCode`,pr.`ptCode`,pr.`id` AS orderId,pr.`state`,pr.`createTime`,pr.`price` AS totalPrice,");
		sql.append(" pr.`num` AS totalNum,pr.`vendingMachinesCode` AS vmCode,pr.`customerId`,pr.`itemName` AS allItemName,");
		sql.append(" pri.`basicItemId`,pri.`num` as itemNum,pri.`price` as itemPrice,pri.`realTotalPrice`,ib.`name` AS itemName,ib.pic,mul.useMoney");
		sql.append(" FROM pay_record AS pr");
		sql.append(" LEFT JOIN pay_record_item AS pri ON pri.`payRecordId` = pr.`id`");
		sql.append(" LEFT JOIN item_basic AS ib ON ib.`id` = pri.`basicItemId`");
		sql.append(" LEFT JOIN member_use_log AS mul ON mul.`orderType` = 1 AND mul.`orderId` = pr.`id`");
		sql.append(" WHERE pri.num>0 and pr.fakeFlag=0 and pr.`state` != 10008 AND pr.`customerId` = '"+customerId+"'");
		if(state != null){
			sql.append(" AND pr.`state` = '"+state+"'");
		}
		sql.append(" union ");
		sql.append(" SELECT pr.`payCode`,pr.`ptCode`,pr.`id` AS orderId,pr.`state`,pr.`createTime`,pr.`price` AS totalPrice,");
		sql.append(" pr.`num` AS totalNum,pr.`vendingMachinesCode` AS vmCode,pr.`customerId`,pr.`itemName` AS allItemName,");
		sql.append(" pri.`basicItemId`,pri.`num` as itemNum,pri.`price` as itemPrice,pri.`realTotalPrice`,ib.`name` AS itemName,ib.pic,mul.useMoney");
		sql.append(" FROM pay_record_vision AS pr");
		sql.append(" LEFT JOIN pay_record_item_vision AS pri ON pri.`payRecordId` = pr.`id`");
		sql.append(" LEFT JOIN item_basic AS ib ON ib.`id` = pri.`basicItemId`");
		sql.append(" LEFT JOIN member_use_log AS mul ON mul.`orderType` = 1 AND mul.`orderId` = pr.`id`");
		sql.append(" WHERE pri.num>0 and pr.fakeFlag=0 and pr.`state` != 10008 AND pr.`customerId` = '"+customerId+"'");
		if(state != null){
			sql.append(" AND pr.`state` = '"+state+"'");
		}
		sql.append(" ORDER BY createTime desc");
		if(isShowAll == 0){
			sql.append(" limit 20");
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<Long,NewPayRecordDto> payMap = new LinkedHashMap<Long,NewPayRecordDto>();
		NewPayRecordDto payRecord =  new NewPayRecordDto();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				PayRecordItemDto item = new PayRecordItemDto();
				item.setBasicItemId(rs.getLong("basicItemId"));
				item.setItemName(rs.getString("itemName"));
				item.setPic(rs.getString("pic"));
				item.setNum(rs.getInt("itemNum"));
				item.setPrice(rs.getBigDecimal("itemPrice"));
				item.setRealTotalPrice(rs.getBigDecimal("realTotalPrice"));
				Long orderId = rs.getLong("orderId");
				payRecord = payMap.get(orderId);
				if(payRecord == null){
					payRecord = new NewPayRecordDto();
					Integer payState = rs.getInt("state");
					payRecord.setCreateTime(rs.getTimestamp("createTime"));
					payRecord.setAllItemName(rs.getString("allItemName"));
					payRecord.setCustomerId(rs.getLong("customerId"));
					payRecord.setNum(rs.getInt("totalNum"));
					payRecord.setOrderId(orderId);
					payRecord.setStateName(PayStateEnum.findStateName(payState));
					payRecord.setPayCode(rs.getString("payCode"));
					payRecord.setPtCode(rs.getString("ptCode"));
					payRecord.setState(payState.toString());
					payRecord.setTotalPrice(rs.getBigDecimal("totalPrice"));
					payRecord.setVmCode(rs.getString("vmCode"));
					payRecord.setUseMoney(rs.getBigDecimal("useMoney"));
					payMap.put(orderId, payRecord);
				}
				System.out.println(payRecord.getOrderId()+"-");
				payRecord.getItemList().add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliPayRecordDaoImpl--getCustomerOrder--end>");
		ArrayList<NewPayRecordDto> arrayList = new ArrayList<NewPayRecordDto>(payMap.values());
		//Collections.sort(arrayList);排序？
		return arrayList;
	}

	@Override
	public ReturnDataUtil findMachineHistory(PayRecordForm customerMachineForm) {
		log.info("<AliPayRecordDaoImpl>--<findMachineHistory>--start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT vmi.machineVersion,td.doorNO,td.preWeight,ROUND(td.preWeight * 1.0 / ib.standard) AS preNum,ROUND(td.posWeight * 1.0 / ib.standard) AS posNum,td.posWeight,pr.num,td.openedTime,td.closedTime FROM tbl_door_operate_history AS td LEFT JOIN pay_record pr ON pr.operateHistoryId = td.id ");

		sql.append(" left join pay_record_item pri on pr.id=pri.payrecordid");
		sql.append(" LEFT JOIN item_basic ib ON pri.basicItemId = ib.id");
		sql.append(" left join vending_machines_info vmi on vmi.code=pr.vendingMachinesCode");

		sql.append(" WHERE pr.payCode = '"+customerMachineForm.getPayCodeOrName()+"'");

		sql.append(" union ");
		
		sql.append(" SELECT vmi.machineVersion,td.doorNO,td.preWeight,ROUND(td.preWeight * 1.0 / ib.standard) AS preNum,ROUND(td.posWeight * 1.0 / ib.standard) AS posNum,td.posWeight,pr.num,td.openedTime,td.closedTime FROM tbl_door_operate_history_vision AS td LEFT JOIN pay_record_vision pr ON pr.operateHistoryId = td.id ");

		sql.append(" left join pay_record_item_vision pri on pr.id=pri.payrecordid");
		sql.append(" LEFT JOIN item_basic ib ON pri.basicItemId = ib.id");
		sql.append(" left join vending_machines_info vmi on vmi.code=pr.vendingMachinesCode");

		sql.append(" WHERE pr.payCode = '"+customerMachineForm.getPayCodeOrName()+"'");
		log.info("查询语句：" + sql.toString());
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			CustomerMachineHistoryBean bean = new CustomerMachineHistoryBean();
			while (rs.next()) {
				bean.setPosNum(rs.getInt("posNum"));
				bean.setPreNum(rs.getInt("preNum"));
				if(rs.getInt("machineVersion")==2) {//多商品无前后数量
					bean.setPosNum(0);
					bean.setPreNum(0);
				}
				bean.setDoorNO(rs.getInt("doorNO"));
				
				bean.setPreWeight(rs.getDouble("preWeight"));
			    bean.setPosWeight(rs.getDouble("posWeight"));
			    bean.setNum(rs.getInt("num"));
			    bean.setOpenedTime(rs.getTimestamp("openedTime"));
			    bean.setClosedTime(rs.getTimestamp("closedTime"));
			}
			
			data.setReturnObject(bean);
			data.setStatus(1);
			log.info("<AliPayRecordDaoImpl>--<findMachineHistory>----end");
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

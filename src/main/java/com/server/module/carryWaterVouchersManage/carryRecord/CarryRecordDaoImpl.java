package com.server.module.carryWaterVouchersManage.carryRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class CarryRecordDaoImpl extends MySqlFuns implements CarryRecordDao{

	private final static Logger log = LogManager.getLogger(CarryRecordDaoImpl.class);

	@Override
	public Long insertCarryRecord(CarryRecordBean carryRecord) {
		log.info("<CarryRecordDaoImpl--insertCarryRecord--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO carry_record(carryId,num,customerId,orderId,createTime)");
		sql.append(" VALUES(?,?,?,?,NOW())");
		List<Object> param = new ArrayList<Object>();
		param.add(carryRecord.getCarryId());
		param.add(carryRecord.getNum());
		param.add(carryRecord.getCustomerId());
		param.add(carryRecord.getOrderId());
		int id = insertGetID(sql.toString(), param);
		log.info("<CarryRecordDaoImpl--insertCarryRecord--end>");
		if(id>0){
			return Long.valueOf(id);
		}
		return null;
	}
	
	
}

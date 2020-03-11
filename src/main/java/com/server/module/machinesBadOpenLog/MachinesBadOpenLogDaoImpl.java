package com.server.module.machinesBadOpenLog;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;
@Repository
public class MachinesBadOpenLogDaoImpl extends MySqlFuns implements MachinesBadOpenLogDao{

	private final static Logger log = LogManager.getLogger(MachinesBadOpenLogDaoImpl.class);
	
	@Override
	public Long insert(MachinesBadOpenLogBean badOpenLog) {
		log.info("MachinesBadOpenLogDaoImpl--insert--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO machines_badopen_log(vmCode,customerId,createTime)");
		sql.append(" VALUES('"+badOpenLog.getVmCode()+"','"+badOpenLog.getCustomerId()+"','"+DateUtil.formatYYYYMMDDHHMMSS(badOpenLog.getCreateTime())+"')");
		int insertGetID = insertGetID(sql.toString());
		log.info("MachinesBadOpenLogDaoImpl--insert--end");
		return Long.valueOf(insertGetID);
	}

}

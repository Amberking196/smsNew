package com.server.module.distroyMachines;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;
@Repository
public class DistroyMachinesLogDaoImpl extends MySqlFuns implements DistroyMachinesLogDao{

	private static final Logger log = LogManager.getLogger(DistroyMachinesLogDaoImpl.class);
	
	@Override
	public boolean insert(String vmCode, String factoryNumber, String info,String status) {
		log.info("DistoryMachinesLogDaoImpl--insert--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO tbl_distorymachine_log(`code`,machineCode,`status`,info,createTime,createUser,updateTime,updateUser,deleteFlage)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		String now = DateUtil.formatYYYYMMDDHHMMSS(new Date());
		String user = "system";
		param.add(vmCode);
		param.add("HM-"+factoryNumber);
		param.add(status);
		param.add(info);
		param.add(now);
		param.add(user);
		param.add(now);
		param.add(user);
		param.add(0);
		int insert = insert(sql.toString(),param);
		log.info("DistoryMachinesLogDaoImpl--insert--end");
		if(insert>0){
			return true;
		}
		return false;
	}

	
}

package com.server.module.activeDegree;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.sqlUtil.MySqlFuns;
import com.server.util.stateEnum.RegisterFlowEnum;
@Repository
public class UserActiveDegreeDaoImpl extends MySqlFuns implements UserActiveDegreeDao{

	private final static Logger log = LogManager.getLogger(UserActiveDegreeDaoImpl.class);

	@Override
	public RegisterFlowBean save(RegisterFlowBean registerBean) {
		//log.info("UserActiveDegreeDaoImpl--save--start");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO register_flow (customerId,vmCode,register,wxOrAli,registerTime)");
		sql.append(" VALUES(?,?,?,?,now())");
		param.add(registerBean.getCustomerId());
		param.add(registerBean.getVmCode());
		param.add(registerBean.getRegister());
		param.add(registerBean.getWxOrAli());
		int id = insertGetID(sql.toString(), param);
		registerBean.setId(Long.valueOf(id));
		//log.info("UserActiveDegreeDaoImpl--save--end");
		return registerBean;
	}

	@Override
	public boolean update(Long customerId, RegisterFlowEnum event) {
		//log.info("UserActiveDegreeDaoImpl--update--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE register_flow SET "+event.getColumnName()+" = 1,"+event.getTimeName()+" = NOW()");
		sql.append(" WHERE customerId = '"+customerId+"' AND "+event.getColumnName() +" = 0");
		int upate = upate(sql.toString());
		//log.info("UserActiveDegreeDaoImpl--update--end");
		if(upate>0){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean update(Long customerId, Integer buyNum) {
		//log.info("UserActiveDegreeDaoImpl--update--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE register_flow SET "+RegisterFlowEnum.CREATE_ORDER.getColumnName()+" = 1,"+RegisterFlowEnum.CREATE_ORDER.getTimeName()+" = NOW(),buyNum="+buyNum);
		sql.append(" WHERE customerId = '"+customerId+"' AND "+RegisterFlowEnum.CREATE_ORDER.getColumnName() +" = 0");
		int upate = upate(sql.toString());
		//log.info("UserActiveDegreeDaoImpl--update--end");
		if(upate>0){
			return true;
		}
		return false;
	}

	
}

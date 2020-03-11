package com.server.module.zfb_trade.module.dooroperation;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.order.domain.Way;
import com.server.module.zfb_trade.bean.DoorOperateHistoryBean;
import com.server.module.zfb_trade.util.enumparam.DoorOperateEventEnum;

@Service("aliDoorOperationService")
public class AliDoorOperationServiceImpl implements AliDoorOperationService{

	public static Logger log = LogManager.getLogger(AliDoorOperationServiceImpl.class); 
	@Autowired
	@Qualifier("aliDoorOperationDao")
	private AliDoorOperationDao doorOperationDao;
	@Override
	public DoorOperateHistoryBean save(DoorOperateHistoryBean doorOperateHistory) {
		//log.info("<AliDoorOperationServiceImpl--save--start>");
		DoorOperateHistoryBean save = doorOperationDao.save(doorOperateHistory);
		//log.info("<AliDoorOperationServiceImpl--save--end>");
		return save;
	}
	@Override
	public boolean update(DoorOperateHistoryBean doorOperateHistory) {
		//log.info("<AliDoorOperationServiceImpl--update--start>");
		boolean update = doorOperationDao.update(doorOperateHistory);
		//log.info("<AliDoorOperationServiceImpl--update--end>");
		return update;
	}
	@Override
	public boolean comsummateInfo(Long id, Long customerId, MachineInfo machinesInfo) {
		//log.info("<AliDoorOperationServiceImpl--comsummateInfo--start>");
		Integer wayNum = doorOperationDao.get(id, customerId);
		if(wayNum!=null){
			//加入货道
			machinesInfo.setCurrWay(wayNum);
			DoorOperateHistoryBean doorOperate = new DoorOperateHistoryBean();
			doorOperate.setId(id);
			doorOperate.setReferenceId(customerId);
			doorOperate.setClosedTime(new Date());
			doorOperate.setStatus(DoorOperateEventEnum.CLOSED.getCode());
			doorOperate.setRemark("关门成功");
			List<Way> list = machinesInfo.getList();
			if (list.size() >= wayNum && (wayNum - 1) >= 0) {
				Way way = list.get(wayNum -1);
				doorOperate.setPosWeight(way.getWeight());
			}
			boolean update = doorOperationDao.update(doorOperate);
			return update;
		}
		//log.info("<AliDoorOperationServiceImpl--comsummateInfo--end>");
		return false;
	}
	@Override
	public DoorOperateHistoryBean getById(Long id) {
		//log.info("<AliDoorOperationServiceImpl--getById--start>");
		DoorOperateHistoryBean byId = doorOperationDao.getById(id);
		//log.info("<AliDoorOperationServiceImpl--getById--end>");
		return byId;
	}
	@Override
	public DoorOperateHistoryBean getByVmCode(String vmCode) {
		//log.info("<AliDoorOperationServiceImpl--getByVmCodeWay--start>");
		DoorOperateHistoryBean byId = doorOperationDao.getByVmCode(vmCode);
		//log.info("<AliDoorOperationServiceImpl--getByVmCodeWay--end>");
		return byId;
	}
	
	@Override
	public Integer get(Long doorOperateId,Long customerId) {
		return doorOperationDao.get(doorOperateId,customerId);
	}
	
}

package com.server.module.machinesBadOpenLog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MachinesBadOpenLogServiceImpl implements MachinesBadOpenLogService{

	private final static Logger log = LogManager.getLogger(MachinesBadOpenLogServiceImpl.class);
	
	@Autowired
	private MachinesBadOpenLogDao machinesBadOpenLogDao;

	@Override
	public Long insert(MachinesBadOpenLogBean badOpenLog) {
		log.info("MachinesBadOpenLogServiceImpl--insert--start");
		Long insert = machinesBadOpenLogDao.insert(badOpenLog);
		log.info("MachinesBadOpenLogServiceImpl--insert--end");
		return insert;
	}
}

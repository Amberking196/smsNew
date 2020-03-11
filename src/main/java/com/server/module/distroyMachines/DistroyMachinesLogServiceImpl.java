package com.server.module.distroyMachines;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistroyMachinesLogServiceImpl implements DistroyMachinesLogService{

	private static final Logger log = LogManager.getLogger(DistroyMachinesLogServiceImpl.class);
	
	@Autowired
	private DistroyMachinesLogDao distoryMachinesLogDao;
	
	@Override
	public boolean insert(String vmCode, String factoryNumber, String info, String status) {
		log.info("DistoryMachinesLogServiceImpl--insert--start");
		boolean insert = distoryMachinesLogDao.insert(vmCode, factoryNumber, info, status);
		log.info("DistoryMachinesLogServiceImpl--insert--end");
		return insert;
	}

}

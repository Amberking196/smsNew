package com.server.module.carryWaterVouchersManage.carryRecord;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarryRecordServiceImpl implements CarryRecordService{
	
	private final static Logger log = LogManager.getLogger(CarryRecordDaoImpl.class);

	@Autowired
	private CarryRecordDao carryRecordDao;

	@Override
	public Long insertCarryRecord(CarryRecordBean carryRecord) {
		log.info("<CarryRecordServiceImpl--insertCarryRecord--start>");
		Long id = carryRecordDao.insertCarryRecord(carryRecord);
		log.info("<CarryRecordServiceImpl--insertCarryRecord--end>");
		return id;
	}
}

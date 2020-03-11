package com.server.module.zfb_trade.module.replenish;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplenishServiceImpl implements ReplenishService{

	private static Logger log = LogManager.getLogger(ReplenishDaoImpl.class);
	
	@Autowired
	private ReplenishDao replenishDao;

	@Override
	public List<ReplenishDto> queryReplenish(ReplenishForm form) {
		log.info("<ReplenishServiceImpl--queryReplenish--start>");
		List<ReplenishDto> queryReplenish = replenishDao.queryReplenish(form);
		log.info("<ReplenishServiceImpl--queryReplenish--end>");
		return queryReplenish;
	}

	@Override
	public boolean adjustReplenishNum(Integer adjustNum,Long Id) {
		log.info("<ReplenishServiceImpl--adjustReplenishNum--start>");
		boolean result = replenishDao.adjustReplenishNum(adjustNum, Id);
		log.info("<ReplenishServiceImpl--adjustReplenishNum--end>");
		return result;
	}

	@Override
	public Long getCurrentReplenish(String vmCode, Integer wayNum, Long basicItemId, Long userId) {
		log.info("<ReplenishServiceImpl--getCurrentReplenish--start>");
		Long id = replenishDao.getCurrentReplenish(vmCode, wayNum, basicItemId, userId);
		log.info("<ReplenishServiceImpl--getCurrentReplenish--end>");
		return id;
	}
	
	@Override
	public boolean checkIfReplenishOne(String vmCode,Integer wayNum,Long basicItemId,Long userId) {
		log.info("<ReplenishServiceImpl--checkIfReplenishOne--start>");
		return replenishDao.checkIfReplenishOne(vmCode, wayNum, basicItemId, userId);
	}

}

package com.server.module.zfb_trade.module.vmitem;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("aliVmitemService")
public class AliVmitemServiceImpl implements AliVmitemService{

	public static Logger log = LogManager.getLogger(AliVmitemServiceImpl.class); 
	@Autowired
	@Qualifier("aliVmitemDao")
	private AliVmitemDao vmitemDao;

	@Override
	public Long queryBasicItemId(String vmCode, Integer wayNum) {
		log.info("<AliVmitemServiceImpl--queryBasicItemId--start>");
		Long queryBasicItemId = vmitemDao.queryBasicItemId(vmCode, wayNum);
		log.info("<AliVmitemServiceImpl--queryBasicItemId--end>");
		return queryBasicItemId;
	}

	@Override
	public List<ItemDto> queryVmitem(String vmCode) {
		log.info("<AliVmitemServiceImpl--queryVmitem--start>");
		List<ItemDto> queryVmitem = vmitemDao.queryVmitem(vmCode);
		log.info("<AliVmitemServiceImpl--queryVmitem--end>");
		return queryVmitem;
	}
	
	
}

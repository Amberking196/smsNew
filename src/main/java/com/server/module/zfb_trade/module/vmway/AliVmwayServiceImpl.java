package com.server.module.zfb_trade.module.vmway;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.server.module.commonBean.ItemInfoDto;
import com.server.module.trade.vendingMachinesWayItem.WayValidatorDto;

@Service("aliVmwayService")
public class AliVmwayServiceImpl implements AliVmwayService{

	public static Logger log = LogManager.getLogger(AliVmwayServiceImpl.class); 
	@Autowired
	@Qualifier("aliVmwayDao")
	private AliVmwayDao vmwayDao;
	@Override
	public VendingMachinesWayBean queryByWayAndVmcode(String vmCode, Integer wayNo) {
		log.info("<AliVmwayServiceImpl--queryByWayAndVmcode--start>");
		VendingMachinesWayBean vmwList = vmwayDao.queryByWayAndVmcode(vmCode, wayNo);
		log.info("<AliVmwayServiceImpl--queryByWayAndVmcode--end>");
		return vmwList;
	}
	
	@Override
	public List<VendingMachinesWayBean> queryByWayAndVmcode(String vmCode) {
		log.info("<AliVmwayServiceImpl--queryByWayAndVmcode--start>");
		List<VendingMachinesWayBean> vmwList = vmwayDao.queryByWayAndVmcode(vmCode);
		log.info("<AliVmwayServiceImpl--queryByWayAndVmcode--start>");
		return vmwList;
	}

	@Override
	public WayValidatorDto getOneWayInfo(String vmCode, Integer wayNum) {
		log.info("<AliVmwayServiceImpl--getOneWayInfo--start>");
		WayValidatorDto oneWayInfo = vmwayDao.getOneWayInfo(vmCode, wayNum);
		log.info("<AliVmwayServiceImpl--getOneWayInfo--end>");
		return oneWayInfo;
	}

	@Override
	public List<ItemInfoDto> getOneWayItemInfo(String vmCode, Integer wayNum) {
		log.info("<AliVmwayServiceImpl--getOneWayItemInfo--start>");
		List<ItemInfoDto> oneWayItemInfo = vmwayDao.getOneWayItemInfo(vmCode, wayNum);
		log.info("<AliVmwayServiceImpl--getOneWayItemInfo--end>");
		return oneWayItemInfo;
	}

}

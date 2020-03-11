package com.server.module.zfb_trade.module.vminfo;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.server.module.zfb_trade.module.replenish.ReplenishForm;

@Service("aliVminfoService")
public class AliVminfoServiceImpl implements AliVminfoService{

	public static Logger log = LogManager.getLogger(AliVminfoServiceImpl.class); 
	@Autowired
	@Qualifier("aliVminfoDao")
	private AliVminfoDao vminfoDao;
	
	@Override
	public List<VminfoDto> queryVMList(String condition, String companyIds, Integer machineType ,Integer state) {
		log.info("<AliVminfoServiceImpl--queryVMList--start>");
		List<VminfoDto> queryVMList = vminfoDao.queryVMList(condition, companyIds, machineType,state);
		log.info("<AliVminfoServiceImpl--queryVMList--end>");
		return queryVMList;
	}

	@Override
	public List<VminfoDto> queryOwnVMList(String condition,  Long dutyId, Integer machineType,Integer state) {
		log.info("<AliVminfoServiceImpl--queryOwnVMList--start>");
		List<VminfoDto> queryOwnVMList = vminfoDao.queryOwnVMList(condition, dutyId, machineType,state);
		log.info("<AliVminfoServiceImpl--queryOwnVMList--end>");
		return queryOwnVMList;
	}

	@Override
	public List<VminfoDto> queryReplenishVMList(ReplenishForm replenishForm) {
		log.info("<AliVminfoServiceImpl--queryReplenishVMList--start>");
		List<VminfoDto> queryReplenishVMList = vminfoDao.queryReplenishVMList(replenishForm);
		log.info("<AliVminfoServiceImpl--queryReplenishVMList--end>");
		return queryReplenishVMList;
	}

	@Override
	public Integer queryAllMachinesNum(Integer companyId, String companyIds) {
		log.info("<AliVminfoServiceImpl--queryAllMachinesNum--start>");
		Integer queryAllMachinesNum = vminfoDao.queryAllMachinesNum(companyId, companyIds);
		log.info("<AliVminfoServiceImpl--queryAllMachinesNum--end>");
		return queryAllMachinesNum;
	}

	@Override
	public VminfoDto queryByVmCode(String vmCode) {
		log.info("<AliVminfoServiceImpl--queryByVmCode--start>");
		VminfoDto queryByVmCode = vminfoDao.queryByVmCode(vmCode);
		log.info("<AliVminfoServiceImpl--queryByVmCode--end>");
		return queryByVmCode;
	}

}

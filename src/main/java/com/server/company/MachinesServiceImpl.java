package com.server.company;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MachinesServiceImpl implements MachinesService{
	
	private Logger log = LogManager.getLogger(MachinesDaoImpl.class);

	@Autowired
	private MachinesDao machinesDao;

	@Override
	public Integer getCompanyIdByVmCode(String vmCode) {
		//log.info("<MachinesServiceImpl--getCompanyIdByVmCode--start>");
		Integer companyId = machinesDao.getCompanyIdByVmCode(vmCode);
		//log.info("<MachinesServiceImpl--getCompanyIdByVmCode--end>");
		return companyId;
	}

	@Override
	public Integer getCompanyIdByPayCode(String payCode) {
		//log.info("<MachinesServiceImpl--getCompanyIdByPayCode--start>");
		Integer companyId = machinesDao.getCompanyIdByPayCode(payCode);
		//log.info("<MachinesServiceImpl--getCompanyIdByPayCode--end>");
		return companyId;
	}

	@Override
	public boolean isSonCompany(Integer sonCompany, Integer faCompany) {
		//log.info("<MachinesServiceImpl--isSonCompany--start>");
		boolean result = machinesDao.isSonCompany(sonCompany, faCompany);
		//log.info("<MachinesServiceImpl--isSonCompany--end>");
		return result;
	}

	@Override
	public List<Integer> getOtherCompanyIdByVmCode(String vmCode) {
		//log.info("<MachinesServiceImpl--getOtherCompanyIdByVmCode--start>");
		List<Integer> companyIds = machinesDao.getOtherCompanyIdByVmCode(vmCode);
		//log.info("<MachinesServiceImpl--getOtherCompanyIdByVmCode--end>");
		return companyIds;
	}

	@Override
	public boolean isReplenishCompanyMan(String vmCode, Integer companyId) {
		//log.info("<MachinesServiceImpl--isReplenishCompanyMan--start>");
		boolean result = machinesDao.isReplenishCompanyMan(vmCode, companyId);
		//log.info("<MachinesServiceImpl--isReplenishCompanyMan--end>");
		return result;
	}

	@Override
	public Integer getCompanyIdByPayCodeStore(String payCode) {
		Integer companyId = machinesDao.getCompanyIdByPayCodeStore(payCode);
		return companyId;
	}
	
	
}

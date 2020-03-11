package com.server.module.machine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.zfb_trade.util.ResultUtil;
import com.server.util.ReturnDataUtil;

@RestController
@RequestMapping("/machineInfo")
public class MachineInfoController {
	private final static Logger log = LogManager.getLogger(MachineInfoController.class);
	@Autowired
	private MachineInfoService machineInfoService;
	
	@PostMapping("/getCompanyId")
	public ReturnDataUtil getCompanyByVmcode(String vmCode){
		log.info("<MachineInfoController--getCompanyByVmcode--start>");
		Integer companyId = machineInfoService.getCompanyByVmcode(vmCode);
		log.info("<MachineInfoController--getCompanyByVmcode--end>");
		if(companyId==null){
			ResultUtil.selectError();
		}
		return ResultUtil.success(companyId);
	}
}

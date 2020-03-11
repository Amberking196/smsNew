package com.server.module.trade.submerchant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subMerchant")
public class SubMerchantController {

	private static final Logger log = LogManager.getLogger(SubMerchantController.class);
	@Autowired
	private SubMerchantService subMerchantService;
	
	/**
	 * 根据vmCode查询子商户信息
	 * @author hebiting
	 * @date 2018年6月20日下午2:05:58
	 * @param vmCode
	 * @return
	 */
	@PostMapping("/queryByVmCode")
	public SubMerchantBean queryByVmCode(String vmCode){
		log.info("<SubMerchantController--queryByVmCode--start>");
		SubMerchantBean queryByVmCode = subMerchantService.queryByVmCode(vmCode);
		log.info("<SubMerchantController--queryByVmCode--end>");
		return queryByVmCode;
	}
}

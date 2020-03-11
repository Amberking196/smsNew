package com.server.module.carryWaterVouchersManage.carryWaterVouchers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: hjc create time: 2018-11-06 09:02:25
 */
@Api(value = "CarryWaterVouchersController", description = "提水券管理")
@RestController
@RequestMapping("/carryWaterVouchers")
public class CarryWaterVouchersController {

	private static Logger log = LogManager.getLogger(CarryWaterVouchersController.class);
	@Autowired
	private CarryWaterVouchersService carryWaterVouchersServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;

	@ApiOperation(value = "提水券管理列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) CarryWaterVouchersForm carryWaterVouchersForm) {
		log.info("<CarryWaterVouchersController>-------<listPage>-------start");

		if (carryWaterVouchersForm == null) {
			carryWaterVouchersForm = new CarryWaterVouchersForm();
		}
		
		returnDataUtil = carryWaterVouchersServiceImpl.listPage(carryWaterVouchersForm);

		log.info("<CarryWaterVouchersController>-------<listPage>-------end");
		return returnDataUtil;
	}

	

}

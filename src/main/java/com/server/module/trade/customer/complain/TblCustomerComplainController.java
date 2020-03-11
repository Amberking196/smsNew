package com.server.module.trade.customer.complain;

import com.server.util.ReturnDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author name: why create time: 2018-11-22 17:23:16
 */
@Api(value = "TblCustomerComplainController", description = "用户投诉")
@RestController
@RequestMapping("/tblCustomerComplain")
public class TblCustomerComplainController {

	private static Logger log = LogManager.getLogger(TblCustomerComplainController.class);
	@Autowired
	private TblCustomerComplainService tblCustomerComplainServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;


	
	@ApiOperation(value = "用户投诉添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody TblCustomerComplainBean entity) {
		log.info("<CustomerMessageController>----<add>------start");
		Integer count = tblCustomerComplainServiceImpl.findComplaintsNumberById();
		if(count<=10){
			TblCustomerComplainBean tblCustomerComplainBean = tblCustomerComplainServiceImpl.add(entity);
			if (tblCustomerComplainBean != null) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("故障申报成功！");
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("故障申报失败！");
			}
		}else{
			returnDataUtil.setStatus(-99);
			returnDataUtil.setMessage("亲，您已经提问10次了，不可以继续提问了哟！如果还有问题，请电话联系客服，谢谢！");
		}
		log.info("<CustomerMessageController>----<add>------end");
		return returnDataUtil;
	}



}

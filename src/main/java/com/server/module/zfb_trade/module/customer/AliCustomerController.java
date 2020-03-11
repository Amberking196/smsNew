package com.server.module.zfb_trade.module.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("aliCustomer")
public class AliCustomerController {

	@Autowired
	private AliCustomerService customerService;
	
	@PostMapping("/queryCustomer")
	public List<CustomerBean> queryCustomer(@RequestBody List<Long> customerIdList){
		return customerService.queryCustomer(customerIdList);
	}
}

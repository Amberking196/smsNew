package com.server.module.trade.web.order.closed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.util.ReturnDataUtil;

@RestController
@RequestMapping("/order")
public class CusClosedController {
	

	@Autowired
	private CusClosedService cusClosedService;

	/**
	 * 版本 1 单商品机器关门
	 * @author hebiting
	 * @date 2019年1月29日下午5:10:38
	 * @param param
	 * @return
	 */
	@PostMapping("/ver1")
	public ReturnDataUtil closedDoorVer1(@RequestBody String param){
		return cusClosedService.closedVer1(param);
	}
	/**
	 * 版本2 多商品机器关门
	 * @author hebiting
	 * @date 2019年1月29日下午5:10:47
	 * @param param
	 * @return
	 */
	@PostMapping("/ver2")
	public ReturnDataUtil closedDoorVer2(@RequestBody String param){
		return cusClosedService.closedVer2(param);
	}
	
	/**
	 * 版本2 多商品机器货道全开时 关门接口
	 * @author hebiting
	 * @date 2019年1月29日下午5:12:10
	 * @param param
	 * @return
	 */
	public ReturnDataUtil allClosedDoorVer2(@RequestBody String param){
		return cusClosedService.allClosedVer2(param);
	}
	/**
	 * 未关门发送短信提醒
	 * @author hebiting
	 * @date 2019年1月29日下午5:10:56
	 * @param factoryNum
	 * @return
	 */
	@PostMapping("/notClosedSendMsg")
	public ReturnDataUtil notClosedSendMsg(@RequestBody String factoryNum){
		return cusClosedService.notClosedSendMsg(factoryNum);
	}
}

package com.server.module.trade.web.order.preferential;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.coupon.MachinesLAC;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.bean.PayRecordBean;
import com.server.module.trade.order.domain.MachineInfo;

@Component
public class HandlerManager {

	private Handler beginHandler;
	@Autowired
	private ActivityHandler activityHandler;
	@Autowired
	private CarryHandler carryHandler;
	@Autowired
	private CouponHandler couponHandler;
	@Autowired
	private MemberHandler memberHandler;
	@Autowired
	private PayHandler payHandler;
	@Autowired
	private StockHandler stockHandler;
	@Autowired
	private CreateOrderHandler createOrderHandler;
	
	@PostConstruct
	public void chain(){
		beginHandler = stockHandler;
		stockHandler.setHandler(carryHandler);
		carryHandler.setHandler(activityHandler);
		activityHandler.setHandler(couponHandler);
		couponHandler.setHandler(memberHandler);
		memberHandler.setHandler(createOrderHandler);
		createOrderHandler.setHandler(payHandler);
	}
	
	public void handler(MachineInfo machineInfo,UserVo userVo,PayRecordBean payRecord,MachinesLAC mlac,String items){
		List<HandlerTypeEnum> typeList = new ArrayList<HandlerTypeEnum>();
		beginHandler.handler(machineInfo, userVo, payRecord, mlac, items, typeList);
	}
}

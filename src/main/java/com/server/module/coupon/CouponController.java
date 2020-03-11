package com.server.module.coupon;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.trade.util.UserUtil;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.util.ReturnDataUtil;

@RestController
@RequestMapping("/coupon")
public class CouponController {
	
	private static final Logger log = LogManager.getLogger(CouponController.class);
	
	@Autowired
	private CouponService couponService;
	
	@PostMapping("/available")
	public ReturnDataUtil available(String vmCode){
		log.info("<CouponController--available--start>");
		Long customerId = UserUtil.getUser().getId();
		MachinesLAC machinesLAC = couponService.getMachinesLAC(vmCode);
		if(machinesLAC!=null){
			List<CouponDto> availableCoupon = couponService.getAvailableCoupon(customerId, machinesLAC);
			log.info("<CouponController--available--end>");
			return ResultUtil.success(availableCoupon);
		}else{
			log.info("<CouponController--available--end>");
			return ResultUtil.error();
		}
	}
	
	//订单所用的优惠券
	@PostMapping("/usedCouponId")
	public ReturnDataUtil usedCouponId(@RequestBody String couponIds){
		log.info("<CouponController--usedCouponId--start>");
		List<CouponBean> couponBeans = couponService.getCouponInfos(couponIds);
		log.info("<CouponController--usedCouponId--end>");
		return ResultUtil.success(couponBeans);

	}
}

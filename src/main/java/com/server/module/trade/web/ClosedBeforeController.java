package com.server.module.trade.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.server.common.bean.KeyValueBean;
import com.server.module.activity.ActivityBean;
import com.server.module.activity.ActivityService;
import com.server.module.carryWaterVouchersManage.carryWaterVouchers.CarryDto;
import com.server.module.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersService;
import com.server.module.coupon.CouponBean;
import com.server.module.coupon.CouponService;
import com.server.module.coupon.MachinesLAC;
import com.server.module.coupon.MoreGoodsCouponDto;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.calculate.activity.ActivityExecute;
import com.server.module.trade.order.calculate.carry.CarryExecute;
import com.server.module.trade.order.calculate.coupon.CouponExecute;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.util.UserUtil;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;

@RestController
@RequestMapping("/closedBefore")
public class ClosedBeforeController {
	
	private final static Logger log = LogManager.getLogger(ClosedBeforeController.class);
	
	@Autowired
	private CouponService couponService;
	@Autowired
	private CarryWaterVouchersService carryService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private CarryExecute carryExecute;
	@Autowired
	private ActivityExecute activityExecute;
	@Autowired
	private CouponExecute couponExecute;

	@PostMapping("/precomputation")
	public Map<String,Object> precomputation(@RequestBody MachineInfo machineInfo){
		UserVo user = UserUtil.getUser();
		ActivityBean act = null;
		BigDecimal originalPrice = BigDecimal.ZERO;
		StringBuilder appendItem = new StringBuilder();
		log.info(JSON.toJSONString(machineInfo));
		Map<CouponBean, Integer> usedCouponMap = new HashMap<CouponBean, Integer>();
		List<ItemChangeDto> itemChangeList = machineInfo.getItemChangeList();
		for(int i=0;i<itemChangeList.size();i++){
			if(itemChangeList.get(i).getChangeNum()<0){
				Integer changeNum = Math.abs(itemChangeList.get(i).getChangeNum());
				appendItem.append(itemChangeList.get(i).getBasicItemId());
				originalPrice=originalPrice.add(new BigDecimal(itemChangeList.get(i).getPrice().toString()).multiply(new BigDecimal(changeNum.toString())));
				if(i!=itemChangeList.size()-1){
					appendItem.append(",");
				}
			}
		}
		String basicItemIds = appendItem.toString();
		BigDecimal finalPrice = originalPrice;
		machineInfo.setTotalPrice(originalPrice);
		boolean canPartakeActivity = true;
		MachinesLAC machinesLAC = couponService.getMachinesLAC(machineInfo.getVmCode());
		Map<CarryDto, Integer> usedCarryMap = new HashMap<CarryDto, Integer>();
		Map<CarryDto, List<Long>> carryMap = carryService.getCarryWaterVoucherByCustomer(user.getId(), machinesLAC, basicItemIds);
		if(carryMap != null && carryMap.size() > 0){
			canPartakeActivity = false;
			finalPrice = carryExecute.chooceAndCalculate(carryMap, usedCarryMap, machineInfo);
		}
		//是否可以参与活动或者优惠券
		if(canPartakeActivity){
			// 是否可以使用优惠券
			boolean canUseCoupon = true;
			Map<ActivityBean, List<Long>> activityMap = activityService.getMoreGoodsActivity(machinesLAC, 
					machineInfo.getTotalPrice(),basicItemIds);
			Set<ActivityBean> keySet = activityMap.keySet();
			if (keySet != null && keySet.size() > 0) {
				Map<String, Object> result = activityExecute.chooceAndCalculate(activityMap, machineInfo);
				if ((boolean) result.get("canDo")) {
					finalPrice = (BigDecimal) result.get("finalPrice");
					act = (ActivityBean)result.get("activity");
					if(originalPrice.equals(finalPrice)){
						canUseCoupon = true;
					}else{
						canUseCoupon = false;
					}
				}
			}
			if (canUseCoupon) {
				MoreGoodsCouponDto moreGoodsCoupon = couponService.getMoreGoodsCoupon(user.getId(), machinesLAC,
						machineInfo.getTotalPrice().doubleValue(), basicItemIds);
				Set<CouponBean> canUseCouponSet = moreGoodsCoupon.getCouponMap().keySet();
				if (canUseCouponSet != null && canUseCouponSet.size() > 0) {
					finalPrice = couponExecute.chooceAndCalculate(moreGoodsCoupon, machineInfo, usedCouponMap);
				}
			}
		}
		BigDecimal preferentialPrice = originalPrice.subtract(finalPrice);
		List<KeyValueBean<String,String>> couponList = new ArrayList<KeyValueBean<String,String>>();
		if(usedCouponMap != null && usedCouponMap.size()>0){
			for(Map.Entry<CouponBean, Integer> entry : usedCouponMap.entrySet()){
				KeyValueBean<String,String> keyValue = new KeyValueBean<>(entry.getKey().getName(),entry.getValue().toString());
				couponList.add(keyValue);
			}
		}
		List<KeyValueBean<String,String>> carryList = new ArrayList<KeyValueBean<String,String>>();
		if(usedCarryMap != null && usedCarryMap.size()>0){
			for(Map.Entry<CarryDto, Integer> entry : usedCarryMap.entrySet()){
				KeyValueBean<String,String> keyValue = new KeyValueBean<>(entry.getKey().getCarryName(),entry.getValue().toString());
				carryList.add(keyValue);
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("preferentialPrice", preferentialPrice);
		result.put("originalPrice", originalPrice);
		result.put("finalPrice", finalPrice);
		result.put("partakeActivity",act!=null?act.getRemark():null);
		result.put("usedCouponMap",couponList);
		result.put("usedCarryMap", carryList);
		return result;	
	}
}

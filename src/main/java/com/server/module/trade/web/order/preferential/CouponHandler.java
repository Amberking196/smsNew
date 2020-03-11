package com.server.module.trade.web.order.preferential;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.activeDegree.UserActiveDegreeDao;
import com.server.module.coupon.CouponBean;
import com.server.module.coupon.CouponDao;
import com.server.module.coupon.MachinesLAC;
import com.server.module.coupon.MoreGoodsCouponDto;
import com.server.module.store.CouponLog;
import com.server.module.store.StoreOrderDao;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.bean.PayRecordBean;
import com.server.module.trade.order.calculate.coupon.CouponExecute;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.util.stateEnum.RegisterFlowEnum;
@Component
public class CouponHandler extends PreferentialHandler{

	@Autowired
	private CouponDao couponDaoImpl;
	@Autowired
	private CouponExecute couponExecute;
	@Autowired
	private StoreOrderDao storeOrderDaoImpl;
	@Autowired
	private UserActiveDegreeDao userActiveDegreeDao;
	
	public CouponHandler(){
		setType(HandlerTypeEnum.PREFERENTIAL);
	}

	public CouponHandler(Handler carryHandler){
		setType(HandlerTypeEnum.PREFERENTIAL);
		setHandler(carryHandler);
	}
	
	@Override
	public void handler(MachineInfo machineInfo,UserVo userVo,PayRecordBean payRecord,MachinesLAC mlac,String items,List<HandlerTypeEnum> typeList) {
		Map<CouponBean, Integer> usedCouponMap = new HashMap<CouponBean, Integer>();
		if(!hasSimilar(typeList)){
			List<Long> couponIds = new ArrayList<Long>();
			MoreGoodsCouponDto moreGoodsCoupon = couponDaoImpl.getMoreGoodsCoupon(userVo.getId(), mlac,
					machineInfo.getTotalPrice().doubleValue(), items);
			Set<CouponBean> canUseCouponSet = moreGoodsCoupon.getCouponMap().keySet();
			if (canUseCouponSet != null && canUseCouponSet.size() > 0) {
				BigDecimal finalPrice = couponExecute.chooceAndCalculate(moreGoodsCoupon, machineInfo, usedCouponMap);
				if(!payRecord.getPrice().equals(finalPrice)){
					typeList.add(getType());
					payRecord.setPrice(finalPrice);
				}
				// 将使用的优惠券减少
				for (Map.Entry<CouponBean, Integer> entry : usedCouponMap.entrySet()) {
					couponIds.add(entry.getKey().getId());
					couponDaoImpl.reduceCoupon(entry.getKey().getCouponCustomerId(), entry.getValue());
				}
				String couponJoin = StringUtils.join(couponIds,",");
				payRecord.setCouponId(couponJoin);
			}
		}
		if(getHandler() != null){
			getHandler().handler(machineInfo, userVo, payRecord,mlac,items,typeList);
		}
		if (usedCouponMap != null && usedCouponMap.size() > 0) {
			for (Map.Entry<CouponBean, Integer> entry : usedCouponMap.entrySet()) {
				CouponLog couponLog = new CouponLog();
				couponLog.setCouponId(entry.getKey().getId());
				couponLog.setOrderId(payRecord.getId());
				couponLog.setCouponCustomerId(userVo.getId());
				couponLog.setMoney(machineInfo.getTotalPrice().doubleValue());
				couponLog.setDeductionMoney(entry.getKey().getDeductionMoney());
				couponLog.setType(1);// 机器优惠券1商城优惠券2
				// 插入优惠券使用记录
				storeOrderDaoImpl.insertCouponLog(couponLog);
			}
			userActiveDegreeDao.update(userVo.getId(), RegisterFlowEnum.USED_PERCENT);
		}
	}

}

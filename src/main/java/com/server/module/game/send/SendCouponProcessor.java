package com.server.module.game.send;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.coupon.CouponBean;
import com.server.module.coupon.CouponCustomerBean;
import com.server.module.coupon.CouponService;
import com.server.module.game.turntable.GameDto;
import com.server.module.game.turntable.GamePrizeBean;
import com.server.module.trade.util.UserUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.CouponEnum;

@Component
public class SendCouponProcessor implements SendPrizeProcessor{

	@Autowired 
	private CouponService couponServiceImpl;
	@Autowired
	private CouponService couponService; 
	
	@Override
	public ReturnDataUtil send(GamePrizeBean prize, GameDto gameDto) {
		ReturnDataUtil returnDataUtil=new  ReturnDataUtil();
		if(prize.getRewardId()!=null && prize.getRewardId()>0) {
			//获取优惠券信息
			CouponBean couponBean = couponServiceImpl.getCouponInfo(prize.getRewardId());
			CouponCustomerBean couCusBean = new CouponCustomerBean();
			couCusBean.setQuantity(couponBean.getSendMax()*prize.getAmount());
			couCusBean.setCouponId(couponBean.getId());
			couCusBean.setCustomerId(UserUtil.getUser().getId());
			couCusBean.setStartTime(couponBean.getLogicStartTime());
			couCusBean.setEndTime(couponBean.getLogicEndTime());
			couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
			couponService.insertCouponCustomer(couCusBean);
			returnDataUtil.setMessage("恭喜您抽中"+couponBean.getSendMax()*prize.getAmount()+"张优惠券，请前往我的信息→我的优惠券 查看！");
			returnDataUtil.setReturnObject(gameDto);
		}else {
			returnDataUtil.setMessage("谢谢惠顾！");
			returnDataUtil.setReturnObject(gameDto);
		}
		return returnDataUtil;
	}

	
	
	
	

}

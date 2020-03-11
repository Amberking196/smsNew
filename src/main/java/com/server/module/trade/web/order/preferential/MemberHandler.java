package com.server.module.trade.web.order.preferential;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.company.MachinesDao;
import com.server.module.coupon.MachinesLAC;
import com.server.module.member.MemberDao;
import com.server.module.member.MemberUseLog;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.bean.PayRecordBean;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.util.stateEnum.CompanyEnum;
import com.server.util.stateEnum.PayTypeEnum;
@Component
public class MemberHandler extends PreferentialHandler{

	@Autowired
	private MachinesDao machinesDao;
	@Autowired
	private MemberDao memberDao;
	
	public MemberHandler(){
		setType(HandlerTypeEnum.ELSE);
	}

	public MemberHandler(Handler carryHandler){
		setType(HandlerTypeEnum.ELSE);
		setHandler(carryHandler);
	}

	
	@Override
	public void handler(MachineInfo machineInfo,UserVo userVo,PayRecordBean payRecord,MachinesLAC mlac,String items,List<HandlerTypeEnum> typeList) {
		boolean useMemberMoney = false;
		BigDecimal useMoney = BigDecimal.ZERO;
		BigDecimal limit = new BigDecimal("0.01");
		BigDecimal zeroDecimal = BigDecimal.ZERO;
		if(!hasSimilar(typeList)){
			typeList.add(getType());
			if(payRecord.getPrice().compareTo(limit)>=0 && machinesDao.isSonCompany(mlac.getCompanyId(), CompanyEnum.YOUSHUI.getCompanyId())){
				BigDecimal memberMoney = memberDao.getMemberMoney(userVo.getId());
				if(memberMoney!=null && memberMoney.compareTo(payRecord.getPrice())>=0 && memberDao.updateMemberMoney(userVo.getId(), payRecord.getPrice())){
					useMoney = payRecord.getPrice();
					payRecord.setPrice(zeroDecimal);
					//优水余额支付
					payRecord.setMemberPay(useMoney);
					payRecord.setPayType(PayTypeEnum.YS_BALANCE.getIndex());
					useMemberMoney = true;
				}
			}
		}
		if(getHandler() != null){
			getHandler().handler(machineInfo, userVo, payRecord,mlac,items, typeList);
		}
		if(useMemberMoney){
			MemberUseLog memberLog = new MemberUseLog();
			memberLog.setCustomerId(userVo.getId());
			memberLog.setOrderId(payRecord.getId());
			memberLog.setUseMoney(useMoney);
			memberDao.insertMemberUseLog(memberLog);
		}
	}
}

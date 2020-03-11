package com.server.module.trade.web.order.preferential;


import java.util.List;

import com.server.module.coupon.MachinesLAC;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.bean.PayRecordBean;
import com.server.module.trade.order.domain.MachineInfo;

public interface Handler {

	public void handler(MachineInfo machineInfo,UserVo userVo,PayRecordBean payRecord,
			MachinesLAC mlac,String items,List<HandlerTypeEnum> typeList);
}

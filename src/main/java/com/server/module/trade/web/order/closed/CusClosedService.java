package com.server.module.trade.web.order.closed;

import com.server.util.ReturnDataUtil;

public interface CusClosedService {

	public ReturnDataUtil closedVer1(String param);
	
	public ReturnDataUtil closedVer2(String param);
	
	public ReturnDataUtil allClosedVer2(String param);
	
	public ReturnDataUtil notClosedSendMsg(String factoryNum);
}

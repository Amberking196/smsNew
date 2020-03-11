package com.server.module.zfb_trade.service;

import com.server.util.ReturnDataUtil;

public interface AliSmsService {

	public ReturnDataUtil sendMessage(String phone,String msg);
}

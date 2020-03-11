package com.server.module.zfb_trade.service;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;


public interface AlipayService {

	/**
	 * @Description: 检查用户是否已签约 
	 */
	String querySign(String alipayUserId,String vmCode);
	
	/**
	 * @Description: 扣款
	 */
	String cutPayment(BigDecimal price,String payCode,String itemName,String agreementNo);

	/**
	 * @Description: 没用到？
	 */
	void wapPayment(BigDecimal price,String payCode,String itemName,String vmCode,HttpServletResponse response) throws IOException;
	
}

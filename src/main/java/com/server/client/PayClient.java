package com.server.client;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.URIBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.util.HttpUtil;
import com.server.util.StringUtil;

public class PayClient {
	
	/**
	 * 支付宝免密代扣
	 * @author hebiting
	 * @date 2018年6月22日下午3:44:16
	 */
	public static String zfbWithholdPay(BigDecimal price, String payCode, String itemName, String agreementNo,String vmCode){
		URIBuilder url = null;
		try {
			url = new URIBuilder("http://yms.youshuidaojia.com/alipay/cutPayment");
			url.addParameter("price", price.toString());
			url.addParameter("payCode", payCode);
			url.addParameter("itemName", itemName);
			url.addParameter("agreementNo", agreementNo);
			url.addParameter("vmCode", vmCode);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		String jsonResult = HttpUtil.get(url);
		String ptCode = null;
		if(StringUtil.isNotBlank(jsonResult)){
			ptCode = JsonUtil.toObject(jsonResult, new TypeReference<String>(){});
		}
		return ptCode;
	}

	/**
	 * 支付宝手机网站支付
	 * @author hebiting
	 * @date 2018年6月22日下午3:44:25
	 */
	public static String zfbPay(BigDecimal price, String payCode, String itemName, String vmCode,
			HttpServletResponse response){
		URIBuilder url = null;
		try {
			url = new URIBuilder("http://yms.youshuidaojia.com/alipay/cutPayment");
			url.addParameter("price", price.toString());
			url.addParameter("payCode", payCode);
			url.addParameter("itemName", itemName);
			url.addParameter("vmCode", vmCode);
			url.addParameter("response", response.toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		String returnJson = HttpUtil.get(url);
		String form = JsonUtil.toObject(returnJson, new TypeReference<String>(){});
		return form;
	}
	
	/**
	 * 微信免密代扣
	 * @author hebiting
	 * @date 2018年6月22日下午3:44:33
	 */
	public static String wxWithholdPay(String payCode, BigDecimal price, String contractId, String itemName){
		URIBuilder url = null;
		try {
			url = new URIBuilder("http://yms.youshuidaojia.com/wxpay/wxWithhlod");
			url.addParameter("payCode", payCode);
			url.addParameter("price", price.toString());
			url.addParameter("contractId", contractId);
			url.addParameter("itemName", itemName);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		String jsonResult = HttpUtil.get(url);
		String xmlResult = null;
		if(StringUtil.isNotBlank(jsonResult)){
			xmlResult = JsonUtil.toObject(jsonResult, new TypeReference<String>(){});
		}
		return xmlResult;
	}
	
	/**
	 * 微信支付
	 * @author hebiting
	 * @date 2018年6月22日下午3:44:42
	 */
	public static Map<String,Object> wxPay(Long orderId,String openId, String url, String spbill_create_ip){
		URIBuilder uri = null;
		try {
			uri = new URIBuilder("http://yms.youshuidaojia.com/wxpay/wxWithhlod");
			uri.addParameter("orderId", orderId.toString());
			uri.addParameter("openId", openId);
			uri.addParameter("url", url);
			uri.addParameter("spbill_create_ip", spbill_create_ip);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		String jsonResult = HttpUtil.get(uri);
		Map<String, Object> result = JsonUtil.toObject(jsonResult, new TypeReference<Map<String,Object>>(){});
		return result;
	}
}

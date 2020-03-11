package com.github.wxpay.sdk;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class WXPayConfigImpl extends WXPayConfig {
	private String key = "*";
 
	@Override
	public String getAppID() {
 
		return "*";
	}
 
	@Override
	public String getMchID() {
 
		return "*";
	}
 
	@Override
	public String getKey() {
 
		return key;
	}
 
	@Override
	public InputStream getCertStream() {
		//return null;
		return this.getClass().getResourceAsStream("apiclient_cert.p12");
	}
 
	@Override
	public IWXPayDomain getWXPayDomain() {
		IWXPayDomain iwxPayDomain = new IWXPayDomain() {
			@Override
			public void report(String domain, long elapsedTimeMillis, Exception ex) {
 
			}
			@Override
			public DomainInfo getDomain(WXPayConfig config) {
				return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
			}
		};
		return iwxPayDomain;
	}
 
	@Override
	public boolean shouldAutoReport() {
 
		return false;
	}
 
	public void setKey(String key) {
		this.key = key;
	}
 
	public WXPayConfigImpl(String key) {
		super();
		this.key = key;
	}
 
	public WXPayConfigImpl() {
		super();
		
	}
 
	/**
	 * 获取沙箱密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getsignkey() throws Exception {
		WXPayConfigImpl config = new WXPayConfigImpl();
		WXPay pay = new WXPay(new WXPayConfigImpl());
		int readTimeoutMs = 5000;
		int connectTimeoutMs = 5000;
		Map<String, String> reqData = new HashMap<>();
		reqData.put("mch_id", config.getMchID());
		reqData.put("nonce_str", WXPayUtil.generateNonceStr());
		String urlSuffix = "/sandboxnew/pay/getsignkey";
		String string = pay.requestWithoutCert(urlSuffix, pay.fillRequestData(reqData), connectTimeoutMs,
				readTimeoutMs);
		System.out.println("getsignkey"+string);
		Map<String, String> map = WXPayUtil.xmlToMap(string);
		System.out.println("sandbox_signkey"+map.get("sandbox_signkey"));
		return map.get("sandbox_signkey");
	}

	@Override
	public String getAppKey() {
		// TODO Auto-generated method stub
		return null;
	}
 
}

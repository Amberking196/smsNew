package com.server.module.trade.config.pay;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;
import com.server.util.StringUtil;

/**
 * 普通商户参数配置
 * @author hebiting
 *
 */
public class MyWXPayConfig extends WXPayConfig {
	
	private Integer companyId;
	private String appID;
	private String mchID;
	private String key;
	private InputStream certStream;
	private int httpConnectTimeoutMs = 5000;
	private int httpReadTimeoutMs = 10000;
	private String planId;
	private Integer usingCompanyConfig;
	private IWXPayDomain iwxPayDomain;
	private String keyPath;
	private String appKey;
	
	
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getKeyPath() {
		return keyPath;
	}

	public void setKeyPath(String keyPath) {
		this.keyPath = keyPath;
	}

	public void setIWXPayDomain(IWXPayDomain iwxPayDomain){
		this.iwxPayDomain = iwxPayDomain;
	}
	
	public IWXPayDomain getWXPayDomain(){
		return iwxPayDomain;
	}
	
	
	public Integer getUsingCompanyConfig() {
		return usingCompanyConfig;
	}
	public void setUsingCompanyConfig(Integer usingCompanyConfig) {
		this.usingCompanyConfig = usingCompanyConfig;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getAppID() {
		return appID;
	}
	public void setAppID(String appID) {
		this.appID = appID;
	}
	public String getMchID() {
		return mchID;
	}
	public void setMchID(String mchID) {
		this.mchID = mchID;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public InputStream getCertStream() throws Exception {
		if (StringUtil.isBlank(this.getMchID())) {
			throw new Exception("请确保商户号mchId已设置");
		}
		if (StringUtil.isBlank(keyPath)) {
			throw new Exception("请确保证书文件地址keyPath已配置");
		}
		InputStream inputStream;
		final String prefix = "classpath:";
		String fileHasProblemMsg = "证书文件【" + keyPath + "】有问题，请核实！";
		String fileNotFoundMsg = "证书文件【" + keyPath + "】不存在，请核实！";
		if (keyPath.startsWith(prefix)) {
			String path = StringUtils.removeFirst(keyPath, prefix);
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			inputStream = MyWXPayConfig.class.getResourceAsStream(path);
			if (inputStream == null) {
				throw new Exception(fileNotFoundMsg);
			}
		} else {
			try {
				File file = new File(keyPath);
				if (!file.exists()) {
					throw new Exception(fileNotFoundMsg);
				}
				inputStream = new FileInputStream(file);
			} catch (IOException e) {
				throw new Exception(fileHasProblemMsg, e);
			}
		}
		return inputStream;
	}
	public void setCertStream(InputStream certStream) {
		this.certStream = certStream;
	}
	public int getHttpConnectTimeoutMs() {
		return httpConnectTimeoutMs;
	}
	public void setHttpConnectTimeoutMs(int httpConnectTimeoutMs) {
		this.httpConnectTimeoutMs = httpConnectTimeoutMs;
	}
	public int getHttpReadTimeoutMs() {
		return httpReadTimeoutMs;
	}
	public void setHttpReadTimeoutMs(int httpReadTimeoutMs) {
		this.httpReadTimeoutMs = httpReadTimeoutMs;
	}
	public void setCertStream(String keyPath) throws Exception{
		if (StringUtil.isBlank(this.getMchID())) {
			throw new Exception("请确保商户号mchId已设置");
		}

		if (StringUtil.isBlank(keyPath)) {
			throw new Exception("请确保证书文件地址keyPath已配置");
		}

		InputStream inputStream;
		final String prefix = "classpath:";
		String fileHasProblemMsg = "证书文件【" + keyPath + "】有问题，请核实！";
		String fileNotFoundMsg = "证书文件【" + keyPath + "】不存在，请核实！";
		if (keyPath.startsWith(prefix)) {
			String path = StringUtils.removeFirst(keyPath, prefix);
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			inputStream = MyWXPayConfig.class.getResourceAsStream(path);
			if (inputStream == null) {
				throw new Exception(fileNotFoundMsg);
			}
		} else {
			try {
				File file = new File(keyPath);
				if (!file.exists()) {
					throw new Exception(fileNotFoundMsg);
				}

				inputStream = new FileInputStream(file);
			} catch (IOException e) {
				throw new Exception(fileHasProblemMsg, e);
			}
		}
		this.certStream = inputStream;
	}
}

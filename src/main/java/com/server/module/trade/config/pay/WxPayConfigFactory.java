package com.server.module.trade.config.pay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.server.redis.RedisService;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CompanyEnum;
import com.server.util.stateEnum.MerchantTypeEnum;

@Component
@Scope("singleton")
public class WxPayConfigFactory {
	
	@Autowired
	private MerchantInfoDao merchantInfoDao;
	@Autowired
	private RedisService redisService;
	
	private Map<Integer,String> redisFlagMap = new HashMap<Integer,String>();

	private final Map<Integer,WXPayConfig> wxPayConfigMap = new HashMap<Integer,WXPayConfig>();
	
	private final Map<Integer,WXPay> wxPayMap = new HashMap<Integer,WXPay>();
	
	private final String redisName = "WxPayConfig";
	
	/**
	 * 初始化
	 * @author hebiting
	 * @date 2018年7月7日上午11:38:43
	 */
	@PostConstruct
	public void initMethod(){
		List<MerchantInfo> merchantInfoList = merchantInfoDao.findMerchantInfo(MerchantTypeEnum.MERCHANT.getType());
		for (MerchantInfo merchantInfo : merchantInfoList) {
			MyWXPayConfig wxPayConfig = new MyWXPayConfig();
			wxPayConfig.setAppID(merchantInfo.getAppId());
			wxPayConfig.setMchID(merchantInfo.getMchId());
			wxPayConfig.setKey(merchantInfo.getMchKey());
			wxPayConfig.setPlanId(merchantInfo.getPlanId());
			wxPayConfig.setCompanyId(merchantInfo.getCompanyId());
			wxPayConfig.setUsingCompanyConfig(merchantInfo.getUsingCompanyConfig());
			wxPayConfig.setIWXPayDomain(WXPayDomainSimpleImpl.instance());
			wxPayConfig.setKeyPath(merchantInfo.getKeyPath());
			wxPayConfig.setAppKey(merchantInfo.getAppKey());
			redisFlagMap.put(merchantInfo.getCompanyId(), "1");
			wxPayConfigMap.put(merchantInfo.getCompanyId(), wxPayConfig);
			WXPay wxPay = null;
			try {
				wxPay = new WXPay(wxPayConfig);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			wxPayMap.put(merchantInfo.getCompanyId(), wxPay);
		}
	}
	
	public WXPayConfig getWXPayConfig(Integer companyId){
		ensureConfig(companyId);
		WXPayConfig wxPayConfig = wxPayConfigMap.get(companyId);
		if(wxPayConfig == null){
			wxPayConfig = wxPayConfigMap.get(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		}
		return wxPayConfig;
	}
	
	private synchronized void ensureConfig(Integer companyId){
		boolean needUpdate = false;
		String flag = redisFlagMap.get(companyId);
		String redisFlag = redisService.getString(redisName+companyId);
		boolean flagBlank = StringUtil.isBlank(flag);
		boolean redisBlank = StringUtil.isBlank(redisFlag);
		if (!flagBlank && redisBlank) {
			redisService.setString(redisName+companyId,flag);
		} else if (!flagBlank && !redisBlank) {
			if(!flag.equals(redisFlag)){
				needUpdate = true;
				redisFlagMap.put(companyId, redisFlag);
			}
		} else {
			needUpdate = true;
			redisFlagMap.put(companyId, "1");
			redisService.setString(redisName+companyId,"1");
		}
		if(needUpdate){
			MerchantInfo merchant = merchantInfoDao.findMerchantInfoByCompanyId(companyId);
			if(merchant!=null){
				MyWXPayConfig wxPayConfig = new MyWXPayConfig();
				wxPayConfig.setAppID(merchant.getAppId());
				wxPayConfig.setMchID(merchant.getMchId());
				wxPayConfig.setKey(merchant.getMchKey());
				wxPayConfig.setPlanId(merchant.getPlanId());
				wxPayConfig.setCompanyId(merchant.getCompanyId());
				wxPayConfig.setUsingCompanyConfig(merchant.getUsingCompanyConfig());
				wxPayConfig.setIWXPayDomain(WXPayDomainSimpleImpl.instance());
				wxPayConfig.setKeyPath(merchant.getKeyPath());
				wxPayConfigMap.put(merchant.getCompanyId(), wxPayConfig);
				WXPay wxPay = null;
				try {
					wxPay = new WXPay(wxPayConfig);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				wxPayMap.put(merchant.getCompanyId(), wxPay);
			}
		}
	}
	
	
	public WXPayConfig getWXPayConfig(final String mchId){
		if(StringUtils.isNotBlank(mchId)){
			for (Map.Entry<Integer, WXPayConfig> element : wxPayConfigMap.entrySet()) {
				if(mchId.equals(element.getValue().getMchID())){
					return getWXPayConfig(element.getKey());
				}
			}
		}
		return getWXPayConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
	}
	
	public WXPay getWXPay(Integer companyId){
		ensureConfig(companyId);
		WXPay wxPay = wxPayMap.get(companyId);
		if(wxPay == null){
			wxPay = wxPayMap.get(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		}
		return wxPay;
	}

	public Map<Integer, WXPayConfig> getWxPayConfigMap() {
		return wxPayConfigMap;
	}

	public Map<Integer, WXPay> getWxPayMap() {
		return wxPayMap;
	}
	
	public synchronized void updateConfig(MerchantInfo merchantInfo){
		MyWXPayConfig wxPayConfig = new MyWXPayConfig();
		wxPayConfig.setAppID(merchantInfo.getAppId());
		wxPayConfig.setMchID(merchantInfo.getMchId());
		wxPayConfig.setKey(merchantInfo.getMchKey());
		wxPayConfig.setPlanId(merchantInfo.getPlanId());
		wxPayConfig.setCompanyId(merchantInfo.getCompanyId());
		wxPayConfig.setUsingCompanyConfig(merchantInfo.getUsingCompanyConfig());
		wxPayConfig.setIWXPayDomain(WXPayDomainSimpleImpl.instance());
		wxPayConfig.setKeyPath(merchantInfo.getKeyPath());
		wxPayConfigMap.put(merchantInfo.getCompanyId(), wxPayConfig);
		WXPay wxPay = null;
		try {
			wxPay = new WXPay(wxPayConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
		wxPayMap.put(merchantInfo.getCompanyId(), wxPay);
	}
}

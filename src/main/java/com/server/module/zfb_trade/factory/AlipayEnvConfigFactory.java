package com.server.module.zfb_trade.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.zfb_trade.bean.AliPayConfigBean;
import com.server.module.zfb_trade.module.aliInfo.AliInfoBean;
import com.server.module.zfb_trade.module.aliInfo.AliInfoService;
import com.server.module.zfb_trade.paramconfig.AlipayServiceEnvConfig;
import com.server.redis.RedisService;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CompanyEnum;


@Component
public class AlipayEnvConfigFactory {

	@Autowired
	private AliInfoService aliInfoService;
	
	@Autowired
	private AlipayServiceEnvConfig alipayServiceEnvConfig;
	
	@Autowired
	private RedisService redisService;
	
	public static final Map<Integer,AlipayServiceEnvConfig> map = new HashMap<Integer,AlipayServiceEnvConfig>();
	private Map<Integer,String> redisFlagMap = new HashMap<Integer,String>();
	private final String redisName = "AlipayEnvConfig";
	
	/**
	 * 初始化
	 * @author hebiting
	 * @date 2018年7月7日上午11:38:52
	 */
	@PostConstruct
	public void initMethod(){
		List<AliInfoBean> allAliInfo = aliInfoService.getAllAliInfo();
		allAliInfo.forEach(aliInfo -> {
			AlipayServiceEnvConfig clone = null;
			try {
				clone = (AlipayServiceEnvConfig)alipayServiceEnvConfig.clone();
				clone.setApp_id(aliInfo.getAppId());
				clone.setAlipay_public_key(aliInfo.getAlipayPublicKey());
				clone.setApp_private_key(aliInfo.getAppPrivateKey());
				clone.setApp_public_key(aliInfo.getAppPublicKey());
				clone.setPartner(aliInfo.getPartner());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			redisFlagMap.put(aliInfo.getCompanyId(), "1");
			map.put(aliInfo.getCompanyId(), clone);
		});
	}
	
	public AlipayServiceEnvConfig getAlipayServiceEnvConfig(Integer companyId){
		ensureEnvConfig(companyId);
		AlipayServiceEnvConfig alipayEnvConfig = map.get(companyId);
		if(alipayEnvConfig == null){
			alipayEnvConfig = map.get(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		}
		return alipayEnvConfig;
	}
	
	private synchronized void ensureEnvConfig(Integer companyId){
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
			AliInfoBean aliInfo = aliInfoService.getAliInfo(companyId);
			AlipayServiceEnvConfig clone = null;
			try {
				clone = (AlipayServiceEnvConfig)alipayServiceEnvConfig.clone();
				clone.setApp_id(aliInfo.getAppId());
				clone.setAlipay_public_key(aliInfo.getAlipayPublicKey());
				clone.setApp_private_key(aliInfo.getAppPrivateKey());
				clone.setApp_public_key(aliInfo.getAppPublicKey());
				clone.setPartner(aliInfo.getPartner());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			map.put(aliInfo.getCompanyId(), clone);
		}
	}
	
	public synchronized boolean updateConfigEnv(AliPayConfigBean alipayBean){
		AlipayServiceEnvConfig clone = null;
		try {
			clone = (AlipayServiceEnvConfig)alipayServiceEnvConfig.clone();
			clone.setApp_id(alipayBean.getAppId());
			clone.setAlipay_public_key(alipayBean.getAlipay_public_key());
			clone.setApp_private_key(alipayBean.getApp_private_key());
			clone.setApp_public_key(alipayBean.getApp_public_key());
			clone.setPartner(alipayBean.getPartner());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return false;
		}
		map.put(alipayBean.getCompanyId(), clone);
		return true;
	}
}

package com.server.module.zfb_trade.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.server.module.zfb_trade.bean.AliPayConfigBean;
import com.server.module.zfb_trade.paramconfig.AlipayServiceEnvConfig;
import com.server.redis.RedisService;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CompanyEnum;

@Component
public class AlipayAPIClientFactory {

	private  Logger log = LogManager.getLogger(AlipayAPIClientFactory.class);
	
	@Autowired
	private AlipayEnvConfigFactory alipayEnvFactory;
	@Autowired
	private RedisService redisService;
	
	private static final Map<Integer,AlipayClient> map = new HashMap<Integer,AlipayClient>();
	private Map<Integer,String> redisFlagMap = new HashMap<Integer,String>();
	private final String redisName = "AlipayAPIClient";
	/**
	 * 初始化
	 * @author hebiting
	 * @date 2018年7月10日下午7:11:51
	 */
	@PostConstruct
	public void initMethod(){
		Set<Integer> companySet = alipayEnvFactory.map.keySet();
		companySet.forEach(companyId -> {
			AlipayServiceEnvConfig alipayServiceEnv = alipayEnvFactory.map.get(companyId);
			AlipayClient alipayClient = new DefaultAlipayClient(
					alipayServiceEnv.alipay_gateway,
					alipayServiceEnv.app_id, 
					alipayServiceEnv.app_private_key,
					AlipayServiceEnvConfig.FORMAT_JSON,
					AlipayServiceEnvConfig.CHARSET_UTF8,
					alipayServiceEnv.alipay_public_key,
	    			AlipayServiceEnvConfig.SIGN_TYPE);
			map.put(companyId, alipayClient);
			redisFlagMap.put(companyId, "1");
		});
	}
	
	public AlipayClient getAlipayClient(Integer companyId){
		log.info("<AlipayAPIClientFactory--getAlipayClient--start>");
		ensureConfig(companyId);
		AlipayClient alipayClient = map.get(companyId);
		if(alipayClient == null){
			alipayClient = map.get(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		}
		log.info("<AlipayAPIClientFactory--getAlipayClient--end>");
		return alipayClient;
	}
	
	public synchronized void ensureConfig(Integer companyId){
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
			AlipayServiceEnvConfig alipayServiceEnv = alipayEnvFactory.getAlipayServiceEnvConfig(companyId);
			AlipayClient alipayClient = new DefaultAlipayClient(
					alipayServiceEnv.alipay_gateway,
					alipayServiceEnv.app_id, 
					alipayServiceEnv.app_private_key,
					AlipayServiceEnvConfig.FORMAT_JSON,
					AlipayServiceEnvConfig.CHARSET_UTF8,
					alipayServiceEnv.alipay_public_key,
	    			AlipayServiceEnvConfig.SIGN_TYPE);
			map.put(companyId, alipayClient);
		}
	}
	
	public synchronized boolean updateConfigClient(AliPayConfigBean alipayBean){
		AlipayServiceEnvConfig alipayServiceEnv = alipayEnvFactory.map.get(alipayBean.getCompanyId());
		if(alipayServiceEnv == null){
			return false;
		}
		AlipayClient alipayClient = new DefaultAlipayClient(
				alipayServiceEnv.alipay_gateway,
				alipayServiceEnv.app_id, 
				alipayServiceEnv.app_private_key,
				AlipayServiceEnvConfig.FORMAT_JSON,
				AlipayServiceEnvConfig.CHARSET_UTF8,
				alipayServiceEnv.alipay_public_key,
    			AlipayServiceEnvConfig.SIGN_TYPE);
		map.put(alipayBean.getCompanyId(), alipayClient);
		return true;
	}
}

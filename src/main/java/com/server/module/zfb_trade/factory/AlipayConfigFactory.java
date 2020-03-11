package com.server.module.zfb_trade.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.zfb_trade.bean.AliPayConfigBean;
import com.server.module.zfb_trade.module.aliInfo.AliInfoService;
import com.server.module.zfb_trade.module.aliInfo.AliMerchantInfoBean;
import com.server.module.zfb_trade.paramconfig.AlipayConfig;
import com.server.redis.RedisService;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CompanyEnum;


@Component
public class AlipayConfigFactory {

	@Autowired
	private AlipayConfig alipayConfig;
	
	@Autowired
	private AliInfoService aliInfoService;
	
	@Autowired
	private RedisService redisService;
	
	private static final Map<Integer,AlipayConfig> map = new HashMap<Integer,AlipayConfig>();
	private Map<Integer,String> redisFlagMap = new HashMap<Integer,String>();
	private final String redisName = "AlipayConfig";
	/**
	 * 初始化
	 * @author hebiting
	 * @date 2018年7月7日上午11:38:52
	 */
	@PostConstruct
	public void initMethod(){
		List<AliMerchantInfoBean> allAliMerchantInfo = aliInfoService.getAllAliMerchantInfo();
		allAliMerchantInfo.forEach(merchantInfo -> {
			AlipayConfig clone = null;
			try {
				clone = (AlipayConfig)alipayConfig.clone();
				clone.setSeller_id(merchantInfo.getPartner());
				clone.setPartner(merchantInfo.getPartner());
				clone.setKey(merchantInfo.getKey());
				clone.setTemplate_id(merchantInfo.getTemplateId());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			redisFlagMap.put(merchantInfo.getCompanyId(), "1");
			map.put(merchantInfo.getCompanyId(), clone);
		});
	}
	
	public AlipayConfig getAlipayConfig(Integer companyId){
		ensureConfig(companyId);
		AlipayConfig aliConfig = map.get(companyId);
		if(aliConfig == null){
			aliConfig = map.get(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		}
		return aliConfig;
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
			AliMerchantInfoBean merchantInfo = aliInfoService.getAliMerchantInfo(companyId);
			if(merchantInfo!=null){
				AlipayConfig clone = null;
				try {
					clone = (AlipayConfig)alipayConfig.clone();
					clone.setSeller_id(merchantInfo.getPartner());
					clone.setPartner(merchantInfo.getPartner());
					clone.setKey(merchantInfo.getKey());
					clone.setTemplate_id(merchantInfo.getTemplateId());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				map.put(merchantInfo.getCompanyId(), clone);
			}
		}
	}
	
	public synchronized boolean updateConfig(AliPayConfigBean alipayBean){
		AlipayConfig clone = null;
		try {
			clone = (AlipayConfig)alipayConfig.clone();
			clone.setSeller_id(alipayBean.getPartner());
			clone.setPartner(alipayBean.getPartner());
			clone.setKey(alipayBean.getKey());
			clone.setTemplate_id(alipayBean.getTemplate_id());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return false;
		}
		map.put(alipayBean.getCompanyId(), clone);
		return true;
	}
}

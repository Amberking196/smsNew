package com.server.module.trade.config.gzh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.server.redis.RedisService;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CompanyEnum;


@Component
@Scope("singleton")
public class WXGzhConfigFactory {

	@Autowired
	private OfficialAccountDao officialAccountDao;
	@Autowired
	private RedisService redisService;
	
	private final Map<Integer,MyWXGzhConfig> gzhConfigList = new HashMap<Integer,MyWXGzhConfig>();
	private Map<Integer,String> redisFlagMap = new HashMap<Integer,String>();
	private final String redisName = "WXGzhConfig";
	
	/**
	 * 初始化
	 * @author hebiting
	 * @date 2018年7月7日上午11:38:34
	 */
	@PostConstruct
	public void initMethod(){
		List<OfficialAccountInfo> oaList = officialAccountDao.findOfficialAccountInfo();
		for (OfficialAccountInfo oaInfo : oaList) {
			MyWXGzhConfig gzhConfig = new MyWXGzhConfig();
			gzhConfig.setAppId(oaInfo.getAppId());
			gzhConfig.setSecret(oaInfo.getSecret());
			gzhConfig.setToken(oaInfo.getToken());
			gzhConfig.setAppAppId(oaInfo.getAppAppId());
			gzhConfig.setAppSecret(oaInfo.getAppSecret());
			redisFlagMap.put(oaInfo.getCompanyId(), "1");
			gzhConfigList.put(oaInfo.getCompanyId(), gzhConfig);
		}
	}
	
	public MyWXGzhConfig getMyWXGzhConfig(Integer companyId){
		ensureGzhConfig(companyId);
		MyWXGzhConfig myWXGzhConfig = gzhConfigList.get(companyId);
		if(myWXGzhConfig == null){
			myWXGzhConfig = gzhConfigList.get(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		}
		return myWXGzhConfig; 
	}

	private synchronized void ensureGzhConfig(Integer companyId){
		String flag = redisFlagMap.get(companyId);
		if(StringUtil.isBlank(flag)){
			return ;
		}
		String redisFlag = redisService.getString(redisName+companyId);
		if(StringUtil.isBlank(redisFlag)){
			redisService.setString(redisName+companyId,flag);
			return ;
		}
		if(!redisFlag.equals(flag)){
			redisFlagMap.put(companyId,redisFlag);
			OfficialAccountInfo oaInfo = officialAccountDao.findOfficialAccountInfoByCompanyId(companyId);
			if(oaInfo!=null){
				MyWXGzhConfig gzhConfig = new MyWXGzhConfig();
				gzhConfig.setAppId(oaInfo.getAppId());
				gzhConfig.setSecret(oaInfo.getSecret());
				gzhConfig.setToken(oaInfo.getToken());
				gzhConfigList.put(oaInfo.getCompanyId(), gzhConfig);
			}
		}
	}

}

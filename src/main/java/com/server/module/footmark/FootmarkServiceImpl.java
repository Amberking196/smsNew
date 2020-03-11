package com.server.module.footmark;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.trade.auth.UserVo;
import com.server.redis.RedisService;
import com.server.util.stateEnum.ModeEnum;

@Service
public class FootmarkServiceImpl implements FootmarkService{

	private Logger log = LogManager.getLogger(FootmarkServiceImpl.class);
	
	@Autowired
	private FootmarkDao footmarkDao;
	@Autowired
	private RedisService redisService;

	@Override
	public Long insertFootmark(FootmarkBean footmarkBean) {
		//log.info("<FootmarkServiceImpl--insertFootmark--start>");
		Long id = footmarkDao.insertFootmark(footmarkBean);
		//log.info("<FootmarkServiceImpl--insertFootmark--start>");
		return id;
	}
	
	public Long insertFootmarkAndRedis(UserVo user,ModeEnum mode,String vmCode,Long enterId){
		log.info("<FootmarkServiceImpl--insertFootmarkAndRedis--start>");
		FootmarkBean footmark = new FootmarkBean(user,mode,vmCode,enterId);
		Long id = footmarkDao.insertFootmark(footmark);
		FootmarkRedisBean footRedis = new FootmarkRedisBean(mode,id);
		redisService.set("redis_mode_id"+vmCode,footRedis,600);
		log.info("<FootmarkServiceImpl--insertFootmarkAndRedis--end>");
		return id ;
	}
	

	public FootmarkBean findFinalReplenishRecord(String vmCode,Integer wayNum,Long basicItemId,Long userId) {
		return footmarkDao.findFinalReplenishRecord(vmCode,wayNum,basicItemId,userId);
	}
}

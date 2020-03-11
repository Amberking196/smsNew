package com.server.module.event;

import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.google.common.collect.Lists;
import com.server.redis.RedisService;


@Component
@Scope("singleton")
public class EventConfig {
	@Autowired
	private EventDao eventDao;

	@Autowired 
	private RedisService redisService;
	
	private String version ="1";
	private List<EventBean> list=Lists.newArrayList();
	
	@PostConstruct
	public void initMethod(){
		list=eventDao.eventList();
        String eventConfigVersion =redisService.getString("eventConfigVersion");
        if(StringUtils.isNotBlank(eventConfigVersion)) {
        	version=eventConfigVersion;
        }
	}
	
	public List<EventBean> getEventList(){
		return list;
	}
	
	public List<EventBean>  updateEventList(String eventConfigVersion) {
		list=eventDao.eventList();
		version=eventConfigVersion;
		return list;
	}

	public String getVersion() {
		return version;
	}

}

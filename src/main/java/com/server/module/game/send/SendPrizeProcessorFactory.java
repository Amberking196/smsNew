package com.server.module.game.send;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.server.util.StringUtil;
import com.server.util.stateEnum.GamePrizeTypeEnum;

@Component
public class SendPrizeProcessorFactory implements ApplicationContextAware{

	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public SendPrizeProcessor getSendPrizeProcessor(GamePrizeTypeEnum prizeType){
		String beanName = prizeType.getBeanName();
		if(StringUtil.isBlank(beanName)){
			return null;
		}
		SendPrizeProcessor sendProcessor = applicationContext.getBean(beanName,SendPrizeProcessor.class);
		if(sendProcessor == null){
			throw new RuntimeException("无可用奖励分发器:"+beanName);
		}
		return sendProcessor;
	}
	
}

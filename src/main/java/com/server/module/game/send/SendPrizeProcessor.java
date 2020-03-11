package com.server.module.game.send;

import com.server.module.game.turntable.GameDto;
import com.server.module.game.turntable.GamePrizeBean;
import com.server.module.game.turntable.GamePrizeReceiveBean;
import com.server.util.ReturnDataUtil;

public interface SendPrizeProcessor {

	/**
	 * 发送奖励
	 * @author why
	 * @date 2019年3月7日 上午10:02:08 
	 * @param prize
	 * @param gameDto
	 * @return
	 */
	public ReturnDataUtil send(GamePrizeBean prize,GameDto gameDto);
}

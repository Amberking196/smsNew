package com.server.module.game.send;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerService;
import com.server.module.game.turntable.GameDto;
import com.server.module.game.turntable.GamePrizeBean;
import com.server.module.trade.util.UserUtil;
import com.server.util.ReturnDataUtil;

/**
 * 提水券发送
 * 
 * @author why
 * @date: 2019年3月7日 上午10:20:47
 */
@Component
public class SendCarryWaterProcessor implements SendPrizeProcessor {

	private static Logger log = LogManager.getLogger(SendCarryWaterProcessor.class);
	@Autowired
	private CarryWaterVouchersCustomerService carryWaterVouchersCustomerServiceImpl;

	@Override
	public ReturnDataUtil send(GamePrizeBean prize, GameDto gameDto) {
		log.info("<SendCarryWaterProcessor>-----<send>-----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		if (prize.getRewardId() != null && prize.getRewardId() > 0) {
			carryWaterVouchersCustomerServiceImpl.add(prize.getRewardId(), UserUtil.getUser().getId(),
					prize.getAmount(), null);
			returnDataUtil.setMessage("恭喜您抽中" + prize.getAmount() + "张提水券，请前往我的信息→我的提水券 查看！");
			returnDataUtil.setReturnObject(gameDto);
		} else {
			returnDataUtil.setMessage("谢谢惠顾");
			returnDataUtil.setReturnObject(gameDto);
		}
		log.info("<SendCarryWaterProcessor>-----<send>-----end");
		return returnDataUtil;
	}
}

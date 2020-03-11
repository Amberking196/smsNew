package com.server.module.game.turntable;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.coupon.CouponService;
import com.server.module.coupon.MachinesLAC;
import com.server.module.game.send.SendPrizeProcessor;
import com.server.module.game.send.SendPrizeProcessorFactory;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.customer.TblCustomerBean;
import com.server.module.trade.customer.TblCustomerService;
import com.server.module.trade.util.UserUtil;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.redis.RedisService;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.GamePrizeTypeEnum;

@RestController
@RequestMapping("/game")
public class GameController {

	private final static Logger log = LogManager.getLogger(GameController.class);

	@Autowired
	private GameService gameService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private SendPrizeProcessorFactory sendPrizeProcessorFactory;
	@Autowired
	private CouponService couponService;
	@Autowired
	private TblCustomerService tblCustomerService;

	/**
	 * 获取游戏奖品内容
	 * 
	 * @author hebiting
	 * @date 2018年8月20日下午5:10:32
	 * @param gameId
	 * @return
	 */
	@PostMapping("/getGamePrize")
	public ReturnDataUtil getGamePrize(Integer gameId) {
		log.info("<GameController--getGamePrize--start>");
		List<GamePrizeBean> gamePrize = gameService.getGamePrize(gameId);
		if (gamePrize != null && gamePrize.size() > 0) {
			return ResultUtil.success(gamePrize);
		}
		return ResultUtil.error();
	}

	/**
	 * 抽奖
	 * 
	 * @author hebiting
	 * @date 2018年8月20日下午5:32:15
	 * @param gameId
	 */
	@PostMapping("/lottery")
	public ReturnDataUtil lottery(Integer gameId) {
		log.info("<GameController>-----<lottery>----start");
		UserVo user = UserUtil.getUser();
		GameBean game = gameService.getGame(gameId);
		Integer totalTimes = game.getTimes();
		// 获取用户的抽奖次数
		Integer times = redisService.get("game" + gameId + "customer" + user.getId(), Integer.class);
		log.info("抽奖次数====" + times);
		boolean canDo = false;
		// 判断游戏 是否购买后可参与(0:不需要 1：购买后参与)
		if (game.getNeedGo() == 1) {
			if (redisService.exists("orderUserId" + user.getId())) {
				canDo = true;
				redisService.del("orderUserId" + user.getId());
			}
		} else {
			canDo = true;
		}
		if (canDo && (times == null || times < totalTimes)) {
			if (times == null) {
				times = 0;
			}
			DateTime today = new DateTime();
			DateTime tommrow = new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 23, 59, 59);
			int expires = tommrow.getSecondOfDay() - today.getSecondOfDay();
			// 抽奖次数 写入redis
			redisService.set("game" + gameId + "customer" + user.getId(), Integer.valueOf(++times), expires);
			// 获取游戏所有奖品
			List<GamePrizeBean> gamePrize = gameService.getGamePrize(gameId);
			// 获取奖励
			GamePrizeBean prize = getPrize(gamePrize);
			boolean updatePrizeTotal = false;
			// 谢谢惠顾 1 0 普通
			if (prize.getDefaultFlag() == 1) {
				updatePrizeTotal = true;
			} else {
				if (prize.getTotal() > 0) {
					updatePrizeTotal = gameService.updatePrizeTotal(prize);
				}
			}
			// 如果 奖品抽完了 默认谢谢惠顾
			if (!updatePrizeTotal) {
				List<GamePrizeBean> collect = gamePrize.stream().filter(prizes -> prizes.getDefaultFlag() == 1)
						.collect(Collectors.toList());
				if (collect != null && collect.size() > 0) {
					prize = collect.get(0);
				} else {
					return ResultUtil.error(ResultEnum.ILLEGAL_STATE, null);
				}
			}
			GamePrizeReceiveBean gpr = new GamePrizeReceiveBean();
			DateTime now = new DateTime();
			gpr.setCreateTime(now.toDate());
			gpr.setCustomerId(user.getId());
			gpr.setGamePrizeId(prize.getId());
			Integer canReceive = game.getCanReceive();
			gpr.setEndTime(now.plusDays(canReceive).toDate());
			// 奖品绑定用户
			Long insertGameReceive = gameService.insertGameReceive(gpr);
			if (insertGameReceive != null && insertGameReceive > 0) {
				GameDto gameDto = new GameDto();
				int indexOf = gamePrize.indexOf(prize);
				int time = totalTimes - times;
				gameDto.setId(insertGameReceive);
				gameDto.setIndexOf(indexOf);
				gameDto.setTimes(time);
				log.info("结果======" + indexOf);
				SendPrizeProcessor sendPrizeProcessor = sendPrizeProcessorFactory
						.getSendPrizeProcessor(GamePrizeTypeEnum.getType(prize.getType()));
				if (sendPrizeProcessor != null) {
					ReturnDataUtil returnDataUtil = sendPrizeProcessor.send(prize, gameDto);
					return returnDataUtil;
				}
			} else {
				return ResultUtil.error(ResultEnum.ADD_FAILED, null);
			}
		}
		return ResultUtil.error(ResultEnum.GAME_TIMES_NOT_ENOUGH, null);
	}

	// 获取奖励
	private GamePrizeBean getPrize(List<GamePrizeBean> gamePrize) {
		log.info("<GameController>-----<getPrize>----start");
		Random random = new Random();
		Integer total = 0;
		for (int i = 0; i < gamePrize.size(); i++) {
			total += gamePrize.get(i).getWeight();
		}
		int nextInt = random.nextInt(total);
		log.info("奖励随机号" + nextInt);
		for (int i = 0; i < gamePrize.size(); i++) {
			if (nextInt < gamePrize.get(i).getWeight()) {
				log.info("奖品名称:" + JsonUtil.toJson(gamePrize.get(i)));
				return gamePrize.get(i);
			} else {
				nextInt -= gamePrize.get(i).getWeight();
			}
		}
		return null;
	}

	/**
	 * 查询用户奖品
	 * 
	 * @author why
	 * @date 2019年3月2日 下午3:23:11
	 * @return
	 */
	@PostMapping("/getCusPrize")
	public ReturnDataUtil getCusPrize() {
		log.info("<GameController>-----<getCusPrize>----start");
		Long customerId = UserUtil.getUser().getId();
		List<PrizeReceiveDto> gamePrizeReceive = gameService.getGamePrizeReceive(customerId);
		if (gamePrizeReceive != null && gamePrizeReceive.size() > 0) {
			log.info("<GameController>-----<getCusPrize>----end");
			return ResultUtil.success(gamePrizeReceive);
		}
		log.info("<GameController>-----<getCusPrize>----end");
		return ResultUtil.error(ResultEnum.SELECT_NOT_DATA, null);
	}

	/**
	 * 根据vmCode获取可玩游戏
	 * 
	 * @author hebiting
	 * @date 2018年8月30日上午8:46:59
	 * @param vmCode
	 * @return
	 */
	@PostMapping("/getAvailableGame")
	public ReturnDataUtil getAvailableGame(String vmCode) {
		log.info("<GameController>-----<getAvailableGame>----start");
		Map<String, Object> resultData = new HashMap<String, Object>();
		// 根据机器编码获取该机器线路，区域，公司归属
		MachinesLAC machinesLAC = couponService.getMachinesLAC(vmCode);
		// 获取机器可玩游戏
		List<GameBean> gameList = gameService.getAvailableGame(machinesLAC);
		if (gameList != null && gameList.size() > 0) {
			GameBean minGame = Collections.max(gameList, new Comparator<GameBean>() {
				@Override
				public int compare(GameBean a, GameBean b) {
					return a.getTarget().compareTo(b.getTarget());
				}
			});
			// 获取游戏所有奖品
			List<GamePrizeBean> gamePrize = gameService.getGamePrize(minGame.getId());
			// 获取用户的抽奖次数
			Integer times = redisService.get("game" + minGame.getId() + "customer" + UserUtil.getUser().getId(),
					Integer.class);
			if ((times != null && times < minGame.getTimes())) {
				minGame.setTimes(minGame.getTimes() - times);
			}
			resultData.put("game", minGame);
			resultData.put("gamePrize", gamePrize);
			log.info("<GameController>-----<getAvailableGame>----end");
			return ResultUtil.success(resultData);
		}
		log.info("<GameController>-----<getAvailableGame>----end");
		return ResultUtil.selectError();
	}

	@PostMapping("/updateAddress")
	public ReturnDataUtil updateAddress(@RequestBody GamePrizeReceiveBean gamePrizeReceive) {
		log.info("<GameController>-----<updateAddress>----start");
		boolean updateGameReceive = gameService.updateGameReceive(gamePrizeReceive);
		if (updateGameReceive) {
			log.info("<GameController>-----<updateAddress>----end");
			return ResultUtil.success(updateGameReceive);
		}
		log.info("<GameController>-----<updateAddress>----end");
		return ResultUtil.error(ResultEnum.SELECT_NOT_DATA, null);
	}

	@PostMapping("/raffle")
	public ReturnDataUtil raffle(String vmCode) {
		log.info("<GameController>-----<raffle>----start");
		// 根据机器编码获取该机器线路，区域，公司归属
		MachinesLAC machinesLAC = couponService.getMachinesLAC(vmCode);
		// 获取机器可玩游戏
		List<GameBean> gameList = gameService.getAvailableGame(machinesLAC);
		if (gameList != null && gameList.size() > 0) {
			GameBean minGame = Collections.max(gameList, new Comparator<GameBean>() {
				@Override
				public int compare(GameBean a, GameBean b) {
					return a.getTarget().compareTo(b.getTarget());
				}
			});
			//获取游戏所需积分
			Integer integral = minGame.getIntegral();
			TblCustomerBean bean = tblCustomerService.getCustomerById(UserUtil.getUser().getId());
			if(bean.getIntegral()!=null && bean.getIntegral()>integral) {
				return ResultUtil.success(ResultEnum.GAME_SUCCESS,null);
			}
		}
		log.info("<GameController>-----<raffle>----end");
		return ResultUtil.selectError();
	}
}

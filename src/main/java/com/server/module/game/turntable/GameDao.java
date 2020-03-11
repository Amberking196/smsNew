package com.server.module.game.turntable;

import java.util.List;

import com.server.module.coupon.MachinesLAC;

public interface GameDao {

	/**
	 * 新增游戏
	 * @author hebiting
	 * @date 2018年8月20日下午2:49:05
	 * @param game
	 * @return
	 */
	Integer insertGame(GameBean game);
	
	/**
	 * 新增游戏奖品
	 * @author hebiting
	 * @date 2018年8月20日下午2:49:12
	 * @param gamePrize
	 * @return
	 */
	Long insertGamePrize(GamePrizeBean gamePrize);
	
	/**
	 * 用户奖品分发
	 * @author hebiting
	 * @date 2018年8月20日下午2:49:28
	 * @param gamePrizeReceive
	 * @return
	 */
	Long insertGameReceive(GamePrizeReceiveBean gamePrizeReceive);
	
	/**
	 * 更新游戏信息
	 * @author hebiting
	 * @date 2018年8月20日下午2:49:45
	 * @param game
	 * @return
	 */
	boolean updateGame(GameBean game);
	
	/**
	 * 更新奖品信息
	 * @author hebiting
	 * @date 2018年8月20日下午2:49:53
	 * @param gamePrize
	 * @return
	 */
	boolean updateGamePrize(GamePrizeBean gamePrize);
	
	/**
	 * 更新奖品剩余数量
	 * @author hebiting
	 * @date 2018年8月21日下午3:05:39
	 * @param gamePrize
	 * @return
	 */
	boolean updatePrizeTotal(GamePrizeBean gamePrize);
	/**
	 * 更新领取信息
	 * @author hebiting
	 * @date 2018年8月20日下午2:50:01
	 * @param gamePrizeReceive
	 * @return
	 */
	boolean updateGameReceive(GamePrizeReceiveBean gamePrizeReceive);
	
	/**
	 * 获取游戏所有奖品
	 * @author hebiting
	 * @date 2018年8月20日下午2:53:52
	 * @param gameId
	 * @return
	 */
	List<GamePrizeBean> getGamePrize(Integer gameId);
	
	/**
	 * 获取奖品信息
	 * @author hebiting
	 * @date 2018年8月20日下午2:53:52
	 * @param gamePrizeId
	 * @return
	 */
	GamePrizeBean getPrizeById(Long gamePrizeId);
	
	
	/**
	 * 获取游戏信息
	 * @author hebiting
	 * @date 2018年8月21日上午9:21:59
	 * @param gameId
	 * @return
	 */
	GameBean getGame(Integer gameId);
	
	
	/**
	 * 获取游戏信息
	 * @author hebiting
	 * @date 2018年8月21日上午9:21:59
	 * @param gameId
	 * @return
	 */
	GamePrizeReceiveBean getGamePrizeReceive(Integer prizeReceiveId);
	
	/**
	 * 获取用户的奖品
	 * @author why
	 * @date 2019年3月2日 下午3:25:31 
	 * @param customerId
	 * @param type
	 * @return
	 */
	List<PrizeReceiveDto> getGamePrizeReceive(Long customerId);
	
	
	/**
	 * 获取可玩游戏
	 * @author hebiting
	 * @date 2018年8月30日上午8:56:40
	 * @param machinesLAC
	 * @return 仅返回游戏id 游戏名称name
	 */
	List<GameBean> getAvailableGame(MachinesLAC machinesLAC);
}

package com.server.module.game.turntable;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.coupon.MachinesLAC;
import com.server.util.stateEnum.GamePrizeTypeEnum;

@Service
public class GameServiceImpl implements GameService{
	
	private final static Logger log = LogManager.getLogger(GameServiceImpl.class);

	@Autowired
	private GameDao gameDao;

	@Override
	public Integer insertGame(GameBean game) {
		log.info("<GameServiceImpl--insertGame--start>");
		Integer insertGame = gameDao.insertGame(game);
		if(insertGame !=null && insertGame>0){
			GamePrizeBean gamePrize = new GamePrizeBean();
			gamePrize.setDefaultFlag(1);
			gamePrize.setAmount(1);
			gamePrize.setDeleteFlag(0);
			gamePrize.setGameId(insertGame);
			gamePrize.setName("谢谢惠顾");
			gamePrize.setTotal(Integer.MAX_VALUE);
			gamePrize.setType(GamePrizeTypeEnum.NOTHING.getState());
			gameDao.insertGamePrize(gamePrize);
		}
		log.info("<GameServiceImpl--insertGame--end>");
		return insertGame;
	}

	@Override
	public Long insertGamePrize(GamePrizeBean gamePrize) {
		log.info("<GameServiceImpl--insertGamePrize--start>");
		Long insertGamePrize = gameDao.insertGamePrize(gamePrize);
		log.info("<GameServiceImpl--insertGamePrize--end>");
		return insertGamePrize;
	}

	@Override
	public Long insertGameReceive(GamePrizeReceiveBean gamePrizeReceive) {
		log.info("<GameServiceImpl--insertGameReceive--start>");
		Long insertGamePrize = gameDao.insertGameReceive(gamePrizeReceive);
		log.info("<GameServiceImpl--insertGameReceive--end>");
		return insertGamePrize;
	}

	@Override
	public boolean updateGame(GameBean game) {
		log.info("<GameServiceImpl--updateGame--start>");
		boolean updateGame = gameDao.updateGame(game);
		log.info("<GameServiceImpl--updateGame--end>");
		return updateGame;
	}

	@Override
	public boolean updateGamePrize(GamePrizeBean gamePrize) {
		log.info("<GameServiceImpl--updateGamePrize--start>");
		boolean updateGame = gameDao.updateGamePrize(gamePrize);
		log.info("<GameServiceImpl--updateGamePrize--end>");
		return updateGame;
	}

	@Override
	public boolean updateGameReceive(GamePrizeReceiveBean gamePrizeReceive) {
		log.info("<GameServiceImpl--updateGameReceive--start>");
		boolean updateGame = gameDao.updateGameReceive(gamePrizeReceive);
		log.info("<GameServiceImpl--updateGameReceive--end>");
		return updateGame;
	}

	@Override
	public List<GamePrizeBean> getGamePrize(Integer gameId) {
		log.info("<GameServiceImpl--getGamePrize--start>");
		List<GamePrizeBean> gamePrize = gameDao.getGamePrize(gameId);
		log.info("<GameServiceImpl--getGamePrize--end>");
		return gamePrize;
	}

	@Override
	public GameBean getGame(Integer gameId) {
		log.info("<GameServiceImpl--getGame--start>");
		GameBean game = gameDao.getGame(gameId);
		log.info("<GameServiceImpl--getGame--end>");
		return game;
	}

	@Override
	public boolean updatePrizeTotal(GamePrizeBean gamePrize) {
		log.info("<GameServiceImpl--updatePrizeTotal--start>");
		boolean updatePrizeTotal = gameDao.updatePrizeTotal(gamePrize);
		log.info("<GameServiceImpl--updatePrizeTotal--end>");
		return updatePrizeTotal;
	}

	@Override
	public GamePrizeReceiveBean getGamePrizeReceive(Integer prizeReceiveId) {
		log.info("<GameServiceImpl--getGamePrizeReceive--start>");
		GamePrizeReceiveBean gamePrizeReceive = gameDao.getGamePrizeReceive(prizeReceiveId);
		log.info("<GameServiceImpl--getGamePrizeReceive--end>");
		return gamePrizeReceive;
	}

	@Override
	public GamePrizeBean getPrizeById(Long gamePrizeId) {
		log.info("<GameServiceImpl--getPrizeById--start>");
		GamePrizeBean prizeById = gameDao.getPrizeById(gamePrizeId);
		log.info("<GameServiceImpl--getPrizeById--end>");
		return prizeById;
	}

	@Override
	public List<PrizeReceiveDto> getGamePrizeReceive(Long customerId) {
		log.info("<GameServiceImpl--getGamePrizeReceive--start>");
		List<PrizeReceiveDto> gamePrizeReceive = gameDao.getGamePrizeReceive(customerId);
		log.info("<GameServiceImpl--getGamePrizeReceive--end>");
		return gamePrizeReceive;
	}

	@Override
	public List<GameBean> getAvailableGame(MachinesLAC machinesLAC) {
		log.info("<GameServiceImpl--getAvailableGame--start>");
		List<GameBean> availableGame = gameDao.getAvailableGame(machinesLAC);
		log.info("<GameServiceImpl--getAvailableGame--end>");
		return availableGame;
	}
}

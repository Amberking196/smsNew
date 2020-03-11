package com.server.module.game.turntable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.module.coupon.MachinesLAC;
import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class GameDaoImpl extends MySqlFuns implements GameDao{

	private final static Logger log = LogManager.getLogger(GameDaoImpl.class);

	@Override
	public Integer insertGame(GameBean game) {
		log.info("<GameDaoImpl--insertGame--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		Date date = new Date();
		sql.append(" INSERT INTO game(`name`,times,target,companyId,areaId,vmCode,needGo,`type`,`startTime`,`endTime`,canReceive,createUser,createTime,updateUser,updateTime)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?)");
		param.add(game.getName());
		param.add(game.getTimes());
		param.add(game.getTarget());
		param.add(game.getCompanyId());
		param.add(game.getAreaId());
		param.add(game.getVmCode());
		param.add(game.getNeedGo());
		param.add(game.getType());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(game.getStartTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(game.getEndTime()));
		param.add(game.getCanReceive());
		param.add(game.getCreateUser());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(date));
		param.add(game.getUpdateUser());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(date));
		int insertGetID = insertGetID(sql.toString(), param);
		log.info("<GameDaoImpl--insertGame--end>");
		return insertGetID;
	}

	@Override
	public Long insertGamePrize(GamePrizeBean gamePrize) {
		log.info("<GameDaoImpl--insertGamePrize--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO game_prize(`name`,`type`,`weight`,total,amount,rewardId,gameId,deleteFlag,defaultFlag)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?)");
		param.add(gamePrize.getName());
		param.add(gamePrize.getType());
		param.add(gamePrize.getWeight());
		param.add(gamePrize.getTotal());
		param.add(gamePrize.getAmount());
		param.add(gamePrize.getRewardId());
		param.add(gamePrize.getGameId());
		param.add(gamePrize.getDeleteFlag());
		param.add(gamePrize.getDefaultFlag());
		int insertGetID = insertGetID(sql.toString(), param);
		log.info("<GameDaoImpl--insertGamePrize--end>");
		return (long)insertGetID;
	}

	@Override
	public Long insertGameReceive(GamePrizeReceiveBean gamePrizeReceive) {
		log.info("<GameDaoImpl--insertGameReceive--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO game_prize_receive(gamePrizeId,customerId,createTime,endTime,receiveTime,addressId)");
		sql.append(" VALUES(?,?,?,?,?,?)");
		param.add(gamePrizeReceive.getGamePrizeId());
		param.add(gamePrizeReceive.getCustomerId());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(gamePrizeReceive.getCreateTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(gamePrizeReceive.getEndTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(gamePrizeReceive.getReceiveTime()));
		param.add(gamePrizeReceive.getAddressId());
		int insertGetID = insertGetID(sql.toString(), param);
		log.info("<GameDaoImpl--insertGameReceive--end>");
		return (long)insertGetID;
	}

	@Override
	public boolean updateGame(GameBean game) {
		log.info("<GameDaoImpl--updateGame--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		Date date = new Date();
		sql.append(" UPDATE game SET target=?,companyId=?,areaId=?,vmCode=?,needGo=?,times=?,`name`=?,`type`=?,startTime=?,endTime=?,canReceive=?,deleteFlag=?,updateUser=?,updateTime=?");
		sql.append(" WHERE id = ?");
		param.add(game.getName());
		param.add(game.getTarget());
		param.add(game.getCompanyId());
		param.add(game.getAreaId());
		param.add(game.getVmCode());
		param.add(game.getNeedGo());
		param.add(game.getTimes());
		param.add(game.getType());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(game.getStartTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(game.getEndTime()));
		param.add(game.getCanReceive());
		param.add(game.getDeleteFlag());
		param.add(game.getUpdateUser());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(date));
		param.add(game.getId());
		int upate = upate(sql.toString(), param);
		log.info("<GameDaoImpl--updateGame--end>");
		if(upate>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean updateGamePrize(GamePrizeBean gamePrize) {
		log.info("<GameDaoImpl--updateGamePrize--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE game_prize SET `name`=?,`type`=?,weight=?,total=?,amount=?,rewardId=?,gameId=?,deleteFlag=?,defaultFlag=?");
		sql.append(" WHERE id = ?");
		param.add(gamePrize.getName());
		param.add(gamePrize.getType());
		param.add(gamePrize.getWeight());
		param.add(gamePrize.getTotal());
		param.add(gamePrize.getAmount());
		param.add(gamePrize.getRewardId());
		param.add(gamePrize.getGameId());
		param.add(gamePrize.getDeleteFlag());
		param.add(gamePrize.getDefaultFlag());
		param.add(gamePrize.getId());
		int upate = upate(sql.toString(), param);
		log.info("<GameDaoImpl--updateGamePrize--end>");
		if(upate>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean updateGameReceive(GamePrizeReceiveBean gamePrizeReceive) {
		log.info("<GameDaoImpl--updateGameReceive--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE game_prize_receive SET receiveTime = ?,addressId=?  WHERE id = ?");
		param.add(DateUtil.formatYYYYMMDDHHMMSS(gamePrizeReceive.getReceiveTime()));
		param.add(gamePrizeReceive.getAddressId());
		param.add(gamePrizeReceive.getId());
		int upate = upate(sql.toString(),param);
		log.info("<GameDaoImpl--updateGameReceive--end>");
		if(upate>0){
			return true;
		}
		return false;
	}

	@Override
	public List<GamePrizeBean> getGamePrize(Integer gameId) {
		log.info("<GameDaoImpl--getGamePrize--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,`name`,`type`,weight,total,amount,rewardId,gameId,deleteFlag,defaultFlag FROM game_prize WHERE deleteFlag = 0 AND gameId = "+gameId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GamePrizeBean> prizeList = new ArrayList<GamePrizeBean>();
		GamePrizeBean gamePrize = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				gamePrize = new GamePrizeBean();
				gamePrize.setAmount(rs.getInt("amount"));
				gamePrize.setDeleteFlag(rs.getInt("deleteFlag"));
				gamePrize.setGameId(rs.getInt("gameId"));
				gamePrize.setId(rs.getLong("id"));
				gamePrize.setName(rs.getString("name"));
				gamePrize.setTotal(rs.getInt("total"));
				gamePrize.setRewardId(rs.getLong("rewardId"));
				gamePrize.setType(rs.getInt("type"));
				gamePrize.setWeight(rs.getInt("weight"));
				gamePrize.setDefaultFlag(rs.getInt("defaultFlag"));
				prizeList.add(gamePrize);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GameDaoImpl--getGamePrize--end>");
		return prizeList;
	}

	@Override
	public GameBean getGame(Integer gameId) {
		log.info("<GameDaoImpl--getGame--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,target,companyId,areaId,vmCode,needGo,times,`name`,`type`,startTime,endTime,canReceive,createUser,createTime,deleteFlag,updateUser,updateTime");
		sql.append(" FROM game WHERE deleteFlag = 0 AND id = "+gameId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GameBean game = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				game = new GameBean();
				game.setAreaId(rs.getInt("areaId"));
				game.setTarget(rs.getInt("target"));
				game.setCompanyId(rs.getInt("companyId"));
				game.setVmCode(rs.getString("vmCode"));
				game.setNeedGo(rs.getInt("needGo"));
				game.setTimes(rs.getInt("times"));
				game.setCanReceive(rs.getInt("canReceive"));
				game.setCreateTime(rs.getTimestamp("createTime"));
				game.setCreateUser(rs.getLong("createUser"));
				game.setDeleteFlag(rs.getInt("deleteFlag"));
				game.setEndTime(rs.getTimestamp("endTime"));
				game.setId(rs.getInt("id"));
				game.setName(rs.getString("name"));
				game.setStartTime(rs.getTimestamp("startTime"));
				game.setType(rs.getInt("type"));
				game.setUpdateTime(rs.getTimestamp("updateTime"));
				game.setUpdateUser(rs.getLong("updateUser"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GameDaoImpl--getGame--end>");
		return game;
	}

	@Override
	public boolean updatePrizeTotal(GamePrizeBean gamePrize) {
		log.info("<GameDaoImpl--updatePrizeTotal--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE game_prize SET total = total - amount");
		sql.append(" WHERE total>amount and id = "+gamePrize.getId());
		log.info("sql:"+sql);
		int upate = upate(sql.toString());
		log.info("<GameDaoImpl--updatePrizeTotal--end>");
		if(upate>0){
			return true;
		}
		return false;
	}

	@Override
	public GamePrizeReceiveBean getGamePrizeReceive(Integer prizeReceiveId) {
		log.info("<GameDaoImpl--getGamePrizeReceive--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,gamePrizeId,customerId,createTime,endTime,receiveTime,addressId FROM game_prize_receive WHERE id = "+prizeReceiveId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GamePrizeReceiveBean prizeReceive = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				prizeReceive = new GamePrizeReceiveBean();
				prizeReceive.setCreateTime(rs.getTimestamp("createTime"));
				prizeReceive.setCustomerId(rs.getLong("customerId"));
				prizeReceive.setEndTime(rs.getTimestamp("endTime"));
				prizeReceive.setGamePrizeId(rs.getLong("gamePrizeId"));
				prizeReceive.setId(rs.getLong("id"));
				prizeReceive.setReceiveTime(rs.getTimestamp("receiveTime"));
				prizeReceive.setAddressId(rs.getLong("addressId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GameDaoImpl--getGamePrizeReceive--end>");
		return prizeReceive;
	}

	@Override
	public GamePrizeBean getPrizeById(Long gamePrizeId) {
		log.info("<GameDaoImpl--getPrizeById--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,`name`,`type`,weight,total,amount,rewardId,gameId,deleteFlag,defaultFlag FROM game_prize WHERE deleteFlag = 0 AND id = "+gamePrizeId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GamePrizeBean gamePrize = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				gamePrize = new GamePrizeBean();
				gamePrize.setAmount(rs.getInt("amount"));
				gamePrize.setDeleteFlag(rs.getInt("deleteFlag"));
				gamePrize.setGameId(rs.getInt("gameId"));
				gamePrize.setId(rs.getLong("id"));
				gamePrize.setName(rs.getString("name"));
				gamePrize.setTotal(rs.getInt("total"));
				gamePrize.setRewardId(rs.getLong("rewardId"));
				gamePrize.setType(rs.getInt("type"));
				gamePrize.setWeight(rs.getInt("weight"));
				gamePrize.setDefaultFlag(rs.getInt("defaultFlag"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GameDaoImpl--getPrizeById--end>");
		return gamePrize;
	}

	@Override
	public List<PrizeReceiveDto> getGamePrizeReceive(Long customerId) {
		log.info("<GameDaoImpl--getGamePrizeReceive--start>");
		StringBuffer sql = new StringBuffer();
		List<PrizeReceiveDto> receiveList = new ArrayList<PrizeReceiveDto>();
		sql.append(" SELECT gp.id AS prizeId,gp.name AS prizeName ,gp.type AS prizeType ,");
		sql.append(" gp.amount,gpr.id AS prizeReceiveId,gpr.customerId,gpr.endTime,gpr.createTime,sg.pic");
		sql.append(" FROM game_prize AS gp INNER JOIN game_prize_receive AS gpr ");
		sql.append(" ON gp.`id` = gpr.`gamePrizeId` LEFT JOIN shopping_goods sg on gp.rewardId=sg.id ");
		sql.append("  WHERE 1 = 1 and gp.type=3 and gp.defaultFlag=0");
		sql.append(" AND gpr.`customerId` = "+customerId);
		log.info("获取用户的奖品sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PrizeReceiveDto prizeReceive = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				prizeReceive = new PrizeReceiveDto();
				prizeReceive.setAmount(rs.getInt("amount"));
				prizeReceive.setCustomerId(rs.getLong("customerId"));
				prizeReceive.setPrizeId(rs.getLong("prizeId"));
				prizeReceive.setPrizeName(rs.getString("prizeName"));
				prizeReceive.setPrizeReceiveId(rs.getLong("prizeReceiveId"));
				prizeReceive.setPrizeType(rs.getInt("prizeType"));
				prizeReceive.setCreateTime(rs.getTimestamp("createTime"));
				prizeReceive.setPic(rs.getString("pic"));
				receiveList.add(prizeReceive);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GameDaoImpl--getGamePrizeReceive--end>");
		return receiveList;
	}

	@Override
	public List<GameBean> getAvailableGame(MachinesLAC machinesLAC) {
		log.info("<GameDaoImpl--getAvailableGame--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT g.id,g.`name`,g.times,g.prizeNum,g.target,g.integral FROM game AS g");
		sql.append(" WHERE 1 = 1 AND g.startTime <= NOW() AND g.endTime > NOW()");
		sql.append(" AND g.DeleteFlag = 0");
		sql.append(" AND ( CASE ");
		sql.append("     WHEN g.target = 1 AND g.companyId = '"+machinesLAC.getCompanyId()+"' THEN 1");
		sql.append("     WHEN g.target = 2 AND g.areaId = '"+machinesLAC.getAreaId()+"' THEN 1");
		sql.append("     WHEN g.target = 3 AND g.vmCode = '"+machinesLAC.getVmCode()+"' THEN 1");
		sql.append("     ELSE 0 END ) = 1");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GameBean> gameList = new ArrayList<GameBean>();
		GameBean game = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				game = new GameBean();
				game.setId(rs.getInt("id"));
				game.setName(rs.getString("name"));
				game.setTimes(rs.getInt("times"));
				game.setPrizeNum(rs.getInt("prizeNum"));
				game.setTarget(rs.getInt("target"));
				game.setIntegral(rs.getInt("integral"));
				gameList.add(game);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GameDaoImpl--getAvailableGame--end>");
		return gameList;
	}
}

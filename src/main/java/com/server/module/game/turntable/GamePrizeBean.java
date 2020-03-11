package com.server.module.game.turntable;

public class GamePrizeBean {

	//自增标识
	private Long id;
	//奖品名称
	private String name;
	//奖品类型(0:无奖励 1：积分 2：优惠券 3：商品 4:现金)
	private Integer type;
	//奖品权重 概率
	private Integer weight;
	//奖品总数
	private Integer total;
	//奖励数量
	private Integer amount;
	//奖励对应类型id
	private Long rewardId;
	//对应游戏id
	private Integer gameId;
	//默认标识 0：普通 1：默认
	private Integer deleteFlag;
	//删除标识 0：未删除 1：已删除
	private Integer defaultFlag;
	
	
	
	public Integer getDefaultFlag() {
		return defaultFlag;
	}
	public void setDefaultFlag(Integer defaultFlag) {
		this.defaultFlag = defaultFlag;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Long getRewardId() {
		return rewardId;
	}
	public void setRewardId(Long rewardId) {
		this.rewardId = rewardId;
	}
	public Integer getGameId() {
		return gameId;
	}
	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	
}

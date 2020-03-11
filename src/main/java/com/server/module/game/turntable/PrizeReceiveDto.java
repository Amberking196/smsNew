package com.server.module.game.turntable;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PrizeReceiveDto {

	//自增id
	private Long prizeReceiveId;
	// 用户id
	private Long customerId;
	//奖品id
	private Long prizeId;
	//奖品名称
	private String prizeName;
	//奖品类型
	private Integer prizeType;
	//奖品类型名称
	private String prizeTypeName;
	//奖品数量
	private Integer amount;
	//奖品图片
	private String pic ;
	//中奖时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	public String getPrizeTypeName() {
		return prizeTypeName;
	}
	public void setPrizeTypeName(String prizeTypeName) {
		this.prizeTypeName = prizeTypeName;
	}
	public Long getPrizeReceiveId() {
		return prizeReceiveId;
	}
	public void setPrizeReceiveId(Long prizeReceiveId) {
		this.prizeReceiveId = prizeReceiveId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getPrizeId() {
		return prizeId;
	}
	public void setPrizeId(Long prizeId) {
		this.prizeId = prizeId;
	}
	public String getPrizeName() {
		return prizeName;
	}
	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}
	public Integer getPrizeType() {
		return prizeType;
	}
	public void setPrizeType(Integer prizeType) {
		this.prizeType = prizeType;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}

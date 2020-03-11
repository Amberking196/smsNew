package com.server.module.coupon;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MoreGoodsCouponDto {

	private Map<CouponBean,Integer> couponMap;
	private Map<CouponBean,List<Long>> itemMap;
	private Set<CouponBean> noBindItemList;
	
	
	public MoreGoodsCouponDto(){}
	
	public MoreGoodsCouponDto(Map<CouponBean,Integer> couponMap,Map<CouponBean,List<Long>> itemMap,Set<CouponBean> noBindItemList){
		this.couponMap = couponMap;
		this.itemMap = itemMap;
		this.noBindItemList = noBindItemList;
	}
	
	
	
	public Set<CouponBean> getNoBindItemList() {
		return noBindItemList;
	}

	public void setNoBindItemList(Set<CouponBean> noBindItemList) {
		this.noBindItemList = noBindItemList;
	}

	public Map<CouponBean, Integer> getCouponMap() {
		return couponMap;
	}
	public void setCouponMap(Map<CouponBean, Integer> couponMap) {
		this.couponMap = couponMap;
	}
	public Map<CouponBean, List<Long>> getItemMap() {
		return itemMap;
	}
	public void setItemMap(Map<CouponBean, List<Long>> itemMap) {
		this.itemMap = itemMap;
	}
}

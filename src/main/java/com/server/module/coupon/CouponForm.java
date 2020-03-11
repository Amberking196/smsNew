package com.server.module.coupon;

import com.server.util.stateEnum.CouponEnum;

/**
 * 优惠券查询条件
 * @author hebiting
 *
 */
public class CouponForm {

	private Integer companyId;

	private Integer areaId;
	
	private String vmCode;
	
	private Integer way;// 1：购买返券，2：自助领券，3：活动赠券,4:注册赠券,5：可购买优惠券,6:关注赠券,7:会员赠券,8:邀请赠券
	
	private Integer useWhere;// 1 机器优惠劵   2 商城优惠劵
	//是否限制范围
	private boolean limitRange;
	// 2 未使用  3 已使用   4 已过期
	private Integer state;
	public CouponForm(){}
	
	public CouponForm(MachinesLAC machinesLAC,Integer way){
		this.companyId = machinesLAC.getCompanyId();
		this.areaId = machinesLAC.getAreaId();
		this.vmCode = machinesLAC.getVmCode();
		this.way = way;
		this.useWhere = CouponEnum.USE_MACHINES.getState();
		this.limitRange = true;
	}
	
	public CouponForm(MachinesLAC machinesLAC,Integer way,Integer useWhere){
		this.companyId = machinesLAC.getCompanyId();
		this.areaId = machinesLAC.getAreaId();
		this.vmCode = machinesLAC.getVmCode();
		this.way = way;
		this.useWhere = useWhere;
		this.limitRange = true;
	}
	
	
	public Integer getUseWhere() {
		return useWhere;
	}

	public void setUseWhere(Integer useWhere) {
		this.useWhere = useWhere;
	}

	public Integer getWay() {
		return way;
	}

	public void setWay(Integer way) {
		this.way = way;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getVmCode() {
		return vmCode;
	}

	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public boolean getLimitRange() {
		return limitRange;
	}

	public void setLimitRange(boolean limitRange) {
		this.limitRange = limitRange;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
}

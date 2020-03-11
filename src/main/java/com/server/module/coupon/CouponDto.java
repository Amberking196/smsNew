package com.server.module.coupon;

public class CouponDto {

	private Long id;
	private String name;//券名称
	private Integer type;// 优惠券类型    1 满减券  2  固定券 3 折扣券
	private Integer formulaMode; //结算类型 ： 0：按订单结算 1：按桶数结算

	private Integer useWhere;// 1 机器优惠劵   2 商城优惠劵 
	private Integer target;// 机器优惠劵的适用范围  1 公司  2 区域 3 机器
	private Long companyId;
	private String companyName;
	private Long areaId;
	private String areaName;
	private String vmCode;
	private Double money;//满X元  满减券不需要设置
	private Double deductionMoney;//优惠金额(若类型为折扣券，则此处是折扣)
	private String pic;
	private Integer bindProduct;//0 不绑定商品   1 绑定商品
	private Double maximumDiscount;
	private Integer quantity;//coupon_customer中剩余多少张优惠券
	private String startTime;
	private String endTime;
	
	public Double getMaximumDiscount() {
		return maximumDiscount;
	}
	public void setMaximumDiscount(Double maximumDiscount) {
		this.maximumDiscount = maximumDiscount;
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
	public Integer getFormulaMode() {
		return formulaMode;
	}
	public void setFormulaMode(Integer formulaMode) {
		this.formulaMode = formulaMode;
	}
	public Integer getUseWhere() {
		return useWhere;
	}
	public void setUseWhere(Integer useWhere) {
		this.useWhere = useWhere;
	}
	public Integer getTarget() {
		return target;
	}
	public void setTarget(Integer target) {
		this.target = target;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Double getDeductionMoney() {
		return deductionMoney;
	}
	public void setDeductionMoney(Double deductionMoney) {
		this.deductionMoney = deductionMoney;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Integer getBindProduct() {
		return bindProduct;
	}
	public void setBindProduct(Integer bindProduct) {
		this.bindProduct = bindProduct;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}

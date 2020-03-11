package com.server.module.activity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ActivityBean {

	// 主键id
		private Long id;
		// 促销活动名称
		private String name;
		// 促销活动类型 1.固定活动 2.时间段活动
		private Integer type;
		//时间段id
		private String timeQuantumId;
		// 活动开始时间
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date startTime;
		// 活动结束时间
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date endTime;
		// 活动折扣类型 1.固定 2.满减 3.折扣
		private Integer discountType;
		// 订单金额
		private Double money;
		// 扣减金额
		private Double deductionMoney;
		// 促销活动适用范围 1.公司 2.区域 3.机器
		private Integer target;
		// 公司id
		private Long companyId;
		// 区域id
		private Long areaId;
		// 区域名称
		private String areaName;
		// 公司名称
		private String companyName;
		// 售货机编号
		private String vmCode;
		// 是否绑定商品   0 不绑定商品  1 绑定商品
		private Integer bindProduct;
		// 活动应用范围 1 .机器活动 2 .商城活动
		private Integer useWhere;
		// 活动图片
		private String pic;
		// 活动备注
		private String remark;
		// 创建时间
		private Date createTime;
		// 创建用户
		private Long createUser;
		// 更新时间
		private Date updateTime;
		// 更新时间
		private Long updateUser;
		// 是否删除 0 未删除 1 已删除
		private Integer deleteFlag;
		//时间段 
		private String timeFrame;
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
		public String getTimeQuantumId() {
			return timeQuantumId;
		}
		public void setTimeQuantumId(String timeQuantumId) {
			this.timeQuantumId = timeQuantumId;
		}
		public Date getStartTime() {
			return startTime;
		}
		public void setStartTime(Date startTime) {
			this.startTime = startTime;
		}
		public Date getEndTime() {
			return endTime;
		}
		public void setEndTime(Date endTime) {
			this.endTime = endTime;
		}
		public Integer getDiscountType() {
			return discountType;
		}
		public void setDiscountType(Integer discountType) {
			this.discountType = discountType;
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
		public String getCompanyName() {
			return companyName;
		}
		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}
		public String getVmCode() {
			return vmCode;
		}
		public void setVmCode(String vmCode) {
			this.vmCode = vmCode;
		}
		public Integer getBindProduct() {
			return bindProduct;
		}
		public void setBindProduct(Integer bindProduct) {
			this.bindProduct = bindProduct;
		}
		public Integer getUseWhere() {
			return useWhere;
		}
		public void setUseWhere(Integer useWhere) {
			this.useWhere = useWhere;
		}
		public String getPic() {
			return pic;
		}
		public void setPic(String pic) {
			this.pic = pic;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public Long getCreateUser() {
			return createUser;
		}
		public void setCreateUser(Long createUser) {
			this.createUser = createUser;
		}
		public Date getUpdateTime() {
			return updateTime;
		}
		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}
		public Long getUpdateUser() {
			return updateUser;
		}
		public void setUpdateUser(Long updateUser) {
			this.updateUser = updateUser;
		}
		public Integer getDeleteFlag() {
			return deleteFlag;
		}
		public void setDeleteFlag(Integer deleteFlag) {
			this.deleteFlag = deleteFlag;
		}
		public String getTimeFrame() {
			return timeFrame;
		}
		public void setTimeFrame(String timeFrame) {
			this.timeFrame = timeFrame;
		}
		
		@Override
		public boolean equals(Object obj){
			if (this == obj)
				return true;
			if ((obj == null) || (obj.getClass() != this.getClass()))
				return false;
			// object must be Test at this point
			ActivityBean coupon = (ActivityBean) obj;
			return this.id.equals(coupon.getId());
		}
		
		@Override
		public int hashCode(){
			int result = 17;
			result = result+this.id.hashCode()*31;
			return result;
		}
		
}

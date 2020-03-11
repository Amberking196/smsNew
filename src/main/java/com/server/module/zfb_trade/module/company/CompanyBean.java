package com.server.module.zfb_trade.module.company;

import java.util.Date;

public class CompanyBean {

	//自增id
		private Integer id;
		//名称
		private String name;
		//父级id
		private Integer parentId;
		//负责人
		private String principal;
		//手机号
		private String phone;
		//邮箱地址
		private String mail;
		//地区id
		private Integer areaId;
		//状态
		private Integer state;
		//公司简称
		private String shortName;
		//创建时间
		private Date createTime;
		//公司地址
		private String location;
		//公司logo
		private String logoPic;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getParentId() {
			return parentId;
		}
		public void setParentId(Integer parentId) {
			this.parentId = parentId;
		}
		public String getPrincipal() {
			return principal;
		}
		public void setPrincipal(String principal) {
			this.principal = principal;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getMail() {
			return mail;
		}
		public void setMail(String mail) {
			this.mail = mail;
		}
		public Integer getAreaId() {
			return areaId;
		}
		public void setAreaId(Integer areaId) {
			this.areaId = areaId;
		}
		public Integer getState() {
			return state;
		}
		public void setState(Integer state) {
			this.state = state;
		}
		public String getShortName() {
			return shortName;
		}
		public void setShortName(String shortName) {
			this.shortName = shortName;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getLogoPic() {
			return logoPic;
		}
		public void setLogoPic(String logoPic) {
			this.logoPic = logoPic;
		}
		
		
		
}

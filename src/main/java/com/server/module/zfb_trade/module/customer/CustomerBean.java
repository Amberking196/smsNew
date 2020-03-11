package com.server.module.zfb_trade.module.customer;

import java.util.Date;

public class CustomerBean {

		// bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增标识',
		private Long id; 
		// varchar(32) DEFAULT NULL COMMENT 'openId',
		private String openId; 
		// int(2) NOT NULL DEFAULT '0' COMMENT '2-支付宝;1-微信',
		private Integer type; 
		// varchar(32) DEFAULT '' COMMENT '支付宝用户ID',
		private String alipayUserId;
		// varchar(32) DEFAULT NULL COMMENT '用户手机号',
		private String phone;
		// varchar(32) DEFAULT '' COMMENT '昵称',
		private String nickname;
		// varchar(255) DEFAULT '‘’' COMMENT '用户名称',
		private String UserName; 
		// int(1) DEFAULT NULL COMMENT '性别',
		private Integer sexId; 
		// varchar(64) DEFAULT NULL COMMENT '城市',
		private String city; 
		// varchar(64) DEFAULT NULL COMMENT '省份',
		private String province; 
		// varchar(64) DEFAULT NULL COMMENT '国家',
		private String country; 
		// varchar(512) DEFAULT NULL COMMENT '头像',
		private String headImgUrl; 
		// double(6,2) DEFAULT '0.00' COMMENT '纬度',
		private Double latitude; 
		// double(6,2) DEFAULT '0.00' COMMENT '经度',
		private Double longitude;
		// varchar(1) DEFAULT NULL COMMENT '是否实名认证 T是通过  F是没有实名认证 ',
		private String isCertified; 
		// varchar(1) DEFAULT NULL COMMENT '否是学生',
		private String isStudentCertified;
		// varchar(1) DEFAULT NULL COMMENT '用户状态：Q
		private String userStatus; 
		// 代表快速注册用户 T代表已认证用户 B代表被冻结账户 W代表已注册，未激活的账户',varchar(1) DEFAULT NULL COMMENT '1 代表公司账户 2
		private String userType; 
		// timestamp NOT NULL DEFAULT '0000-00-00
		private Date createTime; 
		// varchar(20) DEFAULT '‘’' COMMENT '创建人ID',
		private String createId; 
		// timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间'
		private Date updateTime; 
		// varchar(20) DEFAULT NULL COMMENT '最后更新人',
		private String lastUpdateId; 
		// smallint(1) DEFAULT '0' COMMENT '删除标识: 0 正常，1  已删除',
		private Byte deleteFlag;
		//机器编号
		private String vmCode;
		
		private Long inviterId;
		
		private int isLoginUser;
		
		public int getIsLoginUser() {
			return isLoginUser;
		}
		public void setIsLoginUser(int isLoginUser) {
			this.isLoginUser = isLoginUser;
		}
		public Long getInviterId() {
			return inviterId;
		}
		public void setInviterId(Long inviterId) {
			this.inviterId = inviterId;
		}
		public String getVmCode() {
			return vmCode;
		}
		public void setVmCode(String vmCode) {
			this.vmCode = vmCode;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getOpenId() {
			return openId;
		}
		public void setOpenId(String openId) {
			this.openId = openId;
		}
		public Integer getType() {
			return type;
		}
		public void setType(Integer type) {
			this.type = type;
		}
		public String getAlipayUserId() {
			return alipayUserId;
		}
		public void setAlipayUserId(String alipayUserId) {
			this.alipayUserId = alipayUserId;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getNickname() {
			return nickname;
		}
		public void setNickname(String nickname) {
			this.nickname = nickname;
		}
		public String getUserName() {
			return UserName;
		}
		public void setUserName(String userName) {
			UserName = userName;
		}
		public Integer getSexId() {
			return sexId;
		}
		public void setSexId(Integer sexId) {
			this.sexId = sexId;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public String getHeadImgUrl() {
			return headImgUrl;
		}
		public void setHeadImgUrl(String headImgUrl) {
			this.headImgUrl = headImgUrl;
		}
		public Double getLatitude() {
			return latitude;
		}
		public void setLatitude(Double latitude) {
			this.latitude = latitude;
		}
		public Double getLongitude() {
			return longitude;
		}
		public void setLongitude(Double longitude) {
			this.longitude = longitude;
		}
		public String getIsCertified() {
			return isCertified;
		}
		public void setIsCertified(String isCertified) {
			this.isCertified = isCertified;
		}
		public String getIsStudentCertified() {
			return isStudentCertified;
		}
		public void setIsStudentCertified(String isStudentCertified) {
			this.isStudentCertified = isStudentCertified;
		}
		public String getUserStatus() {
			return userStatus;
		}
		public void setUserStatus(String userStatus) {
			this.userStatus = userStatus;
		}
		public String getUserType() {
			return userType;
		}
		public void setUserType(String userType) {
			this.userType = userType;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public String getCreateId() {
			return createId;
		}
		public void setCreateId(String createId) {
			this.createId = createId;
		}
		public Date getUpdateTime() {
			return updateTime;
		}
		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}
		public String getLastUpdateId() {
			return lastUpdateId;
		}
		public void setLastUpdateId(String lastUpdateId) {
			this.lastUpdateId = lastUpdateId;
		}
		public Byte getDeleteFlag() {
			return deleteFlag;
		}
		public void setDeleteFlag(Byte deleteFlag) {
			this.deleteFlag = deleteFlag;
		} 
		
		
}

package com.server.module.trade.customer;

import java.math.BigDecimal;
import java.util.Date;
import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

/**
 * table name: tbl_customer author name: yjr create time: 2018-04-16 15:06:05
 */
@Data
@Entity(tableName = "tbl_customer", id = "id", idGenerate = "auto")
public class TblCustomerBean {

	// @JsonIgnore public String tableName="tbl_customer";
	// @JsonIgnore public String selectSql="select * from tbl_customer where 1=1 ";
	// @JsonIgnore public String selectSql1="select
	// id,openId,type,alipayUserId,phone,nickname,UserName,sexId,city,province,country,headImgUrl,latitude,longitude,isCertified,isStudentCertified,userStatus,userType,createTime,createId,updateTime,lastUpdateId,deleteFlag
	// from tbl_customer where 1=1 ";
	private Long id;
	private String openId;
	private String huafaAppOpenId;
	private Integer type;
	private String alipayUserId;
	private String phone;
	private String nickname;
	private String userName;
	private Integer sexId;
	private String city;
	private String province;
	private String country;
	private String headImgUrl;
	private Double latitude;
	private Double longitude;
	private String isCertified;
	private String isStudentCertified;
	private String userStatus;
	private String userType;
	private Date createTime;
	private String createId;
	private String vmCode;// 注册的机器编号
	private Date updateTime;
	private String lastUpdateId;
	private Integer deleteFlag;

	private Long inviterId;
	private  Integer integral;
	// 会员开始时间
	public Date startTime;
	// 会员结束时间
	public Date endTime;
	// 用户余额
	private BigDecimal userBalance;
	// 是否是会员 0非会员 1会员
	public Integer isMember;
	// 会员类型id
	public Long memberTypeId;

	private Integer isSend;//0未发送关注券  1已发送关注券
	private Integer follow;//0未关注公众号  1已关注公众号
	
	private Integer isLoginUser;
	
	@NotField
	private String appOpenId;//app的openid
	
	@NotField
	private Integer companyId;
	
	@NotField
	private String unionId;
	
	
}

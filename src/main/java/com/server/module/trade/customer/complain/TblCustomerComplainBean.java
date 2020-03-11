package com.server.module.trade.customer.complain;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import com.server.module.trade.customer.complainReply.TblCustomerComplainReplyBean;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * table name: customer_message author name: why create time: 2018-08-17
 * 08:48:16
 */
@Data
@Entity(tableName = "tbl_customer_complain", id = "id", idGenerate = "auto")
public class TblCustomerComplainBean {

	//主键ID
	private Long id;
	//售货机编号
	private String vmCode;
	//用户电话
	private String phone;
	//投诉类型
	private Integer type;
	//投诉内容
	private String content;
	//图片名称
	private String picName;
	//投诉状态
	private Integer state;
	//创建时间
	private Date createTime;
	//创建用户
	private Long createUser;
	//更新时间
	private Date updateTime;
	//更新用户
	private Long updateUser;
	//是否删除 0 未删除  1 已删除
	private Integer deleteFlag;
	//用户id
	private Long customerId;

	@NotField
	private String  typeLabel;
	@NotField
	private Integer  num;
	@NotField
	private String createTimes;
	@NotField
	private String stateLabel;
	@NotField
	private  String  nickName;
	@NotField
	private List<TblCustomerComplainReplyBean> list;

	public String getTypeLabel() {
		if(type==1){
			typeLabel="扣错款";
		}
		if(type==2){
			typeLabel="未扣款";
		}
		if(type==3){
			typeLabel="门打不开";
		}
		if(type==4){
			typeLabel="没有商品";
		}
		if(type==5){
			typeLabel="货物过期";
		}
		if(type==6){
			typeLabel="货物破损";
		}
		if(type==7){
			typeLabel="其他";
		}
		if(type==8){
			typeLabel="无法获取手机验证码";
		}
		return typeLabel;
	}

	public String getStateLabel() {
		if(state==0){
			stateLabel="未回复";
		}
		if(state==1){
			stateLabel="已回复";
		}
		return stateLabel;
	}
	

}

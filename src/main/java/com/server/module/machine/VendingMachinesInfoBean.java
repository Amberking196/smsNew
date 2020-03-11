package com.server.module.machine;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import lombok.Data;

import java.util.Date;

/**
 * table name:  vending_machines_info
 * author name: yjr
 * create time: 2018-03-20 15:19:26
 */ 
@Data
@Entity(tableName="vending_machines_info",id="code",idGenerate="assign")
public class VendingMachinesInfoBean {


	
	//售货机编号
	private String code;
	//公司标识
	private Integer companyId;
	//状态
	private Integer state;
	//机器标识
	private Integer machinesBaseId;
	//经度
	private Double lon;
	//纬度
	private Double lat;
	//禁用货道号
	private String banWayNumber;
	//售货机名称
	private String name;
	//地址名称
	private String locatoinName;
	//销售商品类型
	private String itemType;
	//定位时间
	private Date locatoinDate;
	//责任人ID
	private Long respManId;
	//联系人
	private String linkman;
	//错误警告
	private Integer errorWarn;
	//是否在H5显示
	private Integer isShowH5;
	//二维码 url
	private String url;
	//区域Id
	private Integer areaId;
	//线路Id
	private Integer lineId; 
	//二维码
	private String qrCode;
	
	private Integer baseId;
	//更新时间
	private Date updateTime;
	//创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") 
	private Date createTime;

	// 备注
	private String untieRemark;

	private Long district;//区域
	private String districtName;
	
	//货道数
	@NotField
	private Integer wayCount;

	private Integer machineVersion;//机器版本  1 支持 一个货道一种商品 2 多种商品
	//补货公司id
	@NotField
	private Integer replenishCompanyId;
	
	@NotField
	private String stateName;

	
}


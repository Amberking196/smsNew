package com.server.module.carryWaterVouchersManage.carryWaterVouchers;

import java.math.BigDecimal;
import java.util.Date;

import org.joda.time.DateTime;
import jdk.nashorn.internal.ir.annotations.Ignore;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

/**
 * table name: carry_water_vouchers author name: why create time: 2018-11-03
 * 09:02:25
 */
@Data
@Entity(tableName = "carry_water_vouchers", id = "id", idGenerate = "auto")
public class CarryWaterVouchersBean {

	// 主键id
	private Long id;
	// 提水券名称
	private String name;
	// 提水劵的适用范围 1 公司 2 区域 3 机器
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
	// 是否绑定商品 0 不绑定商品 1 绑定商品
	private Integer bindProduct;
	// 返还提水券数量
	private Integer sendMax;
	// 有效时间类型：0 绝对时间 1 相对时间
	private Integer periodType;
	// 有效天数 当periodType=1 时 该字段有意义，优惠的有效时间从领取当天now 到now+periodDay 为有效期
	// 注:用户的所属优惠劵有效期统一保存到coupon_customer 的 startTime,endTime ，判断有效期统一从这里取数
	private Integer periodDay;
	// 开始时间
	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	// 结束时间
	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	// 创建时间
	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	// 创建用户
	private Long createUser;
	// 更新时间
	private Date updateTime;
	// 更新用户
	private Long updateUser;
	// 是否删除 0删除 1已删除
	private Integer deleteFlag;
	// 提水券图片
	private String pic;
	// 备注
	private String remark;

	@NotField
	private String targetLabel;
	@NotField
	private String validState;
	@NotField
	private Long shoppingGoodsId;
	// 库存
	@NotField
	private Long quantity;
	@NotField
	private BigDecimal costPrice;
	@NotField
	private BigDecimal salesPrice;
	@NotField
	private String number;
	// 销售状态 5100 正常销售 5101 暂停销售
	@NotField
	private Long state;

	@NotField
	private String stateName;
	@NotField
	private Date logicEndTime;
	@NotField
	private Date logicStartTime;

	// 商城显示位置 0 首页商品 1 广告商品'
	@NotField
	private Integer isHomeShow;
	@NotField
	private String isHomeShowLabel;

	public String getStateName() {
		if (5100 == state) {
			stateName = "正常销售";
		}
		if (5101 == state) {
			stateName = "暂停销售";
		}
		return stateName;
	}

	public String getIsHomeShowLabel() {
		if (0 == isHomeShow) {
			isHomeShowLabel = "首页";
		}
		if (1 == isHomeShow) {
			isHomeShowLabel = "广告";
		}
		return isHomeShowLabel;
	}

	public String getTargetLabel() {
		// 1 公司 2 区域 3 机器
		if (target == 0)
			targetLabel = "";
		if (target == 1)
			targetLabel = "公司";
		if (target == 2)
			targetLabel = "区域";
		if (target == 3)
			targetLabel = "机器";
		return targetLabel;
	}

	public String getValidState() {
		Long now = System.currentTimeMillis();
		if (now < this.startTime.getTime()) {
			return "未开始";
		}
		if (now > this.endTime.getTime()) {
			return "已结束";
		}
		if (now > this.startTime.getTime() && now < this.endTime.getTime()) {
			return "已开始";
		}
		return "";

	}
	
	
	@Ignore
	public Date getLogicEndTime(){
		if(this.getPeriodType()==1){//非绝对时间
			 int day=this.getPeriodDay();
			DateTime dt2 = new DateTime(System.currentTimeMillis());
            dt2=dt2.plusDays(day);
            dt2=dt2.withHourOfDay(23);
            dt2=dt2.withMinuteOfHour(59);
            dt2=dt2.withSecondOfMinute(59);
            return dt2.toDate();
		}else{
			return this.endTime;
		}
	}
	@Ignore
	public Date getLogicStartTime(){
		if(this.getPeriodType()==1){//非绝对时间
			DateTime dt2 = new DateTime(System.currentTimeMillis());
			dt2=dt2.withHourOfDay(0);
			dt2=dt2.withMinuteOfHour(0);
			dt2=dt2.withSecondOfMinute(0);
			return dt2.toDate();
		}else{
			return this.startTime;
		}
	}

}

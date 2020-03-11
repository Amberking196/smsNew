package com.server.module.trade.web;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.server.company.MachinesService;
import com.server.module.activeDegree.UserActiveDegreeService;
import com.server.module.footmark.FootmarkBean;
import com.server.module.footmark.FootmarkService;
import com.server.module.member.MemberService;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.config.pay.MyWXPayConfig;
import com.server.module.trade.config.pay.WxPayConfigFactory;
import com.server.module.trade.customer.TblCustomerService;
import com.server.module.trade.order.OrderService;
import com.server.module.trade.order.ReviseRedisValue;
import com.server.module.trade.util.ResultBean;
import com.server.module.trade.util.UserUtil;
import com.server.module.trade.vendingMachinesWay.VendingMachinesWayServiceImpl;
import com.server.module.trade.vendingMachinesWayItem.MoreGoodsWayDto;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemService;
import com.server.module.trade.vendingMachinesWayItem.WayValidatorDto;
import com.server.module.trade.web.form.ReviseForm;
import com.server.module.zfb_trade.module.logininfo.AliLoginInfoService;
import com.server.module.zfb_trade.module.logininfo.LoginInfoBean;
import com.server.module.zfb_trade.module.vmway.AliVmwayService;
import com.server.module.zfb_trade.util.enumparam.DoorStateEnum;
import com.server.redis.RedisService;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CommandVersionEnum;
import com.server.util.stateEnum.ModeEnum;
import com.server.util.stateEnum.OpenStateEnum;
import com.server.util.stateEnum.RegisterFlowEnum;
/**
 * 现已废弃，用户开门接口已转换为：CusOpenController，其他补货接口已放置在mms项目中
 * @author hebiting
 *
 */
@Controller
@RequestMapping(value = "/index")
public class IndexController {
	public static Logger log = LogManager.getLogger( IndexController.class);   
	@Autowired
	private OrderService orderServiceImpl;
	@Autowired
	private RedisService redisService;
	@Autowired
	private VendingMachinesWayServiceImpl vendingMachinesWayServiceImpl;
	@Autowired
	@Qualifier("aliVmwayService")
	private AliVmwayService vmwayService;
	@Autowired
	private MachinesService machinesService;
	@Autowired
	private WxPayConfigFactory wxpayConfigFactory;
	@Autowired
	private TblCustomerService tblCustomerService;
	@Autowired
	private AliLoginInfoService loginInfoService;
	@Autowired
	private VmwayItemService vmwayItemService;
	@Autowired
	private FootmarkService footmarkService;
	@Autowired
	private UserActiveDegreeService userActiveDegreeService;
	@Autowired
	private MemberService memberService;
    /**
     * 货道列表
     * @param vmCode
     * @type 1 客户  2  运维
     * @return
     */
	@RequestMapping("/listWay")
	@ResponseBody
	public ResultBean<?> listWay(String vmCode,int type) {
		Integer version = orderServiceImpl.getVersionByVmCode(vmCode);
		if(CommandVersionEnum.VER2.getState().equals(version)){
			if(type == 1){
				List<MoreGoodsWayDto> wayInfo = vmwayItemService.getWayInfo(vmCode,type);
				return new ResultBean<List<MoreGoodsWayDto>>(wayInfo);
			}else if(type ==2){
				Map<String,Object> map=new  HashMap<String,Object>();
				List<MoreGoodsWayDto> wayInfo = vmwayItemService.getWayInfo(vmCode,type);
				String findTemperature = vendingMachinesWayServiceImpl.findTemperature(vmCode);
				map.put("wayInfo", wayInfo);
				map.put("temperature", findTemperature);
				return new ResultBean<Map<String,Object>>(map);
			}
		}else{
			if(type==1) {
				List<MoreGoodsWayDto> wayInfo = vendingMachinesWayServiceImpl.getWayInfo(vmCode,type);
				return new ResultBean<List<MoreGoodsWayDto>>(wayInfo);
			}else if(type==2) {
				Map<String,Object> map=new  HashMap<String,Object>();
				List<MoreGoodsWayDto> wayInfo = vendingMachinesWayServiceImpl.getWayInfo(vmCode,type);
				String findTemperature = vendingMachinesWayServiceImpl.findTemperature(vmCode);
				map.put("wayInfo", wayInfo);
				map.put("temperature", findTemperature);
				return new ResultBean<Map<String,Object>>(map);
			}
		}
		return null;
	}
    /**
     * 更新音量
     * @param vmCode 
     * @param volume
     * @return
     */
	@RequestMapping("/updateVolume")
	@ResponseBody
	public ResultBean<?> updateVolume(String vmCode,String volume) {
		ResultBean<?> result = new ResultBean<>();
		// 检查是否登录
		UserVo userVo = UserUtil.getUser();
		// 检查改用户是否有购买资格
		if (userVo == null) {
			result.setCode(ResultBean.NO_LOGIN);
			result.setMsg("未登录");
			return result;
		}
		if(userVo.getType()!=UserVo.USER_ADMIN){
			result.setCode(ResultBean.NO_LOGIN);
			result.setMsg("你不是运维人员,不能操作");
			return result;
		}
		Boolean updateVolume=vendingMachinesWayServiceImpl.updateVolume(vmCode,volume);
		if(updateVolume == true) {
			result.setCode(ResultBean.SUCCESS);
			result.setMsg("修改成功");
		}
		else {
			result.setCode(ResultBean.UPDATE_VOLUME_FAIL);
			result.setMsg("修改失败");
		}
		return result;
	}
    /**
     * 开门接口  买家
     * @param vmCode
     * @param way
     * @return
     */
	@RequestMapping("/openDoor")
	@ResponseBody
	public ResultBean<?> openDoor(String vmCode, Integer way) {
		ResultBean<?> result = new ResultBean<>();
		// 检查是否登录
		UserVo userVo = UserUtil.getUser();
		//userId=1000l;
		// 检查改用户是否有购买资格
		if (userVo == null) {
			result.setCode(ResultBean.NO_LOGIN);
			result.setMsg("未登录");
			return result;
		}
		//判断是否签约
		Integer companyId = machinesService.getCompanyIdByVmCode(vmCode);
		MyWXPayConfig wxPayConfig = (MyWXPayConfig)wxpayConfigFactory.getWXPayConfig(companyId);
		Integer usingCompanyConfig = wxPayConfig.getUsingCompanyConfig();
		boolean b=orderServiceImpl.isEntrust(usingCompanyConfig,userVo.getOpenId());
		Integer version = orderServiceImpl.getVersionByVmCode(vmCode);
		WayValidatorDto validator = null;
		if(CommandVersionEnum.VER2.getState().equals(version)){
			validator = vmwayItemService.getOneWayInfo(vmCode, way);
		}else{
			validator = vmwayService.getOneWayInfo(vmCode, way);
		}
		if (validator == null) {
			result.setCode(ResultBean.ITEM_ABNORMAL);
			result.setMsg("商品不存在");
			return result;
		}
		if(!b){//还没签约
			BigDecimal memberMoney = memberService.getMemberMoney(userVo.getId());
			if(memberMoney.compareTo(validator.getMinUnitPrice())<0){
				result.setCode(ResultBean.NO_ENTRUST);
				result.setMsg("未签约免密代扣");
				return result;
			}
		}
		//检查是否有未支付订单
		b=orderServiceImpl.isHaveNotPayOrder(userVo.getId());
		if(b){
			result.setCode(ResultBean.ORDER_NOT_PAY);
			result.setMsg("有未支付订单,请先支付");
			return result;
		}
		if (DoorStateEnum.UNAVAILABLE.getCode().equals(validator.getState())) {
			result.setCode(ResultBean.ITEM_ABNORMAL);
			result.setMsg("商品已停止售卖，请选择其他商品");
			return result;
		} else if (!(validator.getNum() > 0)) {
			result.setCode(ResultBean.ITEM_ABNORMAL);
			result.setMsg("商品已售罄，请选择其他商品");
			return result;
		}
		tblCustomerService.updateCusVmCode(userVo.getId(), vmCode);
		Map<String, Object> baseInfo = orderServiceImpl.getBaseInfoByVmCode(vmCode);
		String openFlag = orderServiceImpl.sendCommandToOpenDoor1(baseInfo , vmCode, way);
		if(OpenStateEnum.NEW_OPEN_SUCCESS.getState().equals(openFlag)){
			redisService.del("orderReuslt_"+vmCode);
			result.setCode(ResultBean.NEW_OPEN_SUCCESS);
			userActiveDegreeService.update(userVo.getId(),RegisterFlowEnum.OPEN_MACHINES);
			result.setMsg("开门成功");
			return result;
		}else if(OpenStateEnum.REOPEN_FAIL.getState().equals(openFlag)){
        	result.setCode(ResultBean.Machines_Opened);
			result.setMsg("当前有用户正在使用,请稍后");
			return result;
		}else if(OpenStateEnum.OPEN_SUCCESS.getState().equals(openFlag)){
			redisService.del("orderReuslt_"+vmCode);
			userActiveDegreeService.update(userVo.getId(),RegisterFlowEnum.OPEN_MACHINES);
			result.setMsg("开门成功");
			return result;
		}else if(OpenStateEnum.MACHINES_BUSY.getState().equals(openFlag)){
			result.setCode(ResultBean.MACHINES_BUSY);
			result.setMsg(OpenStateEnum.MACHINES_BUSY.getName());
			return result;
		}else if (("-1").equals(openFlag)){
			result.setCode(ResultBean.ITEM_ABNORMAL);
			result.setMsg("商品已停止售卖，请选择其他商品");
			return result;
		}else {
			result.setCode(ResultBean.FAIL);
			result.setMsg("开门失败");
			return result;
		}
	}
	
	
	
	
	
}

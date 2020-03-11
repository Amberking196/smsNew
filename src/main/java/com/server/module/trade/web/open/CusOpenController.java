package com.server.module.trade.web.open;

import java.math.BigDecimal;
import java.util.List;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.server.company.MachinesService;
import com.server.module.member.MemberService;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.config.pay.MyWXPayConfig;
import com.server.module.trade.config.pay.WxPayConfigFactory;
import com.server.module.trade.customer.TblCustomerService;
import com.server.module.trade.order.OrderService;
import com.server.module.trade.util.UserUtil;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemService;
import com.server.module.trade.vendingMachinesWayItem.WayValidatorDto;
import com.server.module.zfb_trade.module.vmbase.AliVendingMachinesService;
import com.server.module.zfb_trade.module.vmbase.VmbaseInfoDto;
import com.server.module.zfb_trade.module.vmway.AliVmwayService;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.module.zfb_trade.util.enumparam.DoorStateEnum;
import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.CommandVersionEnum;

@RestController
@RequestMapping("/cusOpen")
public class CusOpenController {
	
	@Autowired
	private MachinesService machinesService;
	@Autowired
	private WxPayConfigFactory wxpayConfigFactory;
	@Autowired
	private OrderService orderServiceImpl;
	@Autowired
	private VmwayItemService vmwayItemService;
	@Autowired
	@Qualifier("aliVmwayService")
	private AliVmwayService vmwayService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private TblCustomerService tblCustomerService;
	@Autowired
	private CusOpenService cusOpenService;
	@Autowired
	@Qualifier("aliVendingMachinesService")
	private AliVendingMachinesService vendingMachinesService;

	/**
	 * 开门接口
	 * @author hebiting
	 * @date 2019年1月30日下午2:20:23
	 * @param vmCode
	 * @param way
	 * @return
	 */
	@PostMapping("/do")
	public ReturnDataUtil openDoor(String vmCode, Integer way){
		// 检查是否登录
		UserVo userVo = UserUtil.getUser();
		// 检查改用户是否有购买资格
		if (userVo == null) {
			return ResultUtil.error(ResultEnum.NOT_LOGIN);
		}

		VmbaseInfoDto baseInfo = vendingMachinesService.getBaseInfo(vmCode);
		boolean isFreeMachine = false;
		if(baseInfo.getFreeMachine()==1){
			isFreeMachine=true;
		}
		//判断是否签约
		Integer companyId = machinesService.getCompanyIdByVmCode(vmCode);

		MyWXPayConfig wxPayConfig = (MyWXPayConfig)wxpayConfigFactory.getWXPayConfig(companyId);

		boolean b=orderServiceImpl.isEntrust(wxPayConfig.getUsingCompanyConfig(),userVo.getOpenId());


		WayValidatorDto validator = null;
		if(CommandVersionEnum.VER2.getState().equals(baseInfo.getMachinesVersion())){
			validator = vmwayItemService.getOneWayInfo(vmCode, way);
		}else{
			validator = vmwayService.getOneWayInfo(vmCode, way);
		}
		if (validator == null) {
			return ResultUtil.error(ResultEnum.ITEM_NOT_EXIST);
		}
		if(!b && !isFreeMachine){//还没签约
			BigDecimal memberMoney = memberService.getMemberMoney(userVo.getId());
			if(memberMoney.compareTo(validator.getMinUnitPrice())<0){
				return ResultUtil.error(ResultEnum.NOT_ENTRUST_AND_NOT_RECHARGE);
			}
		}
		//检查是否有未支付订单
		if(orderServiceImpl.isHaveNotPayOrder(userVo.getId()) && !isFreeMachine){
			return ResultUtil.error(ResultEnum.ORDER_NOT_PAY);
		}
		if (DoorStateEnum.UNAVAILABLE.getCode().equals(validator.getState())) {
			return ResultUtil.error(ResultEnum.DOOR_NOT_AVAILABLE);
		} else if (!(validator.getNum() > 0) && !isFreeMachine) {
			return ResultUtil.error(ResultEnum.ITEM_SOLD_OUT);
		}
		tblCustomerService.updateCusVmCode(userVo.getId(), vmCode);
		return cusOpenService.openDoor(baseInfo,vmCode, way, userVo,validator.getMinStandard());
	}
	/**
	 * 判断机器是否开门成功
	 * @author hebiting
	 * @date 2019年1月30日下午2:20:11
	 * @param vmCode
	 * @return
	 */
	@PostMapping("/isOpened")
	public ReturnDataUtil isOpened(String vmCode){
		return cusOpenService.isOpened(vmCode);
	}

}

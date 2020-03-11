package com.server.module.trade.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.command.StandardCommandCreator;
import com.server.common.bean.KeyValueBean;
import com.server.company.MachinesService;
import com.server.module.activity.ActivityBean;
import com.server.module.activity.ActivityService;
import com.server.module.commonBean.ItemInfoDto;
import com.server.module.coupon.CouponService;
import com.server.module.coupon.MachinesLAC;
import com.server.module.itemBasic.ItemBasicService;
import com.server.module.member.MemberService;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.config.pay.MyWXPayConfig;
import com.server.module.trade.config.pay.WxPayConfigFactory;
import com.server.module.trade.order.OrderService;
import com.server.module.trade.order.calculate.activity.chooce.ActivityChooce;
import com.server.module.trade.util.UserUtil;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemService;
import com.server.module.zfb_trade.module.vmway.AliVmwayService;
import com.server.module.zfb_trade.paramconfig.H5URLConfig;
import com.server.module.zfb_trade.service.AlipayService;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CommandVersionEnum;

@RestController
@RequestMapping("/openBefore")
public class OpenBeforeController {

	public static Logger log = LogManager.getLogger(OpenBeforeController.class);
	@Autowired
	private OrderService orderServiceImpl;
	@Autowired
	private WxPayConfigFactory wxpayConfigFactory;
	@Autowired
	private MachinesService machinesService;
	@Autowired
	private VmwayItemService vmwayItemService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private CouponService couponService;
	@Autowired
	private ActivityChooce activityChooce;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AliVmwayService vmwayService;
	@Autowired
	private ItemBasicService itemBasicService;
	@Autowired
	private AlipayService alipayService;
	@Autowired
	private H5URLConfig h5URLConfig;
	@Autowired
	private StandardCommandCreator standardCreator;
	//只是一个很大很大的数字
	private BigDecimal flag = new BigDecimal(100000000);

	@PostMapping("/canDo")
	public ReturnDataUtil before(String vmCode, Integer wayNum) {
		UserVo user = UserUtil.getUser();
		if (user == null) {
			return ResultUtil.error(ResultEnum.NOT_LOGIN);
		}
		boolean isEntrust = false;
		if(user.getPayType() == UserVo.Pay_Type_WeiXin){
			Integer companyId = machinesService.getCompanyIdByVmCode(vmCode);
			MyWXPayConfig wxPayConfig = (MyWXPayConfig) wxpayConfigFactory.getWXPayConfig(companyId);
			Integer usingCompanyConfig = wxPayConfig.getUsingCompanyConfig();
			isEntrust = orderServiceImpl.isEntrust(usingCompanyConfig, user.getOpenId());
		}else if(user.getPayType() == UserVo.Pay_Type_ZhiFuBao){
			String agreementNo = alipayService.querySign(user.getOpenId(),vmCode);
			// 未签约跳转签约界面
			if (StringUtil.isBlank(agreementNo)) {
				String data = h5URLConfig.signUrl.concat("?vmCode=").concat(vmCode);
				return ResultUtil.error(ResultEnum.FORWARD_SIGN_PAGE, data, true);
			}else {
				isEntrust = true;
			}
		}
		Integer version = orderService.getVersionByVmCode(vmCode);
		List<ItemInfoDto> itemList = null;
		if (CommandVersionEnum.VER1.getState().equals(version)) {
			itemList = vmwayService.getOneWayItemInfo(vmCode, wayNum);
		} else if (CommandVersionEnum.VER2.getState().equals(version)) {
			itemList = vmwayItemService.getOneWayItmeInfo(vmCode, wayNum);
		}
		if (itemList == null || itemList.size() == 0) {
			return ResultUtil.error(ResultEnum.SELECT_NOT_DATA);
		}
		BigDecimal minUnitPrice = BigDecimal.ZERO;
		StringBuffer basicItemIds = new StringBuffer();
		for (int i = 0; i < itemList.size(); i++) {
			basicItemIds.append(itemList.get(i).getBasicItemId());
			if (minUnitPrice.equals(BigDecimal.ZERO) || itemList.get(i).getUnitPrice().compareTo(minUnitPrice) < 0) {
				minUnitPrice = itemList.get(i).getUnitPrice();
			}
			if (i != itemList.size() - 1) {
				basicItemIds.append(",");
			}
		}
		// 判断用户是否签约，若未签约是否充值金额大于该商品金额
		if (!isEntrust) {
			BigDecimal memberMoney = memberService.getMemberMoney(user.getId());
			if(memberMoney == null){
				return ResultUtil.error(ResultEnum.NOT_LOGIN);
			}
			if (memberMoney.compareTo(minUnitPrice) < 0) {
				// 未签约且优水余额不足，须签约或者充值
				return ResultUtil.error(ResultEnum.NOT_ENTRUST_AND_NOT_RECHARGE);
			}
		}
		// 查看该货道商品是否有参与活动，给用户提示信息
		MachinesLAC machinesLAC = couponService.getMachinesLAC(vmCode);
		Map<ActivityBean, List<Long>> moreGoodsActivity = activityService.getMoreGoodsActivity(machinesLAC,
				flag, basicItemIds.toString());
		// 选取最小区域可参与活动
		ActivityBean minAreaActivity = null;
		if (moreGoodsActivity != null && moreGoodsActivity.size() > 0) {
			minAreaActivity = activityChooce.chooce(new ArrayList<ActivityBean>(moreGoodsActivity.keySet()));
		}
		if (minAreaActivity != null) {
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("activityName", minAreaActivity.getRemark());
			List<Long> basicItemList = moreGoodsActivity.get(minAreaActivity);
			if (basicItemList == null) {
				result.put("isAll", 1);
				return ResultUtil.success(result);
			} else if (basicItemList.size() > 0) {
				String allTakepart = StringUtils.join(basicItemList,",");
				List<KeyValueBean<String,String>> partakeItemList = itemBasicService.getItemByIds(allTakepart);
				result.put("isAll", 0);
				result.put("partakeItemList", partakeItemList);
				return ResultUtil.success(result);
			}
		}
		//发送规格命令
//		String factoryNum = orderService.getFactoryNumberByVmCode(vmCode);
//		standardCreator.sendStandard(factoryNum,version, user, null);
		return ResultUtil.success();
	}
}

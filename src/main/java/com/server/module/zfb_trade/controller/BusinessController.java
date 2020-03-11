package com.server.module.zfb_trade.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayOpenPublicUserFollowQueryRequest;
import com.alipay.api.response.AlipayOpenPublicUserFollowQueryResponse;
import com.server.module.activeDegree.RegisterFlowBean;
import com.server.module.activeDegree.UserActiveDegreeService;
import com.server.module.coupon.CouponBean;
import com.server.module.coupon.CouponCustomerBean;
import com.server.module.coupon.CouponForm;
import com.server.module.coupon.CouponService;
import com.server.module.coupon.MachinesLAC;
import com.server.module.payRecord.AliPayRecordService;
import com.server.module.payRecord.PayRecordDto;
import com.server.module.payRecord.PayRecordForm;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.auth.util.JwtTokenUtil;
import com.server.module.trade.order.OrderService;
import com.server.module.trade.util.UserUtil;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemService;
import com.server.module.trade.vendingMachinesWayItem.WayValidatorDto;
import com.server.module.trade.web.open.CusOpenService;
import com.server.module.zfb_trade.bean.AdminConstant;
import com.server.module.zfb_trade.bean.OpenDoorBean;
import com.server.module.zfb_trade.convert.AliCustomerConvert;
import com.server.module.zfb_trade.convert.PayRecordConvert;
import com.server.module.zfb_trade.factory.AlipayAPIClientFactory;
import com.server.module.zfb_trade.module.creditwithheld.AliCreditWithheldService;
import com.server.module.zfb_trade.module.customer.AliCustomerService;
import com.server.module.zfb_trade.module.customer.CustomerBean;
import com.server.module.zfb_trade.module.dooroperation.AliDoorOperationService;
import com.server.module.zfb_trade.module.logininfo.AliLoginInfoService;
import com.server.module.zfb_trade.module.vmbase.AliVendingMachinesService;
import com.server.module.zfb_trade.module.vmbase.VmbaseInfoDto;
import com.server.module.zfb_trade.module.vmitem.AliVmitemService;
import com.server.module.zfb_trade.module.vmway.AliVmwayService;
import com.server.module.zfb_trade.paramconfig.H5URLConfig;
import com.server.module.zfb_trade.service.AliSmsService;
import com.server.module.zfb_trade.service.AlipayService;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.module.zfb_trade.util.enumparam.DoorStateEnum;
import com.server.module.zfb_trade.util.enumparam.OpenTypeEnum;
import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.redis.MachinesKey;
import com.server.redis.RedisService;
import com.server.redis.SmsKey;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CommandVersionEnum;
import com.server.util.stateEnum.CouponEnum;
import com.server.util.stateEnum.OpenStateEnum;
import com.server.util.stateEnum.PayStateEnum;
import com.server.util.stateEnum.RegisterFlowEnum;

@RestController
@RequestMapping("/aliBusiness")
public class BusinessController {

	public static Logger log = LogManager.getLogger(BusinessController.class); 
	@Autowired
	@Qualifier("aliVendingMachinesService")
	private AliVendingMachinesService vendingMachinesService;
	@Autowired
	private AlipayService alipayService;
	@Autowired
	private H5URLConfig h5URLConfig;
	@Autowired
	@Qualifier("aliPayRecordService")
	private AliPayRecordService payRecordService;
	@Autowired
	@Qualifier("aliVmwayService")
	private AliVmwayService vmwayService;
	@Autowired
	@Qualifier("aliVmitemService")
	private AliVmitemService vmitemService;
	@Autowired
	private RedisService redisService;
	@Autowired
	@Qualifier("aliDoorOperationService")
	private AliDoorOperationService doorOperationService;
	@Autowired
	@Qualifier("aliLoginInfoService")
	private AliLoginInfoService loginInfoService;
	@Autowired
	private AliSmsService smsService;
	@Autowired
	@Qualifier("aliCustomerService")
	private AliCustomerService customerService;
	@Autowired
	private OrderService orderService;
	@Autowired
	@Qualifier("aliCreditWithheldService")
	private AliCreditWithheldService creditWithheldService;
	@Autowired
	private AlipayAPIClientFactory alipayClientFactory;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private CouponService couponService;
	@Autowired
	private OrderService orderServiceImpl;
	@Autowired
	private VmwayItemService vmwayItemService;
	@Autowired
	private UserActiveDegreeService userActiveDegreeService;
	@Autowired
	private CusOpenService cusOpenService;

	@PostMapping("/weightOpenDoor")
	public ReturnDataUtil openDoor(@RequestBody OpenDoorBean openDoor, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("<BusinessController--openDoor--start>");
		Integer openType = openDoor.getOpenType();
		if (OpenTypeEnum.CONSUME.getCode().equals(openType)) {
			// 消费开门
			return consumeOpenDoor(openDoor, request);
		} else if (OpenTypeEnum.OPERATE.getCode().equals(openType)) {
			// 运维开门
			return operateOpenDoor(openDoor, request);
		}
		return ResultUtil.error();
	}

	/**
	 * 消费开门
	 * 
	 * @author hebiting
	 * @date 2018年4月27日上午11:30:41
	 * @param openDoor
	 * @param request
	 * @return
	 */
	public ReturnDataUtil consumeOpenDoor(OpenDoorBean openDoor, HttpServletRequest request) {
		log.info("<BusinessController--consumeOpenDoor--start>");
		try {
			String vmCode = openDoor.getVmCode();
			Integer wayNum = openDoor.getDoorNO();
			Assert.hasText(vmCode, "机器编号不能为空");
			Assert.notNull(wayNum, "货道号不能为空");
			CustomerBean customer = (CustomerBean) request.getAttribute(AdminConstant.LOGIN_USER);
			UserVo userVo = UserUtil.getUser();
			if(customer==null){
				return ResultUtil.error(ResultEnum.ILLEGAL_STATE,null,false);
			}
			String alipayUserId = customer.getAlipayUserId();
			Long customerId = customer.getId();
			String agreementNo = alipayService.querySign(alipayUserId,vmCode);
			// 未签约跳转签约界面
			if (StringUtil.isBlank(agreementNo)) {
				String data = h5URLConfig.signUrl.concat("?vmCode=").concat(vmCode);
				return ResultUtil.error(ResultEnum.FORWARD_SIGN_PAGE, data, true);
			}
			// 判断手机是否注册验证
			String phone = customer.getPhone();
			if (StringUtil.isBlank(phone)) {
				String data = h5URLConfig.phoneVerifyPage.concat("?vmCode=").concat(vmCode);
				return ResultUtil.error(ResultEnum.PHONE_NOT_VERIFY, data, true);
			}
			// 检查是否有未支付订单
			if (payRecordService.booleanIsArrearage(customerId)) {
				// 发送欠款通知
				String msg = "温馨提示: 亲爱的 " + customer.getNickname() + "\t" + customer.getUserName()
						+ ",你尚有未付款的订单，请付完款后再来购买 ";
				smsService.sendMessage(phone, msg);
				return ResultUtil.error(ResultEnum.ORDER_NOT_PAY, msg);
			}
			VmbaseInfoDto baseInfo = vendingMachinesService.getBaseInfo(vmCode);
			WayValidatorDto validator = null;
			if(CommandVersionEnum.VER2.getState().equals(baseInfo.getMachinesVersion())){
				validator = vmwayItemService.getOneWayInfo(vmCode, wayNum);
			}else{
				validator = vmwayService.getOneWayInfo(vmCode, wayNum);
			}
			if (validator == null) {
				return ResultUtil.error(ResultEnum.MACHINE_NOT_EXIST, null);
			} else if (DoorStateEnum.UNAVAILABLE.getCode().equals(validator.getState())) {
				return ResultUtil.error(ResultEnum.DOOR_NOT_AVAILABLE, null);
			} else if (!(validator.getNum() > 0)) {
				return ResultUtil.error(ResultEnum.ITEM_SOLD_OUT, null);
			}
			return cusOpenService.openDoor(baseInfo, vmCode, wayNum, userVo, validator.getMinStandard());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultUtil.error(ResultEnum.SERVER_INNER_ERROR, null);
	}

	/**
	 * 运维开门
	 * 
	 * @author hebiting
	 * @date 2018年4月27日上午11:39:44
	 * @param openDoor
	 * @param request
	 * @return
	 */
	public ReturnDataUtil operateOpenDoor(OpenDoorBean openDoor, HttpServletRequest request) {
		log.info("<BusinessController--operateOpenDoor--start>");
		Integer doorNo = openDoor.getDoorNO();
		String vmCode = openDoor.getVmCode();
		Map<String, Object> baseInfo = orderService.getBaseInfoByVmCode(vmCode);
		String openFlag = orderService.sendCommandToOpenDoor1(baseInfo,vmCode, doorNo);
		log.info("<BusinessController--operateOpenDoor--end>");
		if(String.valueOf("0").equals(openFlag)){
			return ResultUtil.success();
		}else{
			return ResultUtil.error(ResultEnum.OPEN_DOOR_ERROR,null,false);
		}
	}

	/**
	 * 注册
	 * 
	 * @author hebiting
	 * @date 2018年5月5日下午4:12:37
	 * @param phone
	 * @param code
	 * @return
	 */
	@PostMapping("/register")
	@ResponseBody
	public ReturnDataUtil register(@RequestBody Map<String, String> param, HttpServletRequest request) {
		log.info("<BusinessController--register--start>");
		CustomerBean customer = (CustomerBean) request.getAttribute(AdminConstant.LOGIN_USER);
		if(customer==null){
			return ResultUtil.error(ResultEnum.ILLEGAL_STATE,null,false);
		}
		String phone = param.get("phone");
		String code = param.get("code");
		String vmCode = param.get("vmCode");
		VmbaseInfoDto baseInfo = vendingMachinesService.getBaseInfo(vmCode);
		Integer freeMachine=baseInfo.getFreeMachine();

		String phoneCode = redisService.get(SmsKey.getCodeByMobile, phone);
		log.info(phoneCode);
		if (code != null && code.equals(phoneCode)) {
			log.info(h5URLConfig.weightH5Page);
			CustomerBean queryByPhone = customerService.queryByPhone(phone);
			if(queryByPhone == null){
				customer.setPhone(phone);
				if(customerService.queryIsLoginUserByPhone(phone)) {
					customer.setIsLoginUser(1);
				}
				if (customerService.updateCustomer(customer)) {
					RegisterFlowBean registerBean = new RegisterFlowBean(customer.getId(),vmCode,(int)UserVo.Pay_Type_ZhiFuBao);
					userActiveDegreeService.save(registerBean);
					MachinesLAC machinesLAC = couponService.getMachinesLAC(vmCode);
					CouponForm couponForm = new CouponForm(machinesLAC, CouponEnum.REGISTER_COUPON.getState(), null);
					List<CouponBean> presentCoupon = couponService.getPresentCoupon(couponForm);
					if (presentCoupon != null && presentCoupon.size() > 0) {
						List<CouponBean> collect = presentCoupon.stream().filter(coupon -> !couponService.isReceive(customer.getId(), coupon.getId())).collect(Collectors.toList());
						collect.forEach(coupon -> {
									CouponCustomerBean couCusBean = new CouponCustomerBean();
									couCusBean.setQuantity(coupon.getSendMax());
									couCusBean.setCouponId(coupon.getId());
									couCusBean.setCustomerId(customer.getId());
									couCusBean.setStartTime(coupon.getLogicStartTime());
									couCusBean.setEndTime(coupon.getLogicEndTime());
									couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
									couponService.insertCouponCustomer(couCusBean);
								});
					}
					if(freeMachine==0){
						return ResultUtil.success(h5URLConfig.weightH5Page.concat("?vmCode=").concat(vmCode), true);
					}else{
						return ResultUtil.success(h5URLConfig.weightH5PageForFree.concat("?vmCode=").concat(vmCode), true);
					}
				} else {
					return ResultUtil.error();
				}
			}else{
				CustomerBean wxCustomer = AliCustomerConvert.convertAliCustomerToWx(customer, queryByPhone);
				if (customerService.delCustomer(customer.getId()) && customerService.updateCustomer(wxCustomer)) {
					String randomKey = jwtTokenUtil.getRandomKey();
					UserVo userVo = new UserVo();
					userVo.setId(wxCustomer.getId());
					userVo.setOpenId(wxCustomer.getAlipayUserId());
					userVo.setPayType(UserVo.Pay_Type_ZhiFuBao);
					userVo.setType(UserVo.USER_CUSTOMER);
					String token = jwtTokenUtil.generateToken(JsonUtil.toJson(userVo), randomKey);
					if(freeMachine==0){
						return ResultUtil.success(h5URLConfig.weightH5Page.concat("?vmCode=").concat(vmCode).concat("&token=").concat(token), true);
					}else{
						return ResultUtil.success(h5URLConfig.weightH5PageForFree.concat("?vmCode=").concat(vmCode).concat("&token=").concat(token), true);
					}
				} else {
					return ResultUtil.error();
				}
			}
		}
		return ResultUtil.error(ResultEnum.VERIFYCODE_UNCORRECT, null, false);
	}


	/**
	 * 查询该机器所有订单
	 * 
	 * @author hebiting
	 * @date 2018年4月28日下午2:33:09
	 * @param vmCode
	 * @param request
	 * @return
	 */
	@PostMapping("/findAllOrder")
	public ReturnDataUtil findAllOrder(@RequestParam String vmCode, HttpServletRequest request) {
		log.info("<BusinessController--findAllOrder--start>");
		Assert.hasText(vmCode, "智能设备编号不能为空");
		PayRecordForm form = new PayRecordForm();
		form.setVmCode(vmCode);
		List<PayRecordDto> recordList = payRecordService.findPayRecord(form);
		PayRecordConvert.payRecordConver(recordList);
		log.info("<BusinessController--findAllOrder--end>");
		return ResultUtil.success(recordList);
	}

	/**
	 * 查询用户所有订单
	 * 
	 * @author hebiting
	 * @date 2018年5月5日下午5:57:08
	 * @param vmCode
	 * @param request
	 * @return
	 */
	@PostMapping("/findAllUserOrder")
	public ReturnDataUtil findAllUserOrder(@RequestParam(required = false) String vmCode, HttpServletRequest request) {
		log.info("<BusinessController--findAllUserOrder--start>");
		CustomerBean customer = (CustomerBean) request.getAttribute(AdminConstant.LOGIN_USER);
		if(customer==null){
			return ResultUtil.error(ResultEnum.ILLEGAL_STATE,null,false);
		}
		List<PayRecordDto> recordList = payRecordService.findUserAllOrder(customer.getId(), null);
		log.info("<BusinessController--findAllUserOrder--end>");
		return ResultUtil.success(recordList);
	}

	/**
	 * 查询用户已支付订单
	 * 
	 * @author hebiting
	 * @date 2018年5月5日下午5:57:08
	 * @param vmCode
	 * @param request
	 * @return
	 */
	@PostMapping("/findPayOrder")
	public ReturnDataUtil findPayOrder(@RequestParam(required = false) String vmCode, HttpServletRequest request) {
		log.info("<BusinessController--findPayOrder--start>");
		CustomerBean customer = (CustomerBean) request.getAttribute(AdminConstant.LOGIN_USER);
		if(customer==null){
			return ResultUtil.error(ResultEnum.ILLEGAL_STATE,null,false);
		}
		List<PayRecordDto> recordList = payRecordService.findUserAllOrder(customer.getId(), PayStateEnum.PAY_SUCCESS.getState());
		log.info("<BusinessController--findPayOrder--end>");
		return ResultUtil.success(recordList);
	}

	/**
	 * 查询用户未支付订单
	 * 
	 * @author hebiting
	 * @date 2018年5月5日下午5:57:08
	 * @param vmCode
	 * @param request
	 * @return
	 */
	@PostMapping("/findNotPayOrder")
	public ReturnDataUtil findNotPayOrder(@RequestParam(required = false) String vmCode, HttpServletRequest request) {
		log.info("<BusinessController--findNotPayOrder--start>");
		CustomerBean customer = (CustomerBean) request.getAttribute(AdminConstant.LOGIN_USER);
		if(customer==null){
			return ResultUtil.error(ResultEnum.ILLEGAL_STATE,null,false);
		}
		List<PayRecordDto> recordList = payRecordService.findUserAllOrder(customer.getId(), PayStateEnum.NOT_PAY.getState());
		log.info("<BusinessController--findNotPayOrder--end>");
		return ResultUtil.success(recordList);
	}

	/**
	 * 查询用户是否关注生活号
	 * @author hebiting
	 * @date 2018年5月16日上午2:31:11
	 * @param vmCode
	 * @param req
	 * @return
	 * @throws AlipayApiException
	 */
	@GetMapping("/isAttention")
	@ResponseBody
	public ReturnDataUtil isAttention(HttpServletRequest req) throws AlipayApiException{
		log.info("<BusinessController--isAttention--start>");
		CustomerBean customer = (CustomerBean)req.getAttribute(AdminConstant.LOGIN_USER);
		if(customer==null){
			return ResultUtil.error(ResultEnum.ILLEGAL_STATE,null,false);
		}
		AlipayClient alipayClient = alipayClientFactory.getAlipayClient(null);
		AlipayOpenPublicUserFollowQueryRequest request = new AlipayOpenPublicUserFollowQueryRequest();
		request.setBizContent("{" +
		"\"user_id\":\""+customer.getAlipayUserId()+"\"" +
		"}");
		AlipayOpenPublicUserFollowQueryResponse response = alipayClient.execute(request);
		log.info("<BusinessController--isAttention--end>");
		if(response.isSuccess()){
			if("F".equals(response.getIsFollow())){
				//跳转去关注页面，之前二维的地址:https://qr.alipay.com/ppx05147k8rsazvc0oehled,https://qr.alipay.com/pvx049977lyzi3dt4v0ub39
				//http://p.alipay.com/P/AYf6YBeM
				return ResultUtil.error("https://qr.alipay.com/ppx05147k8rsazvc0oehled",true);
			}else{
				return ResultUtil.success();
			}
		} else {
			return ResultUtil.error();
		}
	}
}

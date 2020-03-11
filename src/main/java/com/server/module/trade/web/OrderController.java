package com.server.module.trade.web;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.common.collect.Lists;
import com.server.client.MachinesClient;
import com.server.company.MachinesService;
import com.server.module.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerService;
import com.server.module.coupon.*;
import com.server.module.itemBasic.ItemBasicService;
import com.server.module.payRecord.AliPayRecordService;
import com.server.module.payRecord.PayRecordBean;
import com.server.module.payRecord.PayRecordForm;
import com.server.module.product.ShoppingGoodsBean;
import com.server.module.product.ShoppingGoodsDao;
import com.server.module.store.*;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.config.gzh.MyWXGzhConfig;
import com.server.module.trade.config.gzh.WXGzhConfigFactory;
import com.server.module.trade.config.gzh.WxTicketService;
import com.server.module.trade.config.pay.MyWXRequest;
import com.server.module.trade.config.pay.WxPayConfigFactory;
import com.server.module.trade.config.payScore.ResourceInfo;
import com.server.module.trade.config.payScore.ServiceStartInfo;
import com.server.module.trade.constant.ReturnConstant;
import com.server.module.trade.customer.TblCustomerBean;
import com.server.module.trade.customer.TblCustomerService;
import com.server.module.trade.memberOrder.MemberOrderBean;
import com.server.module.trade.memberOrder.MemberOrderService;
import com.server.module.trade.order.OrderResultBean;
import com.server.module.trade.order.OrderService;
import com.server.module.trade.order.dao.PayRecordDao;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.payRecordItem.PayRecordItemService;
import com.server.module.trade.util.HuaFaResult;
import com.server.module.trade.util.ResultBean;
import com.server.module.trade.util.UserUtil;
import com.server.module.trade.web.bean.HuaFaGoods;
import com.server.module.trade.web.open.CusOpenService;
import com.server.module.trade.web.service.SmsServiceImpl;
import com.server.module.zfb_trade.module.vmbase.AliVendingMachinesService;
import com.server.module.zfb_trade.module.vmbase.VmbaseInfoDto;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.redis.MachinesKey;
import com.server.redis.RedisService;
import com.server.util.HttpUtil;
import com.server.util.JsonUtils;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CommandVersionEnum;
import com.server.util.stateEnum.CompanyEnum;
import com.server.util.stateEnum.CouponEnum;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jersey.repackaged.com.google.common.collect.Maps;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Api(value = "OrderController", description = "订单模块")
@Controller
@RequestMapping(value = "/order")
public class OrderController {

	public static Logger log = LogManager.getLogger(OrderController.class);
	@Autowired
	private WxPayConfigFactory wxpayConfigFactory;
	@Autowired
	private WxTicketService wxTicketService;
	@Autowired
	private OrderService orderService;
	@Value("${wechat.pay.notifyUrl}")
	private String notifyUrl;
	@Value("${wechat.pay.payWebUrl}")
	private String payWebUrl;
	@Value("${wechat.pay.storePayUrl}")
	private String storePayUrl;
	@Value("${wechat.pay.storeNotify}")
	private String storeNotify;
	@Value("${wechat.pay.storeGroupNotify}")
	private String storeGroupNotify;
	@Value("${wechat.pay.memberNotify}")
	private String memberNotify;
	@Value("${wechat.pay.balanceNotify}")
	private String balanceNotify;
	@Autowired
	private MachinesService machinesService;
	@Autowired
	private StoreOrderService storeOrderService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private CouponService couponService;
	@Autowired
	private AliPayRecordService payRecordService;
	@Autowired
	private ShoppingGoodsDao shoppingGoodsDao;
	@Autowired
	private CouponDao couponDao;
	@Autowired
	private ItemBasicService itemBasicService;
	@Autowired
	private MemberOrderService memberOrderService;
	@Autowired
	private TblCustomerService tblCustomerService;
	@Autowired
	private PayRecordItemService payRecordItemService;
	@Autowired
	private WXGzhConfigFactory gzhConfigFactory;
	@Autowired
	private CarryWaterVouchersCustomerService carryWaterVouchersCustomerServiceImpl;
	@Autowired
	private PayRecordDao payRecordDao;
	@Autowired
	@Qualifier("aliVendingMachinesService")
	private AliVendingMachinesService vendingMachinesService;
	@Autowired
	private CusOpenService cusOpenService;
	@Autowired
	private SmsServiceImpl smsService;
	@Autowired
	private MachinesClient machinesClient;

	/**
	 * 生成订单接口，关门的时候调用
	 *
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/book", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResultBean<?> closeDoor(@RequestBody String param) {
		log.info("****进入closeDoor*******");
		ResultBean<?> result = new ResultBean<>();
		log.info("关门心跳=" + param);
		if (StringUtil.isBlank(param)) {
			result.setCode(-1);
			result.setMsg("关门心跳为null");
			return result;
		}
		MachineInfo machineInfo = new MachineInfo(param);
		result = orderService.closeDoor(machineInfo, param);
		return result;
	}

	/**
	 * 多商品订单，单次开门
	 *
	 * @author hebiting
	 * @date 2018年9月20日下午2:36:44
	 * @param param
	 * @return
	 */
	@RequestMapping("/moreGoodsOrder")
	@ResponseBody
	public ResultBean<?> moreGoodsOrder(@RequestBody String param) {
		log.info("<OrderController--moreGoodsOrder--start>");
		ResultBean<?> result = orderService.moreGoodsOrder(param);
		log.info("<OrderController--moreGoodsOrder--end>");
		return result;
	}

	/**
	 * 多商品一键开门后，关门调用
	 *
	 * @author hebiting
	 * @date 2018年9月20日下午2:36:53
	 * @param param
	 * @return
	 */
/*	@RequestMapping("/moreGoodsReplenish")
	@ResponseBody
	public ResultBean<?> moreGoodsReplenish(@RequestBody String param) {
		// factNum:*****;goods:1,1:-1,2:-2;goods:2,1:-1,2:-2; goods:3,1:-1,2:-2; goods:4,1:-1,2:-2;all_closed
		log.info("<OrderController--moreGoodsReplenish--start>");
		ResultBean<?> result = orderService.moreGoodsReplenish(param);
		log.info("<OrderController--moreGoodsReplenish--end>");
		return result;
	}*/

	/**
	 * 运维关门(一次全开，门全关闭的时候)
	 *
	 * @param params
	 * @return
	 */
/*	@RequestMapping(value = "/operateClose", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResultBean<?> operateCloseDoor(@RequestBody String param) {
		log.info("****进入operateCloseDoor*******");
		return orderService.operateCloseDoor(param);
	}*/

	/**
	 * 重新设置机器各货道的水数量
	 *
	 * @param vmCode
	 * @return
	 */
	@RequestMapping(value = "/resetWaysNum/{vmCode}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResultBean<?> resetWaysNum(@PathVariable String vmCode) {
		log.info("****进入重置货道水量*******" + vmCode);
		Integer version = orderService.getVersionByVmCode(vmCode);
		if (CommandVersionEnum.VER1.getState().equals(version)) {
			return orderService.resetWaysNum(vmCode);
		}
		return new ResultBean<String>();
	}

	@ApiOperation(value = "我的订单列表", notes = "传递的参数  页码", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@RequestMapping(value = "/myOrderList", method = RequestMethod.GET)
	@ResponseBody
	public ReturnDataUtil myOrderList(Integer curPage) {

		Long customerId = UserUtil.getUser().getId();
		if (curPage == null) {
			curPage = 1;
		}
		return orderService.myOrderList(curPage, customerId);
	}

	/**
	 * 查询售货机开关门的记录/我的订单详情列表
	 *
	 * @param customerMachineForm
	 * @return 返回查询状态，开门记录
	 */
	@RequestMapping(value = "/findMachineHistory", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnDataUtil findMachineHistory(@RequestBody(required = false) PayRecordForm customerMachineForm,
			HttpServletRequest request) {
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		if (customerMachineForm.getPayCodeOrName() != null) {
			return payRecordService.findMachineHistory(customerMachineForm);
		} else {
			returnDataUtil.setMessage("内部平台订单号不存在");
			returnDataUtil.setStatus(0);
			returnDataUtil.setReturnObject(null);
			return returnDataUtil;
		}
	}

	/**
	 * 微信完成订单后的回调，用以确认订单是否完成支付，并更新状态
	 *
	 * @author yjr
	 * @param xml
	 * @return
	 */
	@RequestMapping(value = "/notify")
	public void payNotify(@RequestBody String xml, HttpServletResponse response) {
		log.info("微信订单回调=====notify===="+xml);
		String returnResult = null;
		try {
			Map<String, String> map = WXPayUtil.xmlToMap(xml);
			String outTradeNo = (String) map.get("out_trade_no");
			log.info("outTradeNo==" + outTradeNo);
			outTradeNo = outTradeNo.substring(0, 25);// 获取真实订单号
			Integer companyId = machinesService.getCompanyIdByPayCode(outTradeNo);
			WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
			String result_code = (String) map.get("result_code");
			String transactionId = (String) map.get("transaction_id");
			String userId = (String) map.get("attach");
			boolean b = WXPayUtil.isSignatureValid(map, wxPayConfig.getKey());
			log.info("notify 结果:" + b + "--"+redisService.get(MachinesKey.orderFlag, outTradeNo));
			if (b) {// 订单号 25位
				PayRecordBean payRecord = payRecordService.findPayRecordByPayCode(outTradeNo);
				if (("SUCCESS").equals(result_code)
						&& !("SUCCESS").equals(redisService.get(MachinesKey.orderFlag, outTradeNo))) {
					log.info("回调正常"+outTradeNo);
					redisService.set(MachinesKey.orderFlag, outTradeNo, "SUCCESS");
					redisService.set("orderUserId" + userId, "SUCCESS", 300);
					orderService.paySuccessOrder(outTradeNo, transactionId);
					if (payRecord != null) {
						MachinesLAC machinesLAC = couponService.getMachinesLAC(payRecord.getVendingMachinesCode());
						Long customerId = payRecord.getCustomerId();
						CouponForm couponForm = new CouponForm(machinesLAC, CouponEnum.PURCHASE_COUPON.getState(),
								CouponEnum.USE_MACHINES.getState());
						List<CouponBean> presentCoupon = couponService.getPresentCoupon(couponForm);
						if (presentCoupon != null && presentCoupon.size() > 0) {
							presentCoupon.stream()
									.filter(coupon -> !couponService.isReceive(customerId, coupon.getId()))
									.forEach(coupon -> {
										CouponCustomerBean couCusBean = new CouponCustomerBean();
										couCusBean.setQuantity(coupon.getSendMax());
										couCusBean.setCouponId(coupon.getId());
										couCusBean.setCustomerId(customerId);
										couCusBean.setStartTime(coupon.getLogicStartTime());
										couCusBean.setEndTime(coupon.getLogicEndTime());
										couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
										couponService.insertCouponCustomer(couCusBean);
									});
						}

						// 用户积分更新
//						List<PayRecordItemBean> payRecordItemList = payRecordItemService
//								.getListByPayRecordId(payRecord.getId());
//						for (PayRecordItemBean prib : payRecordItemList) {
//							ItemBasicBean itemBasicBean = itemBasicService.getItemBasic(prib.getBasicItemId());
//							if (itemBasicBean != null && !("克").equals(itemBasicBean.getUnitName())) {
//								TblCustomerBean findBen = tblCustomerService.findBen(customerId);
//								if (findBen != null) {// 会员积分双倍
//									log.info("=====是会员=====");
//									couponService.updateIntegral(customerId, prib.getNum() * 2l);
//								} else {
//									log.info("=====非会员=====");
//									couponService.updateIntegral(customerId, prib.getNum() * 1l);
//								}
//							}
//						}


						TblCustomerBean customer = tblCustomerService.getCustomerById(customerId);

                        //短信通知商户
                        if (payRecord.getItemName().contains("乔府五常大米") || payRecord.getItemName().contains("长粒香米") || payRecord.getItemName().contains("稻花香米")){
                        	//if(payRecord.getItemName().contains("L")){
                            LocalTime time = LocalTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                            String buyTime=time.format(formatter);
                            String msg=buyTime+" "+"用户在"+payRecord.getVendingMachinesCode()+"售货机上用微信购买了"+payRecord.getItemName();
                            smsService.sendMsg("15986666885",msg);
                        }
                        // 邀请人赠券
                        if (customer.getInviterId() > 0 && tblCustomerService.isFirstBuy(customer.getId()).equals(1)
								&& tblCustomerService.isStoreFirstBuy(customer.getId()).equals(0)) {
							//成功邀请获得10积分
							couponDao.updateIntegral(customer.getInviterId(), 11l);
							CouponForm couponForm2 = new CouponForm();
							couponForm2.setLimitRange(false);
							couponForm2.setWay(CouponEnum.INVITE_COUPON.getState());
							List<CouponBean> presentCoupon2 = couponService.getPresentCoupon(couponForm2);
							if (presentCoupon2 != null && presentCoupon2.size() > 0) {
								for (CouponBean coupon : presentCoupon2) {
									CouponCustomerBean couCusBean = new CouponCustomerBean();
									couCusBean.setQuantity(coupon.getSendMax());
									couCusBean.setCouponId(coupon.getId());
									couCusBean.setCustomerId(customer.getInviterId());
									couCusBean.setStartTime(coupon.getLogicStartTime());
									couCusBean.setEndTime(coupon.getLogicEndTime());
									couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
									couponService.insertCouponCustomer(couCusBean);
								}
							}
						}

					}
				}else if (("FAIL").equals(result_code)
						&& StringUtil.isBlank(redisService.get(MachinesKey.orderFlag, outTradeNo))) {
					redisService.set(MachinesKey.orderFlag, outTradeNo, "FAIL");
					log.info("回调异常"+outTradeNo);
					orderService.payFailSendMsg(payRecord.getCustomerId() != null ? Long.valueOf(payRecord.getCustomerId()) : null);
				}
				returnResult = ReturnConstant.SUCCESS;
				writeMsgToWx(returnResult, response);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			returnResult = ReturnConstant.FAIL;
			writeMsgToWx(returnResult, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	private void writeMsgToWx(String msg, HttpServletResponse response) {
		try {
			response.reset();
			PrintWriter printWriter = response.getWriter();
			printWriter.write(msg);
			printWriter.flush();
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 微信完成商城订单后的回调，用以确认订单是否完成支付，并更新状态
	 *
	 * @param xml
	 * @return
	 */
	@RequestMapping(value = "/storeNotify")
	public void payStoreNotify(@RequestBody String xml, HttpServletResponse response) {
		log.info("=====storeNotify====");
		String returnResult = null;
		try {
			Map<String, String> map = WXPayUtil.xmlToMap(xml);
			String outTradeNo = (String) map.get("out_trade_no");
			outTradeNo = outTradeNo.substring(0, 25);// 获取真实订单号
			Integer companyId = machinesService.getCompanyIdByPayCodeStore(outTradeNo);
			WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
			Integer distributionModel = orderService.getDistributionModelByPayCode(outTradeNo);
			String result_code = (String) map.get("result_code");
			String transactionId = (String) map.get("transaction_id");
			boolean b = WXPayUtil.isSignatureValid(map, wxPayConfig.getKey());
			log.info("storeNotify 结果:" + b);
			if (b) {// 订单号 25位
				if (("SUCCESS").equals(result_code)
						&& !("SUCCESS").equals(redisService.get(MachinesKey.orderFlag, outTradeNo))) {
					redisService.set(MachinesKey.orderFlag, outTradeNo, "SUCCESS");
					//更改订单的状态
					orderService.paySuccessStroeOrder(distributionModel,outTradeNo, transactionId,0);


					// 给商城顾客增加存水
					List<OrderDetailBean> orderDetail = storeOrderService.getOrderDetail(outTradeNo);
					Long customerId = orderDetail.get(0).getCustomerId();
					String a="";String plusNum="";
					for (int i=0; i<orderDetail.size();i++) {
						a=plusNum+orderDetail.get(i).getItemName()+"*"+orderDetail.get(i).getNum();
                        plusNum="+";
/*						if(i!=orderDetail.size()-1){
							a=orderDetail.get(i).getItemName()+"*"+orderDetail.get(i).getNum()+"+";
						}else{
							a=orderDetail.get(i).getItemName()+"*"+orderDetail.get(i).getNum();
						}*/
					}
                    log.info("++++++++++"+a);
					if (a.contains("乔府大院")){
					    //if (a.contains("L")||a.contains("桶")||a.contains("券") ){
						TblCustomerBean customer = tblCustomerService.getCustomerById(customerId);
						LocalTime time = LocalTime.now();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						String buyTime=time.format(formatter);
						String msg=buyTime+" "+"用户在优水商城上用微信购买了"+a;
						if(redisService.getString("mealCallStart").equals("1")){
							smsService.sendMsg("15986666885",msg);
						}
					}
					for (OrderDetailBean orderDetailBean : orderDetail) {
						ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsDao.get(orderDetailBean.getItemId());
						// 购买优惠券不增加存水
						if (shoppingGoodsBean.getTypeId().equals(25l)) {// 购买的商品为
																		// 优惠券
																		// 此时绑定用户
							CouponBean couponBean = couponDao.getCouponInfoByProduct(shoppingGoodsBean);
							CouponCustomerBean couponCustomerBean = couponDao.getCouponCustomerBean(customerId,
									couponBean.getId());
							if (couponCustomerBean != null) {
								couponCustomerBean
										.setQuantity(couponCustomerBean.getQuantity() + orderDetailBean.getNum());
								couponCustomerBean
										.setSumQuantity(couponCustomerBean.getSumQuantity() + orderDetailBean.getNum());// 券的总数
								couponDao.updateCouponCustomerBean(couponCustomerBean);
							} else {
								CouponCustomerBean couCusBean = new CouponCustomerBean();
								couCusBean.setCouponId(couponBean.getId());
								couCusBean.setCustomerId(customerId);
								couCusBean.setStartTime(couponBean.getLogicStartTime());
								couCusBean.setEndTime(couponBean.getLogicEndTime());
								couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
								couCusBean.setQuantity(orderDetailBean.getNum());
								couCusBean.setSumQuantity(orderDetailBean.getNum());// 总券数
								couponService.insertCouponCustomerWithSumQuantity(couCusBean);
							}

						} else if (shoppingGoodsBean.getTypeId().equals(26l) || shoppingGoodsBean.getTypeId().equals(27l)) {
							// 购买的商品为提水券 此时绑定用户
							carryWaterVouchersCustomerServiceImpl.add(shoppingGoodsBean.getVouchersId(), customerId,
									orderDetailBean.getNum(),orderDetailBean.getOrderId());
						}else if (shoppingGoodsBean.getTypeId().equals(28l)) {
							// 商品为套餐类型 绑定多张提水券
							ShoppingGoodsBean sgs=shoppingGoodsDao.get(orderDetailBean.getItemId());
							String[] vouchers=StringUtils.split(sgs.getVouchersIds(),",");
							for(String v:vouchers) {
								carryWaterVouchersCustomerServiceImpl.add(Long.valueOf(v), customerId,
										orderDetailBean.getNum(),orderDetailBean.getOrderId());
							}
						}
						else {
							if (orderDetailBean.getDistributionModel() == 1) {// 自取
																				// 直接去给用户存水
								CustomerStockBean stock = storeOrderService.getStock(orderDetailBean.getItemId(),
										orderDetailBean.getCustomerId());
								if (stock == null) {
									stock = new CustomerStockBean();
									stock.setBasicItemId(shoppingGoodsBean.getBasicItemId());
									stock.setItemId(orderDetailBean.getItemId());
									stock.setItemName(orderDetailBean.getItemName());
									stock.setCustomerId(orderDetailBean.getCustomerId());
									stock.setStock(orderDetailBean.getNum());
									stock.setPickNum(0);
									storeOrderService.insert(stock);
								} else {
									stock.setStock(stock.getStock() + orderDetailBean.getNum());
									storeOrderService.update(stock);
								}
							} else {// 非自取 判断用户购买的商品订单是否有绑定套餐 如果有 加入存水
									// 根据商城商品id 查询绑定商品信息
								List<ShoppingGoodsProductBean> shoppingGoodsProductBeanList = storeOrderService
										.getShoppingGoodsProductBean(orderDetailBean.getItemId().longValue());
								if (shoppingGoodsProductBeanList != null && shoppingGoodsProductBeanList.size() > 0) {
									for (ShoppingGoodsProductBean shoppingGoodsProductBean : shoppingGoodsProductBeanList) {
										// 获取用户 拥有当前商品的存量 如果没有就新增 存在就修改
										CustomerStockBean stock = storeOrderService.getStock(
												shoppingGoodsProductBean.getGoodsId().intValue(),
												orderDetailBean.getCustomerId());
										if (stock == null) {
											stock = new CustomerStockBean();
											stock.setBasicItemId(shoppingGoodsProductBean.getItemId());
											stock.setItemId(shoppingGoodsProductBean.getGoodsId().intValue());
											stock.setItemName(shoppingGoodsProductBean.getItemName());
											stock.setCustomerId(orderDetailBean.getCustomerId());
											stock.setStock(orderDetailBean.getNum()
													* shoppingGoodsProductBean.getQuantity().intValue());
											stock.setPickNum(0);
											storeOrderService.insert(stock);
										} else {
											int num = orderDetailBean.getNum()
													* shoppingGoodsProductBean.getQuantity().intValue();
											stock.setStock(stock.getStock() + num);
											storeOrderService.update(stock);
										}
									}

								}
							}
						}
					}

					// 获取优惠券赠送客户
					CouponForm couponForm = new CouponForm();
					couponForm.setWay(CouponEnum.PURCHASE_COUPON.getState());
					couponForm.setLimitRange(false);
					couponForm.setUseWhere(CouponEnum.USE_STORE.getState());
					List<CouponBean> presentCoupon = couponService.getPresentCoupon(couponForm);
					if (presentCoupon != null && presentCoupon.size() > 0) {
						presentCoupon.stream().filter(coupon -> !couponService.isReceive(customerId, coupon.getId()))
								.forEach(coupon -> {
									CouponCustomerBean couCusBean = new CouponCustomerBean();
									couCusBean.setQuantity(coupon.getSendMax());
									couCusBean.setCouponId(coupon.getId());
									couCusBean.setCustomerId(customerId);
									couCusBean.setStartTime(coupon.getLogicStartTime());
									couCusBean.setEndTime(coupon.getLogicEndTime());
									couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
									couponService.insertCouponCustomer(couCusBean);
								});
					}

					// 邀请人赠券
					TblCustomerBean customer = tblCustomerService.getCustomerById(customerId);
					if (customer.getInviterId() > 0 && tblCustomerService.isFirstBuy(customer.getId()).equals(0)
							&& tblCustomerService.isStoreFirstBuy(customer.getId()).equals(1)) {
						//成功邀请获得10积分
						couponDao.updateIntegral(customer.getInviterId(), 11l);
						CouponForm couponForm2 = new CouponForm();
						couponForm2.setLimitRange(false);
						couponForm2.setWay(CouponEnum.INVITE_COUPON.getState());
						List<CouponBean> presentCoupon2 = couponService.getPresentCoupon(couponForm2);
						if (presentCoupon2 != null && presentCoupon2.size() > 0) {
							for (CouponBean coupon : presentCoupon2) {
								CouponCustomerBean couCusBean = new CouponCustomerBean();
								couCusBean.setQuantity(coupon.getSendMax());
								couCusBean.setCouponId(coupon.getId());
								couCusBean.setCustomerId(customer.getInviterId());
								couCusBean.setStartTime(coupon.getLogicStartTime());
								couCusBean.setEndTime(coupon.getLogicEndTime());
								couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
								couponService.insertCouponCustomer(couCusBean);
							}
						}
					}
				}
				returnResult = ReturnConstant.SUCCESS;
				writeMsgToWx(returnResult, response);
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			returnResult = ReturnConstant.FAIL;
			writeMsgToWx(returnResult, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	   public static <E, T> List<T> convertList2List(List<E> input, Class<T> clzz) {
	        List<T> output = Lists.newArrayList();
	        if (CollectionUtils.isNotEmpty(input)) {
	            for (E source : input) {
	                T target = BeanUtils.instantiate(clzz);
	                BeanUtils.copyProperties(source, target);
	                output.add(target);
	            }
	        }
	        return output;
	    }

/*	
 * 测试商城订单通知
*/	
	@RequestMapping(value = "/testOrder")
	public void testOrder(HttpServletResponse response) {
		String outTradeNo="3758201158329341878458170";
		OrderBean ob = orderService.getMessageByPayCode(outTradeNo);
		List<OrderDetailBean> orderDetail = storeOrderService.getOrderDetail(outTradeNo);
		List<HuaFaGoods> huaFaGoods = new ArrayList<HuaFaGoods>();
		Long customerId = orderDetail.get(0).getCustomerId();
		for(OrderDetailBean o:orderDetail) {
			HuaFaGoods hf=new HuaFaGoods();
			BeanUtils.copyProperties(o, hf);
			huaFaGoods.add(hf);
		}
		TblCustomerBean customer = tblCustomerService.getCustomerById(customerId);
		Map<String, Object> huaAppMap = new HashMap<String, Object>();
		//List<ShoppingBean> newlist = orderService.findShoppingBeandByOrderId(ob.getId(),ob.getType());
		//System.out.println("---"+ob.getPrice()+"---"+ob.getNowprice());
		huaAppMap.put("orderId", ob.getId());
		huaAppMap.put("openId", customer.getHuafaAppOpenId());
		huaAppMap.put("state", ob.getState());
		huaAppMap.put("nowprice", ob.getNowprice()!=null?ob.getNowprice():"0");
		huaAppMap.put("payCode", ob.getPayCode());
		huaAppMap.put("createTime", ob.getCreateTime());
		huaAppMap.put("time", ob.getPayTime());
		huaAppMap.put("type", 1);
		huaAppMap.put("useMoney", ob.getPrice().subtract(ob.getNowprice()));
		huaAppMap.put("price", ob.getPrice());
		huaAppMap.put("payType", ob.getPayType());
		huaAppMap.put("stateName", ob.getStateName());
		huaAppMap.put("ptCode", ob.getPtCode());
		huaAppMap.put("product", ob.getProduct());
		huaAppMap.put("phone", customer.getPhone());
		huaAppMap.put("list", huaFaGoods);
		String json = JSON.toJSONString(huaAppMap);//map转String
		//JSONObject jsonObject = JSON.parseObject(json);//String转json
		//String a=HttpUtil.post("https://devapp.huafatech.com/app/water/orderInfo/createWaterOrderInfo", json);
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				// run方法具体重写
				try {
					machinesClient.sendHuaFa(5,outTradeNo, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}});
		t.start();

		log.info("订单传输成功！");
	}

	@RequestMapping(value = "/returnOrder")
	@ResponseBody
	public HuaFaResult returnOrder(@RequestBody String order){
		//HuaFaResult huaFaResult = JSON.parseObject(order,HuaFaResult.class);
		log.info("returnOrder"+order);
		HuaFaResult huaFaResult = new HuaFaResult();
		huaFaResult.setSuccess("true");
		huaFaResult.setMessage("GG");
		return huaFaResult;
	}
	/**
	 * 微信完成商城app订单后的回调，用以确认订单是否完成支付，并更新状态
	 *
	 * @param xml
	 * @return
	 */
	@RequestMapping(value = "/storeAppNotify")
	public void payStoreAppNotify(@RequestBody String xml, HttpServletResponse response) {
		log.info("=====storeAppNotify====");
		String returnResult = null;
		try {
			Map<String, String> map = WXPayUtil.xmlToMap(xml);
			String outTradeNo = (String) map.get("out_trade_no");
			outTradeNo = outTradeNo.substring(0, 25);// 获取真实订单号
			Integer companyId = machinesService.getCompanyIdByPayCodeStore(outTradeNo);
			WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
			OrderBean ob = orderService.getMessageByPayCode(outTradeNo);
			Integer distributionModel = orderService.getDistributionModelByPayCode(outTradeNo);
			String result_code = (String) map.get("result_code");
			String transactionId = (String) map.get("transaction_id");
			boolean b = WXPayUtil.isSignatureValid(map, wxPayConfig.getAppKey());
			log.info("storeNotify 结果:" + b);
			if (b) {// 订单号 25位
				if (("SUCCESS").equals(result_code)
						&& !("SUCCESS").equals(redisService.get(MachinesKey.orderFlag, outTradeNo))) {
					redisService.set(MachinesKey.orderFlag, outTradeNo, "SUCCESS");
					orderService.paySuccessStroeOrder(distributionModel,outTradeNo, transactionId,0);
					// 给商城顾客增加存水
					List<OrderDetailBean> orderDetail = storeOrderService.getOrderDetail(outTradeNo);
					List<HuaFaGoods> huaFaGoods = new ArrayList<HuaFaGoods>();
					Long customerId = orderDetail.get(0).getCustomerId();
					for(OrderDetailBean o:orderDetail) {
						HuaFaGoods hf=new HuaFaGoods();
						BeanUtils.copyProperties(o, hf);
						huaFaGoods.add(hf);
					}
					TblCustomerBean customer = tblCustomerService.getCustomerById(customerId);
					Map<String, Object> huaAppMap = new HashMap<String, Object>();
					//List<ShoppingBean> newlist = orderService.findShoppingBeandByOrderId(ob.getId(),ob.getType());
					huaAppMap.put("orderId", ob.getId());
					huaAppMap.put("openId", customer.getHuafaAppOpenId());
					huaAppMap.put("state", ob.getState());
					huaAppMap.put("nowprice", ob.getNowprice()!=null?ob.getNowprice():"0");
					huaAppMap.put("payCode", ob.getPayCode());
					huaAppMap.put("createTime", ob.getCreateTime());
					huaAppMap.put("time", ob.getPayTime());
					huaAppMap.put("type", 1);
					huaAppMap.put("useMoney", ob.getPrice().subtract(ob.getNowprice()));
					huaAppMap.put("price", ob.getPrice());
					huaAppMap.put("payType", ob.getPayType());
					huaAppMap.put("stateName", ob.getStateName());
					huaAppMap.put("ptCode", ob.getPtCode());
					huaAppMap.put("product", ob.getProduct());
					huaAppMap.put("phone", customer.getPhone());
					huaAppMap.put("list", huaFaGoods);
					String json = JSON.toJSONString(huaAppMap);//map转String
					//JSONObject jsonObject = JSON.parseObject(json);//String转json
					//String a=HttpUtil.post("https://devapp.huafatech.com/app/water/orderInfo/createWaterOrderInfo", json);
					
					//
					Thread t = new Thread(new Runnable(){
						@Override
						public void run(){
							// run方法具体重写
							try {
								machinesClient.sendHuaFa(5,ob.getPayCode(), json);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}});
					t.start();

					log.info("订单传输成功！");
					
					for (OrderDetailBean orderDetailBean : orderDetail) {
						ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsDao.get(orderDetailBean.getItemId());
						// 购买优惠券不增加存水
						if (shoppingGoodsBean.getTypeId().equals(25l)) {// 购买的商品为
																		// 优惠券
																		// 此时绑定用户
							CouponBean couponBean = couponDao.getCouponInfoByProduct(shoppingGoodsBean);
							CouponCustomerBean couponCustomerBean = couponDao.getCouponCustomerBean(customerId,
									couponBean.getId());
							if (couponCustomerBean != null) {
								couponCustomerBean
										.setQuantity(couponCustomerBean.getQuantity() + orderDetailBean.getNum());
								couponCustomerBean
										.setSumQuantity(couponCustomerBean.getSumQuantity() + orderDetailBean.getNum());// 券的总数
								couponDao.updateCouponCustomerBean(couponCustomerBean);
							} else {
								CouponCustomerBean couCusBean = new CouponCustomerBean();
								couCusBean.setCouponId(couponBean.getId());
								couCusBean.setCustomerId(customerId);
								couCusBean.setStartTime(couponBean.getLogicStartTime());
								couCusBean.setEndTime(couponBean.getLogicEndTime());
								couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
								couCusBean.setQuantity(orderDetailBean.getNum());
								couCusBean.setSumQuantity(orderDetailBean.getNum());// 总券数
								couponService.insertCouponCustomerWithSumQuantity(couCusBean);
							}

						} else if (shoppingGoodsBean.getTypeId().equals(26l) || shoppingGoodsBean.getTypeId().equals(27l)) {
							// 购买的商品为提水券 此时绑定用户
							carryWaterVouchersCustomerServiceImpl.add(shoppingGoodsBean.getVouchersId(), customerId,
									orderDetailBean.getNum(),orderDetailBean.getOrderId());
						}else if (shoppingGoodsBean.getTypeId().equals(28l)) {
							// 商品为套餐类型 绑定多张提水券
							ShoppingGoodsBean sgs=shoppingGoodsDao.get(orderDetailBean.getItemId());
							String[] vouchers=StringUtils.split(sgs.getVouchersIds(),",");
							for(String v:vouchers) {
								carryWaterVouchersCustomerServiceImpl.add(Long.valueOf(v), customerId,
										orderDetailBean.getNum(),orderDetailBean.getOrderId());
							}
						}
						else {
							if (orderDetailBean.getDistributionModel() == 1) {// 自取
																				// 直接去给用户存水
								CustomerStockBean stock = storeOrderService.getStock(orderDetailBean.getItemId(),
										orderDetailBean.getCustomerId());
								if (stock == null) {
									stock = new CustomerStockBean();
									stock.setBasicItemId(shoppingGoodsBean.getBasicItemId());
									stock.setItemId(orderDetailBean.getItemId());
									stock.setItemName(orderDetailBean.getItemName());
									stock.setCustomerId(orderDetailBean.getCustomerId());
									stock.setStock(orderDetailBean.getNum());
									stock.setPickNum(0);
									storeOrderService.insert(stock);
								} else {
									stock.setStock(stock.getStock() + orderDetailBean.getNum());
									storeOrderService.update(stock);
								}
							} else {// 非自取 判断用户购买的商品订单是否有绑定套餐 如果有 加入存水
									// 根据商城商品id 查询绑定商品信息
								List<ShoppingGoodsProductBean> shoppingGoodsProductBeanList = storeOrderService
										.getShoppingGoodsProductBean(orderDetailBean.getItemId().longValue());
								if (shoppingGoodsProductBeanList != null && shoppingGoodsProductBeanList.size() > 0) {
									for (ShoppingGoodsProductBean shoppingGoodsProductBean : shoppingGoodsProductBeanList) {
										// 获取用户 拥有当前商品的存量 如果没有就新增 存在就修改
										CustomerStockBean stock = storeOrderService.getStock(
												shoppingGoodsProductBean.getGoodsId().intValue(),
												orderDetailBean.getCustomerId());
										if (stock == null) {
											stock = new CustomerStockBean();
											stock.setBasicItemId(shoppingGoodsProductBean.getItemId());
											stock.setItemId(shoppingGoodsProductBean.getGoodsId().intValue());
											stock.setItemName(shoppingGoodsProductBean.getItemName());
											stock.setCustomerId(orderDetailBean.getCustomerId());
											stock.setStock(orderDetailBean.getNum()
													* shoppingGoodsProductBean.getQuantity().intValue());
											stock.setPickNum(0);
											storeOrderService.insert(stock);
										} else {
											int num = orderDetailBean.getNum()
													* shoppingGoodsProductBean.getQuantity().intValue();
											stock.setStock(stock.getStock() + num);
											storeOrderService.update(stock);
										}
									}

								}
							}
						}
					}

					// 获取优惠券赠送客户
					CouponForm couponForm = new CouponForm();
					couponForm.setWay(CouponEnum.PURCHASE_COUPON.getState());
					couponForm.setLimitRange(false);
					couponForm.setUseWhere(CouponEnum.USE_STORE.getState());
					List<CouponBean> presentCoupon = couponService.getPresentCoupon(couponForm);
					if (presentCoupon != null && presentCoupon.size() > 0) {
						presentCoupon.stream().filter(coupon -> !couponService.isReceive(customerId, coupon.getId()))
								.forEach(coupon -> {
									CouponCustomerBean couCusBean = new CouponCustomerBean();
									couCusBean.setQuantity(coupon.getSendMax());
									couCusBean.setCouponId(coupon.getId());
									couCusBean.setCustomerId(customerId);
									couCusBean.setStartTime(coupon.getLogicStartTime());
									couCusBean.setEndTime(coupon.getLogicEndTime());
									couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
									couponService.insertCouponCustomer(couCusBean);
								});
					}

					// 邀请人赠券
					//customer = tblCustomerService.getCustomerById(customerId);
					if (customer.getInviterId() > 0 && tblCustomerService.isFirstBuy(customer.getId()).equals(0)
							&& tblCustomerService.isStoreFirstBuy(customer.getId()).equals(1)) {
						//成功邀请获得10积分
						couponDao.updateIntegral(customer.getInviterId(), 11l);
						CouponForm couponForm2 = new CouponForm();
						couponForm2.setLimitRange(false);
						couponForm2.setWay(CouponEnum.INVITE_COUPON.getState());
						List<CouponBean> presentCoupon2 = couponService.getPresentCoupon(couponForm2);
						if (presentCoupon2 != null && presentCoupon2.size() > 0) {
							for (CouponBean coupon : presentCoupon2) {
								CouponCustomerBean couCusBean = new CouponCustomerBean();
								couCusBean.setQuantity(coupon.getSendMax());
								couCusBean.setCouponId(coupon.getId());
								couCusBean.setCustomerId(customer.getInviterId());
								couCusBean.setStartTime(coupon.getLogicStartTime());
								couCusBean.setEndTime(coupon.getLogicEndTime());
								couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
								couponService.insertCouponCustomer(couCusBean);
							}
						}
					}
				}
				returnResult = ReturnConstant.SUCCESS;
				writeMsgToWx(returnResult, response);
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			returnResult = ReturnConstant.FAIL;
			writeMsgToWx(returnResult, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * 微信完成商城拼团订单后的回调，用以确认订单是否完成支付，并更新状态
	 *
	 * @param xml
	 * @return
	 */
	@RequestMapping(value = "/storeGroupNotify")
	public void payStoreGroupNotify(@RequestBody String xml, HttpServletResponse response) {
		log.info("=====gourpNotify====");
		String returnResult = null;
		try {
			Map<String, String> map = WXPayUtil.xmlToMap(xml);
			String outTradeNo = (String) map.get("out_trade_no");
			outTradeNo = outTradeNo.substring(0, 25);// 获取真实订单号
			Integer companyId = machinesService.getCompanyIdByPayCode(outTradeNo);
			WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
			Integer distributionModel = orderService.getDistributionModelByPayCode(outTradeNo);
			String result_code = (String) map.get("result_code");
			String transactionId = (String) map.get("transaction_id");
			boolean b = WXPayUtil.isSignatureValid(map, wxPayConfig.getKey());
			log.info("notify 结果:" + b);
			if (b) {// 订单号 25位
				if (("SUCCESS").equals(result_code)
						&& !("SUCCESS").equals(redisService.get(MachinesKey.orderFlag, outTradeNo))) {
					redisService.set(MachinesKey.orderFlag, outTradeNo, "SUCCESS");
					orderService.paySuccessStroeOrder(distributionModel,outTradeNo, transactionId,1);

					OrderBean order = storeOrderService.getStoreOrderbyOutTradeNo(outTradeNo);
					String message = "恭喜你拼团成功，你参加购买的" + order.getName() + "提货券已经发到你的账号上了，请关注优水到家的公众号，优水商城-个人中心-我的提货券里面查看";
					if (order.getCustomerId().equals(order.getStartCustomerId())) {// 团长订单 订单->拼团中

						storeOrderService.updateParCustomerIdAndState(order.getCustomerGroupId(),null,2);

					} else {// 拼团者订单 判断成团人数
						String ids = order.getParticipationCustomerId();
						if (ids == null) {// 第一个拼单人
							if (order.getMinimumGroupSize() == 2) {
								log.info("两人拼团成功");
								String id = order.getCustomerId().toString();
								storeOrderService.updateParCustomerIdAndState(order.getCustomerGroupId(), id, 1);
								List<OrderBean> list = storeOrderService.getStoreOrderbyCustomerGroupId(order.getCustomerGroupId());
//								// 发送客服信息
//								TblCustomerBean customer = tblCustomerService.getCustomerById(order.getCustomerId());
//								tblCustomerService.sendWechatMessage(customer.getOpenId(), message);
//
//								customer = tblCustomerService.getCustomerById(order.getStartCustomerId());
//								tblCustomerService.sendWechatMessage(customer.getOpenId(), message);

								//查询参与本次拼团用户订单
								for (OrderBean orderBean : list) {
									//修改拼团状态
									sendVouchers(orderBean,orderBean.getCustomerId());//派提水券
									orderService.paySpellgroupStroeOrder(orderBean.getPayCode(),2);
									tblCustomerService.sendWechatMessage(orderBean.getOpenid(), message);
								}
							} else {
								storeOrderService.updateParCustomerIdAndState(order.getCustomerGroupId(),
										order.getCustomerId().toString(), null);
							}
						} else if (ids.split(",").length < order.getMinimumGroupSize() - 2) {// 未达到成团人数
							ids = ids + "," + order.getCustomerId();
							storeOrderService.updateParCustomerIdAndState(order.getCustomerGroupId(), ids, null);
						} else if (ids.split(",").length == order.getMinimumGroupSize() - 2) {// 已达到成团人数
							log.info("拼团成功");
							ids = ids + "," + order.getCustomerId();
							storeOrderService.updateParCustomerIdAndState(order.getCustomerGroupId(), ids, 1);
//							String[] customerIds = ids.split(",");
//							for (String id : customerIds) {// 发送客服信息
//								TblCustomerBean customer = tblCustomerService.getCustomerById(Long.valueOf(id));
//								tblCustomerService.sendWechatMessage(customer.getOpenId(), message);
//							}
//							TblCustomerBean customer = tblCustomerService.getCustomerById(order.getStartCustomerId());
//							tblCustomerService.sendWechatMessage(customer.getOpenId(), message);

							//查询参与本次拼团用户订单
							List<OrderBean> list = storeOrderService.getStoreOrderbyCustomerGroupId(order.getCustomerGroupId());
							for (OrderBean orderBean : list) {
								//修改拼团状态
								orderService.paySpellgroupStroeOrder(orderBean.getPayCode(),2);
								sendVouchers(orderBean,orderBean.getCustomerId());//派提水券
								tblCustomerService.sendWechatMessage(orderBean.getOpenid(), message);
							}
						} else if (ids.split(",").length > order.getMinimumGroupSize() - 2) {// 已超过成团人数
							log.info("拼团人数>成团人数");
							ids = ids + "," + order.getCustomerId();
							storeOrderService.updateParCustomerIdAndState(order.getCustomerGroupId(), ids, 1);
							TblCustomerBean customer = tblCustomerService.getCustomerById(order.getCustomerId());
							sendVouchers(order,customer.getId());//派提水券
							tblCustomerService.sendWechatMessage(customer.getOpenId(), message);
						}
					}

					// 邀请人赠券
					TblCustomerBean customer = tblCustomerService.getCustomerById(order.getCustomerId());
					if (customer.getInviterId() > 0 && tblCustomerService.isFirstBuy(customer.getId()).equals(0)
							&& tblCustomerService.isStoreFirstBuy(customer.getId()).equals(1)) {
						//成功邀请获得10积分
						couponDao.updateIntegral(customer.getInviterId(), 10l);
						CouponForm couponForm2 = new CouponForm();
						couponForm2.setLimitRange(false);
						couponForm2.setWay(CouponEnum.INVITE_COUPON.getState());
						List<CouponBean> presentCoupon2 = couponService.getPresentCoupon(couponForm2);
						if (presentCoupon2 != null && presentCoupon2.size() > 0) {
							for (CouponBean coupon : presentCoupon2) {
								CouponCustomerBean couCusBean = new CouponCustomerBean();
								couCusBean.setQuantity(coupon.getSendMax());
								couCusBean.setCouponId(coupon.getId());
								couCusBean.setCustomerId(customer.getInviterId());
								couCusBean.setStartTime(coupon.getLogicStartTime());
								couCusBean.setEndTime(coupon.getLogicEndTime());
								couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
								couponService.insertCouponCustomer(couCusBean);
							}
						}
					}
				}
				returnResult = ReturnConstant.SUCCESS;
			} else {
				returnResult = ReturnConstant.FAIL;
			}
			writeMsgToWx(returnResult, response);
			return;

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			returnResult = ReturnConstant.FAIL;
			writeMsgToWx(returnResult, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public void sendVouchers(OrderBean order,Long customerId) {
		if(order.getVouchersId() > 0 || StringUtils.isNotBlank(order.getVouchersIds())){
			if(order.getTypeId().equals(28)) {//套餐类型
				List<String>  vouchersIds=Arrays.asList(StringUtils.split(order.getVouchersIds(),","));
				for(String v:vouchersIds) {
					carryWaterVouchersCustomerServiceImpl.add(Long.valueOf(v), customerId, order.getNum(), order.getId());
				}
			}else {
				carryWaterVouchersCustomerServiceImpl.add(order.getVouchersId(), customerId, order.getNum(), order.getId());
			}
		}
	}







	/**
	 * 商城下单入口
	 *
	 * @param product
	 * @param url
	 * @param payCode
	 * @param nowprice
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/unifiedStoreOrder")
	@ResponseBody
	public Map<String, Object> unifiedStoreOrder(String product, String payCode, String nowprice, String spbillCreateIp,
			String openId, String url) throws Exception {
		Integer companyId = machinesService.getCompanyIdByPayCode(payCode);
		WXPay wxPay = wxpayConfigFactory.getWXPay(companyId);
		WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
		MyWXRequest request = new MyWXRequest();
		request.setOpenid(openId);
		String nonce_str = UUID.randomUUID().toString().replaceAll("-", "");
		List<String> list = orderService.findItemName(product);
		String products = "";
		for (int a = 0; a < list.size(); a++) {
			if (a == list.size() - 1) {
				products = products + list.get(a);
			} else {
				products = products + list.get(a) + ",";
			}
		}
		request.setAppid(wxPayConfig.getAppID());
		request.setMch_id(wxPayConfig.getMchID());
		request.setDevice_info("WEB");
		request.setBody(products);//
		request.setTrade_type("JSAPI");
		request.setNotify_url(storeNotify);
		request.setNonce_str(nonce_str);
		request.setOut_trade_no(payCode);
		BigDecimal fee = new BigDecimal(nowprice);
		BigDecimal totalFee = fee.multiply(new BigDecimal(100));
		request.setTotal_fee(totalFee.intValue() + "");
		request.setSpbill_create_ip(spbillCreateIp);
		Map<String, String> mapParam = request.requestToMap(request, false);
		Map<String, String> unifiedOrder = wxPay.unifiedOrder(mapParam);
		Map<String, Object> map = Maps.newHashMap();
		Map<String, String> wxJsapiSignature = null;
		if (StringUtil.isNotBlank(url)) {
			wxJsapiSignature = wxTicketService.getJspidSignature(url, companyId);
		} else {
			wxJsapiSignature = wxTicketService.getJspidSignature(storePayUrl, companyId);
		}
		map.put("config", wxJsapiSignature);// 微信jssdk config用
		Map<String, String> payInfo = Maps.newHashMap();
		payInfo.put("appId", wxPayConfig.getAppID());
		payInfo.put("timeStamp", System.currentTimeMillis() / 1000 + "");
		payInfo.put("nonceStr", nonce_str);
		payInfo.put("package", "prepay_id=" + unifiedOrder.get("prepay_id"));
		payInfo.put("signType", "MD5");
		try {
			String sign = WXPayUtil.generateSignature(payInfo, wxPayConfig.getKey());
			payInfo.put("sign", sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("payInfo", payInfo);// 微信支付
		return map;

	}

	/**
	 * 商城组团下单入口
	 *
	 * @param product
	 * @param url
	 * @param payCode
	 * @param nowprice
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/unifiedStoreGroupOrder")
	@ResponseBody
	public Map<String, Object> unifiedStoreGroupOrder(String product, String payCode, String nowprice,
			String spbillCreateIp, String openId, String url) throws Exception {
		log.info("<OrderController>------<unifiedStoreGroupOrder>------start");
		Integer companyId = machinesService.getCompanyIdByPayCode(payCode);
		WXPay wxPay = wxpayConfigFactory.getWXPay(companyId);
		WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
		MyWXRequest request = new MyWXRequest();
		request.setOpenid(openId);
		String nonce_str = UUID.randomUUID().toString().replaceAll("-", "");
		String findShoppingGoogsName = orderService.findShoppingGoogsName(product);
		request.setAppid(wxPayConfig.getAppID());
		request.setMch_id(wxPayConfig.getMchID());
		request.setDevice_info("WEB");
		request.setBody(findShoppingGoogsName);//
		request.setTrade_type("JSAPI");
		request.setNotify_url(storeGroupNotify);
		request.setNonce_str(nonce_str);
		request.setOut_trade_no(payCode);
		BigDecimal fee = new BigDecimal(nowprice);
		BigDecimal totalFee = fee.multiply(new BigDecimal(100));
		request.setTotal_fee(totalFee.intValue() + "");
		request.setSpbill_create_ip(spbillCreateIp);
		Map<String, String> mapParam = request.requestToMap(request, false);
		Map<String, String> unifiedOrder = wxPay.unifiedOrder(mapParam);
		Map<String, Object> map = Maps.newHashMap();
		Map<String, String> wxJsapiSignature = null;
		if (StringUtil.isNotBlank(url)) {
			wxJsapiSignature = wxTicketService.getJspidSignature(url, companyId);
		} else {
			wxJsapiSignature = wxTicketService.getJspidSignature(storePayUrl, companyId);
		}
		map.put("config", wxJsapiSignature);// 微信jssdk config用
		Map<String, String> payInfo = Maps.newHashMap();
		payInfo.put("appId", wxPayConfig.getAppID());
		payInfo.put("timeStamp", System.currentTimeMillis() / 1000 + "");
		payInfo.put("nonceStr", nonce_str);
		payInfo.put("package", "prepay_id=" + unifiedOrder.get("prepay_id"));
		payInfo.put("signType", "MD5");
		try {
			String sign = WXPayUtil.generateSignature(payInfo, wxPayConfig.getKey());
			payInfo.put("sign", sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("payInfo", payInfo);// 微信支付
		return map;

	}

	/**
	 * 商城APP下单入口
	 *
	 * @param product
	 * @param url
	 * @param payCode
	 * @param nowprice
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/unifiedAppStoreOrder")
	@ResponseBody
	public Map<String, Object> unifiedAppStoreOrder(String product, String payCode, String nowprice, String spbillCreateIp,
			String openId, String url) throws Exception {
		Integer companyId = machinesService.getCompanyIdByPayCode(payCode);
		WXPay wxPay = wxpayConfigFactory.getWXPay(companyId);
		WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
		MyWXRequest request = new MyWXRequest();
		request.setOpenid(openId);
		String nonce_str = UUID.randomUUID().toString().replaceAll("-", "");
		List<String> list = orderService.findItemName(product);
		String products = "";
		for (int a = 0; a < list.size(); a++) {
			if (a == list.size() - 1) {
				products = products + list.get(a);
			} else {
				products = products + list.get(a) + ",";
			}
		}
		request.setAppid(wxPayConfig.getAppID());
		request.setMch_id(wxPayConfig.getMchID());
		request.setDevice_info("WEB");
		request.setBody(products);//
		request.setTrade_type("APP");
		request.setNotify_url(storeNotify);
		request.setNonce_str(nonce_str);
		request.setOut_trade_no(payCode);
		BigDecimal fee = new BigDecimal(nowprice);
		BigDecimal totalFee = fee.multiply(new BigDecimal(100));
		request.setTotal_fee(totalFee.intValue() + "");
		request.setSpbill_create_ip(spbillCreateIp);

		Map<String, String> mapParam = request.requestToMap(request, false);
		//则例map中不用加sign
		Map<String, String> unifiedOrder = wxPay.unifiedOrder(mapParam);
		Map<String, Object> map = Maps.newHashMap();
		Map<String, String> wxJsapiSignature = null;
		if (StringUtil.isNotBlank(url)) {
			wxJsapiSignature = wxTicketService.getJspidSignature(url, companyId);
		} else {
			wxJsapiSignature = wxTicketService.getJspidSignature(storePayUrl, companyId);
		}
		map.put("config", wxJsapiSignature);// 微信jssdk config用
		Map<String, String> payInfo = Maps.newHashMap();

		//签名返回给前端
		payInfo.put("appId", wxPayConfig.getAppID());
		payInfo.put("timeStamp", System.currentTimeMillis() / 1000 + "");
		payInfo.put("nonceStr", nonce_str);
		payInfo.put("signType", "MD5");
		payInfo.put("package", "Sign=WXPay");
		payInfo.put("prepay_id", unifiedOrder.get("prepay_id"));
		payInfo.put("partnerid","微信支付分配的商户号");

		//wxPayConfig.getKey() 与 wxPayConfig.getAppID() 需要修改
		try {
			String sign = WXPayUtil.generateSignature(payInfo, wxPayConfig.getKey());
			payInfo.put("sign", sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("payInfo", payInfo);// 微信支付
		return map;

	}

	/**
	 * 普通商户下单及服务商下单统一入口
	 *
	 * @author hebiting
	 * @date 2018年6月9日上午9:43:44
	 * @param orderId
	 * @param url
	 * @param req
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/unifiedOrder")
	@ResponseBody
	public Map<String, Object> unifiedOrderAll(Long orderId, String url, HttpServletRequest req, Model model)
			throws Exception {
		Map<String, Object> payRecord = orderService.getOrder(orderId);
		if (payRecord != null) {
			return unifiedOrder(payRecord, url, req, model);
		}
		return null;
	}

	/**
	 * 普通商户统一下单(详见https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1)
	 * 在发起微信支付前，需要调用统一下单接口，获取"预支付交易会话标识"
	 * 接口地址：https://api.mch.weixin.qq.com/pay/unifiedorder
	 *
	 * @param
	 *            请求对象，注意一些参数如appid、mchid等不用设置，方法内会自动从配置对象中获取到（前提是对应配置中已经设置）
	 */
	public Map<String, Object> unifiedOrder(Map<String, Object> payRecord, String url, HttpServletRequest req,
			Model model) throws Exception {
		String vmCode = payRecord.get("vendingMachinesCode").toString();
		Integer companyId = machinesService.getCompanyIdByVmCode(vmCode);
		WXPay wxPay = wxpayConfigFactory.getWXPay(companyId);
		WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
		UserVo userVo = UserUtil.getUser();
		MyWXRequest request = new MyWXRequest();
		request.setOpenid(userVo.getOpenId());
		String nonce_str = UUID.randomUUID().toString().replaceAll("-", "");
		request.setAppid(wxPayConfig.getAppID());
		request.setMch_id(wxPayConfig.getMchID());
		request.setDevice_info("WEB");
		request.setBody((String) payRecord.get("itemName"));//
		request.setAttach(payRecord.get("customerId").toString());
		request.setTrade_type("JSAPI");
		request.setNotify_url(notifyUrl);
		request.setNonce_str(nonce_str);
		String random = (int) ((Math.random() * 9 + 1) * 1000) + "";
		String outTradeNo = (String) payRecord.get("payCode") + random;// 订单号 25
																		// 位
																		// 后面加后缀，生成随机订单号以免重复
		request.setOut_trade_no(outTradeNo);
		BigDecimal fee = (BigDecimal) payRecord.get("price");
		BigDecimal totalFee = fee.multiply(new BigDecimal(100));
		request.setTotal_fee(totalFee.intValue() + "");
		request.setSpbill_create_ip(req.getRemoteHost());
		Map<String, String> mapParam = request.requestToMap(request, false);
		Map<String, String> unifiedOrder = wxPay.unifiedOrder(mapParam);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> wxJsapiSignature = null;
		if (StringUtil.isNotBlank(url)) {
			wxJsapiSignature = wxTicketService.getJspidSignature(url, companyId);
		} else {
			wxJsapiSignature = wxTicketService.getJspidSignature(payWebUrl, companyId);
		}
		map.put("config", wxJsapiSignature);// 微信jssdk config用
		Map<String, String> payInfo = Maps.newHashMap();
		payInfo.put("appId", wxPayConfig.getAppID());
		payInfo.put("timeStamp", System.currentTimeMillis() / 1000 + "");
		payInfo.put("nonceStr", nonce_str);
		payInfo.put("package", "prepay_id=" + unifiedOrder.get("prepay_id"));
		payInfo.put("signType", "MD5");
		try {
			String sign = WXPayUtil.generateSignature(payInfo, wxPayConfig.getKey());
			payInfo.put("sign", sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("payInfo", payInfo);// 微信支付
		return map;

	}

	/**
	 * 服务商
	 * 统一下单(详见https://pay.weixin.qq.com/wiki/doc/api/jsapi_sl.php?chapter=9_1)
	 * 在发起微信支付前，需要调用统一下单接口，获取"预支付交易会话标识"
	 * 接口地址：https://api.mch.weixin.qq.com/pay/unifiedorder
	 *
	 * @param
	 *            请求对象，注意一些参数如appid、mchid等不用设置，方法内会自动从配置对象中获取到（前提是对应配置中已经设置）
	 */
	// public Map<String, Object> unifiedOrderFacilitator(Map<String, Object>
	// payRecord,String mchId, String url, HttpServletRequest req, Model model)
	// throws WxPayException {
	// UserVo userVo = UserUtil.getUser();
	// WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
	// request.setOpenid(userVo.getOpenId());
	// String nonce_str = UUID.randomUUID().toString().replaceAll("-", "");
	// request.setSubMchId(mchId);
	// request.setAppid(wxFacilitatorConfig.getAppId());
	// request.setMchId(wxFacilitatorConfig.getMchId());
	// request.setDeviceInfo("WEB");
	// request.setBody((String) payRecord.get("itemName"));//
	// request.setTradeType("JSAPI");
	// request.setNotifyUrl(notifyUrl);
	// request.setNonceStr(nonce_str);
	// String random=(int)((Math.random()*9+1)*1000)+"";
	// String outTradeNo=(String) payRecord.get("payCode")+random;//订单号 25 位
	// 后面加后缀，生成随机订单号以免重复
	// request.setOutTradeNo(outTradeNo);
	// BigDecimal fee = (BigDecimal) payRecord.get("price");
	// BigDecimal totalFee = fee.multiply(new BigDecimal(100));
	// request.setTotalFee(totalFee.intValue());
	// request.setSpbillCreateIp(req.getRemoteHost());
	// WxPayUnifiedOrderResult result =
	// this.wxFacilitatorService.unifiedOrder(request);
	// Map<String, Object> map = Maps.newHashMap();
	// WxJsapiSignature wxJsapiSignature = null;
	// try {
	// wxJsapiSignature = wxMpService.createJsapiSignature(payWebUrl);//
	// } catch (WxErrorException e1) {
	// e1.printStackTrace();
	// }
	// map.put("config", wxJsapiSignature);//微信jssdk config用
	// Map<String, String> payInfo = Maps.newHashMap();
	// payInfo.put("appId", wxMpService.getWxMpConfigStorage().getAppId());
	// payInfo.put("timeStamp", System.currentTimeMillis() / 1000 + "");
	// payInfo.put("nonceStr", nonce_str);
	// payInfo.put("package", "prepay_id=" + result.getPrepayId());
	// payInfo.put("signType", "MD5");
	// try {
	// String sign = WXPayUtil.generateSignature(payInfo,
	// wxFacilitatorConfig.getMchKey());
	// payInfo.put("sign", sign);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// map.put("payInfo", payInfo);//微信支付
	// return map;
	//
	// }

	@PostMapping("/getOrder")
	@ResponseBody
	public Map<String, Object> getOrder(Long orderId) {
		return orderService.getOrder(orderId);
	}

	/**
	 * 商城购买会员入口
	 *
	 * @author why
	 * @date 2018年9月26日 下午3:06:39
	 * @param payCode
	 * @param price
	 * @param openId
	 * @param spbillCreateIp
	 * @param url
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/unifiedMemberOrder")
	@ResponseBody
	public Map<String, Object> unifiedMemberOrder(String product, String payCode, String price, String openId,
			String spbillCreateIp, String url) throws Exception {
		log.info("====来到购买会员支付接口-----unifiedMemberOrder====--------start");
		Integer companyId = CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId();
		WXPay wxPay = wxpayConfigFactory.getWXPay(companyId);
		WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
		MyWXRequest request = new MyWXRequest();
		request.setOpenid(openId);
		String nonce_str = UUID.randomUUID().toString().replaceAll("-", "");
		request.setAppid(wxPayConfig.getAppID());
		request.setMch_id(wxPayConfig.getMchID());
		request.setDevice_info("WEB");
		request.setBody(product);
		request.setTrade_type("JSAPI");
		// 回调地址配置
		request.setNotify_url(memberNotify);
		request.setNonce_str(nonce_str);
		request.setOut_trade_no(payCode);
		BigDecimal fee = new BigDecimal(price);
		BigDecimal totalFee = fee.multiply(new BigDecimal(100));
		request.setTotal_fee(totalFee.intValue() + "");
		request.setSpbill_create_ip(spbillCreateIp);
		Map<String, String> mapParam = request.requestToMap(request, false);
		Map<String, String> unifiedOrder = wxPay.unifiedOrder(mapParam);
		Map<String, Object> map = Maps.newHashMap();
		Map<String, String> wxJsapiSignature = null;
		if (StringUtil.isNotBlank(url)) {
			wxJsapiSignature = wxTicketService.getJspidSignature(url, companyId);
		} else {
			wxJsapiSignature = wxTicketService.getJspidSignature(storePayUrl, companyId);
		}
		map.put("config", wxJsapiSignature);// 微信jssdk config用
		Map<String, String> payInfo = Maps.newHashMap();
		payInfo.put("appId", wxPayConfig.getAppID());
		payInfo.put("timeStamp", System.currentTimeMillis() / 1000 + "");
		payInfo.put("nonceStr", nonce_str);
		payInfo.put("package", "prepay_id=" + unifiedOrder.get("prepay_id"));
		payInfo.put("signType", "MD5");
		try {
			String sign = WXPayUtil.generateSignature(payInfo, wxPayConfig.getKey());
			payInfo.put("sign", sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("payInfo", payInfo);// 微信支付
		log.info("====来到购买会员支付接口-----unifiedMemberOrder====--------end");
		return map;
	}

	/**
	 * 微信完成商城购买会员后的回调，用以确认订单是否完成支付，并更新状态
	 *
	 * @param xml
	 * @return
	 */
	@RequestMapping(value = "/memberNotify")
	public void payMemberNotify(@RequestBody String xml, HttpServletResponse response) {
		log.info("=======================memberNotify=======================start");
		String returnResult = null;
		try {
			Map<String, String> map = WXPayUtil.xmlToMap(xml);

			// 获取真实订单号
			String outTradeNo = (String) map.get("out_trade_no");
			outTradeNo = outTradeNo.substring(0, 25);
			MemberOrderBean memberOrder = memberOrderService.getMemberOrder(outTradeNo);

			Integer companyId = CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId();
			WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
			String result_code = (String) map.get("result_code");
			String transactionId = (String) map.get("transaction_id");
			boolean b = WXPayUtil.isSignatureValid(map, wxPayConfig.getKey());
			log.info("payMemberNotify----结果:" + b);
			if (b) {// 订单号 25位
				// 支付成功 更新状态
				if (("SUCCESS").equals(result_code)
						&& !("SUCCESS").equals(redisService.get(MachinesKey.orderFlag, outTradeNo))) {
					redisService.set(MachinesKey.orderFlag, outTradeNo, "SUCCESS");
					// 更新订单状态
					log.info("-----------更新会员订单状态--------------");
					memberOrderService.paySuccessMemberOrder(outTradeNo, transactionId);
					log.info("-----------获取订单信息加入会员--------------outTradeNo=" + outTradeNo);
					// 获取订单信息
					// 成为会员
					boolean addMember = memberOrderService.addMember(memberOrder);
					if (addMember) {// 成为会员之后进行优惠券赠送
						Long customerId = memberOrder.getCustomerId();
						CouponForm couponForm = new CouponForm();
						couponForm.setWay(CouponEnum.Member_COUPON.getState());
						couponForm.setUseWhere(CouponEnum.USE_MACHINES.getState());
						// 获取优惠券赠送方式 为会员赠券的优惠券信息
						List<CouponBean> presentCoupon = couponService.getPresentCoupon(couponForm);
						if (presentCoupon != null && presentCoupon.size() > 0) {
							presentCoupon.stream().forEach(coupon -> {
								CouponCustomerBean couCusBean = new CouponCustomerBean();
								couCusBean.setQuantity(coupon.getSendMax());
								couCusBean.setCouponId(coupon.getId());
								couCusBean.setCustomerId(customerId);
								couCusBean.setStartTime(coupon.getLogicStartTime());
								couCusBean.setEndTime(coupon.getLogicEndTime());
								couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
								couponService.insertCouponCustomer(couCusBean);
							});
						}
						log.info("=======================payMemberNotify=======================end");
					}
				}
				returnResult = ReturnConstant.SUCCESS;
			} else {
				log.info("=======================payMemberNotify=======================end");
				returnResult = ReturnConstant.FAIL;
			}
			log.info("=======================payMemberNotify=======================end");
			writeMsgToWx(returnResult, response);
			return;

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			returnResult = ReturnConstant.FAIL;
			writeMsgToWx(returnResult, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("=======================payMemberNotify=======================end");
		return;
	}

	@RequestMapping("/payBalance")
	@ResponseBody
	public Map<String, Object> payBalance(String payCode, BigDecimal price, HttpServletRequest req, String url)
			throws Exception {
		log.info("=======================payBalance=======================start");
		Long customerId = UserUtil.getUser().getId();
		log.info("<MemberOrderController>用户id============" + customerId);
		TblCustomerBean cus = tblCustomerService.getCustomerById(customerId);
		log.info("=====调用支付接口=====unifiedMemberBalance");
		Map<String, Object> map = unifiedMemberBalance("充值余额", payCode, price.toString(), cus.getOpenId(),
				req.getRemoteHost(), url);
		log.info("=======================payBalance=======================end");
		return map;
	}

	/**
	 * 充值余额支付接口
	 *
	 * @author why
	 * @date 2018年10月8日 下午2:38:11
	 * @param product
	 * @param payCode
	 * @param price
	 * @param openId
	 * @param spbillCreateIp
	 * @param url
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/unifiedMemberBalance")
	@ResponseBody
	public Map<String, Object> unifiedMemberBalance(String product, String payCode, String price, String openId,
			String spbillCreateIp, String url) throws Exception {
		log.info("====充值余额支付接口-----unifiedMemberBalance====--------start");
		//根据payCode查找订单
        MemberOrderBean memberOrder = memberOrderService.getMemberOrder(payCode);
        Integer companyId = machinesService.getCompanyIdByVmCode(memberOrder.getVmCode());
        //Integer companyId = CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId();
		WXPay wxPay = wxpayConfigFactory.getWXPay(companyId);
		WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
		MyWXRequest request = new MyWXRequest();
		request.setOpenid(openId);
		String nonce_str = UUID.randomUUID().toString().replaceAll("-", "");
		request.setAppid(wxPayConfig.getAppID());
		request.setMch_id(wxPayConfig.getMchID());
		request.setDevice_info("WEB");
		request.setBody(product);
		request.setTrade_type("JSAPI");
		// 回调地址配置
		request.setNotify_url(balanceNotify);
		request.setNonce_str(nonce_str);
		request.setOut_trade_no(payCode);
		BigDecimal fee = new BigDecimal(price);
		BigDecimal totalFee = fee.multiply(new BigDecimal(100));
		request.setTotal_fee(totalFee.intValue() + "");
		request.setSpbill_create_ip(spbillCreateIp);
		Map<String, String> mapParam = request.requestToMap(request, false);
		Map<String, String> unifiedOrder = wxPay.unifiedOrder(mapParam);
		Map<String, Object> map = Maps.newHashMap();
		Map<String, String> wxJsapiSignature = null;
		if (StringUtil.isNotBlank(url)) {
			wxJsapiSignature = wxTicketService.getJspidSignature(url, companyId);
		} else {
			//http://webapp.youshuidaojia.com:8080/cMain/pay
			wxJsapiSignature = wxTicketService.getJspidSignature(storePayUrl, companyId);
		}
		map.put("config", wxJsapiSignature);// 微信jssdk config用
		Map<String, String> payInfo = Maps.newHashMap();
		payInfo.put("appId", wxPayConfig.getAppID());
		payInfo.put("timeStamp", System.currentTimeMillis() / 1000 + "");
		payInfo.put("nonceStr", nonce_str);
		payInfo.put("package", "prepay_id=" + unifiedOrder.get("prepay_id"));
		payInfo.put("signType", "MD5");
		try {
			String sign = WXPayUtil.generateSignature(payInfo, wxPayConfig.getKey());
			payInfo.put("sign", sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("payInfo", payInfo);// 微信支付
		log.info("====充值余额支付接口-----unifiedMemberBalance====--------end");
		return map;
	}

	/**
	 * 捐献金额后 微信回调接口
	 *
	 * @author hjc
	 * @date 2018年10月9日 下午2:23:52
	 * @param xml
	 * @return
	 */
	@RequestMapping(value = "/balanceNotify")
	public void payBalanceNotify(@RequestBody String xml, HttpServletResponse response) {
		log.info("=======================payBalanceNotify=======================start");
		String returnResult = null;
		try {
			Map<String, String> map = WXPayUtil.xmlToMap(xml);

			// 获取真实订单号
			String outTradeNo = (String) map.get("out_trade_no");
			outTradeNo = outTradeNo.substring(0, 25);
			MemberOrderBean memberOrder = memberOrderService.getMemberOrder(outTradeNo);
            Integer companyId = machinesService.getCompanyIdByVmCode(memberOrder.getVmCode());

			//Integer companyId = CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId();
			WXPayConfig wxPayConfig = wxpayConfigFactory.getWXPayConfig(companyId);
			String result_code = (String) map.get("result_code");
			String transactionId = (String) map.get("transaction_id");
			boolean b = WXPayUtil.isSignatureValid(map, wxPayConfig.getKey());
			log.info("payBalanceNotify----结果:" + b);
			if (b) {// 订单号 25位
				// 支付成功 更新状态
				if (("SUCCESS").equals(result_code)
						&& !("SUCCESS").equals(redisService.get(MachinesKey.orderFlag, outTradeNo))) {
					redisService.set(MachinesKey.orderFlag, outTradeNo, "SUCCESS");
					// 更新订单状态
					log.info("-----------更新订单状态--------------");
					memberOrderService.paySuccessMemberOrder(outTradeNo, transactionId);
					log.info("-----------获取订单信息--------------outTradeNo=" + outTradeNo);

					// 修改用户余额
						if(memberOrder.getType()==0){
						memberOrderService.updateCustomerBalance(memberOrder);
					}
				}
				returnResult = ReturnConstant.SUCCESS;
			} else {
				log.info("=======================payBalanceNotify=======================end");
				returnResult = ReturnConstant.FAIL;
			}
			log.info("=======================payBalanceNotify=======================end");
			writeMsgToWx(returnResult, response);
			return;

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			returnResult = ReturnConstant.FAIL;
			writeMsgToWx(returnResult, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("=======================payBalanceNotify=======================end");
		return;
	}

	// 是否已关注公众号
	@RequestMapping("/checkFollow")
	@ResponseBody
	public Map<String, Object> checkFollow(String openId) {
		if (openId == null) {
			Long customerId = UserUtil.getUser().getId();
			TblCustomerBean customer = tblCustomerService.getCustomerById(customerId);
			openId = customer.getOpenId();
		}
		Map<String, Object> map = Maps.newHashMap();
		MyWXGzhConfig myWXGzhConfig = gzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String accessToken = wxTicketService.getAccessToken(myWXGzhConfig);
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openId;
		String jsonResult = HttpUtil.get(url);
		Map<String, Object> result = JsonUtil.toObject(jsonResult, new TypeReference<Map<String, Object>>() {
		});
		log.info("用户者信息" + result);
		Integer isFollow = 0;
		Integer subscribe = (Integer) result.get("subscribe");
		if (subscribe != null) {
			if (subscribe == 1) {
				isFollow = 1;
				log.info("用户已关注");
			}
		} else {
			isFollow = 1;
			log.info("用户已关注/subscribe为空");
		}
		map.put("follow", isFollow);
		return map;
	}

	/**
	 * 微信支付完成页
	 *
	 * @author hjc
	 * @param vmCode
	 * @return
	 */
	@RequestMapping("/storeOrderFininshPay")
	@ResponseBody
	public OrderResultBean storeOrderFininshPay(String vmCode) {
		OrderResultBean orderResultBean = redisService.get("orderReuslt_" + vmCode, OrderResultBean.class);
		if (orderResultBean != null) {
			Long customerId = UserUtil.getUser().getId();
			TblCustomerBean customer = tblCustomerService.getCustomerById(customerId);
			String openId = customer.getOpenId();
			if (openId == null) {
				orderResultBean.setFollow(1);
			} else {
				MyWXGzhConfig myWXGzhConfig = gzhConfigFactory
						.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
				String accessToken = wxTicketService.getAccessToken(myWXGzhConfig);
				String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid="
						+ openId;
				String jsonResult = HttpUtil.get(url);
				Map<String, Object> result = JsonUtil.toObject(jsonResult, new TypeReference<Map<String, Object>>() {
				});
				log.info("用户者信息" + result);
				Integer isFollow = 0;
				Integer subscribe = (Integer) result.get("subscribe");
				if (subscribe != null) {
					if (subscribe == 1) {
						isFollow = 1;
						log.info("用户已关注");
					}
				} else {
					isFollow = 1;
					log.info("用户已关注/subscribe为空");
				}
				orderResultBean.setFollow(isFollow);
			}
		}
		return orderResultBean;
	}


	/**
	 * 开通支付分回调POST
	 * @author hjc
	 * @param json
	 */
	@RequestMapping(value = "/startServiceNotify")
	public void startServiceNotify(@RequestBody String jsonResult, HttpServletResponse response) {
		//AEAD_AES_256_GCM算法

		ServiceStartInfo serviceInfo = JsonUtils.toObject(jsonResult, new TypeReference<ServiceStartInfo>() {});
		ResourceInfo  resource = serviceInfo.getResource();
		String afterResult=null;
		try {
			 afterResult=WXPayUtil.aesgcmDecrypt(resource.getAssociated_data(),resource.getNonce(),resource.getCiphertext());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("afterResult"+afterResult);
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isNotBlank(afterResult)) {
			map = JsonUtils.toObject(afterResult, new TypeReference<Map<String, Object>>() {
			});
		}else {

		}
		String openid=(String) map.get("openid");
		String status=(String) map.get("user_service_status");//USER_CLOSE_SERVICE USER_OPEN_SERVICE
		log.info(openid+"开关服务成功"+status);
		//更新tbl_customer表
		String returnResult="200";
		writeMsgToWx(returnResult, response);

	}

	@RequestMapping(value = "/createOrder")
	public void createOrder(@RequestBody String jsonResult, HttpServletResponse response) {

	    //HttpUrl httpurl = HttpUrl.parse(url);

		//https://api.mch.weixin.qq.com/payscore/smartretail-orders
//		appid
//		out_order_no
//		service_id
//		service_start_time
//		service_start_location
//		service_introduction
//		risk_amount
//		attach
		//need_user_conﬁrm
//		openid


	}
	/**
	 * 确认订单回调
	 * @author hjc
	 * @param json
	 */
	@RequestMapping(value = "/confirmOrderNotify")
	public void confirmOrderNotify(@RequestBody String jsonResult, HttpServletResponse response) {

		ServiceStartInfo serviceInfo = JsonUtils.toObject(jsonResult, new TypeReference<ServiceStartInfo>() {});
		ResourceInfo  resource = serviceInfo.getResource();
		String afterResult=null;
		try {
			 afterResult=WXPayUtil.aesgcmDecrypt(resource.getAssociated_data(),resource.getNonce(),resource.getCiphertext());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("afterResult"+afterResult);
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isNotBlank(afterResult)) {
			map = JsonUtils.toObject(afterResult, new TypeReference<Map<String, Object>>() {
			});
		}else {

		}
		String outTradeNo=(String) map.get("out_order_no");
		String finish_ticket=(String) map.get("finish_ticket");//完结凭着
		//更新订单
		com.server.module.trade.order.bean.PayRecordBean payRecordbean = payRecordDao.findPayRecordByPayCode(outTradeNo);

		//		if (("SUCCESS").equals(result_code)
//				&& !("SUCCESS").equals(redisService.get(MachinesKey.orderFlag, outTradeNo))) {
		if(!("CREATESUCCESS").equals(redisService.get(MachinesKey.orderConfirmFlag, outTradeNo))) {
			payRecordDao.updateOrderTicket(outTradeNo,finish_ticket);
			redisService.set(MachinesKey.orderConfirmFlag, outTradeNo, "CREATESUCCESS");
			VmbaseInfoDto baseInfo = vendingMachinesService.getBaseInfo(payRecordbean.getVendingMachinesCode());
			UserVo userVo=new UserVo();
			userVo.setId(payRecordbean.getCustomerId());
			userVo.setPayType(1L);
			userVo.setType(1);
			cusOpenService.openDoorV3(baseInfo,payRecordbean.getVendingMachinesCode(),payRecordbean.getWayNumber().intValue(),userVo);
		}
	}


	@RequestMapping(value = "/payNotify")
	public void pay1Notify(@RequestBody String xml, HttpServletResponse response) {


	}


/*	@ApiOperation(value = "更改配送状态", notes = "更改配送状态", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping("/deliveryCompleted")
	public ReturnDataUtil delivering(Long orderId){
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		boolean bean = orderService.delivering(orderId);
		if (bean) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("配送状态修改成功！");
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("配送状态修改失败！");
		}
		return  returnDataUtil;
	}*/

//	@RequestMapping(value = "/payNotify")
//	public void pay1Notify(@RequestBody String jsonResult, HttpServletResponse response) {
//		ServiceStartInfo serviceInfo = JsonUtils.toObject(jsonResult, new TypeReference<ServiceStartInfo>() {});
//		ResourceInfo  resource = serviceInfo.getResource();
//		String afterResult=null;
//		try {
//			 afterResult=WXPayUtil.aesgcmDecrypt(resource.getAssociated_data(),resource.getNonce(),resource.getCiphertext());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		log.info("afterResult"+afterResult);
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (StringUtil.isNotBlank(afterResult)) {
//			map = JsonUtils.toObject(afterResult, new TypeReference<Map<String, Object>>() {
//			});
//		}else {
//
//		}
//
//		redisService.set("orderUserId" + userId, "SUCCESS", 300);
//		orderService.paySuccessOrder(outTradeNo, transactionId);
//		PayRecordBean payRecord = payRecordService.findPayRecordByPayCode(outTradeNo);
//		if (payRecord != null) {
//			MachinesLAC machinesLAC = couponService.getMachinesLAC(payRecord.getVendingMachinesCode());
//			Long customerId = payRecord.getCustomerId();
//			CouponForm couponForm = new CouponForm(machinesLAC, CouponEnum.PURCHASE_COUPON.getState(),
//					CouponEnum.USE_MACHINES.getState());
//			List<CouponBean> presentCoupon = couponService.getPresentCoupon(couponForm);
//			if (presentCoupon != null && presentCoupon.size() > 0) {
//				presentCoupon.stream()
//						.filter(coupon -> !couponService.isReceive(customerId, coupon.getId()))
//						.forEach(coupon -> {
//							CouponCustomerBean couCusBean = new CouponCustomerBean();
//							couCusBean.setQuantity(coupon.getSendMax());
//							couCusBean.setCouponId(coupon.getId());
//							couCusBean.setCustomerId(customerId);
//							couCusBean.setStartTime(coupon.getLogicStartTime());
//							couCusBean.setEndTime(coupon.getLogicEndTime());
//							couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
//							couponService.insertCouponCustomer(couCusBean);
//						});
//			}
//
//			// 用户积分更新
////			List<PayRecordItemBean> payRecordItemList = payRecordItemService
////					.getListByPayRecordId(payRecord.getId());
////			for (PayRecordItemBean prib : payRecordItemList) {
////				ItemBasicBean itemBasicBean = itemBasicService.getItemBasic(prib.getBasicItemId());
////				if (itemBasicBean != null && !("克").equals(itemBasicBean.getUnitName())) {
////					TblCustomerBean findBen = tblCustomerService.findBen(customerId);
////					if (findBen != null) {// 会员积分双倍
////						log.info("=====是会员=====");
////						couponService.updateIntegral(customerId, prib.getNum() * 2l);
////					} else {
////						log.info("=====非会员=====");
////						couponService.updateIntegral(customerId, prib.getNum() * 1l);
////					}
////				}
////			}
//			// 邀请人赠券
//			TblCustomerBean customer = tblCustomerService.getCustomerById(customerId);
//			if (customer.getInviterId() > 0 && tblCustomerService.isFirstBuy(customer.getId()).equals(1)
//					&& tblCustomerService.isStoreFirstBuy(customer.getId()).equals(0)) {
//				//成功邀请获得10积分
//				couponDao.updateIntegral(customer.getInviterId(), 11l);
//				CouponForm couponForm2 = new CouponForm();
//				couponForm2.setLimitRange(false);
//				couponForm2.setWay(CouponEnum.INVITE_COUPON.getState());
//				List<CouponBean> presentCoupon2 = couponService.getPresentCoupon(couponForm2);
//				if (presentCoupon2 != null && presentCoupon2.size() > 0) {
//					for (CouponBean coupon : presentCoupon2) {
//						CouponCustomerBean couCusBean = new CouponCustomerBean();
//						couCusBean.setQuantity(coupon.getSendMax());
//						couCusBean.setCouponId(coupon.getId());
//						couCusBean.setCustomerId(customer.getInviterId());
//						couCusBean.setStartTime(coupon.getLogicStartTime());
//						couCusBean.setEndTime(coupon.getLogicEndTime());
//						couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
//						couponService.insertCouponCustomer(couCusBean);
//					}
//				}
//			}
//
//		} else if (("FAIL").equals(result_code)
//				&& StringUtil.isBlank(redisService.get(MachinesKey.orderFlag, outTradeNo))) {
//			redisService.set(MachinesKey.orderFlag, outTradeNo, "FAIL");
//			orderService.payFailSendMsg(userId != null ? Long.valueOf(userId) : null);
//		}
//	}
//	returnResult = ReturnConstant.SUCCESS;
//	writeMsgToWx(returnResult, response);
//
//	}
//
//
//



}

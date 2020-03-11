package com.server.module.zfb_trade.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayOpenPublicMessageSingleSendRequest;
import com.alipay.api.response.AlipayOpenPublicMessageSingleSendResponse;
import com.server.client.MachinesClient;
import com.server.company.MachinesService;
import com.server.module.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerService;
import com.server.module.coupon.*;
import com.server.module.payRecord.AliPayRecordService;
import com.server.module.product.ShoppingGoodsBean;
import com.server.module.product.ShoppingGoodsDao;
import com.server.module.store.*;
import com.server.module.trade.customer.TblCustomerBean;
import com.server.module.trade.customer.TblCustomerService;
import com.server.module.trade.memberOrder.MemberOrderBean;
import com.server.module.trade.memberOrder.MemberOrderService;
import com.server.module.trade.order.OrderService;
import com.server.module.trade.web.bean.HuaFaGoods;
import com.server.module.zfb_trade.bean.MsgDto;
import com.server.module.zfb_trade.factory.AlipayAPIClientFactory;
import com.server.module.zfb_trade.factory.AlipayConfigFactory;
import com.server.module.zfb_trade.factory.AlipayEnvConfigFactory;
import com.server.module.zfb_trade.paramconfig.AlipayConfig;
import com.server.module.zfb_trade.paramconfig.AlipayServiceEnvConfig;
import com.server.module.zfb_trade.util.AlipayNotify;
import com.server.module.zfb_trade.util.DateUtil;
import com.server.redis.MachinesKey;
import com.server.redis.RedisService;
import com.server.util.stateEnum.CouponEnum;
import com.server.util.stateEnum.PayStateEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping("/aliInfo")
public class AlipayNotifyController {


	public static Logger log = LogManager.getLogger(AlipayNotifyController.class); 
	@Autowired
	@Qualifier("aliPayRecordService")
	private AliPayRecordService payRecordService;
	@Autowired
	private AlipayNotify alipayNotify;
	@Autowired
	private AlipayAPIClientFactory alipayAPIClientFactory;
	@Autowired
	private AlipayEnvConfigFactory alipayEnvFactory;
	@Autowired
	private AlipayConfigFactory alipayConfigFactory;
	@Value("${alipay.all_order_url}")
	private String allOrderUrl;
	@Value("${alipay.template_id}")
	private String templateId;
	@Autowired
	private MachinesService machinesService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private OrderService orderService;
	@Autowired
    private MemberOrderService memberOrderService;
	@Autowired
	private StoreOrderService storeOrderService;
	@Autowired
	private ShoppingGoodsDao shoppingGoodsDao;
	@Autowired
	private CouponDao couponDao;
	@Autowired
	private CouponService couponService;
	@Autowired
	private CarryWaterVouchersCustomerService carryWaterVouchersCustomerServiceImpl;
	@Autowired
	private TblCustomerService tblCustomerService;
	@Autowired
	private MachinesClient machinesClient;
	/**
	 * 支付宝免密异步通知接口
	 * @author hebiting
	 * @date 2018年4月27日下午9:19:54
	 * @param request
	 * @param response
	 */
	@PostMapping("/notify")
	public void alipayNotify(HttpServletRequest request, HttpServletResponse response) {
		//log.info("<AlipayNotifyController--alipayNotify--start>");
		//log.info("支付宝异步通知接口");
		long time = System.currentTimeMillis();
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		try {
			String payCode = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
			Integer companyId = machinesService.getCompanyIdByPayCode(payCode);
			AlipayConfig alipayConfig = alipayConfigFactory.getAlipayConfig(companyId);
			if (alipayNotify.verify(params,alipayConfig)) {
				if(("TRADE_SUCCESS").equals(tradeStatus)){
					if(!("SUCCESS").equals(redisService.get(MachinesKey.orderFlag, payCode))){
							redisService.set(MachinesKey.orderFlag, payCode, "SUCCESS");
							String ptCode = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
							payRecordService.updatePayState(ptCode,payCode,PayStateEnum.PAY_SUCCESS.getState());
							// 消费提醒
							consumeDetail(payCode, ptCode);
					}
				}else if(("TRADE_FINISHED").equals(tradeStatus)){
					payRecordService.updatePayState(null,payCode,PayStateEnum.PAY_FINISHED.getState());
				}
//				else{
//					payRecordService.updatePayState("",payCode,PayStateEnum.NOT_PAY.getState());
//				}
				response.getWriter().write("success");
				response.getWriter().flush();
			} else {
				response.getWriter().write("failed");
				response.getWriter().flush();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//log.info("【结束请求】耗时=" + (System.currentTimeMillis() - time));
		//log.info("<AlipayNotifyController--alipayNotify--end>");
	}

	/**
	 * 支付宝网页支付异步通知接口
	 * 
	 * @author hebiting
	 * @date 2018年4月27日下午9:19:54
	 * @param request
	 * @param response
	 * @throws AlipayApiException 
	 */
	@PostMapping("/wapNotify")
	public void alipayWapNotify(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException {
		log.info("<AlipayNotifyController--alipayWapNotify--start>");
		long time = System.currentTimeMillis();
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		log.info("支付宝wap异步通知接口");
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
				params.put(name, valueStr);
		}
		try {
			log.info(request.getParameter("sign"));
			String payCode = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
			Integer companyId = machinesService.getCompanyIdByPayCode(payCode);
			AlipayServiceEnvConfig alipayServiceEnv = alipayEnvFactory.getAlipayServiceEnvConfig(companyId);
			if (AlipaySignature.rsaCheckV1(params, alipayServiceEnv.alipay_public_key, AlipayServiceEnvConfig.CHARSET_UTF8,AlipayServiceEnvConfig.SIGN_TYPE) || AlipaySignature.rsaCheckV1(params, alipayServiceEnv.alipay_public_key, AlipayServiceEnvConfig.CHARSET_UTF8,"RSA")) {
				log.info("支付宝wap异步通知签名校验"+tradeStatus);
				if(("TRADE_SUCCESS").equals(tradeStatus)){
					if(!("SUCCESS").equals(redisService.get(MachinesKey.orderFlag, payCode))){
							redisService.set(MachinesKey.orderFlag, payCode, "SUCCESS");
							String ptCode = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
							payRecordService.updatePayState(ptCode,payCode,PayStateEnum.PAY_SUCCESS.getState());
							// 消费提醒
							consumeDetail(payCode, ptCode);
					}
				}else if(("TRADE_FINISHED").equals(tradeStatus)){
					payRecordService.updatePayState(null,payCode,PayStateEnum.PAY_FINISHED.getState());
				}
//				else{
//					payRecordService.updatePayState("",payCode,PayStateEnum.NOT_PAY.getState());
//				}
				response.getWriter().write("success");
				response.getWriter().flush();
			} else {
				response.getWriter().write("failed");
				response.getWriter().flush();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//log.info("【结束请求】耗时=" + (System.currentTimeMillis() - time));
		//log.info("<AlipayNotifyController--alipayWapNotify--end>");
	}

	/**
	 *支付宝完成商城订单后的回调，用以确认订单是否完成支付，并更新状态
	 *
	 * @author hhh
	 * @date 2020-01-07
	 * @param request
	 * @param response
	 * @throws AlipayApiException
	 */
	@PostMapping("/appNotify")
	public void alipayAppNotify(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException {
		log.info("<AlipayNotifyController--AppNotify--start>");
		long time = System.currentTimeMillis();
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		log.info("支付宝App异步通知接口");
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		try {
			log.info(request.getParameter("sign"));
			String payCode = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
			Integer companyId = machinesService.getCompanyIdByPayCode(payCode);
			Integer distributionModel = orderService.getDistributionModelByPayCode(payCode);
			AlipayServiceEnvConfig alipayServiceEnv = alipayEnvFactory.getAlipayServiceEnvConfig(companyId);
			if (AlipaySignature.rsaCheckV1(params, alipayServiceEnv.alipay_public_key, AlipayServiceEnvConfig.CHARSET_UTF8,AlipayServiceEnvConfig.SIGN_TYPE)) {
				log.info("支付宝App异步通知签名校验"+tradeStatus);
				if(("TRADE_SUCCESS").equals(tradeStatus)){
					//判断一下商品的售货方式
					if (distributionModel == 1){
							redisService.set(MachinesKey.orderFlag, payCode, "SUCCESS");
							String ptCode = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
							orderService.updatePayState(ptCode,payCode,PayStateEnum.PAY_SUCCESS.getState(),0);
							// 消费提醒
							consumeDetail(payCode, ptCode);
						}else {
							redisService.set(MachinesKey.orderFlag, payCode, "SUCCESS");
							String ptCode = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
							orderService.updatePayState(ptCode,payCode,PayStateEnum.Delivering.getState(),0);
							// 消费提醒
							consumeDetail(payCode, ptCode);
					}

					OrderBean ob = orderService.getMessageByPayCode(payCode);
					List<OrderDetailBean> orderDetail = storeOrderService.getOrderDetail(payCode);
					TblCustomerBean customer = tblCustomerService.getCustomerById(ob.getCustomerId());
					List<HuaFaGoods> huaFaGoods = new ArrayList<HuaFaGoods>();
					Long customerId = orderDetail.get(0).getCustomerId();
					for(OrderDetailBean o:orderDetail) {
						HuaFaGoods hf=new HuaFaGoods();
						BeanUtils.copyProperties(o, hf);
						huaFaGoods.add(hf);
					}
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
								machinesClient.sendHuaFa(5,payCode, json);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}});
					t.start();

					// 给商城顾客增加存水
					//List<OrderDetailBean> orderDetail = storeOrderService.getOrderDetail(payCode);
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
							String[] vouchers= StringUtils.split(sgs.getVouchersIds(),",");
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
					//TblCustomerBean customer = tblCustomerService.getCustomerById(customerId);
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
				}else if(("TRADE_FINISHED").equals(tradeStatus)){
					payRecordService.updatePayState(null,payCode,PayStateEnum.PAY_FINISHED.getState());
				}
//				else{
//					payRecordService.updatePayState("",payCode,PayStateEnum.NOT_PAY.getState());
//				}
				response.getWriter().write("success");
				response.getWriter().flush();
			} else {
				response.getWriter().write("failed");
				response.getWriter().flush();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//log.info("【结束请求】耗时=" + (System.currentTimeMillis() - time));
		//log.info("<AlipayNotifyController--alipayWapNotify--end>");
	}

	public static String getSignCheckContentV1(Map<String, String> params) {
		if (params == null) {
			return null;
		} else {
			params.remove("sign");
			params.remove("sign_type");
			StringBuffer content = new StringBuffer();
			List<String> keys = new ArrayList(params.keySet());
			Collections.sort(keys);

			for(int i = 0; i < keys.size(); ++i) {
				String key = (String)keys.get(i);
				String value = (String)params.get(key);
				content.append((i == 0 ? "" : "&") + key + "=" + value);
			}

			return content.toString();
		}
	}
	/**
	 * 支付宝捐献通知接口
	 *
	 * @author hjc
	 * @date 2020年1月1日下午9:19:54
	 * @param request
	 * @param response
	 * @throws AlipayApiException
	 */
	@PostMapping("/balanceWapNotify")
	public void balanceWapNotify(HttpServletRequest request, HttpServletResponse response)  {
		//log.info("<AlipayNotifyController--alipayWapNotify--start>");
		long time = System.currentTimeMillis();
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		log.info("支付宝wap异步通知接口");
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		try {
			String payCode = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

            MemberOrderBean memberOrder = memberOrderService.getMemberOrder(payCode);
            Integer companyId = machinesService.getCompanyIdByVmCode(memberOrder.getVmCode());
			// companyId = machinesService.getCompanyIdByPayCode(payCode);
			AlipayServiceEnvConfig alipayServiceEnv = alipayEnvFactory.getAlipayServiceEnvConfig(companyId);
			log.info("校验payCode"+payCode);
			log.info("sign:"+params.get("sign"));
			log.info("sign_type:"+params.get("sign_type"));
			log.info(getSignCheckContentV1(params));
			boolean result= false;
			try {
				result = AlipaySignature.rsaCheckV2(params, alipayServiceEnv.alipay_public_key, AlipayServiceEnvConfig.CHARSET_UTF8,"RSA2");
			} catch (AlipayApiException e) {
				if(getSignCheckContentV1(params).contains("TRADE_SUCCESS")){
					if(!("SUCCESS").equals(redisService.get(MachinesKey.orderFlag, payCode))){
						redisService.set(MachinesKey.orderFlag, payCode, "SUCCESS");
						String ptCode = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
						memberOrderService.paySuccessMemberOrder(payCode, ptCode);
					}
				}else if(("TRADE_FINISHED").equals(tradeStatus)){
					log.info(payCode+"交易完成咯");
					//payRecordService.updatePayState(null,payCode,PayStateEnum.PAY_FINISHED.getState());
				}
				e.printStackTrace();
			}
			if (result) {
				log.info("支付宝wap异步通知签名校验"+tradeStatus);
				if(("TRADE_SUCCESS").equals(tradeStatus)){
					if(!("SUCCESS").equals(redisService.get(MachinesKey.orderFlag, payCode))){
						redisService.set(MachinesKey.orderFlag, payCode, "SUCCESS");
						String ptCode = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
                        memberOrderService.paySuccessMemberOrder(payCode, ptCode);
					}
				}else if(("TRADE_FINISHED").equals(tradeStatus)){
				    log.info(payCode+"交易完成咯");
					//payRecordService.updatePayState(null,payCode,PayStateEnum.PAY_FINISHED.getState());
				}

				response.getWriter().write("success");
				response.getWriter().flush();
			} else {
				response.getWriter().write("failed");
				response.getWriter().flush();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//log.info("【结束请求】耗时=" + (System.currentTimeMillis() - time));
		//log.ifo("<AlipayNotifyController--alipayWapNotify--end>");
	}

	public void dakfj() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("app_id", "2017120900470709");
		params.put("auth_app_id", "2017120900470709");
		params.put("buyer_id", "2088712116524377");
		params.put("buyer_logon_id", "188****8238");
		params.put("buyer_pay_amount", "0.01");
		params.put("charset", "UTF-8");
		params.put("fund_bill_list", "amount:0.01,fundChannel:PCREDIT");
		params.put("gmt_create", "2020-01-16 14:25:35");
		params.put("gmt_payment", "2020-01-16 14:25:35");
		params.put("invoice_amount", "0.01");
		params.put("notify_id", "2020011600222142536024371428548185");
		params.put("notify_time", "2020-01-16 14:25:36");
		params.put("notify_type", "trade_status_sync");
		params.put("out_trade_no", "3899885157915592842478525");
		params.put("point_amount", "0.00");
		params.put("receipt_amount", "0.01");
		params.put("seller_email", "529653848@qq.com");
		params.put("seller_id", "2088821945832190");
		params.put("subject", "捐款金额");
		params.put("total_amount", "0.01");
		params.put("trade_no", "2020011622001424371412829434");
		params.put("trade_status", "TRADE_SUCCESS");
		params.put("version", "1.0");
		params.put("sign_type", "RSA2");
		params.put("sign", "PyumvDUpFUIUDVX8Dm6oHWU5gzOA0qYKYjZVNJBRn2gLPdAZk5hNcL2yQCR2FrmPRCTbC8/xyL/q+OzkG6Je850YKUZ1vifHFWFDxilnLvrlSaPbZ5qXHIwKSrTiq6I51TFFpPiG6FHTqBHX/3nyd2OlHVxJURAo2ZsQDd8N0SOjfBcmZi5Nbjt1iWgGVUFnzDanSDManUyy40+P2uZr8b7RHSPvqolCVtjN5WeRH3B7WneSyfKQNyywnn5T2rH3OW+YshfLWUo/FIbD/mLX0BBGAVzlxUV9H2bfH/+qDNXq9OF+C/LudaEAl6WrfgnxMWVIN5213+KN70FgeuBKxA==");
		String a = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnfuNlGJyLRJHHRrAQ4hRt98pBIu7fwkQxtXw6qjzYRthGzP7fqVRAMjQq6IaaNRje1eIQy+dvF0O97SkXWy0vTezIyCxquYPBQ+DEGcEpDRgFBwhk1XFiyj5EZ+03z2SfXtvIBYv+1/f2DlmyMFpMvtnL8VwOKOr66SXtJMsYR6J2L7Ytha31gJwH0SSSv2VFdjSgxnr9NnDVSMg9JIhRgiQqvBzEC0BzIgAi0fKasxKpmFirwF7aE+rAMSKKBUjJZCd/IFtf2nCljfhXbjHFtP3v2VGAFZbJGeb8mfKDJy/jSm5Oa8ViAaf1eUoG0fyMyQ1f0GzyMbnwuRxGGwgBQIDAQAB";
		System.out.println("?????????");
		try {
			boolean re =AlipaySignature.rsaCheckV1(params, a, AlipayServiceEnvConfig.CHARSET_UTF8, "RSA2");
			if (re) {
				System.out.println("helolo");
			}else{
				System.out.println("你在干什么");
			}
		} catch (AlipayApiException e) {
			System.out.println("helolo");
			e.printStackTrace();
		}
		System.out.println("1");
	}
		/**
	 * 消费提醒
	 * 
	 * @author hebiting
	 * @date 2018年4月27日下午9:21:00
	 * @param payCode
	 * @param ptCode
	 */
	public void consumeDetail(String payCode, String ptCode) {
		//log.info("<AlipayNotifyController--consumeDetail--start>");
		//log.info("消费提醒");
		Integer companyId = machinesService.getCompanyIdByPayCode(payCode);
		AlipayConfig alipayConfig = alipayConfigFactory.getAlipayConfig(companyId);
		AlipayClient alipayClient = alipayAPIClientFactory.getAlipayClient(companyId);
		MsgDto msgDto = payRecordService.findMsgDto(payCode);
		AlipayOpenPublicMessageSingleSendRequest request = new AlipayOpenPublicMessageSingleSendRequest();
		request.setBizContent("{" + "\"to_user_id\":\"" + msgDto.getAlipayUserId() + "\"," + "\"template\":{"
				+ "\"template_id\":\"" + alipayConfig.getTemplate_id() + "\"," + "\"context\":{" + "\"head_color\":\"#000000\","
				+ "\"url\":\"" + allOrderUrl + "\"," + "\"action_name\":\"查看详情\"," + "\"first\":{"
				+ "\"color\":\"#000000\"," + "\"value\":\"广州优水到家工程\"" + "}," + "\"keyword1\":{"
				+ "\"color\":\"#000000\"," + "\"value\":\"" + msgDto.getVmCode() + "\"" + "}," + "\"keyword2\":{"
				+ "\"color\":\"#000000\"," + "\"value\":\"" + msgDto.getItemName() + "\"" + "}," + "\"keyword3\":{"
				+ "\"color\":\"#000000\"," + "\"value\":\"" + ptCode + "\"" + "}," + "\"keyword4\":{"
				+ "\"color\":\"#000000\"," + "\"value\":\""
				+ DateUtil.date2String(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS) + "\"" + "}," + "\"keyword5\":{"
				+ "\"color\":\"#000000\"," + "\"value\":\"" + msgDto.getPrice() + "\"" + "}," + "\"remark\":{"
				+ "\"color\":\"#000000\"," + "\"value\":\"谢谢您的支持!!!\"" + "}" + "}" + "}" + "}");
		try {
			AlipayOpenPublicMessageSingleSendResponse response = alipayClient.execute(request);
			if (response.isSuccess()) {
				//log.info("【调用成功】");
			} else {
				log.info("【调用失败】");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		//log.info("<AlipayNotifyController--consumeDetail--end>");
	}


}

package com.server.module.zfb_trade.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.request.ZolozAuthenticationCustomerFtokenQueryRequest;
import com.alipay.api.request.ZolozAuthenticationCustomerSmilepayInitializeRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.ZolozAuthenticationCustomerFtokenQueryResponse;
import com.alipay.api.response.ZolozAuthenticationCustomerSmilepayInitializeResponse;
import com.server.company.MachinesService;
import com.server.module.payRecord.AliPayRecordService;
import com.server.module.payRecord.PayRecordDto;
import com.server.module.trade.memberOrder.MemberOrderBean;
import com.server.module.trade.memberOrder.MemberOrderService;
import com.server.module.zfb_trade.bean.AliPayConfigBean;
import com.server.module.zfb_trade.factory.AlipayAPIClientFactory;
import com.server.module.zfb_trade.factory.AlipayConfigFactory;
import com.server.module.zfb_trade.factory.AlipayEnvConfigFactory;
import com.server.module.zfb_trade.module.customer.AliCustomerService;
import com.server.module.zfb_trade.paramconfig.AlipayConfig;
import com.server.module.zfb_trade.service.AlipayService;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.PayStateEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/alipay")
public class AlipayController {

	public static Logger log = LogManager.getLogger(AlipayController.class); 
	@Autowired
	private AlipayConfigFactory alipayConfigFactory;
	@Autowired
	private AlipayService alipayService;
	@Autowired
	@Qualifier("aliPayRecordService")
	private AliPayRecordService payRecordService;
	@Autowired
	private AliCustomerService customerService;
	@Autowired
	private AlipayAPIClientFactory alipayAPIClientFactory;
	@Value("${alipay.all_order_url}")
	private String allOrderUrl;
	@Value("${alipay.template_id}")
	private String templateId;
	@Autowired
	private MachinesService machinesService;
	@Autowired
	private AlipayEnvConfigFactory alipayEnvConfigFactory;
	@Autowired
	private MemberOrderService memberOrderService;
	/**
	 * 订单中进行付款，先进行免密支付，如果未扣款成功，则进行有密支付
	 * @author hebiting
	 * @date 2018年5月24日下午7:07:25
	 * @param payRecordId
	 * @param vmCode
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/wapPay")
	public void wapPay(@RequestParam Long payRecordId,@RequestParam String vmCode,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		log.info("<AlipayController--wapPay--start>");
		log.info("payRecordId:"+payRecordId);
//		String ptCode = null;
		PayRecordDto record = payRecordService.getOrderInfo(payRecordId);
//		if(record.getCustomerId()!=null && !record.getState().equals(PayStateEnum.PAY_SUCCESS.getState().toString())){
//			CustomerBean customer = customerService.queryById(record.getCustomerId());
//			if(customer!=null && StringUtil.isNotBlank(customer.getAlipayUserId())){
//				String agreementNo = alipayService.querySign(customer.getAlipayUserId(),vmCode);
//				if(StringUtil.isNotBlank(agreementNo)){
//					ptCode = alipayService.cutPayment(record.getTotalPrice(), record.getPayCode(), record.getItemName(), agreementNo);
//				}
//			}
//		}
		if(record !=null && !record.getState().equals(PayStateEnum.PAY_SUCCESS.getState().toString())){
			Integer companyId = machinesService.getCompanyIdByVmCode(vmCode);
			AlipayClient alipayClient = alipayAPIClientFactory.getAlipayClient(companyId);
			AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
			//AlipayTradeAppPayRequest aliAppPayRequest = new AlipayTradeAppPayRequest();			
			AlipayConfig alipayConfig = alipayConfigFactory.getAlipayConfig(companyId);
			alipayRequest.setReturnUrl(alipayConfig.wap_return_url.concat("?vmCode=").concat(vmCode));
			alipayRequest.setNotifyUrl(alipayConfig.wap_notify_url);// 在公共参数中设置回跳和通知地址
			alipayRequest.setBizContent("{" 
					+ " \"out_trade_no\":\""+record.getPayCode()+"\"," 
					+ " \"total_amount\":\""+record.getTotalPrice()+"\","
					+ " \"subject\":\""+record.getItemName()+"\"," 
					+ " \"product_code\":\"QUICK_WAP_PAY\"" 
					+ " }");// 填充业务参数
			String form = "";
			try {
				form = alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
			} catch (AlipayApiException e) {
				e.printStackTrace();
			}
			log.info("<AlipayController--wapPay--end>");
			response.setContentType("text/html;charset=" + "utf-8");
			response.getWriter().write(form);// 直接将完整的表单html输出到页面
			response.getWriter().flush();
			response.getWriter().close();
		}
	}

	/**
	 * 订单中进行付款，先进行免密支付，如果未扣款成功，则进行有密支付
	 * @author hjc
	 * @date 2018年5月24日下午7:07:25
	 * @param payCode
	 * @param vmCode
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/balanceAliPay")
	public void balanceAliPay(@RequestParam String payCode, HttpServletRequest request,
					   HttpServletResponse response) throws IOException {
		log.info("<AlipayController--balanceAliPay--start>");
		//Map<String, String> map = new HashMap<String, String>();

		MemberOrderBean memberOrder = memberOrderService.getMemberOrder(payCode);
		Integer companyId = machinesService.getCompanyIdByVmCode(memberOrder.getVmCode());//		if(record.getCustomerId()!=null && !record.getState().equals(PayStateEnum.PAY_SUCCESS.getState().toString())){

		if(memberOrder !=null && !memberOrder.getState().equals(PayStateEnum.PAY_SUCCESS.getState().toString())){
			AlipayClient alipayClient = alipayAPIClientFactory.getAlipayClient(companyId);
			AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
			//AlipayTradeAppPayRequest aliAppPayRequest = new AlipayTradeAppPayRequest();
			AlipayConfig alipayConfig = alipayConfigFactory.getAlipayConfig(companyId);
			//http://yms.youshuidaojia.com/aliUser/forward
			alipayRequest.setReturnUrl(alipayConfig.balance_wap_return_url.concat("?vmCode=").concat(memberOrder.getVmCode()));
			alipayRequest.setNotifyUrl(alipayConfig.balance_wap_notify_url);// 在公共参数中设置回跳和通知地址
			alipayRequest.setBizContent("{"
					+ " \"out_trade_no\":\""+memberOrder.getPayCode()+"\","
					+ " \"total_amount\":\""+memberOrder.getPrice()+"\","
					+ " \"subject\":\""+"捐款金额"+"\","
					+ " \"product_code\":\"QUICK_WAP_PAY\""
					+ " }");// 填充业务参数
			String form = "";
			try {
				form = alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
			} catch (AlipayApiException e) {
				e.printStackTrace();
			}
			log.info("<AlipayController--balanceAliPay--end>");
			response.setContentType("text/html;charset=" + "utf-8");
			response.getWriter().write(form);// 直接将完整的表单html输出到页面
			response.getWriter().flush();
			response.getWriter().close();
		}
	}


	/**
	 * 手机网站支付
	 * @author hebiting
	 * @date 2018年5月24日下午7:08:24
	 * @param payCode
	 * @param price
	 * @param vmCode
	 * @param itemName
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/wapPay2")
	public void wapPay2(@RequestParam String payCode,@RequestParam BigDecimal price,
			@RequestParam String vmCode,
			@RequestParam String itemName,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		log.info("<AlipayController--wapPay2--start>");
		
		Integer companyId = machinesService.getCompanyIdByVmCode(vmCode);
		AlipayClient alipayClient = alipayAPIClientFactory.getAlipayClient(companyId);
		AlipayConfig alipayConfig = alipayConfigFactory.getAlipayConfig(companyId);
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
		alipayRequest.setReturnUrl(alipayConfig.wap_return_url.concat("?vmCode=").concat(vmCode));
		alipayRequest.setNotifyUrl(alipayConfig.wap_notify_url);// 在公共参数中设置回跳和通知地址
		alipayRequest.setBizContent("{" 
				+ " \"out_trade_no\":\""+payCode+"\"," 
				+ " \"total_amount\":\""+price+"\","
				+ " \"subject\":\""+itemName+"\"," 
				+ " \"product_code\":\"QUICK_WAP_PAY\"" 
				+ " }");// 填充业务参数
		String form = "";
		try {
			form = alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		log.info("<AlipayController--wapPay2--end>");
		response.setContentType("text/html;charset=" + "utf-8");
		response.getWriter().write(form);// 直接将完整的表单html输出到页面
		response.getWriter().flush();
		response.getWriter().close();
	}
	/**
	 * 支付扣款
	 * 
	 * @author hebiting
	 * @date 2018年4月27日下午4:51:56
	 * @param price
	 * @param payCode
	 * @param itemName
	 * @param agreementNo
	 * @return
	 */
	@GetMapping("/doPay")
	public String cutPayment(@RequestParam("price") BigDecimal price, @RequestParam("payCode") String payCode,
			@RequestParam("itemName") String itemName, @RequestParam("agreementNo") String agreementNo) {
		log.info("<AlipayController--cutPayment--start>");
		String ptCode = "";
		ptCode = alipayService.cutPayment(price, payCode, itemName, agreementNo);
		log.info("<AlipayController--cutPayment--end>");
		return ptCode;
	}
	
	/**
	 * 查询是否签约
	 * @author hebiting
	 * @date 2018年5月24日下午7:08:39
	 * @param alipayUserId
	 * @return 返回签约号
	 */
	@GetMapping("/querySign")
	public String querySign(@RequestParam String alipayUserId,@RequestParam String vmCode){
		log.info("<AlipayController--querySign--start>");
		String agreementNo = null;
		agreementNo = alipayService.querySign(alipayUserId,vmCode);
		log.info("<AlipayController--querySign--end>");
		return agreementNo;
	}

	/**
	 * 更新支付宝参数配置
	 * @author hebiting
	 * @date 2018年8月7日上午10:21:44
	 * @param alipayConfig
	 * @return
	 */
	@PostMapping("/updateAliConfig")
	public synchronized Boolean updateConfig(@RequestBody AliPayConfigBean alipayConfig){
		// HBT  待解决：未能解决重新更新支付参数，负载均衡
//		boolean updateConfigClient = false;
//		boolean updateConfig = false;
//		boolean updateConfigEnv = alipayEnvConfigFactory.updateConfigEnv(alipayConfig);
//		if(updateConfigEnv){
//			updateConfig = alipayConfigFactory.updateConfig(alipayConfig);
//			updateConfigClient = alipayAPIClientFactory.updateConfigClient(alipayConfig);
//		}
//		if(updateConfigClient && updateConfigEnv && updateConfig){
//			return true;
//		}
//		return false;
		return true;
	}


	/**
	 * 支付宝刷脸付初始化请求
	 * @author hjc
	 * @date 2019年12月17日上午9:21:44
	 * @param metaInfo
	 * @return returnDataUtil
	 */
    @PostMapping("/faceInit")
	public ReturnDataUtil faceInit(@RequestParam String metaInfo) throws AlipayApiException {
		//AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","app_id","your private_key","json","GBK","alipay_public_key","RSA2");
        JSONObject zimmetainfo = JSON.parseObject(metaInfo);

        AlipayClient alipayClient = alipayAPIClientFactory.getAlipayClient(76);
		ZolozAuthenticationCustomerSmilepayInitializeRequest request = new ZolozAuthenticationCustomerSmilepayInitializeRequest();
		request.setBizContent("{" +
				"\"zimmetainfo\":\"{ \\\"apdidToken\\\": \\\"设备指纹\\\", " +
				"\\\"appName\\\": \\\"优水到家\\\"," +
				" \\\"appVersion\\\": \\\"应用版本\\\"," +
				" \\\"bioMetaInfo\\\": \\\"生物信息如2.3.0:3,-4\\\" }\"" +
				"  }");
		request.setBizContent(zimmetainfo.toJSONString());
		ZolozAuthenticationCustomerSmilepayInitializeResponse response = alipayClient.execute(request);
        ReturnDataUtil returnDataUtil=new ReturnDataUtil();

		if(response.isSuccess()){
		    String result=response.getResult();
            //"result": "{\"retCode\":\"\",\"retMessage\":\"\",\"result\":{\"zimId\":\" 唤人脸参数\",\"type\":\"zimInit\",\"zimInitClientData\":\"下发参数\"}}"
            FaceInitResult fir=JSON.parseObject(result,FaceInitResult.class);
            System.out.println(fir.getResult().zimId);
            System.out.println(fir.getResult().zimInitClientData);
            returnDataUtil.setReturnObject(fir.getResult());
            System.out.println("调用成功");
		} else {
            returnDataUtil.setReturnObject(null);
            System.out.println("调用失败");
		}
/*        {
            "retCode": "",
                "retMessage": "",
                "result": {
                    "zimId": " 唤人脸参数",
                    "type": "zimInit",
                    "zimInitClientData": "下发参数"
        }
        }*/
		return  returnDataUtil;
	}

	@PostMapping("/faceAliPay")
    public void faceAliPay(@RequestParam  String ftoken) throws AlipayApiException {
        //AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","app_id","your private_key","json","GBK","alipay_public_key","RSA2");
        AlipayClient alipayClient = alipayAPIClientFactory.getAlipayClient(76);

        AlipayTradePayRequest request = new AlipayTradePayRequest();
        //请求参数中scene固定为security_code；
        //请求参数中auth_code为刷脸成功获取到的ftoken值；

        //人脸初始化-开门(用户怎么办)
        //视觉订单生成
        //

        ZolozAuthenticationCustomerFtokenQueryRequest ftokenQueryRequest = new ZolozAuthenticationCustomerFtokenQueryRequest();
        ftokenQueryRequest.setBizContent("{" +
                "\"ftoken\":\"fp0593e8d5c136277f13fd\""
                +"}");
        ZolozAuthenticationCustomerFtokenQueryResponse ftokenQueryResponse = alipayClient.execute(ftokenQueryRequest);
        if(ftokenQueryResponse.isSuccess()){
            String uid = (String)ftokenQueryResponse.getUid();

        }


        request.setBizContent("{" +
                "\"out_trade_no\":\"20150320010101001\"," +
                "\"scene\":\"security_code\"," +
                "\"auth_code\":\""+ftoken+"\"," +
                //"\"product_code\":\"FACE_TO_FACE_PAYMENT\"," +
                "\"subject\":\"Iphone6 16G\"," +
                //"\"buyer_id\":\"2088202954065786\"," +
                //"\"seller_id\":\"2088102146225135\"," +
                "\"total_amount\":88.88," +
                //"\"trans_currency\":\"USD\"," +
                //"\"settle_currency\":\"USD\"," +
                //"\"discountable_amount\":8.88," +
                "\"body\":\"Iphone6 16G\"," +
/*                "      \"goods_detail\":[{" +
                "        \"goods_id\":\"apple-01\"," +
                "\"goods_name\":\"ipad\"," +
                "\"quantity\":1," +
                "\"price\":2000," +
                "\"goods_category\":\"34543238\"," +
                "\"categories_tree\":\"124868003|126232002|126252004\"," +
                "\"body\":\"特价手机\"," +
                "\"show_url\":\"http://www.alipay.com/xxx.jpg\"" +
                "        }]," +*/
                //"\"operator_id\":\"yx_001\"," +
                //"\"store_id\":\"NJ_001\"," +
                //"\"terminal_id\":\"NJ_T_001\"," +
                //"\"extend_params\":{" +
                //"\"sys_service_provider_id\":\"2088511833207846\"," +
                //"\"industry_reflux_info\":\"{\\\\\\\"scene_code\\\\\\\":\\\\\\\"metro_tradeorder\\\\\\\",\\\\\\\"channel\\\\\\\":\\\\\\\"xxxx\\\\\\\",\\\\\\\"scene_data\\\\\\\":{\\\\\\\"asset_name\\\\\\\":\\\\\\\"ALIPAY\\\\\\\"}}\"," +
               // "\"card_type\":\"S0JP0000\"" +
                //"    }," +
                //"\"timeout_express\":\"90m\"," +
                //"\"auth_confirm_mode\":\"COMPLETE：转交易支付完成结束预授权;NOT_COMPLETE：转交易支付完成不结束预授权\"," +
                ///"\"terminal_params\":\"{\\\"key\\\":\\\"value\\\"}\"," +
                //"\"promo_params\":{" +
                //"\"actual_order_time\":\"2018-09-25 22:47:33\"" +
                //"    }," +
                //"\"advance_payment_type\":\"ENJOY_PAY_V2\"," +
                //"\"is_async_pay\":ASYNC_PAY" +
                "  }");
        AlipayTradePayResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }


/*    @Test
    public void tet(){
        String result=
                "{\"retCode\":\"\",\"retMessage\":\"\",\"result\":{\"zimId\":\" 唤人脸参数\",\"type\":\"zimInit\",\"zimInitClientData\":\"下发参数\"}}";
        JSONObject obj= JSON.parseObject(result);//将json字符串转换为json对象
        FaceInitResult fir=JSON.parseObject(result,FaceInitResult.class);
        System.out.println(fir.getResult().zimId);
    }*/

}

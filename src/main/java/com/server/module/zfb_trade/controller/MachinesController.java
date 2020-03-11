package com.server.module.zfb_trade.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AntMerchantExpandAutomatApplyModifyRequest;
import com.alipay.api.request.AntMerchantExpandAutomatApplyUploadRequest;
import com.alipay.api.response.AntMerchantExpandAutomatApplyModifyResponse;
import com.alipay.api.response.AntMerchantExpandAutomatApplyUploadResponse;
import com.server.module.payRecord.AliPayRecordService;
import com.server.module.payRecord.PayRecordDto;
import com.server.module.payRecord.PayRecordForm;
import com.server.module.zfb_trade.bean.AssociateBean;
import com.server.module.zfb_trade.bean.EnterAliBean;
import com.server.module.zfb_trade.factory.AlipayAPIClientFactory;
import com.server.module.zfb_trade.factory.AlipayConfigFactory;
import com.server.module.zfb_trade.module.company.AliCompanyService;
import com.server.module.zfb_trade.module.company.CompanyBean;
import com.server.module.zfb_trade.module.logininfo.LoginInfoBean;
import com.server.module.zfb_trade.module.vminfo.AliVminfoService;
import com.server.module.zfb_trade.module.vminfo.VminfoDto;
import com.server.module.zfb_trade.module.vmway.AliVmwayService;
import com.server.module.zfb_trade.module.vmway.VendingMachinesWayBean;
import com.server.module.zfb_trade.paramconfig.AlipayConfig;
import com.server.module.zfb_trade.service.BooleanService;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.module.zfb_trade.util.enumparam.LoginInfoEnum;
import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.module.zfb_trade.util.enumparam.VmInfoStateEnum;
import com.server.redis.RedisService;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.CompanyEnum;

@RestController
@RequestMapping("/aliMachine")
public class MachinesController {

	public static Logger log = LogManager.getLogger(MachinesController.class); 
	@Autowired
	private BooleanService booleanService;
	@Autowired
	@Qualifier("aliCompanyService")
	private AliCompanyService companyService;
	@Autowired
	@Qualifier("aliVminfoService")
	private AliVminfoService vminfoService;
	@Autowired
	private AliVmwayService vmwayService;
	@Autowired
	private AliPayRecordService payRecordService;
	@Autowired
	private AlipayAPIClientFactory alipayClientFactory;
	@Autowired
	private AlipayConfigFactory alipayConfigFactory;
	@Autowired
	private RedisService redisService;
	
	/**
	 * 查询售货机信息
	 * @author hebiting
	 * @date 2018年5月21日下午3:49:42
	 * @param form
	 * @param machineType
	 * @param request
	 * @return
	 */
	@PostMapping("/queryVMList")
	public ReturnDataUtil queryVMList(@RequestParam(required=false) String form,
			@RequestParam(required=false) Integer machineType,
			HttpServletRequest request){
		log.info("<MachinesController--queryVMList--start>");
		LoginInfoBean bindLoginInfo = booleanService.isBindLoginInfo(request);
		if(bindLoginInfo!=null){
//			log.info("bindLoginInfo"+JsonUtil.toJson(bindLoginInfo));
			if(LoginInfoEnum.PRINCIPAL.getMessage().equals(bindLoginInfo.getIsPrincipal())){
				List<CompanyBean> companyList = companyService.findAllSonCompany(bindLoginInfo.getCompanyId());
				List<Integer> companyIdList = new ArrayList<Integer>();
				companyList.stream().forEach(company-> {
					companyIdList.add(company.getId());
				});
				String companyIds = StringUtils.join(companyIdList,",");
				List<VminfoDto> queryVMList = vminfoService.queryVMList(form, companyIds, machineType,VmInfoStateEnum.MACHINES_NORMAL.getCode());
				return ResultUtil.success(queryVMList);
			}else{
				List<VminfoDto> queryOwnVMList = vminfoService.queryOwnVMList(form, bindLoginInfo.getId(), machineType,VmInfoStateEnum.MACHINES_NORMAL.getCode());
				return ResultUtil.success(queryOwnVMList);
			}
		}
		return ResultUtil.error(ResultEnum.NOT_AUTHORIZED,null,false);
	}
	/**
	 * 查询售货机货道详情信息
	 * @author hebiting
	 * @date 2018年5月21日下午3:49:55
	 * @param vmCode
	 * @param wayNo
	 * @return
	 */
	@PostMapping("/queryDetail")
	public ReturnDataUtil queryDetail(@RequestParam String vmCode,
			@RequestParam(required=false) Integer wayNo){
		log.info("<MachinesController--queryDetail--start>");
		List<VendingMachinesWayBean> vmwList = vmwayService.queryByWayAndVmcode(vmCode);
		log.info("<MachinesController--queryDetail--end>");
		if(vmwList!=null&&vmwList.size()>0){
			return ResultUtil.success(vmwList);
		}
		return ResultUtil.error();
	}
	
	@PostMapping("/queryTradeDetail")
	public ReturnDataUtil queryTradeDetail(@RequestBody PayRecordForm payRecordForm){
		log.info("<MachinesController--queryPayDetail--start>");
		List<PayRecordDto> payRecordList = payRecordService.findPayRecord(payRecordForm);
		log.info("<MachinesController--queryPayDetail--end>");
		if(payRecordList!=null && payRecordList.size()>0){
			return ResultUtil.success(payRecordList);
		}
		return ResultUtil.error();
	}
	
	/**
	 * 机器入驻阿里
	 * @author hebiting
	 * @date 2018年8月1日上午9:15:50
	 * @return
	 */
	@PostMapping("/enterAli")
	public ReturnDataUtil enterAli(@RequestBody EnterAliBean enterAli){
		AlipayClient alipayClient = alipayClientFactory.getAlipayClient(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		AlipayConfig alipayConfig = alipayConfigFactory.getAlipayConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		enterAli.setProduct_user_id(alipayConfig.getPartner());
		enterAli.setMerchant_user_id(alipayConfig.getPartner());
		enterAli.setMachine_type("SMART_CONTAINER");
		enterAli.setMachine_cooperation_type("COOPERATION_EXCLUSIVE");
		enterAli.setMachine_delivery_date(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
		enterAli.setMachine_name(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyName());
		enterAli.setMerchant_user_type("OPERATOR_MERCHANT");
		AssociateBean associate = new AssociateBean();
		associate.setAssociate_type("DISTRIBUTORS");
		associate.setAssociate_user_id(alipayConfig.getPartner());
		enterAli.setAssociate(associate);
		AntMerchantExpandAutomatApplyUploadRequest request = new AntMerchantExpandAutomatApplyUploadRequest(); 
		String json = JsonUtil.toJson(enterAli);
		request.setBizContent(json);
		AntMerchantExpandAutomatApplyUploadResponse response;
		try {
			response = alipayClient.execute(request);
			if(response!= null && response.isSuccess()){ 
				log.info("调用成功"); 
				String alipayTerminalId = response.getAlipayTerminalId();
				log.info("调用成功:"+alipayTerminalId);
				redisService.setString("enterAli-"+enterAli.getTerminal_id(), json);
				return ResultUtil.success(alipayTerminalId);
			} else { 
				log.info("调用失败"); 
			}
		} catch (AlipayApiException e) {
			log.error("入驻出现异常！");
			e.printStackTrace();
		} 
		return ResultUtil.error();
	}
	
	/**
	 * 修改入驻阿里的机器信息
	 * @author hebiting
	 * @date 2018年8月1日下午2:18:43
	 * @return
	 */
	@PostMapping("/modifyEnterAli")
	public ReturnDataUtil modifyEnterAli(@RequestBody EnterAliBean enterAli){
		AlipayClient alipayClient = alipayClientFactory.getAlipayClient(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		AlipayConfig alipayConfig = alipayConfigFactory.getAlipayConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		enterAli.setProduct_user_id(alipayConfig.getPartner());
		enterAli.setMerchant_user_id(alipayConfig.getPartner());
		enterAli.setMachine_type("SMART_CONTAINER");
		enterAli.setMachine_cooperation_type("COOPERATION_EXCLUSIVE");
		enterAli.setMachine_delivery_date(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
		enterAli.setMachine_name(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyName());
		enterAli.setMerchant_user_type("OPERATOR_MERCHANT");
		AssociateBean associate = new AssociateBean();
		associate.setAssociate_type("DISTRIBUTORS");
		associate.setAssociate_user_id(alipayConfig.getPartner());
		enterAli.setAssociate(associate);
		AntMerchantExpandAutomatApplyModifyRequest request = new AntMerchantExpandAutomatApplyModifyRequest();
		String json = JsonUtil.toJson(enterAli);
		request.setBizContent(json);
		try {
			AntMerchantExpandAutomatApplyModifyResponse response = alipayClient.execute(request);
			if(response!= null && response.isSuccess()){ 
				log.info("调用成功"); 
				String alipayTerminalId = response.getAlipayTerminalId();
				log.info("调用成功:"+alipayTerminalId);
				redisService.setString("enterAli-"+enterAli.getTerminal_id(), json);
				return ResultUtil.success(alipayTerminalId);
			} else { 
				log.info("调用失败"); 
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return ResultUtil.error();
	}
}

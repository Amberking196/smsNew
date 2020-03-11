//package com.server.module.trade.wechatPay.config;
//
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.github.binarywang.demo.wxpay.config.WxFacilitatorConfig;
//import com.github.binarywang.demo.wxpay.config.WxFacilitatorProperties;
//import com.github.binarywang.wxpay.config.WxPayConfig;
//import com.github.binarywang.wxpay.service.WxPayService;
//import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
//
///**
// * @author Binary Wang
// */
//@Configuration
//@ConditionalOnClass(WxPayService.class)
//@EnableConfigurationProperties(value = { WxPayProperties.class, WxFacilitatorProperties.class })
//public class WxPayConfiguration {
//	@Autowired
//	private WxPayProperties properties;
//
//	@Bean(name = "wxPayConfig")
//	@ConditionalOnMissingBean
//	public WxPayConfig payConfig() {
//		WxPayConfig payConfig = new WxPayConfig();
//		payConfig.setAppId(this.properties.getAppId());
//		payConfig.setMchId(this.properties.getMchId());
//		payConfig.setMchKey(this.properties.getMchKey());
//		payConfig.setSubAppId(StringUtils.trimToNull(this.properties.getSubAppId()));
//		payConfig.setSubMchId(StringUtils.trimToNull(this.properties.getSubMchId()));
//		payConfig.setKeyPath(this.properties.getKeyPath());
//
//		return payConfig;
//	}
//
//	@Bean(name = "wxPayService")
//	// @ConditionalOnMissingBean
//	public WxPayService wxPayService(WxPayConfig wxPayConfig) {
//		System.out.println("hhhh");
//		System.out.println("wxPayService-WxPayConfig"+wxPayConfig.getMchId());
//		WxPayService wxPayService = new WxPayServiceImpl();
//		wxPayService.setConfig(wxPayConfig);
//		return wxPayService;
//	}
//
//	@Autowired
//	private WxFacilitatorProperties wfProperties;
//
//	@Bean(name = "wxFacilitatorConfig")
//	@ConditionalOnMissingBean
//	public WxFacilitatorConfig getWxFacilitatorConfig() {
//		WxFacilitatorConfig wxFacilitatorConfig = new WxFacilitatorConfig();
//		wxFacilitatorConfig.setAppId(this.wfProperties.getAppId());
//		wxFacilitatorConfig.setMchId(this.wfProperties.getMchId());
//		wxFacilitatorConfig.setMchKey(this.wfProperties.getMchKey());
//		wxFacilitatorConfig.setSubAppId(StringUtils.trimToNull(this.wfProperties.getSubAppId()));
//		wxFacilitatorConfig.setSubMchId(StringUtils.trimToNull(this.wfProperties.getSubMchId()));
//		wxFacilitatorConfig.setKeyPath(this.wfProperties.getKeyPath());
//		return wxFacilitatorConfig;
//	}
//
//	@Bean(name = "wxFacilitatorService")
//	public WxPayService wxFacilitatorService(WxFacilitatorConfig wxFacilitatorConfig) {
//		System.out.println("wxFacilitatorService-wxFacilitatorConfig"+wxFacilitatorConfig.getMchId());
//		WxPayService wxFacilitatorService = new WxPayServiceImpl();
//		wxFacilitatorService.setConfig(wxFacilitatorConfig);
//		return wxFacilitatorService;
//	}
//
//}

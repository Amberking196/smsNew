package com.server.module.zfb_trade.httpclient;

import org.apache.commons.httpclient.NameValuePair;

import lombok.Data;
import lombok.NoArgsConstructor;

/* *
 *类名：HttpRequest
 *功能：Http请求对象的封装
 *详细：封装Http请求
 *版本：3.3
 *日期：2011-08-17
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */
@NoArgsConstructor
@Data
public class HttpRequest {
	/** HTTP GET method */
	public static final String METHOD_GET = "GET";

	/** HTTP POST method */
	public static final String METHOD_POST = "POST";

	/**
	 * 待请求的url
	 */
	private String url = null;

	/**
	 * 默认的请求方式
	 */
	private String method = METHOD_POST;

	private int timeout = 0;

	private int connectionTimeout = 0;

	/**
	 * Post方式请求时组装好的参数值对
	 */
	private NameValuePair[] parameters = null;

	/**
	 * Get方式请求时对应的参数
	 */
	private String queryString = null;

	/**
	 * 默认的请求编码方式
	 */
	private String charset = "GBK";

	/**
	 * 请求发起方的ip地址
	 */
	private String clientIp;

	/**
	 * 请求返回的方式
	 */
	private HttpResultType resultType = HttpResultType.BYTES;

	public HttpRequest(HttpResultType resultType) {
		super();
		this.resultType = resultType;
	}
}

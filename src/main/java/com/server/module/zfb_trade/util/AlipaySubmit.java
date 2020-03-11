package com.server.module.zfb_trade.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

import com.server.module.zfb_trade.httpclient.HttpProtocolHandler;
import com.server.module.zfb_trade.httpclient.HttpRequest;
import com.server.module.zfb_trade.httpclient.HttpResponse;
import com.server.module.zfb_trade.httpclient.HttpResultType;
import com.server.module.zfb_trade.paramconfig.AlipayConfig;
import com.server.util.DateUtil;



/* *
 *类名：AlipaySubmit
 *功能：支付宝各接口请求提交类
 *详细：构造支付宝各接口表单HTML文本，获取远程HTTP数据
 *版本：3.3
 *日期：2012-08-13
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */
@Component
public class AlipaySubmit {

	/**
	 * 生成签名结果
	 * 
	 * @param sPara
	 *            要签名的数组
	 * @return 签名结果字符串
	 */
	public String buildRequestMysign(Map<String, String> sPara , AlipayConfig alipayConfig) {
		String prestr = AlipayCore.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		String mysign = "";
		if (AlipayConfig.SIGN_TYPE.equals("MD5")) {
			mysign = MD5.sign(prestr, alipayConfig.key, AlipayConfig.INPUT_CHARSET);
		}
		return mysign;
	}

	/**
	 * 生成要请求给支付宝的参数数组
	 * 
	 * @param sParaTemp
	 *            请求前的参数数组
	 * @return 要请求的参数数组
	 */
	private Map<String, String> buildRequestPara(Map<String, String> sParaTemp , AlipayConfig alipayConfig) {
		// 除去数组中的空值和签名参数
		Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
		// 生成签名结果
		String mysign = buildRequestMysign(sPara,alipayConfig);

		// 签名结果与签名方式加入请求提交参数组中
		sPara.put("sign", mysign);
		sPara.put("sign_type", AlipayConfig.SIGN_TYPE);

		return sPara;
	}

	/**
	 * 建立请求，以表单HTML形式构造（默认）
	 * 
	 * @param sParaTemp
	 *            请求参数数组
	 * @param strMethod
	 *            提交方式。两个值可选：post、get
	 * @param strButtonName
	 *            确认按钮显示文字
	 * @return 提交表单HTML文本
	 */
	public String buildRequest(Map<String, String> sParaTemp, String strMethod, String strButtonName , AlipayConfig alipayConfig) {
		// 待请求参数数组
		Map<String, String> sPara = buildRequestPara(sParaTemp,alipayConfig);
		List<String> keys = new ArrayList<String>(sPara.keySet());

		StringBuffer sbHtml = new StringBuffer();

		sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + alipayConfig.alipay_gateway_new
				+ "_input_charset=" + AlipayConfig.INPUT_CHARSET + "\" method=\"" + strMethod + "\">");

		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			String value = (String) sPara.get(name);

			sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
		}

		// submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
		sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");

		return sbHtml.toString();
	}

	/**
	 * 建立请求，以表单HTML形式构造（默认）
	 * 
	 * @param sParaTemp
	 *            请求参数数组
	 * @param strMethod
	 *            提交方式。两个值可选：post、get
	 * @param strButtonName
	 *            确认按钮显示文字
	 * @return 提交表单HTML文本
	 * @throws UnsupportedEncodingException
	 */
	public String buildRequest(Map<String, String> sParaTemp , AlipayConfig alipayConfig) throws UnsupportedEncodingException {
		// 待请求参数数组
		Map<String, String> sPara = buildRequestPara(sParaTemp,alipayConfig);
		List<String> keys = new ArrayList<String>(sPara.keySet());

		StringBuffer sbHtml = new StringBuffer();

		sbHtml.append(alipayConfig.alipay_gateway_new);
		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			String value = (String) sPara.get(name);

			if ("request_from_url".equals(name) || "agreement_sign_parameters".equals(name) || "subject".equals(name)) {
				sbHtml.append(name + "=" + URLEncoder.encode(value, AlipayConfig.INPUT_CHARSET) + "&");
			} else {
				sbHtml.append(name + "=" + value + "&");
			}
		}
		return sbHtml.substring(0, sbHtml.lastIndexOf("&"));
	}

	/**
	 * 建立请求，以表单HTML形式构造，带文件上传功能
	 * 
	 * @param sParaTemp
	 *            请求参数数组
	 * @param strMethod
	 *            提交方式。两个值可选：post、get
	 * @param strButtonName
	 *            确认按钮显示文字
	 * @param strParaFileName
	 *            文件上传的参数名
	 * @return 提交表单HTML文本
	 */
	public String buildRequest(Map<String, String> sParaTemp, String strMethod, String strButtonName,
			String strParaFileName , AlipayConfig alipayConfig) {
		// 待请求参数数组
		Map<String, String> sPara = buildRequestPara(sParaTemp,alipayConfig);
		List<String> keys = new ArrayList<String>(sPara.keySet());

		StringBuffer sbHtml = new StringBuffer();

		sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\"  enctype=\"multipart/form-data\" action=\""
				+ alipayConfig.alipay_gateway_new + "_input_charset=" + AlipayConfig.INPUT_CHARSET + "\" method=\""
				+ strMethod + "\">");

		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			String value = (String) sPara.get(name);

			sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
		}

		sbHtml.append("<input type=\"file\" name=\"" + strParaFileName + "\" />");

		// submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");

		return sbHtml.toString();
	}

	/**
	 * 建立请求，以模拟远程HTTP的POST请求方式构造并获取支付宝的处理结果
	 * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值 如：buildRequest("",
	 * "",sParaTemp)
	 * 
	 * @param strParaFileName
	 *            文件类型的参数名
	 * @param strFilePath
	 *            文件路径
	 * @param sParaTemp
	 *            请求参数数组
	 * @return 支付宝处理结果
	 * @throws Exception
	 */
	public String buildRequest(String strParaFileName, String strFilePath, Map<String, String> sParaTemp , AlipayConfig alipayConfig)
			throws Exception {
		// 待请求参数数组
		Map<String, String> sPara = buildRequestPara(sParaTemp,alipayConfig);

		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

		HttpRequest request = new HttpRequest(HttpResultType.BYTES);
		// 设置编码集
		request.setCharset(AlipayConfig.INPUT_CHARSET);

		request.setParameters(generatNameValuePair(sPara));
		request.setUrl(alipayConfig.alipay_gateway_new + "_input_charset=" + AlipayConfig.INPUT_CHARSET);
		HttpResponse response = httpProtocolHandler.execute(request, strParaFileName, strFilePath);
		if (response == null) {
			return null;
		}

		String strResult = response.getStringResult();

		return strResult;
	}

	/**
	 * MAP类型数组转换成NameValuePair类型
	 * @param properties MAP类型数组
	 * @return NameValuePair类型数组
	 */
	private static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
		NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
		}

		return nameValuePair;
	}

	/**
	 * 用于防钓鱼，调用接口query_timestamp来获取时间戳的处理函数 注意：远程解析XML出错，与服务器是否支持SSL等配置有关
	 * @return 时间戳字符串
	 * @throws IOException
	 * @throws DocumentException
	 * @throws MalformedURLException
	 */
	public String query_timestamp(AlipayConfig alipayConfig) throws MalformedURLException, DocumentException, IOException {
		// 构造访问query_timestamp接口的URL串
		String strUrl = alipayConfig.alipay_gateway_new + "service=query_timestamp&partner=" + alipayConfig.partner
				+ "&_input_charset" + AlipayConfig.INPUT_CHARSET;
		StringBuffer result = new StringBuffer();

		SAXReader reader = new SAXReader();
		Document doc = reader.read(new URL(strUrl).openStream());

		List<Node> nodeList = doc.selectNodes("//alipay/*");

		for (Node node : nodeList) {
			// 截取部分不需要解析的信息
			if (node.getName().equals("is_success") && node.getText().equals("T")) {
				// 判断是否有成功标示
				List<Node> nodeList1 = doc.selectNodes("//response/timestamp/*");
				for (Node node1 : nodeList1) {
					result.append(node1.getText());
				}
			}
		}

		return result.toString();
	}

	public String getShortLink(String longlink,AlipayConfig alipayConfig) throws Exception {
		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "alipay.mobile.short.link.apply");
		sParaTemp.put("_input_charset", AlipayConfig.INPUT_CHARSET);
		sParaTemp.put("partner", alipayConfig.partner);
		sParaTemp.put("timestamp", DateUtil.getCurrentTime());
		sParaTemp.put("real_url", longlink);
		sParaTemp.put("open_way", "ALIPAYS");
		sParaTemp.put("valid_time", "30");

		String result = this.buildRequest("", "", sParaTemp,alipayConfig);
		Document doc = DocumentHelper.parseText(result);
		if ("T".equals(selectNodeText("//alipay/is_success", doc)))
			return selectNodeText("//alipay/response/alipay/short_link_url", doc);

		return null;
	}

	public String selectNodeText(String path, Document doc) {
		Node node = doc.selectSingleNode(path);
		if (node != null) {
			return node.getText();
		}
		return "";
	}
}

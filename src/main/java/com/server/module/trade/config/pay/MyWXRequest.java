package com.server.module.trade.config.pay;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.server.module.zfb_trade.util.JsonUtil;


public class MyWXRequest {

	//公众账号ID
	private String appid;
	//商户号
	private String mch_id;
	//子商户公众账号ID
	private String sub_appid;
	//子商户号	
	private String sub_mch_id;
	//随机字符串	
	private String nonce_str;
	//签名	
	private String sign;
	//签名类型	
	private String sign_type;
	//商品描述	
	private String body;
	//商户订单号	
	private String out_trade_no;
	//总金额	
	private String total_fee;
	//终端IP	
	private String spbill_create_ip;
	//通知地址	
	private String notify_url;
	//交易类型	
	private String trade_type;
	//用户标识	
	private String openid;
	//设备号	
	private String device_info;
	//附加数据
	private String attach;
	
	
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getDevice_info() {
		return device_info;
	}
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getSub_appid() {
		return sub_appid;
	}
	public void setSub_appid(String sub_appid) {
		this.sub_appid = sub_appid;
	}
	public String getSub_mch_id() {
		return sub_mch_id;
	}
	public void setSub_mch_id(String sub_mch_id) {
		this.sub_mch_id = sub_mch_id;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	/**
	 * 将参数转化为map结构
	 * @author hebiting
	 * @date 2018年6月20日下午2:19:33
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws IntrospectionException 
	 */
	public Map<String,String> requestToMap(MyWXRequest request,boolean isFacilitator) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException{
		Class type = request.getClass();    
        Map<String,String> returnMap = new HashMap<String,String>();    
        BeanInfo beanInfo = Introspector.getBeanInfo(type);    
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();    
        for (int i = 0; i< propertyDescriptors.length; i++) {    
            PropertyDescriptor descriptor = propertyDescriptors[i];    
            String propertyName = descriptor.getName();    
            if(!isFacilitator && (("sub_mch_id").equals(propertyName) || ("sub_appid").equals(propertyName) )){
            	continue;
            }
            if (!propertyName.equals("class")) {  
                Method readMethod = descriptor.getReadMethod();    
                Object result = readMethod.invoke(request, new Object[0]);    
                if (result != null) {    
                    returnMap.put(propertyName, result.toString());    
                } else {    
                    returnMap.put(propertyName, "");    
                } 
            }    
        }    
        return returnMap;   
	}
	
	public static void main(String[] args){
		MyWXRequest request = new MyWXRequest();
		request.setAppid("1231231");
		request.setBody("shjashda ");
		try {
			Map<String, String> requestToMap = request.requestToMap(request,true);
			System.out.println(JsonUtil.toJson(requestToMap));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IntrospectionException e) {
			e.printStackTrace();
		}
	}
	
}

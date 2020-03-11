package com.server.module.trade.support;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.cglib.beans.BeanMap;

import com.google.common.collect.Maps;


/**
 * 
 * @author tianyh 
 * @Description:普通短信发送实体类
 */
public class SmsSendRequest {
	/**
	 * 用户账号，必填
	 */
	private String account;
	/**
	 * 用户密码，必填
	 */
	private String password;
	/**
	 * 短信内容。长度不能超过536个字符，必填
	 */
	private String msg;
	/**
	 * 机号码。多个手机号码使用英文逗号分隔，必填
	 */
	private String phone;
	
	
	/**
	 * 定时发送短信时间。格式为yyyyMMddHHmm，值小于或等于当前时间则立即发送，默认立即发送，选填
	 */
	private String sendtime;
	/**
	 * 是否需要状态报告（默认false），选填
	 */
	private String report;
	/**
	 * 下发短信号码扩展码，纯数字，建议1-3位，选填
	 */
	private String extend;
	/**
	 * 该条短信在您业务系统内的ID，如订单号或者短信发送记录流水号，选填
	 */
	private String uid;
	
	public SmsSendRequest() {
		
	}
	public SmsSendRequest(String msg, String phone) {
		super();
		this.account = "N1445247";
		this.password = "31oVBqdpPF9ec1";
		this.msg = msg;
		this.phone = phone;
	}
	
	public static <T> Map<String, Object> toMap(T bean) {  
	    Map<String, Object> map = Maps.newHashMap();  
	    if (bean != null) {  
	        BeanMap beanMap = BeanMap.create(bean);  
	       /* for (Object key : beanMap.keySet()) {  
	            map.put(key+"", beanMap.get(key));  
	        }*/    
	        map.put("account", beanMap.get("account"));
	        map.put("msg", beanMap.get("msg"));
	        map.put("password", beanMap.get("password"));
	        map.put("phone", beanMap.get("phone"));
	    }  
	    return map;  
	}  
    /** 
 * 将对象装换为map 
 * @param bean 
 * @return 
 */  
public static <T> Map<String, Object> beanToMap(T bean) {  
    Map<String, Object> map = Maps.newHashMap();  
    if (bean != null) {  
        BeanMap beanMap = BeanMap.create(bean);  
        for (Object key : beanMap.keySet()) {  
            map.put(key+"", beanMap.get(key));  
        }             
    }  
    return map;  
}  
	/*public Map<String,Object> toMap(){
		Map<String,Object> map=Maps.newHashMap();
		try {
			BeanMap.
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("phone="+this.getPhone());
		System.out.println(map.get("phone"));
		return map;
	}*/
	public SmsSendRequest(String account, String password, String msg, String phone) {
		super();
		this.account = account;
		this.password = password;
		this.msg = msg;
		this.phone = phone;
	}
	public SmsSendRequest(String account, String password, String msg, String phone, String report) {
		super();
		this.account = account;
		this.password = password;
		this.msg = msg;
		this.phone = phone;
		this.report=report;
	}
	
	public SmsSendRequest(String account, String password, String msg, String phone, String report,String sendtime) {
		super();
		this.account = account;
		this.password = password;
		this.msg = msg;
		this.phone = phone;
		this.sendtime=sendtime;
		this.report=report;
	}
	public SmsSendRequest(String account, String password, String msg, String phone, String sendtime,String report,String uid) {
		super();
		this.account = account;
		this.password = password;
		this.msg = msg;
		this.phone = phone;
		this.sendtime=sendtime;
		this.report=report;
		this.uid=uid;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSendtime() {
		return sendtime;
	}
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	public String getExtend() {
		return extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
}

package com.server.util;

import com.server.module.zfb_trade.util.JsonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class HttpUtil {

	private static final Logger log = LogManager.getLogger(HttpUtil.class);
	
	public static String post(String url){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String result = null;
		URIBuilder uri = null;
		try {
			uri = new URIBuilder(url);
			HttpPost post = new HttpPost(uri.build());
			//设置连接超时
			RequestConfig config = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			post.setConfig(config);
			response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity,"UTF-8");
			}else{
				log.info(url+"--请求失败--response:"+JsonUtil.toJson(response));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(response!=null){
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	public static String post(String url,String jsonParam){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String result = null;
		URIBuilder uri = null;
		try {
			uri = new URIBuilder(url);
			HttpPost post = new HttpPost(uri.build());
			post.setHeader("Content-Type","application/json;charset=UTF-8");
			HttpEntity valueEntity = new StringEntity(jsonParam,"UTF-8");
			post.setEntity(valueEntity);
			//设置连接超时
			RequestConfig config = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			post.setConfig(config);
			response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity,"UTF-8");
			}else{
				log.info(url+"--请求失败--response:"+JsonUtil.toJson(response));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(response!=null){
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * CloseableHttpClient未关闭
	 * @author hebiting
	 * @date 2018年11月15日下午4:17:15
	 * @param url
	 * @param jsonParam
	 * @return
	 */
	public static String post(URI url,String jsonParam){
		long a=System.currentTimeMillis();
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String result = null;
		try {
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-Type","application/json;charset=UTF-8");
			//post.setHeader("Connection","close");
			HttpEntity valueEntity = new StringEntity(jsonParam,"UTF-8");
			post.setEntity(valueEntity);
			//设置连接超时
			RequestConfig config = RequestConfig.custom().setSocketTimeout(45000).setConnectTimeout(10000).build();
			post.setConfig(config);
			response = client.execute(post);
			long b=System.currentTimeMillis();
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity,"UTF-8");
			}else{
				log.info(url+"--请求失败--response:"+JsonUtil.toJson(response));
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(response!=null){
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	public static String get(String url){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String result = null;
		URIBuilder uri = null;
		try {
			uri = new URIBuilder(url);
			HttpGet get = new HttpGet(uri.build());
			//设置连接超时
			RequestConfig config = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			get.setConfig(config);
			response = client.execute(get);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity,"UTF-8");
			}else{
				log.info(url+"--请求失败--response:"+JsonUtil.toJson(response));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(response!=null){
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	public static String get(URIBuilder url){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String result = null;
		try {
			HttpGet get = new HttpGet(url.build());
			//设置连接超时
			RequestConfig config = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			get.setConfig(config);
			response = client.execute(get);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity,"UTF-8");
			}else{
				log.info(url+"--请求失败--response:"+JsonUtil.toJson(response));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(response!=null){
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	public static String post(URIBuilder url){
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String result = null;
		try {
			HttpPost post = new HttpPost(url.build());
			//设置连接超时
			RequestConfig config = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			post.setConfig(config);
			response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity,"UTF-8");
			}else{
				log.info(url+"--请求失败--response:"+JsonUtil.toJson(response));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(response!=null){
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(client!=null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String get(URIBuilder url,int times){
		String result = null;
		try(CloseableHttpClient client = HttpClients.createDefault();){
			HttpGet get = new HttpGet(url.build());
			//设置连接超时
			RequestConfig config = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			get.setConfig(config);
			for(int i=0;i<times;i++){
				try(CloseableHttpResponse response = client.execute(get);){
					if(response.getStatusLine().getStatusCode() == 200){
						HttpEntity entity = response.getEntity();
						result = EntityUtils.toString(entity,"UTF-8");
						break;
					}else{
						log.info(url+"--请求失败--response:"+JsonUtil.toJson(response));
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} 
		return result;
	}
	
	public static String get(String url,int times){
		String result = null;
		try(CloseableHttpClient client = HttpClients.createDefault();){
			HttpGet get = new HttpGet(url);
			//设置连接超时
			RequestConfig config = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			get.setConfig(config);
			for(int i=0;i<times;i++){
				try(CloseableHttpResponse response = client.execute(get);){
					if(response.getStatusLine().getStatusCode() == 200){
						HttpEntity entity = response.getEntity();
						result = EntityUtils.toString(entity,"UTF-8");
						break;
					}else{
						log.info(url+"--请求失败--response:"+JsonUtil.toJson(response));
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return result;
	}
}

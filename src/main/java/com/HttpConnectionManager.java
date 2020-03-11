package com;
 
import java.io.IOException; 
import java.util.HashMap;
import java.util.Map; 

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest; 
import org.apache.http.ParseException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder; 
import org.apache.http.conn.socket.ConnectionSocketFactory; 
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory; 
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient; 
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager; 
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

@Component
public class HttpConnectionManager {
	 
    private static PoolingHttpClientConnectionManager clientConnectionManager=null;  
    private static CloseableHttpClient httpClient=null;  
    private static RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();  
    private final static Object syncLock = new Object();  
    static {  
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()  
                .register("https", SSLConnectionSocketFactory.getSocketFactory())  
                .register("http", PlainConnectionSocketFactory.getSocketFactory())  
                .build();  
        clientConnectionManager =new PoolingHttpClientConnectionManager(socketFactoryRegistry);  
        clientConnectionManager.setMaxTotal(50000);  
        clientConnectionManager.setDefaultMaxPerRoute(25000);  
    }  
      
    public static CloseableHttpClient getHttpClient(){  
        if(httpClient == null){  
            synchronized (syncLock){  
                if(httpClient == null){  
                    BasicCookieStore cookieStore = new BasicCookieStore();  
                    BasicClientCookie cookie = new BasicClientCookie("sessionID", "######");  
                    cookie.setDomain("#####");  
                    cookie.setPath("/");  
                    cookieStore.addCookie(cookie);  
                    httpClient =HttpClients.custom().setConnectionManager(clientConnectionManager).setDefaultCookieStore(cookieStore).setDefaultRequestConfig(config).build();  
                }  
               }  
            }  
        return httpClient;  
    }  
      
      
    public static HttpEntity httpGet(String url, Map<String,Object> headers){  
        CloseableHttpClient httpClient = getHttpClient();  
        HttpRequest httpGet = new HttpGet(url);  
        if(headers!=null&&!headers.isEmpty()){  
            httpGet = setHeaders(headers, httpGet);  
        }  
        CloseableHttpResponse response = null;  
        try{  
            response =httpClient.execute((HttpGet)httpGet);  
            HttpEntity entity = response.getEntity();  
            return entity;  
        }catch (Exception e){  
            e.printStackTrace();  
  
        }  
        return null;  
    }  
      
    /** 
       使用表单键值对传参 
    */  
    public static HttpEntity httpPost(String url,Map<String,Object> headers,String data){  
        CloseableHttpClient httpClient = getHttpClient();  
        HttpRequest request = new HttpPost(url);  
        if(headers!=null&&!headers.isEmpty()){  
            request = setHeaders(headers,request);  
        }  
        CloseableHttpResponse response = null;  
        //UrlEncodedFormEntity uefEntity;  
        try {  
            HttpPost httpPost = (HttpPost) request; 
            //uefEntity = new UrlEncodedFormEntity(data,"UTF-8");  
            //httpPost.setEntity(uefEntity);  
            if(data!=null) { 
                httpPost.setEntity(new StringEntity(data, ContentType.create("application/json", "UTF-8")));  
            }
            response=httpClient.execute(httpPost);  
            HttpEntity entity = response.getEntity();  
            return entity;  
        } catch (IOException e) {  
            e.printStackTrace();  
  
        }  
        return null;  
    }  
      
    /**
     * 设置请求头信息 
     * @param headers
     * @param request
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static HttpRequest setHeaders(Map<String,Object> headers, HttpRequest request) {  
        for (Map.Entry entry : headers.entrySet()) {  
            if (!entry.getKey().equals("Cookie")) {  
                request.addHeader((String) entry.getKey(), (String) entry.getValue());  
            } else {  
                Map<String, Object> Cookies = (Map<String, Object>) entry.getValue();  
                for (Map.Entry entry1 : Cookies.entrySet()) {  
                    request.addHeader(new BasicHeader("Cookie", (String) entry1.getValue()));  
                }  
            }  
        }  
        return request;  
    }  
  
    public static void main(String[] args) { 
    	String f="d";
    	String d="command:{"+f+"}"; 
    	 String url="http://youshui.natapp1.cc/adminUser/test";
    	// String url="http://127.0.0.1:6663/adminUser/test";
    	Map<String,Object> h=new HashMap<String,Object>();
    	h.put("token", "login");
    	HttpEntity httpEntity=HttpConnectionManager.httpPost(url, h, d); 
		String str="";
		try {
			str = EntityUtils.toString(httpEntity, "UTF-8");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Response:" + "\n" + str); 
    	//HttpConnectionManager.httpPost();
    }  
}

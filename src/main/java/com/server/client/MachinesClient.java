package com.server.client;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.dao.OrderDao;
import com.server.module.trade.order.vo.OpenDoorVo;
import com.server.module.trade.util.HuaFaResult;
import com.server.redis.RedisService;
import com.server.util.HttpUtil;
import com.server.util.JsonUtils;
import com.server.util.ReviseUtil;
import com.server.util.stateEnum.TransferStateEnum;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


@Component
public class MachinesClient {
	
	private final static Logger log = LogManager.getLogger(MachinesClient.class);
	
	@Value("${heartBeatHost}")
	private String heartBeatHost;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private RedisService redisService;

	public String sendHuaFa(Integer times,String payCode,String json)  {
		String back = null;
//		URI uri = null;
//		try {
//			uri = new URIBuilder().setScheme("http").setHost(112.74.173.67:8502).setPath("/openDoor").build();
//		} catch (URISyntaxException e) {
//			log.error("URISyntaxException",e);
//			e.printStackTrace();
//		}
		//String url="https://devapp.huafatech.com/app/water/orderInfo/createWaterOrderInfo";
		String url="http://localhost:8099/order/returnOrder";
		for(int i=0;i<times;i++){
			back = HttpUtil.post(url,json);
			HuaFaResult huaFaResult = JSON.parseObject(back,HuaFaResult.class);
			log.info("sendingPayFinishOrder"+payCode);
			if(huaFaResult !=null && huaFaResult.getSuccess().equals("true")){
				log.info(i+"发送成功");
				redisService.del(payCode);
				break;
			}
			redisService.setString(payCode,json);
			try {
				Thread.sleep((times+1)*5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return back;
	}

	public String sendCommand(UserVo userVo,String factoryNumber,String command){
		String returnValue = null;
		OpenDoorVo openDoorVo = new OpenDoorVo();
		openDoorVo.setFromClient(userVo.getId().toString());
		openDoorVo.setMessage(command);
		openDoorVo.setToClient(factoryNumber);
		log.info(JSON.toJSONString(openDoorVo));
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http").setHost(heartBeatHost).setPath("/openDoor").build();
		} catch (URISyntaxException e) {
			log.error("URISyntaxException",e);
			e.printStackTrace();
		}
		returnValue = HttpUtil.post(uri,JSON.toJSONString(openDoorVo));
		return returnValue;
	}
	
	public String sendCommand(OpenDoorVo openDoorVo){
		String returnValue = null;
		log.info(JSON.toJSONString(openDoorVo));
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http").setHost(heartBeatHost).setPath("/openDoor").build();
		} catch (URISyntaxException e) {
			log.error("URISyntaxException",e);
			e.printStackTrace();
		}
		returnValue = HttpUtil.post(uri,JSON.toJSONString(openDoorVo));
		return returnValue;
	}
	/**
	 * 发送命令如果返回结果失败，则重复请求times次
	 * @author hebiting
	 * @date 2019年3月5日上午10:31:06
	 * @param userVo
	 * @param factoryNumber
	 * @param command
	 * @param times
	 * @return
	 */
	public String sendCommandTimes(UserVo userVo,String factoryNumber,String command,Integer times){
		if(times == null || times<=0)
			times = 1;
		String back = null;
		for(int i=0;i<times;i++){
			back = sendCommand(userVo,factoryNumber,command);
			if(TransferStateEnum.SUCCESS.getState().equals(back))
				break;
		}
		return back;
	}

	public String restartMachines(String factoryNumber) {
		String returnValue = null;
		//获取重启机器命令
		String restartCommand = ReviseUtil.restartCommand();
//		String factoryNumber = orderDao.getVmCodeByFactoryNumber(vmCode);
//		if(StringUtil.isBlank(factoryNumber)){
//			log.info(vmCode+"：该机器出厂编号为空，无法向机器发送命令操作,开门用户信息：");
//			return returnValue;
//		}
		String reString = redisService.getString("HM-"+factoryNumber.trim());
		if(!reString.contains("s:0")) {
			log.info(factoryNumber+"机器非空闲"+reString);
			return null;//1失败
		}
		factoryNumber = factoryNumber.trim();
		returnValue = sendCommand(factoryNumber,restartCommand,"adminSystem");
		return returnValue;
	}
	
	public String sendCommand(String factNum,String command,String fromClient){
		String returnValue = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		OpenDoorVo openDoorVo = new OpenDoorVo();
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http").setHost(heartBeatHost).setPath("/openDoor").build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		openDoorVo.setFromClient(fromClient);
		openDoorVo.setMessage(command);
		openDoorVo.setToClient(factNum);
		HttpPost httpPost = new HttpPost(uri);
		String str = JSON.toJSONString(openDoorVo);// 将json对象转换为字符串
		log.info("重启命令"+str);
		StringEntity requestEntity = new StringEntity(str, "utf-8");
		requestEntity.setContentEncoding("UTF-8");
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setEntity(requestEntity);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			if(response != null && 200 == response.getStatusLine().getStatusCode()){
				String responseEntity = EntityUtils.toString(response.getEntity());
				returnValue = JsonUtils.toObject(responseEntity, new TypeReference<String>(){});
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(response!=null){
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(httpclient!=null){
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}

}

package com.server.module.trade.vendingMachinesWay;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.server.module.footmark.FootmarkBean;
import com.server.module.footmark.FootmarkDao;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.dao.OrderDao;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.order.domain.Way;
import com.server.module.trade.order.vo.OpenDoorVo;
import com.server.module.trade.util.UserUtil;
import com.server.module.trade.vendingMachinesWayItem.MoreGoodsWayDto;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.redis.RedisService;
import com.server.util.ReturnDataUtil;
import com.server.util.ReviseUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.ModeEnum;

/**
 * author name: yjr 
 * create time: 2018-04-12 14:04:38
 */
@Service
public class VendingMachinesWayServiceImpl implements VendingMachinesWayService {

	private static Logger log = LogManager.getLogger(VendingMachinesWayDaoImpl.class);
	@Autowired
	private VendingMachinesWayDao vendingMachinesWayDaoImpl;
	
	@Autowired
	private RedisService redisService;
	@Autowired
	private OrderDao orderDao;
	@Value("${heartBeatHost}")
	private String heartBeatHost;
	@Autowired
	private FootmarkDao footmarkDao;
	
	public ReturnDataUtil listPage(VendingMachinesWayCondition condition) {
		return vendingMachinesWayDaoImpl.listPage(condition);
	}

	public VendingMachinesWayBean add(VendingMachinesWayBean entity) {
		entity.setItemId(0l);
		entity.setState(30001);
		entity.setFullNum(0);
		entity.setNum(0);
		entity.setCreateTime(new Date());
		return vendingMachinesWayDaoImpl.insert(entity);
	}
	
	public ReturnDataUtil checkAdd(VendingMachinesWayBean entity){
		
		String sql="select count(*) from vending_machines_way where vendingMachinesCode=? and wayNumber=?";
		List<Object> list=Lists.newArrayList();
		list.add(entity.getVendingMachinesCode());
		list.add(entity.getWayNumber());
		long count=vendingMachinesWayDaoImpl.selectCount(sql, list);
		ReturnDataUtil re=new ReturnDataUtil();
		if(count>0){
			re.setStatus(0);
			re.setMessage("该货道已创建");
			re.setReturnObject(false);
			return re;
			
		}
		return re;
		
	}

	public boolean update(VendingMachinesWayBean entity) {
		return vendingMachinesWayDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return vendingMachinesWayDaoImpl.delete(id);
	}

	public List<VendingMachinesWayBean> list(VendingMachinesWayCondition condition) {
		return null;
	}

	public VendingMachinesWayBean get(Object id) {
		return vendingMachinesWayDaoImpl.get(id);
	}

	@Override
	public List<WayDto> listAll(String vmCode,int type) {
		//type=1 客户 不传type也当做是客户
		if(type<=1){
			return vendingMachinesWayDaoImpl.listAll(vmCode);

		}else if(type==2){//运维  增加货道重量数据
			String factoryNumber=orderDao.getFactoryNumberByVmCode(vmCode);
			String content = redisService.getString("HM-" + factoryNumber);
			log.info("运维首页机器心跳=="+content);
			if(content==null){
				return vendingMachinesWayDaoImpl.listAll(vmCode);

			}
			MachineInfo info=new MachineInfo(content);
			List<Way> listway=info.getList();
			List<WayDto> list=vendingMachinesWayDaoImpl.listAll(vmCode);
			for (WayDto wayDto : list) {
				for (Way way : listway) {
					if(wayDto.getWayNumber()==way.getWayNumber()){
						wayDto.setWeight(way.getWeight());
					}
				}
			}
			return list;
			
		}
		return Lists.newArrayList();
		
	}

	
	public ReturnDataUtil editWayAndItem(BindItemDto dto) {
		boolean b=vendingMachinesWayDaoImpl.editWayAndItem(dto);
		return new ReturnDataUtil(b);
	}
	/**
	 * 货道商品统计
	 */
	public ReturnDataUtil statisticsWayNum(StatisticsWayNumCondition condition){
		
		return vendingMachinesWayDaoImpl.statisticsWayNum(condition);
	}

	/**
	 * 获取售货机 温度
	 */
	@Override
	public String findTemperature(String vmCode) {
		//根据售货机编号 获取出厂编号
		String factoryNumber=orderDao.getFactoryNumberByVmCode(vmCode);
		//根据出厂编号 那都售货机心跳
		String content = redisService.getString("HM-" + factoryNumber);
		log.info("机器心跳=="+content);
		if(content!=null) {
			if(content.contains("c:")) {
				String[] split = content.split("c:");
				String substring = split[1].substring(0, 1);
				if(substring.equals("+")) {
					return split[1].substring(1, 3)+"℃";
				}else {
					return split[1].substring(0, 3)+"℃";
				}
			}else {
				return "机器无温度";
			}
		}
		return "机器无温度";
	}
	/**
	 * 获取售货机 音量volume
	 */
	@Override
	public String findVolume(String vmCode) {
		//根据售货机编号 获取出厂编号
		String factoryNumber=orderDao.getFactoryNumberByVmCode(vmCode);
		//根据出厂编号 那都售货机心跳
		String content = redisService.getString("HM-" + factoryNumber);
		log.info("机器心跳=="+content);
		if(content!=null) {
			if(content.contains("v:")) {
				String[] split = content.split("v:");
				String substring = split[1].substring(0, 2);
				if(substring.equals("99")){
					return "99";
				}
				else if(!Character.isDigit(substring.charAt(1))){//7&
					return String.valueOf(substring.charAt(0));
				}
				else {
					return String.valueOf(substring.charAt(1));//07
				}
			}else {
				return "无";
			}
		}
		return "无";
	}

	/**
	 * 更新售货机音量volume
	 */
	public Boolean updateVolume(String vmCode,String volume) {
		//根据售货机编号 获取出厂编号
		String factoryNumber=orderDao.getFactoryNumberByVmCode(vmCode);
		//根据出厂编号 那都售货机心跳
		String content = redisService.getString("HM-" + factoryNumber);
		log.info("机器心跳=="+content);
		UserVo userVo = UserUtil.getUser();
		//t:2;n:191500000002;d:1,1,1,1;s:0;p:0;w:3700,3500,2500,3650;c:+28;v:03&58
		//    n:191500000002;d:1,1,1,1;v:02&24$
		if(content!=null) {
			
/*				String[] splitt = content.split("v:");
				String newContent=content.replace(splitt[1].substring(0, 2), "0"+volume);
				redisService.setString("HM-" + factoryNumber,newContent);
				return true;*/
				
				String[] splitd = content.split(";");
				StringBuilder sb=new StringBuilder();
				sb.append(splitd[1]+";"+splitd[2]+";v:0"+volume);
				Integer command=ReviseUtil.checkCodeSum(sb.toString());
				sb.append("&"+command+"$");
				
				CloseableHttpClient httpclient = HttpClients.createDefault();
				OpenDoorVo openDoorVo = new OpenDoorVo();
				URI uri = null;
				try {
					uri = new URIBuilder().setScheme("http").setHost(heartBeatHost).setPath("/openDoor").build();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				openDoorVo.setFromClient(userVo.getId().toString());
				openDoorVo.setMessage(sb.toString());
				openDoorVo.setToClient(factoryNumber);
				log.info("发送修改音量指令"+sb.toString());
				HttpPost httpPost = new HttpPost(uri);
				String str = JSON.toJSONString(openDoorVo);// 将json对象转换为字符串
				StringEntity requestEntity = new StringEntity(str, "utf-8");
				requestEntity.setContentEncoding("UTF-8");
				httpPost.setHeader("Content-type", "application/json");
				httpPost.setEntity(requestEntity);
				CloseableHttpResponse response = null;
				try {
					response = httpclient.execute(httpPost);
					log.info("====response=====");
					log.info(response.toString());
					if (200 == response.getStatusLine().getStatusCode()){
						log.info("====responseSuccess=====");
						FootmarkBean footmark = new FootmarkBean(userVo, ModeEnum.UPDATE_VOLUME_MODE, vmCode);
						Long id = footmarkDao.insertFootmark(footmark);
						orderDao.saveHeartBeatLog(sb.toString(), factoryNumber, 'u',userVo==null?null:userVo.getId(),userVo==null?null:userVo.getType(),id);
						return true;
						//String responseEntity = EntityUtils.toString(response.getEntity());
						//returnValue = JsonUtil.toObject(responseEntity, new TypeReference<String>() {
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						response.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

		}
		return false;
	}

	@Override
	public List<MoreGoodsWayDto> getWayInfo(String vmCode,Integer type) {
		//log.info("<VendingMachinesWayServiceImpl--getWayInfo--start>");
		List<MoreGoodsWayDto> wayInfo = vendingMachinesWayDaoImpl.getWayInfo(vmCode);
		if(type == 2){
			String factoryNumber=orderDao.getFactoryNumberByVmCode(vmCode);
			String content = redisService.getString("HM-" + factoryNumber);
			log.info("运维首页机器心跳=="+content);
			if(StringUtil.isNotBlank(content)){
				MachineInfo info=new MachineInfo(content);
				List<Way> listway=info.getList();
				log.info("shuzu "+JsonUtil.toJson(listway));
				for (Way way : listway) {
					Integer wayNumber = way.getWayNumber();
					for(MoreGoodsWayDto moreGoodsWayDto : wayInfo){
						if(moreGoodsWayDto.getWayNumber().equals(wayNumber)){
							moreGoodsWayDto.setWayWeight(way.getWeight());
						}
					}
				}
			}
		}
		//log.info("<VendingMachinesWayServiceImpl--getWayInfo--end>");
		return wayInfo;
	}
	
	/**
	 * 根据售货机出厂编号 得到对应机器货道的商品规格 组成一个字符串 返回
	 */
	public String findItemStandard(String factnum) {
		String result = "g:";
		List<String> findItemStandard = vendingMachinesWayDaoImpl.findItemStandard(factnum);
		if (findItemStandard != null && findItemStandard.size()>0) {
			for (int i = 0; i < findItemStandard.size(); i++) {
				result = result.concat(findItemStandard.get(i));
				if (i!=findItemStandard.size()-1) {
					result = result.concat(",");
				}
			}
			Integer sum = ReviseUtil.checkCodeSum(result);
			result = result.concat("&" + sum + "$");
			return result;
		} else {
			return null;
		}
	}

}

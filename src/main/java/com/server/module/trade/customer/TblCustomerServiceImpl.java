package com.server.module.trade.customer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.server.module.trade.config.gzh.MyWXGzhConfig;
import com.server.module.trade.config.gzh.WXGzhConfigFactory;
import com.server.module.trade.config.gzh.WxTicketService;
import com.server.module.trade.customer.wxcustomer.WxCustomerService;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.CompanyEnum;

/**
 * author name: yjr create time: 2018-04-16 15:06:05
 */
@Service
public class TblCustomerServiceImpl implements TblCustomerService {

	private static Logger log = LogManager.getLogger(TblCustomerServiceImpl.class);
	@Autowired
	private TblCustomerDao tblCustomerDaoImpl;
	@Autowired
	private WxCustomerService wxCustomerService;
	@Autowired
	private WXGzhConfigFactory gzhConfigFactory;
	@Autowired 
	private WxTicketService wxTicketService;
	
	public ReturnDataUtil listPage(TblCustomerCondition condition) {
		return tblCustomerDaoImpl.listPage(condition);
	}

	public TblCustomerBean add(TblCustomerBean entity) {
		return tblCustomerDaoImpl.insert(entity);
	}

	public boolean update(TblCustomerBean entity) {
		return tblCustomerDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return tblCustomerDaoImpl.delete(id);
	}

	public List<TblCustomerBean> list(TblCustomerCondition condition) {
		return null;
	}

	public TblCustomerBean get(Object id) {
		return tblCustomerDaoImpl.get(id);
	}

	@Override
	public Integer isFirstBuy(Long customerId) {
		log.info("<TblCustomerServiceImpl--isFirstBuy--start>");
		Integer firstBuy = tblCustomerDaoImpl.isFirstBuy(customerId);
		log.info("<TblCustomerServiceImpl--isFirstBuy--end>");
		return firstBuy;
	}

	@Override
	public Integer isStoreFirstBuy(Long customerId) {
		log.info("<TblCustomerServiceImpl--isStoreFirstBuy--start>");
		Integer firstBuy = tblCustomerDaoImpl.isStoreFirstBuy(customerId);
		log.info("<TblCustomerServiceImpl--isStoreFirstBuy--end>");
		return firstBuy;
	}
	
	@Override
	public boolean updateCusVmCode(Long customerId, String vmCode) {
		log.info("<TblCustomerServiceImpl--updateCusVmCode--start>");
		boolean result = false;
		if(tblCustomerDaoImpl.cusVmCodeIsNull(customerId)){
			if(tblCustomerDaoImpl.updateCusVmCode(customerId, vmCode)){
				result = true;
			}
		}else{
			result = false;
		}
		log.info("<TblCustomerServiceImpl--updateCusVmCode--end>");
		return result;
	}
	
	public List<TblCustomerBean> getCustomerByCompanyId(Integer companyId){
		log.info("<TblCustomerServiceImpl--getCustomerByCompanyId--start>");
		List<TblCustomerBean> list = tblCustomerDaoImpl.getCustomerByCompanyId(companyId);
		log.info("<TblCustomerServiceImpl--getCustomerByCompanyId--end>");
		return list;
	}

	public TblCustomerBean getCustomerById(Long customerId) {
		log.info("<TblCustomerServiceImpl--getCustomerById--start>");
		TblCustomerBean tblCustomerBean = tblCustomerDaoImpl.getCustomerById(customerId);
		log.info("<TblCustomerServiceImpl--getCustomerById--end>");
		return tblCustomerBean;
	}


	@Override
	public TblCustomerBean findBen(Long customerId) {
		log.info("<TblCustomerServiceImpl--findBen--start>");
		TblCustomerBean findBen = tblCustomerDaoImpl.findBen(customerId);
		log.info("<TblCustomerServiceImpl--findBen--end>");
		return findBen;
	}
	
	@Override
	public void sendWechatMessage(String openId,String message) {
		log.info("<TblCustomerServiceImpl--sendWechatMessage--start>");
		MyWXGzhConfig myWXGzhConfig = gzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String accessToken=wxTicketService.getAccessToken(myWXGzhConfig);
        RestTemplate rest = new RestTemplate();
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken ;
        //{
        //	"touser":"OPENID",
        //	"msgtype":"text",
        //	"text":
        //	{
        //     	"content":"Hello World"
        //	}
    	//}
        Map<String,Object> param = new HashMap<>();
        param.put("touser", openId);
        param.put("msgtype", "text");
        
        Map<String,Object> text = new HashMap<>();
        text.put("content", message);
        param.put("text", text);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HttpEntity requestEntity = new HttpEntity(param, headers);
        Map result = null;
        try {
            ResponseEntity<Map> entity = rest.exchange(url, HttpMethod.POST, requestEntity,Map.class, new Object[0]);
            log.info("调用发送客服信息接口返回结果:" + entity.getBody());
            result = (Map) entity.getBody();
        } catch (Exception e) {
            log.error("调用发送客服信息接口异常",e);
        }
		log.info("<TblCustomerServiceImpl--sendWechatMessage--end>");
	}

	@Override
	public List<TblCustomerBean> myInviteRewards(){
		log.info("<TblCustomerServiceImpl>----<myInviteRewards>----start>");
		List<TblCustomerBean> list = tblCustomerDaoImpl.myInviteRewards();
		log.info("<TblCustomerServiceImpl>----<myInviteRewards>----end>");
		return list;
	}

	@Override
	public TblCustomerBean getCustomerBeanById(Long customerId) {
		log.info("<TblCustomerServiceImpl>----<getCustomerBeanById>----start>");
		TblCustomerBean bean = tblCustomerDaoImpl.getCustomerBeanById(customerId);
		log.info("<TblCustomerServiceImpl>----<getCustomerBeanById>----end>");
		return bean;
	}

	@Override
	public boolean updateCustomerBean(Long customerId, Long rewards, Integer type) {
		log.info("<TblCustomerServiceImpl>------<updateCustomerBean>------start");
		boolean flag = tblCustomerDaoImpl.updateCustomerBean(customerId, rewards, type);
		log.info("<TblCustomerServiceImpl>------<updateCustomerBean>------end");
		return flag;
	}
}

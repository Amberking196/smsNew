package com.server.module.trade.web.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.server.module.activeDegree.RegisterFlowBean;
import com.server.module.activeDegree.UserActiveDegreeDao;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.config.gzh.WechatUser;
import com.server.module.trade.customer.TblCustomerBean;
import com.server.module.trade.customer.TblCustomerDao;
import com.server.module.trade.customer.wxcustomer.WxCustomerBean;
import com.server.module.trade.customer.wxcustomer.WxCustomerDao;
import com.server.module.trade.customer.wxcustomer.WxCustomerService;
import com.server.module.trade.exception.MyException;
import com.server.module.trade.support.RedisConstants;
import com.server.module.zfb_trade.module.customer.AliCustomerDao;
import com.server.module.zfb_trade.module.vminfo.AliVminfoDao;
import com.server.module.zfb_trade.module.vminfo.VminfoDto;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CompanyEnum;
import com.server.util.stateEnum.PayTypeEnum;
@Service
public class UserServiceImpl implements UserService {
	public static Logger log = LogManager.getLogger( UserServiceImpl.class);    

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private TblCustomerDao customerDao;
	@Autowired
	private AliVminfoDao aliVminfoDao;
	@Autowired
	private WxCustomerDao wxCustomerDao;
	@Autowired
	private UserService userService;
	@Autowired
	private WxCustomerService wxCustomerService;
	@Autowired
	private UserActiveDegreeDao userActiveDegreeDao;
	@Autowired
	private AliCustomerDao aliCustomerDao;
	
	
	@Override
	public boolean checkUser(Integer type, String openId) {
         
		TblCustomerBean customer=customerDao.getCustomer(type, openId);
		log.info("customer="+customer);
		if(customer==null){//未注册
			 return false;
		}else{
			return true;
		}
	}
	
	@Override
	public TblCustomerBean getCustomer(Integer type, String openId) {
         
		TblCustomerBean customer=customerDao.getCustomer(type, openId);
		return customer;
	}

	@Override
	public Map<String,Object> register(Integer type,String openId, String phone, String smsCode,String vmCode,String inviteId,WechatUser wechatUser) throws MyException {
		TblCustomerBean customer=customerDao.getCustomerByPhone(phone);
		TblCustomerBean tblCustomerBean=getCustomer(PayTypeEnum.WEIXIN.getIndex(),openId);

		Map<String,Object> result = new HashMap<String,Object>();
		Boolean isInsert = false;
		log.info("customer=="+customer);
			if(customer==null){
				isInsert = true;
				//检测是否为微信商城上无手机号的用户
				if(tblCustomerBean==null) {
					customer=new TblCustomerBean();
					customer.setType(type);
					customer.setOpenId(openId);
					customer.setUpdateTime(new Date());
					customer.setCreateTime(new Date());
					customer.setPhone(phone);
					customer.setVmCode(vmCode);
					if(StringUtils.isNotBlank(inviteId)) {
						customer.setInviterId(Long.valueOf(inviteId));
					}
					if(aliCustomerDao.queryIsLoginUserByPhone(phone)) {
						customer.setIsLoginUser(1);
					}
					customer=customerDao.insert(customer);
					log.info("insert customer id="+customer.getId());
				}else {
					tblCustomerBean.setPhone(phone);
					customerDao.update(tblCustomerBean);
				}
			}else{
				customer.setOpenId(openId);
				customer.setUpdateTime(new Date());
				customerDao.update(customer);
			}
			if(tblCustomerBean==null) {
				WxCustomerBean wxCustomer = new WxCustomerBean();
				wxCustomer.setOpenId(openId);
				if(vmCode == null){
					//若是商城用户注册，则无vmCode，直接判定为广州优水公司下用户
					wxCustomer.setCompanyId(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
				}else{
					VminfoDto vminfo = aliVminfoDao.queryByVmCode(vmCode);
					wxCustomer.setCompanyId(vminfo!=null?vminfo.getCompanyId():null);
				}
				wxCustomer.setCustomerId(customer.getId());
				wxCustomer.setCreateTime(new Date());
				wxCustomer.setUpdateTime(new Date());
				if(wechatUser!=null && StringUtil.isNotBlank(wechatUser.getUnionid())) {
					wxCustomer.setUnionid(wechatUser.getUnionid());
				}
				wxCustomer = wxCustomerDao.insert(wxCustomer);
			}
			if(customer==null) {
				result.put("customerId", tblCustomerBean.getId());
			}else {
				result.put("customerId", customer.getId());
			}
			result.put("isInsert", isInsert);
			return result;

	}
	
	
	@Override
	public Map<String,Object> shopRegister(Integer type,String openId, String phone, String smsCode,String vmCode,WechatUser wechatUser) throws MyException {
		TblCustomerBean customer=customerDao.getCustomerByPhone(phone);
		Map<String,Object> result = new HashMap<String,Object>();
		Boolean isUpdate = false;
		log.info("customer=="+customer);
		//无手机号新用户
		if(customer==null){
			customer=new TblCustomerBean();
			customer.setType((int)UserVo.Pay_Type_WeiXin);
			customer.setOpenId(openId);
			customer.setUpdateTime(new Date());
			customer.setCreateTime(new Date());
			customer.setPhone(phone);
			//广州地区用户手动输入，其他地区自动绑定
			customer.setVmCode(vmCode);
			if(StringUtil.isNotBlank(wechatUser.getQr_scene_str())) {//从邀请二维码扫描关注
				customer.setInviterId(Long.valueOf(wechatUser.getQr_scene_str().substring(6)));
			}
			if(wechatUser.getSubscribe()!=null) {//记录用户是否关注公众号  
				customer.setFollow(wechatUser.getSubscribe());
			}
			if(wechatUser.getNickname()!=null) {//用户name 
				customer.setNickname(wechatUser.getNickname());
				customer.setHeadImgUrl(wechatUser.getHeadimgurl());
				customer.setSexId(wechatUser.getSex());
			}
			
			if(aliCustomerDao.queryIsLoginUserByPhone(phone)) {
				customer.setIsLoginUser(1);
			} 
			customer=userService.insert(customer);
			RegisterFlowBean registerBean = new RegisterFlowBean(customer.getId(),customer.getVmCode(),(int)UserVo.Pay_Type_WeiXin);
			userActiveDegreeDao.save(registerBean);
			VminfoDto vminfo = aliVminfoDao.queryByVmCode(vmCode);
			WxCustomerBean wxCustomer = new WxCustomerBean();
			wxCustomer.setOpenId(openId);
			wxCustomer.setCompanyId(vminfo!=null?vminfo.getCompanyId():null);
			//wxCustomer.setCompanyId(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
			wxCustomer.setCustomerId(customer.getId());
			wxCustomer.setCreateTime(new Date());
			wxCustomer.setUpdateTime(new Date());
			wxCustomer.setNickname(wechatUser.getNickname());
			wxCustomer.setHeadimgurl(wechatUser.getHeadimgurl());
			wxCustomer.setSex(wechatUser.getSex());
			if(StringUtil.isNotBlank(wechatUser.getUnionid())) {
				wxCustomer.setUnionid(wechatUser.getUnionid());
			}
			wxCustomer=wxCustomerService.insert(wxCustomer);
			isUpdate=true;
		}else{//支付宝用户
			if(wechatUser.getSubscribe()!=null) {//记录用户是否关注公众号  
				customer.setFollow(wechatUser.getSubscribe());
			}
			customer.setOpenId(openId);
			customer.setUpdateTime(new Date());
			customerDao.update(customer);
			isUpdate=true;
		}
		TblCustomerBean tblCustomerBean=getCustomer(PayTypeEnum.WEIXIN.getIndex(),openId);
		if(tblCustomerBean==null) {
			WxCustomerBean wxCustomer = new WxCustomerBean();
			wxCustomer.setOpenId(openId);
			if(vmCode == null){
				//若是商城用户注册，则无vmCode，直接判定为广州优水公司下用户
				wxCustomer.setCompanyId(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
			}else{
				VminfoDto vminfo = aliVminfoDao.queryByVmCode(vmCode);
				wxCustomer.setCompanyId(vminfo!=null?vminfo.getCompanyId():null);
			}
			wxCustomer.setCustomerId(customer.getId());
			wxCustomer.setCreateTime(new Date());
			wxCustomer.setUpdateTime(new Date());
			if(StringUtil.isNotBlank(wechatUser.getUnionid())) {
				wxCustomer.setUnionid(wechatUser.getUnionid());
			}
			if(wechatUser.getNickname()!=null) {//用户name 
				wxCustomer.setNickname(wechatUser.getNickname());
				wxCustomer.setHeadimgurl(wechatUser.getHeadimgurl());
				wxCustomer.setSex(wechatUser.getSex());
			}
			wxCustomer = wxCustomerDao.insert(wxCustomer);
		}

		result.put("customerId", customer.getId());
		result.put("isUpdate", isUpdate);
		return result;
	}
	
	@Override
	public Map<String,Object> appRegister(Integer type,String openId, String phone, String smsCode,String vmCode,WechatUser wechatUser) throws MyException {
		TblCustomerBean customer=customerDao.getCustomerByPhone(phone);
		Map<String,Object> result = new HashMap<String,Object>();
		Boolean isUpdate = false;
		log.info("customer=="+customer);
		
		if(customer==null){//无手机号新用户
			customer=new TblCustomerBean();
			customer.setType((int)UserVo.Pay_Type_WeiXin);
			customer.setAppOpenId(openId);
			customer.setUpdateTime(new Date());
			customer.setCreateTime(new Date());
			customer.setPhone(phone);
			customer.setVmCode("1988000080");//新用户默认绑定80
			
			if(wechatUser.getNickname()!=null) {//用户name 
				customer.setNickname(wechatUser.getNickname());
				customer.setHeadImgUrl(wechatUser.getHeadimgurl());
				customer.setSexId(wechatUser.getSex());
			}
			if(aliCustomerDao.queryIsLoginUserByPhone(phone)) {//内部员工标识
				customer.setIsLoginUser(1);
			} 
			
			
			customer=userService.insert(customer);
			
			RegisterFlowBean registerBean = new RegisterFlowBean(customer.getId(),customer.getVmCode(),(int)UserVo.Pay_Type_WeiXin);
			userActiveDegreeDao.save(registerBean);
		
			WxCustomerBean wxCustomer = new WxCustomerBean();
			wxCustomer.setAppOpenId(openId);
			wxCustomer.setCompanyId(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
			wxCustomer.setCustomerId(customer.getId());
			wxCustomer.setCreateTime(new Date());
			wxCustomer.setUpdateTime(new Date());
			wxCustomer.setNickname(wechatUser.getNickname());
			wxCustomer.setHeadimgurl(wechatUser.getHeadimgurl());
			wxCustomer.setSex(wechatUser.getSex());
			wxCustomer.setUnionid(wechatUser.getUnionid());
			wxCustomer=wxCustomerService.insert(wxCustomer);
			
			isUpdate=true;
		}else{//支付宝用户或首登app 的微信用户(无openid / 有openid)
			customer.setAppOpenId(openId);
			customer.setUpdateTime(new Date());
			customerDao.update(customer);
			//更新wxcustomer 支付宝生成wx表记录吗

			WxCustomerBean wxCustomer = new WxCustomerBean();
			wxCustomer.setAppOpenId(openId);
			wxCustomer.setCompanyId(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
			wxCustomer.setCustomerId(customer.getId());
			wxCustomer.setCreateTime(new Date());
			wxCustomer.setUpdateTime(new Date());
			wxCustomer.setNickname(wechatUser.getNickname());
			wxCustomer.setHeadimgurl(wechatUser.getHeadimgurl());
			wxCustomer.setSex(wechatUser.getSex());
			wxCustomer.setProvince(wechatUser.getProvince());
			wxCustomer.setCountry(wechatUser.getCountry());
			wxCustomer.setUnionid(wechatUser.getUnionid());
			wxCustomer.setOpenId(customer.getOpenId());
			
			if(customer.getOpenId()!=null) {
				wxCustomerService.update(wxCustomer);
			}else {
				wxCustomerService.insert(wxCustomer);			
			}
			isUpdate=true;
		}
		
//		TblCustomerBean tblCustomerBean=getCustomer(PayTypeEnum.APP.getIndex(),openId);
//		if(tblCustomerBean==null) {
//			WxCustomerBean wxCustomer = new WxCustomerBean();
//			wxCustomer.setOpenId(openId);
//			if(vmCode == null){
//				//若是商城用户注册，则无vmCode，直接判定为广州优水公司下用户
//				wxCustomer.setCompanyId(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
//			}else{
//				VminfoDto vminfo = aliVminfoDao.queryByVmCode(vmCode);
//				wxCustomer.setCompanyId(vminfo!=null?vminfo.getCompanyId():null);
//			}
//			wxCustomer.setCustomerId(customer.getId());
//			wxCustomer.setCreateTime(new Date());
//			wxCustomer.setUpdateTime(new Date());
//			if(wechatUser.getNickname()!=null) {//用户name 
//				wxCustomer.setNickname(wechatUser.getNickname());
//				wxCustomer.setHeadimgurl(wechatUser.getHeadimgurl());
//				wxCustomer.setSex(wechatUser.getSex());
//			}
//			wxCustomer = wxCustomerDao.insert(wxCustomer);
//		}

		result.put("customerId", customer.getId());
		result.put("isUpdate", isUpdate);
		return result;
	}
	
	@SuppressWarnings("unused")
	private boolean checkCode(String phone,String code){
		String redisCode=stringRedisTemplate.opsForValue().get(RedisConstants.SMS_CODE_PRE+phone);
		if(code.equals(redisCode))
			return true;
		return false;
	}

	@Override
	public TblCustomerBean insert(TblCustomerBean tblCustomerBean) {
		return customerDao.insert(tblCustomerBean);
	}
    
	
	
}

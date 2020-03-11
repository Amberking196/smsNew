package com.server.module.zfb_trade.module.logininfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.server.module.zfb_trade.paramconfig.H5URLConfig;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.module.zfb_trade.util.enumparam.LoginInfoEnum;
import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.util.MD5Utils;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

@Service("aliLoginInfoService")
public class AliLoginInfoServiceImpl implements AliLoginInfoService{

	public static Logger log = LogManager.getLogger(AliLoginInfoServiceImpl.class); 
	@Autowired
	@Qualifier("aliLoginInfoDao")
	private AliLoginInfoDao loginInfoDao;
	@Autowired
	private MD5Utils md5;
	@Autowired
	private H5URLConfig h5URLConfig;

	@Override
	public ReturnDataUtil doLogin(String loginCode, String password, String aliUserId, String vmCode) {
		log.info("<AliLoginInfoServiceImpl--doLogin--start>");
		loginCode = loginCode.trim();
		password = md5.getMd5(password);
		log.info("password:++++++++++++++++"+password);
		LoginInfoBean loginInfo = loginInfoDao.login(loginCode, password);
		// 判断用户存在且未绑定才能登录成功
		if(loginInfo!=null){
			if(StringUtil.isBlank(loginInfo.getAliUserId())){
				if(LoginInfoEnum.LOGIN_STATUS_AVAILABLE.getCode().equals(loginInfo.getStatus())){
					loginInfo.setAliUserId(aliUserId);
					loginInfoDao.update(loginInfo);
					if(identityAuth(loginInfo, vmCode)){
						log.info("【登录用户信息】loginInfo:"+ JsonUtil.toJson(loginInfo));
						return ResultUtil.success(h5URLConfig.replenishH5Page.concat("?vmCode=").concat(vmCode)
								.concat("&loginCode=").concat(loginInfo.getLoginCode()),true);
					}else{
						return ResultUtil.error(ResultEnum.NOT_AUTHORIZED,null);
					}
				}
				return ResultUtil.error(ResultEnum.USER_INVALID,null);
			}
			return ResultUtil.error(ResultEnum.USER_HASBIND,null);
		}
		return ResultUtil.error(ResultEnum.USER_NOT_CORRECT,null);
	}

	@Override
	public boolean identityAuth(LoginInfoBean loginInfo, String vmCode) {
		log.info("<AliLoginInfoServiceImpl--identityAuth--start>");
		//必须是未禁用的帐号
		if(loginInfo==null || !loginInfo.getStatus().equals(1)){
			return false;
		}
		if(LoginInfoEnum.PRINCIPAL.getMessage().equals(loginInfo.getIsPrincipal())){
			return true;
		}
		if(loginInfoDao.isDuty(loginInfo.getId(), vmCode)){
			return true;
		}
		return false;
	}

	@Override
	public ReturnDataUtil queryLogin(String loginCode, String password, String alipayUserId) {
		log.info("<AliLoginInfoServiceImpl--queryLogin--start>");
		loginCode = loginCode.trim();
		password = md5.getMd5(password);
		LoginInfoBean loginInfo = loginInfoDao.login(loginCode, password);
		// 判断用户存在且未绑定才能登录成功
		if(loginInfo!=null){
			if(StringUtil.isBlank(loginInfo.getAliUserId())){
				if(LoginInfoEnum.LOGIN_STATUS_AVAILABLE.getCode().equals(loginInfo.getStatus())){
					loginInfo.setAliUserId(alipayUserId);
					loginInfoDao.update(loginInfo);
					return ResultUtil.success(h5URLConfig.vmListPage);
				}
				return ResultUtil.error(ResultEnum.USER_INVALID,null);
			}
			return ResultUtil.error(ResultEnum.USER_HASBIND,null);
		}
		return ResultUtil.error(ResultEnum.USER_NOT_CORRECT,null);
	}

	@Override
	public LoginInfoBean findLoginInfo(String alipayUserId) {
		log.info("<AliLoginInfoServiceImpl--findLoginInfo--start>");
		LoginInfoBean findLoginInfo = loginInfoDao.findLoginInfo(alipayUserId);
		log.info("<AliLoginInfoServiceImpl--findLoginInfo--end>");
		return findLoginInfo;
	}

	@Override
	public LoginInfoBean queryByPhone(String phone) {
		log.info("<AliLoginInfoServiceImpl--queryByPhone--start>");
		LoginInfoBean queryByPhone = loginInfoDao.queryByPhone(phone);
		log.info("<AliLoginInfoServiceImpl--queryByPhone--end>");
		return queryByPhone;
	}

	@Override
	public LoginInfoBean queryById(Long id) {
		log.info("<AliLoginInfoServiceImpl--queryById--start>");
		LoginInfoBean queryById = loginInfoDao.queryById(id);
		log.info("<AliLoginInfoServiceImpl--queryById--end>");
		return queryById;
	}
}

package com.server.module.zfb_trade.convert;

import java.util.Date;

import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.server.module.zfb_trade.module.customer.CustomerBean;
import com.server.module.zfb_trade.util.enumparam.CustomerEnum;

public class AlipayUser2CustomerConverter {
	/**
	 * @Description: 封装参数 
	 */
	public static CustomerBean setCutomerData(AlipayUserInfoShareResponse userinfoShareResponse) {
		CustomerBean customer = new CustomerBean();
		customer.setType(CustomerEnum.ACCOUNT_TYPE_ALI.getCode()); // '1-微信;2-支付宝',
		customer.setAlipayUserId(userinfoShareResponse.getUserId()); // 支付宝用户的userId（alipay_user_id）
		customer.setNickname(userinfoShareResponse.getNickName()); // 昵称
		customer.setUserName(userinfoShareResponse.getUserName()); // 用户名称
		if (userinfoShareResponse.getGender() != null) { // 性别（F：女性；M：男性）。
			if (userinfoShareResponse.getGender().equalsIgnoreCase(CustomerEnum.SEX_FEMALE.getMessage())) {
				customer.setSexId(CustomerEnum.SEX_FEMALE.getCode());
			} else if (userinfoShareResponse.getGender().equalsIgnoreCase(CustomerEnum.SEX_MALE.getMessage())) {
				customer.setSexId(CustomerEnum.SEX_MALE.getCode());
			}
		}
		customer.setProvince(userinfoShareResponse.getProvince()); // 省份、
		customer.setCity(userinfoShareResponse.getCity()); // 城市
		customer.setHeadImgUrl(userinfoShareResponse.getAvatar());
		customer.setUserType(userinfoShareResponse.getUserType()); // 支付宝用户类型：1 代表公司账户  2代表个人账户
		customer.setUserStatus(userinfoShareResponse.getUserStatus()); // 用户状态:
		// 用户状态（Q/T/B/W）Q代表快速注册用户T代表已认证用户B代表被冻结账户W代表已注册，
		customer.setIsCertified(userinfoShareResponse.getIsCertified()); // 是否通过实名认证:T是通过
		customer.setIsStudentCertified(userinfoShareResponse.getIsStudentCertified()); // 是否是学生
		customer.setPhone(userinfoShareResponse.getPhone()); // 电话号码
		customer.setCreateTime(new Date());
		return customer;
	}
}

package com.server.module.trade.auth;


import lombok.Data;

@Data
public class UserVo {
    public static int USER_CUSTOMER=1;
    public static int USER_ADMIN=2;
    public static long Pay_Type_WeiXin=1;
    public static long Pay_Type_ZhiFuBao=2;
    public static long Pay_Type_Currency=3;

	
	Long id;
	
	Integer type;//类型  1 买家用户   2 运维人员
	
	String openId;
	String unionId;//平台统一id

	Long payType;// 支付类型  1 微信  2 支付宝   3微信支付宝通用
	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		// object must be Test at this point
		UserVo user = (UserVo) obj;
		boolean result = this.id.equals(user.getId()) && this.type.equals(user.getType()) 
				&& this.openId.equals(user.getOpenId()) && this.payType.equals(user.getPayType());
		return result;
	}
	
	@Override
	public int hashCode(){
		int result = 17;
		result = result*31 + this.id.hashCode();
		result = result*31 + this.type.hashCode();
		result = result*31 + this.openId.hashCode();
		result = result*31 + this.payType.hashCode();
		return result;
	}
	
}

package com.server.redis;
/**
 * 此 key前缀不带类名
 * @author hebiting
 *
 */
public class NormalRedisKey extends BasePrefix{

	public NormalRedisKey(String prefix) {
		super(prefix);
	}
	
	public NormalRedisKey(String prefix,int expireSeconds) {
		super(prefix,expireSeconds);
	}
	
	@Override
	public String getPrefix() {
		return super.getNormalPrefix();
	}
	
	/**
	 * 开门标志位，在开门接口时set进去,
	 * prefix后接factoryNumber
	 */
	public static NormalRedisKey OHM = new NormalRedisKey("OHM-",10);
	/**
	 * 记录版本2机器开门时，发送的商品变化信息,
	 * prefix后接factoryNumber
	 */
	public static NormalRedisKey GOODS = new NormalRedisKey("GOODS_");
	/**
	 * 机器开门时set，记录当前开启货道号
	 */
	public static NormalRedisKey CurrWay = new NormalRedisKey("CurrWay-",12*3600);
	
	/**
	 * 机器在未关门时，sms会接受心跳进行计算验证，是否处于非正常购买流程，这是数据库记录机器未关门的时间间隔,
	 * prefix后接vmCode
	 */
	public static NormalRedisKey badOpenLog = new NormalRedisKey("badOpenLog",3600);

	/**
	 * 非正常开门时，货道号,
	 * prefix后接vmCode
	 */
	public static NormalRedisKey improperWay = new NormalRedisKey("improperWay");
	/**
	 * 非正常开门时，存放上一次扣款后重量,
	 * prefix后接vmCode
	 */
	public static NormalRedisKey improperWeight = new NormalRedisKey("improperWeight");
	/**
	 * 非正常开门后，记录当前无用户的机器标志位,
	 * prefix后接factoryNumber
	 */
	public static NormalRedisKey workMachines = new NormalRedisKey("workMachines_",1800);
	/**
	 * 用作记录，机器所有货道已关闭，但心跳仍然显示有货道未关闭，这时，不再发送未关门短信给用户,
	 * prefix后接vmCode
	 */
	public static NormalRedisKey doorInduction = new NormalRedisKey("doorInduction");
	
	/**
	 * 开关门历史记录id，开门时set进去，用于在关门时处理订单,
	 * prefix后接vmCode
	 */
	public static NormalRedisKey OperateHistoryId = new NormalRedisKey("OperateHistoryId-");
	/**
	 * 关门接口在接口关门请求后，再次记录开门历史记录id，用于确认是否 扣款正确,
	 * prefix后接vmCode
	 */
	public static NormalRedisKey ConsOperateHistoryId = new NormalRedisKey("ConsOperateHistoryId-");

	/**
	 * 关门接口在接口关门请求后，再次记录开门用户信息，用于确认是否 扣款正确,
	 * prefix后接vmCode
	 */
	public static NormalRedisKey ConsOperateHistoryUserId = new NormalRedisKey("ConsOperateHistoryUserId-");
	/**
	 * 开门时，set进去,用于防止提前 消费closeflag
	 * prefix后接factoryNumber
	 */
	public static NormalRedisKey openflag = new NormalRedisKey("openflag-",3);
	/**
	 * 开门时set进去，用于在...忘记了
	 * prefix后接factoryNumber
	 */
	public static NormalRedisKey closeflag = new NormalRedisKey("closeflag-",300);
	/**
	 * 关门结算时set进去，用于获取用户购买商品信息
	 */
	public static NormalRedisKey orderReuslt = new NormalRedisKey("orderReuslt_",300);
}

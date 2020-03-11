package com.server.module.zfb_trade.util.enumparam;
public enum ResultEnum{
	UNKOWN_ERROR(-2,"未知错误"),
	NOT_LOGIN(-1,"登录失败或登录超时"),
	ERROR (0, "失败"),
	SUCCESS(1,"成功"),
	SELECT_NOT_DATA(2, "查询无数据"),
	ADD_FAILED (3, "添加失败"),
	COMPANY_NOT_EXIST(4, "找不到所属公司"),
	UPDATE_FAILED (5, "更新失败"),
	ILLEGAL_PARAMS (6, "非法参数"),
	DELETE_FAILED (7, "删除失败"),
	ILLEGAL_STATE(9, "非法状态"),
	RECORD_LIMIT(10, "记录数量达到最大值"),

	DAO_DUPLICATE(101, "该记录已存在"),
	
	CUSTOMER_NOT_EXIST(11, "账户不存在"),
	MACHINE_NOT_EXIST(12, "机器不存在"),
	WAY_NOT_EXIST(13, "货道不存在"),
	
	USER_NOT_CORRECT(14,"账号或密码错误"),
	USER_INVALID(15,"用户已禁用"),
	USER_HASBIND(16,"用户已绑定支付宝账号,请联系管理员"),
	NOT_AUTHORIZED(17,"抱歉,你没有操作权限"),
	NOT_REALNAME(18,"请实名验证后，再购物"),
	ITEM_NOT_EXIST(21, "商品不存在"),
	
	VERIFYCODE_UNCORRECT(61,"手机验证码不正确"),
	VERIFYCODE_TIMEOUT(62,"验证超时,请重新获取验证码"),
	
	GAME_TIMES_NOT_ENOUGH(81,"亲，购物完成后才能参与一次抽奖哟！"),
	GAME_SUCCESS(82,"亲，恭喜你获得一次抽奖机会，请前往抽奖吧！"),
	NOT_ENTRUST_AND_NOT_RECHARGE(91,"未签约且优水余额不足"),
	
	//验证关系10xx
	NOT_SUPPORT(1001, "暂时不支持该操作"),
	AUTH_FAILED(1002, "认证失败"),
	AUTH_TIMEOUT(1003, "认证超时"),
	//机器11xx
	MACHINES_VERSION_ERROR(1101,"机器版本异常"),
	//缓存20xx
	CACHE_ERROR(2001, "缓存异常"),
	CACHE_GET_ERROR(2002, "获取缓存失败"),
	
	//开门返回值40xx
	NEW_OPEN_SUCCESS(4000,"开门成功!"),
	MECHINE_NOT_FREE(4001,"温馨提示:当前有用户正在使用，请稍后！感谢您的光临！"),
	PHONE_NOT_VERIFY(4002,"温馨提示:支付宝未进行手机认证,请先手机验证!"),
	ORDER_NOT_PAY(4003,"您有未支付订单请点击我的订单支付完毕再进行购水！感谢您的光临！"),
	ITEM_SOLD_OUT(4004,"温馨提示:该商品已售罄,请尝试选择其他门!感谢您的光临！"),
	SERVER_INNER_ERROR(4005,"网络故障,请稍候再试!"),
	WECHAT_ERROR(4006,"请先开通微信支付分"),

	// 运维人员的提示
	OPEN_DOOR_ERROR(4006,"开门失败,检查系统是否正常!"),
	FORWARD_SIGN_PAGE(4007,"跳转签约页面"),
	DOOR_NOT_AVAILABLE(4008,"温馨提示:当前商品暂停销售,请选择其他商品!感谢您的光临！"),
	MACHINES_BUSY(4009,"机器繁忙，请稍后重试"),
	MACHINES_NOT_HEART(4010,"机器无心跳，无法开门"),
	MACHINES_BREAK_DOWN(4011,"机器故障，无法开门"),
	MACHINES_STOP_FOR_CUSTOMER(4012,"该窗口已经停止售卖商品，请选择其他商品，给您带来不便我们深感抱歉！感谢您的光临！"),
	MACHINES_IS_OPENED(4013,"该窗口已开启，请勿重复点击开门"),
	MACHINES_PRE_DOOR_NOT_CLOSED(4014,"窗口未关闭，请关闭后再开启其他窗口"),
	//退款返回值50xx
	REFUND_SUCCESS(5001,"退款操作成功"),
	REFUND_NOT_AUTHORIZED(5002,"无权进行退款操作"),
	REFUND_ERROR(5003,"退款失败"),
	REFUND_PROCESS_ERROR(5004,"退款程序异常"),
	REFUND_ORDER_NOT_EXISTS(5005,"订单不存在，退款失败"),
	REFUND_ORDER_ERROR(5006,"退款订单异常，无法退款"),
	REFUND_PRICE_MISMATCH(5007,"退款金额不匹配"),
	REFUND_APPLICATION_FAIL(5008,"退款申请失败")
	;
	
	private Integer code;
	private String message;
	/**
	 * @param code
	 * @param message
	 */
	private ResultEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	public static String getMessage(Integer code) {
		for (ResultEnum c : ResultEnum.values()) {
			if (c.getCode() == code) {
				return c.getMessage();
			}
		}
		return null;
	}
}

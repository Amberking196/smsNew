package com.server.module.zfb_trade.bean;

public class TcpMachineRedisConstant {

	/*
	 * 缓存时间半个小时
	 */
	public static int TIME_OUT = 1800;
	/*
	 * 机器信息缓存key，%s为出厂编号
	 */
	public static String MK_TCP_MACHINE = "tcp_machine_%s";
	/*
	 * 待完成任务，机器再次空闲之后才清除 结构区分桶装水和rfid 基本包含：账户，账户类型【顾客1，运维2】，开门信息，其他指令
	 */
	public static String HK_CURRENT_TASK = "current_task";
	/*
	 * 最新会话信息 包含最后一次请求信息 售卖机编号
	 */
	public static String HK_COOKIES = "cookies";

	/**
	 * 心跳监听
	 */
	public static String MACHINE_HEARTBEAT_TIMEOUT_PREFIX = "machine_heartbeat_timeout_";
	public static Integer MACHINE_HEARTBEAT_TIMEOUT_EXPIRE = 600;
	public static Integer MACHINE_HEARTBEAT_TIMEOUT = 300;
	public static String MACHINE_HEARTBEAT_LISTEN_SET = "machine_heartbeat_listen_set";
	public static Integer MACHINE_HEARTBEAT_LISTEN_SET_EXPIRE = 900;

	public static String getCacheKey(String factoryNumber) {
		return String.format(MK_TCP_MACHINE, factoryNumber);
	}
}

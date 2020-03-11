package com.server.module.machinesBadOpenLog;

public interface MachinesBadOpenLogDao {

	/**
	 * 插入非正常开门记录
	 * @author hebiting
	 * @date 2019年2月1日下午3:54:38
	 * @param badOpenLog
	 * @return
	 */
	public Long insert(MachinesBadOpenLogBean badOpenLog);
}

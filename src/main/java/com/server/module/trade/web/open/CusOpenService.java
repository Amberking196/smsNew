package com.server.module.trade.web.open;

import com.server.module.trade.auth.UserVo;
import com.server.module.zfb_trade.module.vmbase.VmbaseInfoDto;
import com.server.util.ReturnDataUtil;

public interface CusOpenService {

	/**
	 * 开门接口
	 * @author hebiting
	 * @date 2019年3月2日上午10:01:56
	 * @param baseInfo
	 * @param vmCode
	 * @param wayNumber
	 * @param userVo
	 * @param minStandard
	 * @return
	 */
	public ReturnDataUtil openDoor(VmbaseInfoDto baseInfo,String vmCode ,Integer wayNumber,UserVo userVo,Integer minStandard);
	public ReturnDataUtil openDoorV3(VmbaseInfoDto baseInfo,String vmCode, Integer wayNumber,UserVo userVo);
	/**
	 * 判断是否开门
	 * @author hebiting
	 * @date 2019年3月2日上午10:02:02
	 * @param vmCode
	 * @return
	 */
	public ReturnDataUtil isOpened(String vmCode);
}

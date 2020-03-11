package com.server.module.zfb_trade.convert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.order.domain.Way;
import com.server.module.zfb_trade.bean.DoorOperateHistoryBean;
import com.server.module.zfb_trade.util.enumparam.DoorOperateEventEnum;

public class Msg2DoorOperateConverter {
	private static Log log = LogFactory.getLog(Msg2DoorOperateConverter.class);

	/**
	 * @Description: 设置参数 
	 */
	public static DoorOperateHistoryBean setDoorOperateHistoryData(MachineInfo info,Integer wayNum,String vmCode,UserVo userVo) {
		DoorOperateHistoryBean doorOperateHistory = new DoorOperateHistoryBean();
		Integer preWeight = null;
		List<Way> wayList = info.getList();
		doorOperateHistory.setDoorNO(wayNum);
		doorOperateHistory.setCreateTime(new Date());
		doorOperateHistory.setOpenedTime(new Date());
		doorOperateHistory.setVmCode(vmCode);
		doorOperateHistory.setOpenType(userVo.getType());
		doorOperateHistory.setReferenceId(userVo.getId());
		if(wayList!=null){
			if((wayNum-1)>=0 && wayNum<=wayList.size()){
				Way way = wayList.get(wayNum-1);
				preWeight = way.getWeight();
			}
		}
		doorOperateHistory.setPreWeight(preWeight);
		doorOperateHistory.setStatus(DoorOperateEventEnum.WAIT.getCode());
		log.info("【转换】doorOperateHistory"+doorOperateHistory);
		return doorOperateHistory;
	}
	
	
	public static List<DoorOperateHistoryBean> setAllDoorOperateHistoryData(MachineInfo info,Integer totalWayNum,String vmCode,UserVo userVo){
		List<DoorOperateHistoryBean> historyList = new ArrayList<DoorOperateHistoryBean>();
		Date date = new Date();
		List<Way> wayList = info.getList();
		for (int i = 0; i < totalWayNum; i++) {
			DoorOperateHistoryBean doorOperateHistory = new DoorOperateHistoryBean();
			Integer preWeight = null;
			doorOperateHistory.setDoorNO((i+1));
			doorOperateHistory.setCreateTime(date);
			doorOperateHistory.setOpenedTime(date);
			doorOperateHistory.setVmCode(vmCode);
			doorOperateHistory.setOpenType(userVo.getType());
			doorOperateHistory.setReferenceId(userVo.getId());
			if(wayList!=null){
				if(i<wayList.size()){
					Way way = wayList.get(i);
					preWeight = way.getWeight();
				}
			}
			doorOperateHistory.setPreWeight(preWeight);
			doorOperateHistory.setStatus(DoorOperateEventEnum.WAIT.getCode());
			historyList.add(doorOperateHistory);
		}
		return historyList;
	} 
}

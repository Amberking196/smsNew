package com.server.util;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemBean;


public class CalculateUtil {
	
	private static Logger log = LogManager.getLogger(CalculateUtil.class);

	/**
	 * 计算是否误差过大
	 * -1:参数为null;
	 * 0：正常 ;
	 * 1：误差在 规格*0.1;
	 * 2：误差在 规格*0.2;
	 * 3：误差在 规格*0.3;  
	 * 4：误差在 规格*0.4;
	 * @author hebiting
	 * @date 2018年6月25日上午10:29:12
	 * @param unitWeight
	 * @param weight
	 * @return
	 */
	public static Integer isNormal(Integer unitWeight,Integer weight){
		if(unitWeight == null || weight == null){
			return -1;
		}
		int remainder = Math.abs(weight) % unitWeight;
		if(remainder >= (unitWeight*0.4d) && remainder <= (unitWeight*0.6d)){
			return 4;
		}else if(remainder >= (unitWeight*0.3d) && remainder <= (unitWeight*0.7d)){
			return 3;
		}else if(remainder >= (unitWeight*0.2d) && remainder <= (unitWeight*0.8d)){
			return 2;
		}else if(remainder >= (unitWeight*0.1d) && remainder <= (unitWeight*0.9d)){
			return 1;
		}
		return  0;
	}
	public static String reviseCommand(MachineInfo machineInfo,List<VmwayItemBean> list) {
		StringBuffer command = new StringBuffer();
		command.append("s:").append(machineInfo.getCurrWay());
		command.append(";").append(1).append(";");
		for(int i = 0; i < list.size(); i++) {
			command.append(list.get(i).getOrderNumber()+":"+list.get(i).getNum()+","+list.get(i).getWeight());
			if (i != list.size()- 1) {
				command.append(";");
			}
		}
		command.append("&").append(checkCodeSum(command.toString())).append("$");
		return command.toString();
	}
	public static Integer checkCodeSum(String command){
		if(StringUtils.isNotBlank(command)){
			int sum = 0;
			char[] ss = command.toCharArray();
			for (char c : ss) {
				if(Character.isDigit(c)){
					Integer unitChar = Integer.valueOf(String.valueOf(c));
					sum+=unitChar;
				}
			}
			return sum;
		}
		return 0;
	}
}

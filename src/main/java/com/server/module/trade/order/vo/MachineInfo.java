package com.server.module.trade.order.vo;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.Lists;
import com.server.module.trade.util.MathUtil;

import lombok.Data;

@Data
public class MachineInfo {
	 private final Log log = LogFactory.getLog(this.getClass());

	private String params;
	private String factoryNumber;
	private String vmCode;
	private String type;
	private String state;
	private String power;
	//当前门号
	private Integer currWay;
	//当前门质量差额
	private Integer currWeight;
	
	List<Way> list=Lists.newArrayList();
	public MachineInfo(String params){
		paser(params);
	}
	private void paser(String params){
		//t:2;n:a00;d:1,1,1,1;s:0;p:0;w:3700,3500,2500,3650&45

		String[] arr1=params.split("&");
		
		String[] ps=arr1[0].split(";");
		
		for (String str : ps) {
			
			String[] temp=str.split(":");
			
			if(temp[0].equals("t")){
				this.type=temp[1];
				
			}else if(temp[0].equals("n")){
				this.factoryNumber=temp[1];
			}else if(temp[0].equals("d")){
				String[] ways=temp[1].split(",");
				genWays(ways.length);
				for (int i=0;i<ways.length;i++) {
					list.get(i).setState(ways[i]);
				}
				
			}else if(temp[0].equals("s")){
				this.state=temp[1];
				
			}else if(temp[0].equals("p")){
				this.power=temp[1];
				
			}else if(temp[0].equals("w")){
				String[] weights=temp[1].split(",");
				genWays(weights.length);
				for (int i=0;i<weights.length;i++) {
					list.get(i).setWeight(Integer.parseInt(weights[i]));
				}
			}else{
				//数据格式有错
				System.out.println("参数格式有误");
			}
		}
		
		
	}
	
	private void genWays(int count){
		if(list.size()==0){
			for(int i=0;i<count;i++){
				Way way=new Way();
				way.setWayNumber(i+1);
				list.add(way);
			}
		}
	}
	/**
	 * 当前质量减去上一刻的质量
	 * @param preMachineInfo
	 */
	private Integer[] calculateWeight(MachineInfo preMachineInfo){
		List<Way> preList=preMachineInfo.getList();
		Integer[] diffWeights=new Integer[preList.size()];
		for(int i=0;i<preList.size();i++){
			diffWeights[i]=list.get(i).getWeight()-preList.get(i).getWeight();
		}
		return diffWeights;
	}
	
	public void getWhatWayMaxWeight(MachineInfo preMachineInfo){
		Integer[] diffWeights=this.calculateWeight(preMachineInfo);
		log.info("diffWeights=="+diffWeights);
		//取绝对值
		for (int i=0;i<diffWeights.length;i++) {
			diffWeights[i]=Math.abs(diffWeights[i]);
		}
		int index=MathUtil.getMaxIndex(diffWeights);
		int weight=MathUtil.getMaxNum(diffWeights);
		setCurrWay(index+1);
		setCurrWeight(weight);
		
	}
	
	public  void calculate(MachineInfo preMachineInfo){
		this.getWhatWayMaxWeight(preMachineInfo);
	}
	
	

}

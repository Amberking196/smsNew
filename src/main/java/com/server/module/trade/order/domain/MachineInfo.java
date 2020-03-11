package com.server.module.trade.order.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.server.module.trade.util.MathUtil;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CommandVersionEnum;

import lombok.Data;

@Data
public class MachineInfo {
	private final Logger log = LogManager.getLogger(MachineInfo.class);

	private String params;
	private String factoryNumber;
	private String vmCode;
	private String type;
	private String state;
	private String power;
	// 当前门号
	private Integer currWay;
	// 当前门质量差额
	private Integer currWeight;

	// current price
	private BigDecimal price;

	// unit weight
	private Integer unitWeight;
    //支付时数量
	private Integer unit;
	//实际数量
	private Integer realUnit;
	// 总金额
	private BigDecimal totalPrice;
	//温度
	private String temperature;
	//机器版本
	private Integer version;
	//总货道数
	private Integer totalWayNum;
	
	List<ItemChangeDto> itemChangeList = new ArrayList<ItemChangeDto>();

	List<Way> list = Lists.newArrayList();
	

	public MachineInfo() {

	}

	public MachineInfo(String params) {
		this.version = CommandVersionEnum.VER1.getState();
		paser(params);
	}
	
	public MachineInfo(String params,Integer version){
		this.version = version;
		if(CommandVersionEnum.VER1.getState().equals(version)){
			paser(params);
		}else if (CommandVersionEnum.VER2.getState().equals(version)){
			moreGoodsPaser(params);
		}
	}
	
	
	public static void main(String[] args) {
//		String params = "factNum:12121;goods:1;1:-1,2:-2;goods:2;1:-1,2:-2; goods:3;1:-1,2:-2; goods:4;1:-1,2:-2;all_closed";
//		String params = "factNum:860020020021;goods:5;1:-0,2:-1;end";
		String params = "goods:2;1:-1,2: -2,3:+1,4:-5";
		MachineInfo machineInfo = new MachineInfo();
		machineInfo.moreGoodsAllPaser(params);
		System.out.println(JSON.toJSONString(machineInfo));
	}
	
	public void moreGoodsAllPaser(String params){
		//factNum:*****;goods:1;1:-1,2:-2;goods:2;1:-1,2:-2; goods:3;1:-1,2:-2; goods:4;1:-1,2:-2;all_closed
		//factNum:*****;goods:3;1:-1000,2:-1000;end
		this.params = params;
		int currWayNum = 1;
		int totalWayNum = 0;
		this.version = CommandVersionEnum.VER2.getState();
		String[] split = params.split(";");
		for (String param : split) {
			String currParam = param.trim();
			if(currParam.contains("factNum:") && StringUtils.isBlank(this.factoryNumber)){
				this.factoryNumber = currParam.split(":")[1].trim();
			}else if(currParam.contains("goods:")){
				currWayNum = Integer.valueOf(currParam.split(":")[1].trim());
				totalWayNum++;
			}else if(currParam.contains("1:")){
				String[] wayInfo = currParam.split(",");
				for(String change : wayInfo){
					String[] oneChange = change.split(":");//1:-1000 2:-1000
					if(oneChange.length == 2){
						Integer orderNum = Integer.valueOf(oneChange[0].trim());
						Integer changeNum = Integer.valueOf(oneChange[1].trim());
						ItemChangeDto changeInfo = new ItemChangeDto();
						changeInfo.setRealNum(changeNum);
						changeInfo.setWayNum(currWayNum);
						changeInfo.setChangeNum(changeNum);
						changeInfo.setOrderNum(orderNum);
						this.itemChangeList.add(changeInfo);
					}
				}
			}else if(currParam.contains("all_closed")){
				//门全开
				this.totalWayNum = totalWayNum;
			}else if(currParam.contains("end")){
				//开一门
			}else{
				log.error(params+"-错误的命令："+currParam);
			}
		}
	}
	
	public static List<Integer> parseGoodsGetOpenWayList(String goods){
		//factNum:*****;goods:1;1:-1,2:-2;goods:2;1:-1,2:-2; goods:3;1:-1,2:-2; goods:4;1:-1,2:-2;all_closed
		//factNum:*****;goods:3;1:-1000,2:-1000;end
		List<Integer> intList = new ArrayList<Integer>(); 
		if(StringUtils.isNotBlank(goods)){
			while(goods.contains("goods:")){
				goods = goods.substring(goods.indexOf("goods:")+6, goods.length());
				if(goods.contains(";")){
					String wayString = goods.substring(0,goods.indexOf(";"));
					intList.add(Integer.valueOf(wayString));
				}
			}
		}
		return intList;
	}
	
	public static MachineInfo parseParamGetMachineInfo(String params){
		MachineInfo machineInfo = new MachineInfo();
		machineInfo.moreGoodsAllPaser(params);
		return machineInfo;
	}
	
	public void moreGoodsPaser(String params){
//		factNum:******;goods:3;1:-1000,2:-1000,end
		this.version = CommandVersionEnum.VER2.getState();
		String[] split = params.split(";");
		String[] factNumArray = split[0].split(":");
		String factNum = factNumArray[1];
		this.factoryNumber = factNum;
		String[] wayNumArray = split[1].split(":");
		String wayNum = wayNumArray[1];
		String[] itemChangeArray = split[2].split(",");
		for(int i=0;i<itemChangeArray.length-1;i++){
			String[] changeArray = itemChangeArray[i].split(":");
			Integer changeNum = Integer.valueOf(changeArray[1]);
			if(changeNum!=0){
				Integer orderNum = Integer.valueOf(changeArray[0]);
				ItemChangeDto changeInfo = new ItemChangeDto();
				changeInfo.setRealNum(changeNum);
				changeInfo.setWayNum(Integer.valueOf(wayNum));
				changeInfo.setChangeNum(changeNum);
				changeInfo.setOrderNum(orderNum);
				this.itemChangeList.add(changeInfo);
			}
		}
	}
	
	public MachineInfo(String params,Integer version,String vmCode,String factoryNumber) {
		this.version = version;
		this.vmCode = vmCode;
		this.factoryNumber = factoryNumber;
		if(CommandVersionEnum.VER1.getState().equals(version)){
			paser(params);
		}else if (CommandVersionEnum.VER2.getState().equals(version)){
			newPaser(params);
		}
	}
	
	public void newPaser(String command){
		//goods:2;1:-1,2: -2,3:+1,4:-5  
		if(StringUtil.isNotBlank(command)){
			String[] itemChange = command.split(";");
			String[] goodsWay = itemChange[0].split(":");
			String wayNum = goodsWay[1].trim();
			String[] split = itemChange[1].split(",");
			if(split.length>0){
				this.currWay = Integer.valueOf(wayNum);
				for(int i=0;i<split.length;i++){
					String[] itemInfo = split[i].split(":");
					if(Integer.parseInt(itemInfo[1].trim())!=0){
						ItemChangeDto changeInfo = new ItemChangeDto();
						changeInfo.setVmCode(this.vmCode);
						changeInfo.setWayNum(Integer.valueOf(wayNum));
						changeInfo.setRealNum(Integer.valueOf(itemInfo[1].trim()));
						changeInfo.setChangeNum(Integer.valueOf(itemInfo[1].trim()));
						changeInfo.setOrderNum(Integer.valueOf(itemInfo[0].trim()));
						this.itemChangeList.add(changeInfo);
					}
				}
			}
		}
	}

//	public static void main(String[] args) {
//		String param = "goods:3;1:-1,2:-0";
//		String vmCode = "1988000083";
//		String factoryNum = "860020010011";
//		MachineInfo machineInfo = new MachineInfo(param,CommandVersionEnum.VER2.getState(),vmCode,factoryNum);
//		System.out.println(JsonUtil.toJson(machineInfo));
//	}
	
	
	private void paser(String params) {
		// t:2;n:a00;d:1,1,1,1;s:0;p:0;w:3700,3500,2500,3650&45
		log.info("paser params==="+params);
        if(StringUtil.isNotBlank(params)){
        	this.params = params;
        	String[] arr1 = params.split("&");

    		String[] ps = arr1[0].split(";");

    		for (String str : ps) {

    			String[] temp = str.split(":");

    			if (temp[0].equals("t")) {
    				this.type = temp[1];

    			} else if (temp[0].equals("n")) {
    				this.factoryNumber = temp[1];
    			} else if (temp[0].equals("d")) {
    				String[] ways = temp[1].split(",");
    				genWays(ways.length);
    				for (int i = 0; i < ways.length; i++) {
    					list.get(i).setState(ways[i]);
    				}

    			} else if (temp[0].equals("s")) {
    				this.state = temp[1];

    			} else if (temp[0].equals("p")) {
    				this.power = temp[1];

    			} else if (temp[0].equals("w")) {
    				String[] weights = temp[1].split(",");
    				genWays(weights.length);
    				for (int i = 0; i < weights.length; i++) {
    					list.get(i).setWeight(Integer.parseInt(weights[i]));
    				}
    			} else if (temp[0].equals("c")) {
    				this.temperature = temp[1];
    			} else if (temp[0].equals("g")) {
    				
    			} else {
    				// 数据格式有错
    				log.error(params+"--"+temp[0]+"参数格式有误");
    			}
    		}
        }
	}

	private void genWays(int count) {
		if (list.size() == 0) {
			for (int i = 0; i < count; i++) {
				Way way = new Way();
				way.setWayNumber(i + 1);
				list.add(way);
			}
		}
	}

	/**
	 * 当前质量减去上一刻的质量
	 * 
	 * @param preMachineInfo
	 */
	public Integer[] calculateWeight(MachineInfo preMachineInfo) {
		List<Way> preList = preMachineInfo.getList();
		log.info("list way length="+preList.size());
		Integer[] diffWeights = new Integer[preList.size()];
		for (int i = 0; i < preList.size(); i++) {
			diffWeights[i] = list.get(i).getWeight() - preList.get(i).getWeight();
		}
		return diffWeights;
	}
    //获取被开门的门号 及 前后质量差
	public void getWhatWayMaxWeight(MachineInfo preMachineInfo,int currWay) {
		Integer[] diffWeights = this.calculateWeight(preMachineInfo);
		log.info("diffWeights==" + diffWeights.toString());
		log.info("diffWeights=len=" + diffWeights.length);
		// 取绝对值
		Integer[] temps=new Integer[diffWeights.length];//保存原来的值
		for (int i = 0; i < diffWeights.length; i++) {
			temps[i]=diffWeights[i];
			diffWeights[i] = Math.abs(diffWeights[i]);//取绝对值
		}
		if(currWay>=1 && currWay <= diffWeights.length){
			setCurrWay(currWay);
			setCurrWeight(temps[currWay-1]);
		}else{
			log.info("currWay异常，直接计算重量变化差异最大值的门号");
			int index = MathUtil.getMaxIndex(diffWeights);
			log.info("temps--len--"+temps.length+"   index="+index);
			int weight = temps[index];//为正或负   
					//MathUtil.getMaxNum(diffWeights);
			setCurrWay(index + 1);
			setCurrWeight(weight);
		}
	}

	public void calculateWhatWay(MachineInfo preMachineInfo,int currWay) {
		this.getWhatWayMaxWeight(preMachineInfo,currWay);
	}

	/**
	 * 计算购买数量和总金额
	 */
	public void calculateHowUnitAndPrice() {
		Integer u = this.calculateHowUnit(currWeight, unitWeight);
		setUnit(u);
		setRealUnit(u);
		setTotalPrice(price.multiply(new BigDecimal(Integer.toString(u))));

	}
	
	public Integer calculateHowUnit(int currWeight, int unitWeight) {
		int temp=Math.abs(currWeight);
		Integer half = unitWeight * 2/3;
		int u = temp / unitWeight;
		int y = temp % unitWeight;
		if (y > half)
			u = u + 1;

		return u;

	}



//	public static void main(String[] args) {
//
//		MachineInfo info = new MachineInfo();
//
//		System.out.println(info.calculateHowUnit(16, 7));
//	}

	@Override
	public String toString() {
		return "MachineInfo [log=" + log + ", params=" + params + ", factoryNumber=" + factoryNumber + ", vmCode="
				+ vmCode + ", type=" + type + ", state=" + state + ", power=" + power + ", currWay=" + currWay
				+ ", currWeight=" + currWeight + ", price=" + price + ", unitWeight=" + unitWeight + ", unit=" + unit
				+ ", totalPrice=" + totalPrice + ", list=" + list + "]";
	}

}

package com.server.module.trade.vendingMachinesWayItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.commonBean.ItemInfoDto;
import com.server.module.trade.order.dao.OrderDao;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.order.domain.Way;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.redis.RedisService;
import com.server.util.ReviseUtil;
import com.server.util.StringUtil;

@Service
public class VmwayItemServiceImpl implements VmwayItemService{
	
	private final static Logger log = LogManager.getLogger(VmwayItemServiceImpl.class);

	@Autowired
	private VmwayItemDao vmwayItemDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private RedisService redisService;

	@Override
	public ItemChangeDto getChangeInfo(ItemChangeDto itemChangeDto) {
		//log.info("<VmwayItemServiceImpl--getChangeInfo--start>");
		ItemChangeDto changeInfo = vmwayItemDao.getChangeInfo(itemChangeDto);
		//log.info("<VmwayItemServiceImpl--getChangeInfo--end>");
		return changeInfo;
	}

	@Override
	public List<MoreGoodsWayDto> getWayInfo(String vmCode,Integer type) {
		//log.info("<VmwayItemServiceImpl--getWayInfo--start>");
		List<MoreGoodsWayDto> wayInfo = vmwayItemDao.getWayInfo(vmCode);
		log.info("wayInfo---"+JsonUtil.toJson(wayInfo));
		if(type == 2){
			String factoryNumber=orderDao.getFactoryNumberByVmCode(vmCode);
			String content = redisService.getString("HM-" + factoryNumber);
			log.info("运维首页机器心跳=="+content);
			if(StringUtil.isNotBlank(content)){
				MachineInfo info=new MachineInfo(content);
				List<Way> listway=info.getList();
				for (int i=0;i<listway.size();i++) {
					Integer wayNumber = listway.get(i).getWayNumber();
					for(MoreGoodsWayDto moreGoodsWayDto : wayInfo){
						if(moreGoodsWayDto.getWayNumber().equals(wayNumber)){
							moreGoodsWayDto.setWayWeight(listway.get(i).getWeight());
						}
					}
				}
			}
		}
		//log.info("<VmwayItemServiceImpl--getWayInfo--end>");
		return wayInfo;
	}

	@Override
	public boolean updateWayInfo(String vmCode, Integer wayNum, Integer orderNum, Integer num) {
		//log.info("<VmwayItemServiceImpl--updateWayInfo--start>");
		boolean result = vmwayItemDao.updateWayInfo(vmCode, wayNum, orderNum, num);
		//log.info("<VmwayItemServiceImpl--updateWayInfo--end>");
		return result;
	}

	@Override
	public WayValidatorDto getOneWayInfo(String vmCode, Integer wayNum) {
		//log.info("<VmwayItemServiceImpl--getOneWayInfo--start>");
		WayValidatorDto oneWayInfo = vmwayItemDao.getOneWayInfo(vmCode, wayNum);
		//log.info("<VmwayItemServiceImpl--getOneWayInfo--end>");
		return oneWayInfo;
	}

	@Override
	public List<ItemInfoDto> getOneWayItmeInfo(String vmCode, Integer wayNum) {
		//log.info("<VmwayItemServiceImpl--getOneWayItmeInfo--start>");
		List<ItemInfoDto> oneWayItmeInfo = vmwayItemDao.getOneWayItmeInfo(vmCode, wayNum);
		//log.info("<VmwayItemServiceImpl--getOneWayItmeInfo--end>");
		return oneWayItmeInfo;
	}
	
	@Override
	public String getMultilayerCommand(String factnum) {
		//log.info("<VmwayItemServiceImpl--getMultilayerCommand--start>");
		StringBuffer command = new StringBuffer("g:");
		List<WayStandardDto> wayStandardList = vmwayItemDao.getStandardInfo(factnum);
		if(wayStandardList == null || wayStandardList.size() == 0){
			return null;
		}
		Map<Integer, List<WayStandardDto>> wayMap = wayStandardList.stream().collect(Collectors.groupingBy(WayStandardDto::getWayNum));
		List<Integer> wayList = new ArrayList<Integer>(wayMap.keySet());
		Collections.sort(wayList,Integer::compareTo);
		for (int j=0;j<wayList.size();j++) {
			List<WayStandardDto> list = wayMap.get(wayList.get(j));
			Collections.sort(list,new Comparator<WayStandardDto>(){
				@Override
				public int compare(WayStandardDto o1, WayStandardDto o2) {
					
					return Integer.valueOf(o1.getStandard()).compareTo(Integer.valueOf(o2.getStandard()));
				}
			});
			command.append(wayList.get(j)+"-");
			for (int i = 0;i<list.size();i++) {
				if(i<list.size()-1){
					command.append(list.get(i).getStandard()+",");
				}else{
					command.append(list.get(i).getStandard());
				}
			}
			if(j<wayList.size()-1){
				command.append(";");
			}else{
				command.append("&");
			}
		}
		Integer sumString = ReviseUtil.checkCodeSum(command.toString());
		command.append(sumString);
		command.append("$");
		//log.info("<VmwayItemServiceImpl--getMultilayerCommand--end>");
		return command.toString();
	}
}

package com.server.module.activity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.coupon.MachinesLAC;

@Service
public class ActivityServiceImpl implements ActivityService{

	private final static Logger log = LogManager.getLogger(ActivityServiceImpl.class);
	
	@Autowired
	private ActivityDao activityDao;

	@Override
	public List<ActivityBean> getActivity(MachinesLAC machinesLAC, BigDecimal money, Long productId) {
		log.info("<ActivityServiceImpl--getActivity--start>");
		List<ActivityBean> activity = activityDao.getActivity(machinesLAC, money, productId);
		log.info("<ActivityServiceImpl--getActivity--end>");
		return activity;
	}

	@Override
	public List<ActivityProductBean> getActivityProduct(Long activityId) {
		log.info("<ActivityServiceImpl--getActivityProduct--start>");
		List<ActivityProductBean> activityProduct = activityDao.getActivityProduct(activityId);
		log.info("<ActivityServiceImpl--getActivityProduct--end>");
		return activityProduct;
	}

	@Override
	public List<ActivityQuantumBean> getActivityQuantum(String quantnumIds) {
		log.info("<ActivityServiceImpl--getActivityQuantum--start>");
		List<ActivityQuantumBean> activityQuantum = activityDao.getActivityQuantum(quantnumIds);
		log.info("<ActivityServiceImpl--getActivityQuantum--end>");
		return activityQuantum;
	}

	@Override
	public Map<ActivityBean, List<Long>> getMoreGoodsActivity(MachinesLAC machinesLAC, BigDecimal money,
			String productIds) {
		log.info("<ActivityServiceImpl--getActivityQuantum--start>");
		Map<ActivityBean, List<Long>> moreGoodsActivity = activityDao.getMoreGoodsActivity(machinesLAC,money,productIds);
		log.info("<ActivityServiceImpl--getActivityQuantum--end>");
		return moreGoodsActivity;
	}

	@Override
	public ActivityBean getLocalActivity(MachinesLAC machinesLAC, String basicItemIds) {
		log.info("<ActivityServiceImpl--getLocalActivity--start>");
		ActivityBean localActivity = activityDao.getLocalActivity(machinesLAC, basicItemIds);
		log.info("<ActivityServiceImpl--getLocalActivity--end>");
		return localActivity;
	}
	
	
}

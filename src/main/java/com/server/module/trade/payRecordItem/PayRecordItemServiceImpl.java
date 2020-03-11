package com.server.module.trade.payRecordItem;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PayRecordItemServiceImpl implements PayRecordItemService{

	private final static Logger log = LogManager.getLogger(PayRecordItemServiceImpl.class);
	
	@Autowired
	private PayRecordItemDao payRecordItemDao;

	@Override
	public Long insert(PayRecordItemBean recordItem) {
		log.info("<PayRecordItemServiceImpl--insert--start>");
		Long insert = payRecordItemDao.insert(recordItem);
		log.info("<PayRecordItemServiceImpl--insert--end>");
		return insert;
	}
	
	@Override
	public List<PayRecordItemBean> getListByPayRecordId(Long payRecordId){
		log.info("<PayRecordItemServiceImpl--getListByPayRecordId--start>");
		List<PayRecordItemBean>  list = payRecordItemDao.getListByPayRecordId(payRecordId);
		log.info("<PayRecordItemServiceImpl--getListByPayRecordId--end>");
		return list;
	}
}

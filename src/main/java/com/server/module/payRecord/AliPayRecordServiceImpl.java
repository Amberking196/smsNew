package com.server.module.payRecord;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.server.module.zfb_trade.bean.MsgDto;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.PayStateEnum;

@Service("aliPayRecordService")
public class AliPayRecordServiceImpl implements AliPayRecordService{

	public static Logger log = LogManager.getLogger(AliPayRecordServiceImpl.class); 
	@Autowired
	@Qualifier("aliPayRecordDao")
	private AliPayRecordDao payRecordDao;
	@Override
	public boolean booleanIsArrearage(Long customerId) {
		log.info("<AliPayRecordServiceImpl--booleanIsArrearage--start>");
		boolean booleanIsArrearage = payRecordDao.booleanIsArrearage(customerId);
		log.info("<AliPayRecordServiceImpl--booleanIsArrearage--end>");
		return booleanIsArrearage;
	}
	@Override
	public boolean updatePayRecord(String payCode, String ptCode) {
		log.info("<AliPayRecordServiceImpl--updatePayRecord--start>");
		PayRecordBean payRecord = payRecordDao.findPayRecordByPayCode(payCode);
		if(payRecord!=null){
			payRecord.setState(PayStateEnum.PAY_SUCCESS.getState());
			payRecord.setPtCode(ptCode);
			payRecord.setPayTime(new Date());
			if(payRecordDao.updatePayRecord(payRecord)){
				return true;
			}
		}
		return false;
	}
	@Override
	public MsgDto findMsgDto(String payCode) {
		log.info("<AliPayRecordServiceImpl--findMsgDto--start>");
		MsgDto findMsgDto = payRecordDao.findMsgDto(payCode);
		log.info("<AliPayRecordServiceImpl--findMsgDto--end>");
		return findMsgDto;
	}
	@Override
	public List<PayRecordDto> findPayRecord(PayRecordForm payRecordForm) {
		log.info("<AliPayRecordServiceImpl--findPayRecord--start>");
		List<PayRecordDto> findPayRecord = payRecordDao.findPayRecord(payRecordForm);
		log.info("<AliPayRecordServiceImpl--findPayRecord--end>");
		return findPayRecord;
	}
	@Override
	public PayRecordDto findPayRecordById(Long payRecordId) {
		log.info("<AliPayRecordServiceImpl--findPayRecordById--start>");
		PayRecordDto findPayRecordById = payRecordDao.findPayRecordById(payRecordId);
		log.info("<AliPayRecordServiceImpl--findPayRecordById--end>");
		return findPayRecordById;
	}
	@Override
	public boolean updatePayState(String ptCode,String payCode, Integer state) {
		log.info("<AliPayRecordServiceImpl--updatePayState--start>");
		boolean updatePayState = payRecordDao.updatePayState(ptCode,payCode, state);
		log.info("<AliPayRecordServiceImpl--updatePayState--end>");
		return updatePayState;
	}
	@Override
	public PayRecordBean findPayRecordByPayCode(String payCode) {
		log.info("<AliPayRecordServiceImpl--findPayRecordByPayCode--start>");
		PayRecordBean findPayRecordByPayCode = payRecordDao.findPayRecordByPayCode(payCode);
		log.info("<AliPayRecordServiceImpl--findPayRecordByPayCode--end>");
		return findPayRecordByPayCode;
	}
	@Override
	public boolean update(PayRecordBean payRecordBean) {
		log.info("<AliPayRecordServiceImpl--findPayRecordByPayCode--start>");
		boolean updatePayRecord = payRecordDao.updatePayRecord(payRecordBean);
		log.info("<AliPayRecordServiceImpl--findPayRecordByPayCode--start>");
		return updatePayRecord;
	}
	@Override
	public SumPayRecordDto findPayRecordNum(PayRecordForm payRecordForm) {
		log.info("<AliPayRecordServiceImpl--findPayRecordNum--start>");
		SumPayRecordDto findPayRecordNum = payRecordDao.findPayRecordNum(payRecordForm);
		log.info("<AliPayRecordServiceImpl--findPayRecordNum--end>");
		return findPayRecordNum;
	}
	@Override
	public PayRecordDto getOrderInfo(Long id) {
		log.info("<AliPayRecordServiceImpl--getOrderInfo--start>");
		PayRecordDto payRecord = payRecordDao.getOrderInfoVision(id);
		if(payRecord == null) {
			payRecord = payRecordDao.getOrderInfo(id);
		}
		log.info("PayRecordServiceImpl--getById--end");
		return payRecord;
	}
	@Override
	public List<PayRecordDto> findUserAllOrder(Long customerId, Integer state) {
		log.info("<AliPayRecordServiceImpl--getOrderInfo--start>");
		List<PayRecordDto> recordList = payRecordDao.findUserAllOrder(customerId,state);
		log.info("<AliPayRecordServiceImpl--getOrderInfo--end>");
		return recordList;
	}
	@Override
	public List<NewPayRecordDto> getCustomerOrder(Long customerId, Integer state,Integer isShowAll) {
		log.info("<AliPayRecordServiceImpl--getCustomerOrder--start>");
		List<NewPayRecordDto> customerOrder = payRecordDao.getCustomerOrder(customerId,state,isShowAll);
		log.info("<AliPayRecordServiceImpl--getCustomerOrder--end>");
		return customerOrder;
	}
	@Override
	public ReturnDataUtil findMachineHistory(PayRecordForm customerMachineForm) {
		return payRecordDao.findMachineHistory(customerMachineForm);
	}

}

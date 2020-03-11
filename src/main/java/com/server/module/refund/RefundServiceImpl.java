package com.server.module.refund;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RefundServiceImpl implements RefundService{
	
	private final static Logger log = LogManager.getLogger(RefundServiceImpl.class);
	
	@Autowired
	private RefundDao refundDao;

	@Override
	public boolean isPrincipal(Long operateId, String phone) {
		log.info("<RefundServiceImpl--isPrincipal--start>");
		boolean principal = refundDao.isPrincipal(operateId, phone);
		log.info("<RefundServiceImpl--isPrincipal--end>");
		return principal;
	}

	@Override
	public Long insertRefundRecord(RefundRecordBean refundBean) {
		log.info("<RefundServiceImpl--insertRefundRecord--start>");
		Long id = refundDao.insertRefundRecord(refundBean);
		log.info("<RefundServiceImpl--insertRefundRecord--end>");
		return id;
	}

	@Override
	public RefundOrderInfo findOrder(String payCode, Integer orderType) {
		log.info("<RefundServiceImpl--findOrder--start>");
		RefundOrderInfo findOrder = refundDao.findOrder(payCode, orderType);
		log.info("<RefundServiceImpl--findOrder--end>");
		return findOrder;
	}

	@Override
	public List<RefundDto> findGroupBuyOrder(Integer groupId) {
		log.info("<RefundServiceImpl--findGroupBuyOrder--start>");
		List<RefundDto> groupBuyOrderList = refundDao.findGroupBuyOrder(groupId);
		log.info("<RefundServiceImpl--findGroupBuyOrder--end>");
		return groupBuyOrderList;
	}

	@Override
	public boolean updateOrderRefundState(List<String> payCodeList, Integer orderType) {
		log.info("<RefundServiceImpl--updateOrderRefundState--start>");
		boolean result = refundDao.updateOrderRefundState(payCodeList,orderType);
		log.info("<RefundServiceImpl--updateOrderRefundState--end>");
		return result;
	}

	@Override
	public boolean updateGroupBuyState(Integer groupId, Integer state) {
		log.info("<RefundServiceImpl--updateGroupBuyState--start>");
		boolean result = refundDao.updateGroupBuyState(groupId,state);
		log.info("<RefundServiceImpl--updateGroupBuyState--end>");
		return result;
	}

	@Override
	public String getPrincipalInfoById(Long loginInfoId) {
		log.info("<RefundServiceImpl--getPrincipalInfoById--start>");
		String phone = refundDao.getPrincipalInfoById(loginInfoId);
		log.info("<RefundServiceImpl--getPrincipalInfoById--end>");
		return phone;
	}

	@Override
	public Long insertRefundApplication(RefundApplicationBean refundApp) {
		log.info("<RefundServiceImpl--insertRefundApplication--start>");
		Long id = refundDao.insertRefundApplication(refundApp);
		log.info("<RefundServiceImpl--insertRefundApplication--end>");
		return id;
	}

	@Override
	public List<RefundApplicationBean> getRefundApplication(String payCode) {
		log.info("<RefundServiceImpl--getRefundApplication--start>");
		List<RefundApplicationBean> refundApplicationList = refundDao.getRefundApplication(payCode);
		log.info("<RefundServiceImpl--getRefundApplication--end>");
		return refundApplicationList;
	}

	@Override
	public String getPhoneByPayCode(String payCode) {
		log.info("<RefundServiceImpl--getRefundApplication--start>");
		String phone = refundDao.getPhoneByPayCode(payCode);
		log.info("<RefundServiceImpl--getRefundApplication--end>");
		return phone;
	}

	@Override
	public RefundApplicationDto judgeOrderRefund(String payCode, BigDecimal refundPrice) {
		log.info("<RefundServiceImpl--getRefundApplication--start>");
		RefundApplicationDto judgeOrderRefund = refundDao.judgeOrderRefund(payCode,refundPrice);
		log.info("<RefundServiceImpl--getRefundApplication--end>");
		return judgeOrderRefund;
	}

	@Override
	public Long deleteCarry(Long orderId){
		log.info("<RefundServiceImpl--deleteCarry--start>");
		Long id = refundDao.deleteCarry(orderId);
		log.info("<RefundServiceImpl--deleteCarry--end>");
		return id;
	}
}

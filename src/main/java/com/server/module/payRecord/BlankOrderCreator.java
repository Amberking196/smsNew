package com.server.module.payRecord;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.server.module.trade.order.bean.PayRecordBean;
import com.server.module.commonBean.ItemInfoDto;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.dao.PayRecordDao;
import com.server.module.trade.payRecordItem.PayRecordItemBean;
import com.server.module.trade.payRecordItem.PayRecordItemDao;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;
import com.server.util.PayCodeUtil;
import com.server.util.stateEnum.PayStateEnum;
@Component
public class BlankOrderCreator {
	
	@Autowired
	private PayRecordItemDao payRecordItemDao;
	@Autowired
	private PayRecordDao payRecordDao;
	
	
	public void createPayRecordItem(List<ItemChangeDto> itemChangeList,Long payRecordId){
		for(ItemChangeDto itemChangeDto : itemChangeList){
			PayRecordItemBean recordItem  = new PayRecordItemBean();
			recordItem.setBasicItemId(itemChangeDto.getBasicItemId());
			recordItem.setItemName(itemChangeDto.getItemName());
			recordItem.setNum(0);
			recordItem.setPayRecordId(payRecordId);
			recordItem.setPrice(BigDecimal.ZERO.doubleValue());
			recordItem.setRealTotalPrice(BigDecimal.ZERO.doubleValue());
			payRecordItemDao.insert(recordItem);
		}
	}
	
	public void createPayRecordItemByItemInfo(List<ItemInfoDto> itemInfoList,Long payRecordId){
		for(ItemInfoDto itemChangeDto : itemInfoList){
			PayRecordItemBean recordItem  = new PayRecordItemBean();
			recordItem.setBasicItemId(itemChangeDto.getBasicItemId());
			recordItem.setItemName(itemChangeDto.getItemName());
			recordItem.setNum(0);
			recordItem.setPayRecordId(payRecordId);
			recordItem.setPrice(BigDecimal.ZERO.doubleValue());
			recordItem.setRealTotalPrice(BigDecimal.ZERO.doubleValue());
			payRecordItemDao.insert(recordItem);
		}
	}

	public PayRecordBean createPayRecord(UserVo user,String vmCode,Integer wayNum,Long operateHistoryId){
		PayRecordBean payRecord = new PayRecordBean();
		payRecord.setCreateTime(new Date());
		payRecord.setCustomerId(user.getId());
		payRecord.setItemName(null);
		payRecord.setNum(0L);
		String genPayCode = PayCodeUtil.genPayCode(1, vmCode);
		payRecord.setPayCode(genPayCode);
		payRecord.setPayType(user.getPayType().intValue());
		payRecord.setPrice(BigDecimal.ZERO);
		payRecord.setRemark("空白订单");
		payRecord.setState(PayStateEnum.BLANK_ORDER.getState().longValue());
		payRecord.setVendingMachinesCode(vmCode);
		payRecord.setWayNumber(wayNum.longValue());
		payRecord.setOperateHistoryId(operateHistoryId);
		payRecord = payRecordDao.insert(payRecord);
		return payRecord;
	}
	
	public PayRecordBean createPayRecordV3(UserVo user,String vmCode,Integer wayNum,String genPayCode){
		PayRecordBean payRecord = new PayRecordBean();
		payRecord.setCreateTime(new Date());
		payRecord.setCustomerId(user.getId());
		payRecord.setItemName(null);
		payRecord.setNum(0L);
		payRecord.setPayCode(genPayCode);
		payRecord.setPayType(user.getPayType().intValue());
		payRecord.setPrice(BigDecimal.ZERO);
		payRecord.setRemark("支付分空白订单");
		payRecord.setState(PayStateEnum.BLANK_ORDER.getState().longValue());
		payRecord.setVendingMachinesCode(vmCode);
		payRecord.setWayNumber(wayNum.longValue());
		payRecord.setOperateHistoryId(null);
		payRecord = payRecordDao.insert(payRecord);
		return payRecord;
	}
}

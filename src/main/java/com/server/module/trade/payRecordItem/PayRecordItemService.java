package com.server.module.trade.payRecordItem;

import java.util.List;

public interface PayRecordItemService {

	/**
	 * 插入订单商品信息
	 * @author hebiting
	 * @date 2018年9月6日下午5:12:41
	 * @param recordItem
	 * @return
	 */
	public Long insert(PayRecordItemBean recordItem);
	

	/**
	 * 根据订单id获得详情列表
	 * @author hjc
	 * @date 2018年10月9日下午5:12:41
	 * @param payRecordId
	 * @return
	 */
	public List<PayRecordItemBean> getListByPayRecordId(Long payRecordId);
}

package com.server.module.trade.order.dao;

import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.bean.PayRecordBean;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.order.vo.MessageVo;
import com.server.util.ReturnDataUtil;
/**
 * author name: yjr
 * create time: 2018-04-27 09:29:08
 */ 
public interface  PayRecordDao{

public ReturnDataUtil listPage(int curPage,Long customerId);
//public List<PayRecordBean> list(PayRecordCondition condition);
	public boolean update(PayRecordBean entity);
	public boolean delete(Object id);
	public PayRecordBean get(Object id);
	public PayRecordBean insert(PayRecordBean entity);
	public void createPayRecordAndReduceStock(PayRecordBean entity,Long wayId,Integer reduceNum,Integer preNum,String vmCode,int way,Long customerId,Long basicItemId,Long itemId,Long footmarkId);
	public void newCreatePayRecordAndReduceStock(PayRecordBean entity,MachineInfo machineInfo,UserVo userVo,Long footmarkId,boolean existsOpenBeforeOrder);
	/**
	 * 根据支付编码查询交易记录
	 * @author hcj
	 * @date 2018年10月9日下午5:43:48
	 * @param ptCode
	 * @return
	 */
	public PayRecordBean findPayRecordByPtCode(String ptCode);
	/**
	 * 根据支付编码查询交易记录
	 * @author hcj
	 * @date 2018年10月9日下午5:43:48
	 * @param ptCode
	 * @return
	 */
	public PayRecordBean findPayRecordByPayCode(String payCode);
	/**
	 * 根据id获取订单信息
	 * @author hebiting
	 * @date 2018年12月29日上午11:57:42
	 * @param id
	 * @return
	 */
	public PayRecordBean getById(Long id);

	/**
	 * 根据operateId获取订单信息
	 * @author hebiting
	 * @date 2018年12月29日上午11:57:42
	 * @param operateId
	 * @return
	 */
	public PayRecordBean getByOperateId(Long operateId);

	public MessageVo getFinalOrder(String factNum);

	public boolean updateOrderTicket(String outTradeNo,String ticket);
}


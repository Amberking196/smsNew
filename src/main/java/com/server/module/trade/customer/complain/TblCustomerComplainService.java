package com.server.module.trade.customer.complain;

import java.util.List;

/**
 * author name: why create time: 2018-08-17 08:48:16
 */
public interface TblCustomerComplainService {


	/**
	 * 故障申报增加
	 * @param entity
	 * @return
	 */
	public TblCustomerComplainBean add(TblCustomerComplainBean entity);

	/**
	 * 我的故障申报
	 * @param: tblCustomerComplainForm
	 * @return:
	 * @auther: why
	 * @date: 2018/12/1 16:05
	 */
	public List<TblCustomerComplainBean> myDeclaration(TblCustomerComplainForm tblCustomerComplainForm);

	/**
	 * 修改故障申报状态
	 * @param: id
	 * @return:
	 * @auther: why
	 * @date: 2018/12/4 17:48
	 */
	public boolean update(Long  id);

	/**
	 *  用户每天申诉次数
	 * @param:
	 * @return:
	 * @auther: why
	 * @date: 2018/12/20 9:39
	 */
	public  Integer findComplaintsNumberById();


}

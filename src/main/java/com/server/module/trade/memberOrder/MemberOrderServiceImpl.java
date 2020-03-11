package com.server.module.trade.memberOrder;

import com.server.module.trade.customer.TblCustomerBean;
import com.server.module.trade.customer.TblCustomerDao;
import com.server.module.trade.util.UserUtil;
import com.server.module.zfb_trade.module.vmbase.AliVendingMachinesService;
import com.server.module.zfb_trade.module.vmbase.VmbaseInfoDto;
import com.server.util.DateUtil;
import com.server.util.IDUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.PayStateEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * author name: why create time: 2018-09-26 14:36:43
 */
@Service
public class MemberOrderServiceImpl implements MemberOrderService {

	private static Logger log = LogManager.getLogger(MemberOrderServiceImpl.class);
	@Autowired
	private MemberOrderDao memberOrderDaoImpl;
	@Autowired
	private TblCustomerDao tblCustomerDaoImpl;
	@Autowired
	@Qualifier("aliVendingMachinesService")
	private AliVendingMachinesService vendingMachinesService;
	/**
	 * 微信完成商城购买会员订单后的回调，用以确认订单是否完成支付，并更新状态
	 */
	@Override
	public boolean paySuccessMemberOrder(String outTradeNo, String transactionId) {
		log.info("<MemberOrderServiceImpl>------<paySuccessMemberOrder>-----start");
		boolean falg = memberOrderDaoImpl.paySuccessMemberOrder(outTradeNo, transactionId);
		log.info("<MemberOrderServiceImpl>------<paySuccessMemberOrder>-----end");
		return falg;
	}

	/**
	 * 添加 为会员
	 */
	@Override
	public boolean addMember(MemberOrderBean entity) {
		log.info("<MemberOrderServiceImpl>------<addMember>-----start");
		// 查询是否为过期会员
		TblCustomerBean tblCustomerBean = tblCustomerDaoImpl.findBen(entity.getCustomerId());
		if (tblCustomerBean != null) {
			// 已经是会员 进行累加有效期
				// 得到会员开始时间
				Date startTimeLabel = tblCustomerBean.getStartTime();
				String startTime = DateUtil.formatYYYYMMDDHHMMSS(startTimeLabel);
				entity.setStartTime(startTime);
				// 得到会员结束时间
				Date endTimeLabel = tblCustomerBean.getEndTime();
				Calendar cal = Calendar.getInstance();
				cal.setTime(endTimeLabel);
				cal.add(Calendar.MONTH, 1);
				String endTime = DateUtil.formatYYYYMMDDHHMMSS(cal.getTime());
				entity.setEndTime(endTime);
		} else {// 首次成为会员
					// 得到当前时间
				Date date = new Date();
				String startTime = DateUtil.formatYYYYMMDDHHMMSS(date);
				entity.setStartTime(startTime);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.MONTH, 1);
				String endTime = DateUtil.formatYYYYMMDDHHMMSS(cal.getTime());
				entity.setEndTime(endTime);
		}
		boolean addMember = memberOrderDaoImpl.addMember(entity);
		log.info("<MemberOrderServiceImpl>------<addMember>-----end");
		return addMember;
	}

	/**
	 * 获取订单信息
	 */
	@Override
	public MemberOrderBean getMemberOrder(String payCode) {
		log.info("<MemberOrderServiceImpl>------<getMemberOrder>-----start");
		MemberOrderBean memberOrder = memberOrderDaoImpl.getMemberOrder(payCode);
		log.info("<MemberOrderServiceImpl>------<getMemberOrder>-----end");
		return memberOrder;
	}

	/**
	 * 修改用户余额
	 */
	@Override
	public boolean updateCustomerBalance(MemberOrderBean entity) {
		log.info("<MemberOrderServiceImpl>------<updateCustomerBalance>-----start");
		boolean flag = memberOrderDaoImpl.updateCustomerBalance(entity);
		log.info("<MemberOrderServiceImpl>------<getMemberOrder>-----start");
		return flag;
	}

	/**
	 * 增加用户充值订单
	 */
	@Override
	public ReturnDataUtil add(MemberOrderBean entity) {
		log.info("<MemberOrderServiceImpl>-----<add>-------start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		// 得到用户id
		Long customerId = UserUtil.getUser().getId();
		entity.setCustomerId(customerId);
		// 得到用户信息
		TblCustomerBean bean = tblCustomerDaoImpl.getCustomerById(customerId);
		Integer freeMachine=0;
		if(StringUtil.isNotBlank(entity.getVmCode())){
			VmbaseInfoDto baseInfo = vendingMachinesService.getBaseInfo(entity.getVmCode());
			freeMachine=baseInfo.getFreeMachine();
		}

		if(freeMachine==1){
			entity.setType(1);
			createMemberOrder(returnDataUtil,entity);
		}	else {
			if (StringUtils.isBlank(bean.getOpenId())) {
				log.info("==========非微信用户，订单创建失败！============");
				returnDataUtil.setStatus(-99);
				returnDataUtil.setMessage("非微信用户，订单创建失败！");
				return returnDataUtil;
			} else {
				//帮好友充值
				if (StringUtil.isNotBlank(entity.getFriendPhone())) {
					TblCustomerBean vo = tblCustomerDaoImpl.getCustomerByPhone(entity.getFriendPhone());
					if (vo == null) {
						log.info("==========非优水用户，订单创建失败！============");
						returnDataUtil.setStatus(-99);
						returnDataUtil.setMessage("输入手机号非优水用户，订单创建失败！");
						return returnDataUtil;
					} else {
						entity.setFriendCustomerId(vo.getId());
					}
				}
				createMemberOrder(returnDataUtil,entity);
			}
		}
		log.info("<MemberOrderServiceImpl>-----<add>-------end");
		return returnDataUtil;
	}

	public ReturnDataUtil createMemberOrder(ReturnDataUtil returnDataUtil,MemberOrderBean entity){
		// 生成订单编号
		String payCode = IDUtil.getPayCode();
		entity.setState(PayStateEnum.NOT_PAY.getState().longValue());
		entity.setPayCode(payCode);
		MemberOrderBean memberOrderBean = memberOrderDaoImpl.insert(entity);
		if (memberOrderBean != null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("订单创建成功！");
			returnDataUtil.setReturnObject(memberOrderBean);
		} else {
			returnDataUtil.setStatus(-99);
			returnDataUtil.setMessage("订单创建失败！");
			returnDataUtil.setReturnObject(memberOrderBean);
		}
		return returnDataUtil;
	}
}

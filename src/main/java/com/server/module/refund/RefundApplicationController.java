package com.server.module.refund;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.zfb_trade.util.ResultUtil;
import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.RefundApplicationEnum;

@RestController
@RequestMapping("/refundApplication")
public class RefundApplicationController {
	
	@Autowired
	private RefundService refundService;

	/**
	 * 提交退款申请
	 * @author hebiting
	 * @date 2018年10月31日下午2:18:02
	 * @param application
	 * @return
	 */
	@PostMapping("/do")
	public ReturnDataUtil refundApplication(@RequestBody RefundApplicationBean application){
		RefundApplicationDto judgeOrderRefund = refundService.judgeOrderRefund(application.getPayCode(), application.getRefundPrice());
		if(judgeOrderRefund!=null){
			application.setOrderType(judgeOrderRefund.getOrderType());
			application.setPhone(judgeOrderRefund.getPhone());
			application.setPtCode(judgeOrderRefund.getPtCode());
			application.setCompanyId(judgeOrderRefund.getCompanyId());
			application.setState(RefundApplicationEnum.PEDDING_APPROVAL.getState());
			Long id = refundService.insertRefundApplication(application);
			if(id>0){
				return ResultUtil.success();
			}else{
				return ResultUtil.error(ResultEnum.REFUND_APPLICATION_FAIL,null);
			}
		}
		return ResultUtil.error(ResultEnum.REFUND_ORDER_ERROR,null);
	}
	
	/**
	 * 查看退款申请
	 * @author hebiting
	 * @date 2018年10月31日下午2:18:09
	 * @param payCode
	 * @return
	 */
	@PostMapping("/get")
	public ReturnDataUtil getRefundInfo(String payCode){
		List<RefundApplicationBean> applicationList = refundService.getRefundApplication(payCode);
		if(applicationList!=null && applicationList.size()>0){
			return ResultUtil.success(applicationList);
		}
		return ResultUtil.error();
	}
}

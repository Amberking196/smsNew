package com.server.module.payRecord;

import com.server.module.trade.auth.UserVo;
import com.server.module.trade.util.UserUtil;
import com.server.module.zfb_trade.bean.MsgDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alipayRecord")
public class AliPayRecordController {

	@Autowired
	private AliPayRecordService payRecordService;
	/**
	 * 查询订单信息
	 * @author hebiting
	 * @date 2018年5月27日下午8:41:13
	 * @param vmCode
	 * @param customerId
	 * @param state
	 * @return
	 */
	@PostMapping("/findPayRecord")
	public List<PayRecordDto> findPayRecord(@RequestParam(required=false)String vmCode, 
			@RequestParam(required=false)Long customerId,
			@RequestParam(required=false)Integer state) {
		PayRecordForm form = new PayRecordForm();
		form.setVmCode(vmCode);
		form.setCustomerId(customerId);
		form.setState(state);
		return payRecordService.findPayRecord(form);
	}
	
	
	/**
	 * 根据payCode查询订单信息
	 * @author hebiting
	 * @date 2018年5月27日下午8:41:17
	 * @param payCode
	 * @return
	 */
	@PostMapping("/findPayRecordByPayCode")
	public PayRecordBean findPayRecordByPayCode(@RequestParam String payCode){
		return payRecordService.findPayRecordByPayCode(payCode);
	}
	
	
	/**
	 * 更新订单信息
	 * @author hebiting
	 * @date 2018年5月27日下午8:41:23
	 * @param payRecordBean
	 * @return
	 */
	@PostMapping("/update")
	public boolean update(@RequestBody PayRecordBean payRecordBean) {
		return payRecordService.update(payRecordBean);
	}
	
	/**
	 * 更新订单状态及支付平台内部订单号
	 * @author hebiting
	 * @date 2018年6月19日下午4:37:01
	 * @param ptCode
	 * @param payCode
	 * @param state
	 * @return
	 */
	@PostMapping("/updatePayState")
	public boolean updatePayState(String ptCode,String payCode, Integer state) {
		return payRecordService.updatePayState(ptCode, payCode, state);
	}
	
	/**
	 * 查询消费消息
	 * @author hebiting
	 * @date 2018年6月19日下午4:40:08
	 * @param payCode
	 * @return
	 */
	@PostMapping("/findMsgDto")
	public MsgDto findMsgDto(String payCode){
		return payRecordService.findMsgDto(payCode);
	}
	
	/**
	 * 获取用户订单信息
	 * @author hebiting
	 * @date 2018年11月14日下午5:07:13
	 * @param state
	 * @return
	 */
	@GetMapping("/getCustomerOrder")
	public List<NewPayRecordDto> getCustomerOrder(@RequestParam(required = false)Integer state,
			@RequestParam(required = false)Integer isShowAll){
		UserVo user = UserUtil.getUser();
		if(isShowAll == null){
			isShowAll = 0;
		}
		if(user != null && user.getType() == UserVo.USER_CUSTOMER){
			//user.setId(314L);
			List<NewPayRecordDto> customerOrder = payRecordService.getCustomerOrder(user.getId(),state,isShowAll);
			return customerOrder;
		}
		return null;
	}
}

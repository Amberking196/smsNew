package com.server.module.zfb_trade.convert;

import java.util.List;

import com.server.module.payRecord.PayRecordDto;
import com.server.util.stateEnum.PayStateEnum;


public class PayRecordConvert {

	/**
	 * @Description: 订单状态转换类 
	 */
	public static void payRecordConver(List<PayRecordDto> webPayRecordDTOs) {
		if (webPayRecordDTOs != null) {
			for (PayRecordDto webPayRecordDTO : webPayRecordDTOs) {
				Integer state = Integer.parseInt(webPayRecordDTO.getState());
				if (PayStateEnum.PAY_SUCCESS.getState().equals(state)) {
					webPayRecordDTO.setState(PayStateEnum.PAY_SUCCESS.getName());
				} else if (PayStateEnum.NOT_PAY.getState().equals(state)) {
					webPayRecordDTO.setState(PayStateEnum.NOT_PAY.getName());
				} else if (PayStateEnum.PAY_FINISHED.getState().equals(state)){
					webPayRecordDTO.setState(PayStateEnum.PAY_FINISHED.getName());
				}
//				else if (PayStateEnum.outItem.getState().equals(state)) {
//					webPayRecordDTO.setState(PayStateEnum.outItem.getName());
//				} else if (PayStateEnum.payFail.getState().equals(state)) {
//					webPayRecordDTO.setState(PayStateEnum.payFail.getName());
//				} else if (PayStateEnum.cancelOrder.getState().equals(state)) {
//					webPayRecordDTO.setState(PayStateEnum.cancelOrder.getName());
//				} else if (PayStateEnum.refundSuccess.getState().equals(state)) {
//					webPayRecordDTO.setState(PayStateEnum.refundSuccess.getName());
//				} else if (PayStateEnum.refundFail.getState().equals(state)) {
//					webPayRecordDTO.setState(PayStateEnum.refundFail.getName());
//				}
			}
		}
	}
	
}

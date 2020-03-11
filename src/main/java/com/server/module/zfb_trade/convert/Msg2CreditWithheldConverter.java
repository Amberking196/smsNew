package com.server.module.zfb_trade.convert;

import java.util.Map;
import com.server.module.zfb_trade.module.creditwithheld.CreditWithheldBean;
import com.server.module.zfb_trade.util.DateUtil;

public class Msg2CreditWithheldConverter {
	/**
	 * @Description: 信用类赋值 
	 */
	public static CreditWithheldBean setCreditWithheld(Map<String, String[]> requestParams) {
		CreditWithheldBean creditWithheld = new CreditWithheldBean();
		for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			for (int i = 0; i < values.length; i++) {
				if (key.equalsIgnoreCase("agreement_no")) {
					creditWithheld.setAgreementNo(values[i]);
				} else if (key.equalsIgnoreCase("alipay_user_id")) {
					creditWithheld.setAlipayUserId(values[i]);
				} else if (key.equalsIgnoreCase("external_sign_no")) {
					creditWithheld.setExternalSignNo(values[i]);
				} else if (key.equalsIgnoreCase("invalid_time")) {
					if (values[i] != null) {
						creditWithheld.setInvalidTime(DateUtil.format(values[i], "yyyy-MM-dd HH:mm:ss"));
					}
				} else if (key.equalsIgnoreCase("is_success")) {
					creditWithheld.setIsSuccess(values[i]);
				} else if (key.equalsIgnoreCase("product_code")) {
					creditWithheld.setProductCode(values[i]);
				} else if (key.equalsIgnoreCase("scene")) {
					creditWithheld.setScene(values[i]);
				} else if (key.equalsIgnoreCase("sign_modify_time")) {
					if (values[i] != null) {
						creditWithheld.setSignModifyTime(DateUtil.format(values[i], "yyyy-MM-dd HH:mm:ss"));
					}
				} else if (key.equalsIgnoreCase("sign_time")) {
					if (values[i] != null) {
						creditWithheld.setSignTime(DateUtil.format(values[i], "yyyy-MM-dd HH:mm:ss"));
					}
				} else if (key.equalsIgnoreCase("status")) {
					creditWithheld.setStatus(values[i]);
				} else if (key.equalsIgnoreCase("valid_time")) {
					if (values[i] != null) {
						creditWithheld.setValidTime(DateUtil.format(values[i], "yyyy-MM-dd HH:mm:ss"));
					}
				} else if (key.equalsIgnoreCase("sign")) {
					creditWithheld.setSign(values[i]);
				} else if (key.equalsIgnoreCase("sign_type")) {
					creditWithheld.setSignType(values[i]);
				}
			}
		}
		return creditWithheld;
	}
}

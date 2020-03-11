package com.server.module.zfb_trade.module.creditwithheld;

public interface AliCreditWithheldDao {

	/**
	 * 查询用户是否已签免密支付协议
	 * @author hebiting
	 * @date 2018年5月27日下午8:38:15
	 * @param creditWithheld
	 * @return
	 */
	public CreditWithheldBean queryByAliId(CreditWithheldBean creditWithheld);
	
	/**
	 * 保存签约免密协议信息
	 * @author hebiting
	 * @date 2018年5月27日下午8:32:50
	 * @param creditWithheld
	 * @return
	 */
	public boolean save(CreditWithheldBean creditWithheld);
	//未使用
	public boolean update(CreditWithheldBean creditWithheld);
}

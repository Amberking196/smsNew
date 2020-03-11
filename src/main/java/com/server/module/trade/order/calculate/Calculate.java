package com.server.module.trade.order.calculate;

import java.math.BigDecimal;
import java.util.List;

import com.server.module.trade.order.domain.MachineInfo;

public interface Calculate {

	BigDecimal calculate(MachineInfo machineInfo,BigDecimal couponMoney,
			List<Long> itemList,Double maxFavMoney);
}

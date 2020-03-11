package com.server.module.carryWaterVouchersManage.carryWaterVouchers;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.coupon.MachinesLAC;
import com.server.util.ReturnDataUtil;


/**
 * author name: why create time: 2018-11-03 09:02:25
 */
@Service
public class CarryWaterVouchersServiceImpl implements CarryWaterVouchersService {

	private static Logger log = LogManager.getLogger(CarryWaterVouchersServiceImpl.class);
	@Autowired
	private CarryWaterVouchersDao carryWaterVouchersDaoImpl;
	

	/**
	 * 获取提水券信息
	 */
	public CarryWaterVouchersBean get(Long id) {
		log.info("<CarryWaterVouchersServiceImpl>-------<get>------start");
		CarryWaterVouchersBean carryWaterVouchersBean = carryWaterVouchersDaoImpl.get(id);
		log.info("<CarryWaterVouchersServiceImpl>-------<get>------end");
		return carryWaterVouchersBean;
	}

	
	/**
	 * 提水券列表
	 */
	public ReturnDataUtil listPage(CarryWaterVouchersForm carryWaterVouchersForm) {
		log.info("<CarryWaterVouchersServiceImpl>------<listPage>------start");
		ReturnDataUtil listPage = carryWaterVouchersDaoImpl.listPage(carryWaterVouchersForm);
		log.info("<CarryWaterVouchersServiceImpl>------<listPage>------end");
		return listPage;
	}




	@Override
	public Map<CarryDto, List<Long>> getCarryWaterVoucherByCustomer(Long customerId, MachinesLAC mlac,
			String productIds) {
		log.info("<CarryWaterVouchersServiceImpl>-------<getCarryWaterVoucherByCustomer>------start");
		Map<CarryDto, List<Long>> carryMap = carryWaterVouchersDaoImpl.getCarryWaterVoucherByCustomer(customerId, mlac, productIds);
		log.info("<CarryWaterVouchersServiceImpl>-------<getCarryWaterVoucherByCustomer>------start");
		return carryMap;
	}


	
}

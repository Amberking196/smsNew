package com.server.module.carryWaterVouchersManage.carryWaterVouchersCustomer;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersBean;
import com.server.module.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersDao;

/**
 * author name: why create time: 2018-11-03 16:14:12
 */
@Service
public class CarryWaterVouchersCustomerServiceImpl implements CarryWaterVouchersCustomerService {

	private static Logger log = LogManager.getLogger(CarryWaterVouchersCustomerServiceImpl.class);
	@Autowired
	private CarryWaterVouchersCustomerDao carryWaterVouchersCustomerDaoImpl;
	@Autowired
	private CarryWaterVouchersDao carryWaterVouchersDaoImpl;

	

	/**
	 * 提水券绑定用户
	 */
	public CarryWaterVouchersCustomerBean add(Long carryId,Long customerId,Integer num,Long orderId) {
		log.info("<CarryWaterVouchersCustomerServiceImpl>--------<add>------start");
		//得到提水券信息
		CarryWaterVouchersBean carryWaterVouchersBean = carryWaterVouchersDaoImpl.get(carryId);
		//得到本次购买数量和下发张数  最终为下发张数
		Integer sum=carryWaterVouchersBean.getSendMax()*num;
		log.info("最终下发张数======"+sum);
		CarryWaterVouchersCustomerBean entity=new CarryWaterVouchersCustomerBean();
		entity.setCarryId(carryId);
		entity.setCustomerId(customerId);
		entity.setStartTime(carryWaterVouchersBean.getLogicStartTime());
		entity.setEndTime(carryWaterVouchersBean.getLogicEndTime());
		entity.setQuantity(sum.longValue());
		entity.setCreateUser(customerId);
		entity.setOrderId(orderId);
		CarryWaterVouchersCustomerBean bean = carryWaterVouchersCustomerDaoImpl.insert(entity);
		log.info("<CarryWaterVouchersCustomerServiceImpl>--------<add>------end");
		return bean;
	}

	/**
	 * 更新用户提水券
	 */
	public boolean update(CarryWaterVouchersCustomerBean entity) {
		return carryWaterVouchersCustomerDaoImpl.update(entity);
	}


	public List<CarryWaterVouchersCustomerDto> myCarryWaterVouchers(CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm){
		log.info("<CarryWaterVouchersCustomerServiceImpl>--------<myCarryWaterVouchers>------start");
		 List<CarryWaterVouchersCustomerDto> list = carryWaterVouchersCustomerDaoImpl.myCarryWaterVouchers(carryWaterVouchersCustomerForm);
		log.info("<CarryWaterVouchersCustomerServiceImpl>--------<myCarryWaterVouchers>------end");
		return list;
	}

	@Override
	public boolean delete(Long orderId) {
		log.info("<CarryWaterVouchersCustomerServiceImpl>--------<delete>------start");
		boolean delete = carryWaterVouchersCustomerDaoImpl.delete(orderId);
		log.info("<CarryWaterVouchersCustomerServiceImpl>--------<delete>------end");
		return delete;
	}

	
}

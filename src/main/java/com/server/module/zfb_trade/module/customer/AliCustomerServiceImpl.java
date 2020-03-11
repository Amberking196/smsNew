package com.server.module.zfb_trade.module.customer;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("aliCustomerService")
public class AliCustomerServiceImpl implements AliCustomerService{

	public static Logger log = LogManager.getLogger(AliCustomerServiceImpl.class); 
	@Autowired
	@Qualifier("aliCustomerDao")
	private AliCustomerDao customerDao;
	
	@Override
	public CustomerBean queryByAliId(CustomerBean customer) {
		log.info("<AliCustomerServiceImpl--queryByAliId--start>");
		CustomerBean queryByAliId = customerDao.queryByAliId(customer);
		log.info("<AliCustomerServiceImpl--queryByAliId--end>");
		return queryByAliId;
	}

	@Override
	public Long saveCustomer(CustomerBean customer) {
		log.info("<AliCustomerServiceImpl--saveCustomer--start>");
		Long customerId = customerDao.saveCustomer(customer);
		log.info("<AliCustomerServiceImpl--saveCustomer--end>");
		return customerId;
	}

	@Override
	public boolean updateCustomer(CustomerBean customer) {
		log.info("<AliCustomerServiceImpl--updateCustomer--start>");
		boolean updateCustomer = customerDao.updateCustomer(customer);
		log.info("<AliCustomerServiceImpl--updateCustomer--end>");
		return updateCustomer;
	}

	@Override
	public CustomerBean queryById(Long id) {
		log.info("<AliCustomerServiceImpl--queryById--start>");
		CustomerBean queryById = customerDao.queryById(id);
		log.info("<AliCustomerServiceImpl--queryById--end>");
		return queryById;
	}

	@Override
	public List<CustomerBean> queryCustomer(List<Long> customerIdList) {
		log.info("<AliCustomerServiceImpl--queryCustomer--start>");
		List<CustomerBean> queryCustomer = null;
		if(customerIdList!=null && customerIdList.size()>0){
			String idList = StringUtils.join(customerIdList, ",");
			queryCustomer = customerDao.queryCustomer(idList);
		}
		log.info("<AliCustomerServiceImpl--queryCustomer--end>");
		return queryCustomer;
	}

	@Override
	public CustomerBean queryByPhone(String phone) {
		log.info("<AliCustomerServiceImpl--queryByPhone--start>");
		CustomerBean queryByPhone = customerDao.queryByPhone(phone);
		log.info("<AliCustomerServiceImpl--queryByPhone--end>");
		return queryByPhone;
	}

	@Override
	public boolean delCustomer(Long id) {
		log.info("<AliCustomerServiceImpl--delCustomer--start>");
		Integer delCustomer = customerDao.delCustomer(id);
		log.info("<AliCustomerServiceImpl--delCustomer--end>");
		if(delCustomer == 1){
			return true;
		}
		return false;
	}

	/**
	 * 根据phone查询顾客是否自己人
	 * @author hjc
	 * @date 2019年5月31日下午8:34:52
	 * @param id
	 * @return
	 */
	public boolean queryIsLoginUserByPhone(String phone) {
		return customerDao.queryIsLoginUserByPhone(phone);
	}
}

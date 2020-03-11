package com.server.module.trade.customer.address;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService{

	private static final Logger log = LogManager.getLogger(AddressServiceImpl.class);
	
	@Autowired
	private AddressDao addressDao;
	
	@Override
	public AddressBean insert(AddressBean address) {
		log.info("<AddressServiceImpl--insert--start>");
		AddressBean insert = addressDao.insert(address);
		log.info("<AddressServiceImpl--insert--end>");
		return insert;
	}

	@Override
	public boolean update(AddressBean address) {
		log.info("<AddressServiceImpl--update--start>");
		boolean update = addressDao.update(address);
		log.info("<AddressServiceImpl--update--end>");
		return update;
	}

	@Override
	public List<AddressBean> select(Long customerId) {
		log.info("<AddressServiceImpl--select--start>");
		List<AddressBean> select = addressDao.select(customerId);
		log.info("<AddressServiceImpl--select--end>");
		return select;
	}

	@Override
	public boolean delete(Long addressId) {
		log.info("<AddressServiceImpl--delete--start>");
		boolean delete = addressDao.delete(addressId);
		log.info("<AddressServiceImpl--delete--end>");
		return delete;
	}

	@Override
	public boolean updateDefaultFlag(Long customerId) {
		log.info("<AddressServiceImpl--updateDefaultFlag--start>");
		boolean updateDefaultFlag = addressDao.updateDefaultFlag(customerId);
		log.info("<AddressServiceImpl--updateDefaultFlag--start>");
		return updateDefaultFlag;
	}

	@Override
	public AddressBean selectById(Long customerId, Integer id) {
		log.info("<AddressServiceImpl--selectById--start>");
		AddressBean selectById = addressDao.selectById(customerId, id);
		log.info("<AddressServiceImpl--selectById--end>");
		return selectById;
	}

}

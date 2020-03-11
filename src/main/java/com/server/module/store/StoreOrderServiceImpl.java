package com.server.module.store;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreOrderServiceImpl implements StoreOrderService{

	private static Logger log = LogManager.getLogger(StoreOrderDaoImpl.class);
	
	@Autowired
	private StoreOrderDao storeOrderDao;

	@Override
	public CustomerStockBean getStock(Integer itemId,Long customerId) {
		log.info("<StoreOrderServiceImpl--getStock--start>");
		CustomerStockBean stock = storeOrderDao.getStock(itemId,customerId);
		log.info("<StoreOrderServiceImpl--getStock--end>");
		return stock;
	}

	@Override
	public CustomerStockBean insert(CustomerStockBean cusStock) {
		log.info("<StoreOrderServiceImpl--insert--start>");
		CustomerStockBean insert = storeOrderDao.insert(cusStock);
		log.info("<StoreOrderServiceImpl--insert--end>");
		return insert;
	}

	@Override
	public boolean update(CustomerStockBean cusStock) {
		log.info("<StoreOrderServiceImpl--update--start>");
		boolean update = storeOrderDao.update(cusStock);
		log.info("<StoreOrderServiceImpl--update--end>");
		return update;
	}

	@Override
	public List<OrderDetailBean> getOrderDetail(String payCode) {
		log.info("<StoreOrderServiceImpl--getOrderDetail--start>");
		List<OrderDetailBean> orderDetail = storeOrderDao.getOrderDetail(payCode);
		log.info("<StoreOrderServiceImpl--getOrderDetail--end>");
		return orderDetail;
	}

	@Override
	public Integer getStoreItem(Integer itemId) {
		log.info("<StoreOrderServiceImpl--getStoreItem--start>");
		Integer storeItem = storeOrderDao.getStoreItem(itemId);
		log.info("<StoreOrderServiceImpl--getStoreItem--end>");
		return storeItem;
	}

	@Override
	public Integer insertCouponLog(CouponLog coupouLog) {
		log.info("<StoreOrderServiceImpl--insertCouponLog--start>");
		Integer insertCouponLog = storeOrderDao.insertCouponLog(coupouLog);
		log.info("<StoreOrderServiceImpl--insertCouponLog--end>");
		return insertCouponLog;
	}

	@Override
	public Integer insertPickRecord(CustomerPickRecord pickRecord) {
		log.info("<StoreOrderServiceImpl--insertPickRecord--start>");
		Integer insertPickRecord = storeOrderDao.insertPickRecord(pickRecord);
		log.info("<StoreOrderServiceImpl--insertPickRecord--end>");
		return insertPickRecord;
	}

	@Override
	public CustomerStockBean getStockByBasicItem(Long basicItemId, Long customerId) {
		log.info("<StoreOrderServiceImpl--getStockByBasicItem--start>");
		CustomerStockBean stock = storeOrderDao.getStockByBasicItem(basicItemId,customerId);
		log.info("<StoreOrderServiceImpl--getStockByBasicItem--end>");
		return stock;
	}
	
	@Override
	public OrderBean getStoreOrderbyOutTradeNo(String outTradeNo) {
		log.info("<StoreOrderServiceImpl--getStoreOrderbyOutTradeNo--start>");
		OrderBean orderBean = storeOrderDao.getStoreOrderbyOutTradeNo(outTradeNo);
		log.info("<StoreOrderServiceImpl--getStoreOrderbyOutTradeNo--end>");
		return orderBean;
	}
	
	@Override
	public boolean updateParCustomerIdAndState(Long customerGroupId,String participationCustomerId,Integer state) {
		log.info("<StoreOrderServiceImpl--updateParCustomerIdAndState--start>");
		boolean orderBean = storeOrderDao.updateParCustomerIdAndState(customerGroupId,participationCustomerId,state);
		log.info("<StoreOrderServiceImpl--updateParCustomerIdAndState--end>");
		return orderBean;
	}
	@Override
	public Integer insertTcSpellGroup(TblCustomerSpellGroupBean tcGroupBean) {
		log.info("<StoreOrderServiceImpl--insertTcSpellGroup--start>");
		Integer orderBean = storeOrderDao.insertTcSpellGroup(tcGroupBean);
		log.info("<StoreOrderServiceImpl--insertTcSpellGroup--end>");
		return orderBean;
	}
	@Override
	public boolean updateStoreOrderbyCustomerGroupId(Integer customerGroupId,Long orderId) {
		log.info("<StoreOrderServiceImpl--updateStoreOrderbyCustomerGroupId--start>");
		boolean orderBean = storeOrderDao.updateStoreOrderbyCustomerGroupId(customerGroupId,orderId);
		log.info("<StoreOrderServiceImpl--updateStoreOrderbyCustomerGroupId--end>");
		return orderBean;
	}

	@Override
	public List<ShoppingGoodsProductBean> getShoppingGoodsProductBean(Long shoppingGoodsId) {
		log.info("<StoreOrderServiceImpl--getShoppingGoodsProductBean--start>");
		List<ShoppingGoodsProductBean> list = storeOrderDao.getShoppingGoodsProductBean(shoppingGoodsId);
		log.info("<StoreOrderServiceImpl--getShoppingGoodsProductBean--end>");
		return list;
	}

	@Override
	public List<OrderBean> getStoreOrderbyCustomerGroupId(Long customerGroupId) {
		log.info("<StoreOrderServiceImpl>----<getStoreOrderbyCustomerGroupId>----start>");
		List<OrderBean> list = storeOrderDao.getStoreOrderbyCustomerGroupId(customerGroupId);
		log.info("<StoreOrderServiceImpl>----<getStoreOrderbyCustomerGroupId>----end>");
		return list;	
	}

}

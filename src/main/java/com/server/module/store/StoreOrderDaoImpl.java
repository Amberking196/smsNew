package com.server.module.store;

import com.google.common.collect.Lists;
import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class StoreOrderDaoImpl extends MySqlFuns implements StoreOrderDao{

	private static Logger log = LogManager.getLogger(StoreOrderDaoImpl.class);

	@Override
	public CustomerStockBean getStock(Integer itemId,Long customerId) {
		log.info("<StoreOrderDaoImpl--getStock--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,customerId,itemId,itemName,stock,pickNum,createTime,updateTime,basicItemId");
		sql.append(" FROM `tbl_customer_stock` WHERE customerId = "+customerId+" AND itemId = "+itemId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CustomerStockBean cusStock = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				cusStock = new CustomerStockBean();
				cusStock.setId(rs.getLong("id"));
				cusStock.setCreateTime(rs.getTimestamp("createTime"));
				cusStock.setCustomerId(rs.getLong("customerId"));
				cusStock.setItemId(rs.getInt("itemId"));
				cusStock.setItemName(rs.getString("itemName"));
				cusStock.setPickNum(rs.getInt("pickNum"));
				cusStock.setStock(rs.getInt("stock"));
				cusStock.setUpdateTime(rs.getTimestamp("updateTime"));
				cusStock.setBasicItemId(rs.getLong("basicItemId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StoreOrderDaoImpl--getStock--end>");
		return cusStock;
	}

	@Override
	public CustomerStockBean insert(CustomerStockBean cusStock) {
		log.info("<StoreOrderDaoImpl--insert--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO `tbl_customer_stock`(customerId,itemId,itemName,stock,pickNum,basicItemId)");
		sql.append(" VALUES(?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(cusStock.getCustomerId());
		param.add(cusStock.getItemId());
		param.add(cusStock.getItemName());
		param.add(cusStock.getStock());
		param.add(cusStock.getPickNum());
		param.add(cusStock.getBasicItemId());
		int insert = insertGetID(sql.toString(),param);
		log.info("<StoreOrderDaoImpl--insert--end>");
		if(insert > 0){
			cusStock.setId(Long.valueOf(insert));
			return cusStock;
		}
		return null;
	}

	@Override
	public boolean update(CustomerStockBean cusStock) {
		log.info("<StoreOrderDaoImpl--update--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE `tbl_customer_stock` SET stock = ?, pickNum = ? ,updateTime =?");
		sql.append(" WHERE customerId = ? ");
		List<Object> param = new ArrayList<Object>();
		param.add(cusStock.getStock());
		param.add(cusStock.getPickNum());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
		param.add(cusStock.getCustomerId());
		if(cusStock.getBasicItemId()!=null){
			sql.append(" AND basicItemId= ?");
			param.add(cusStock.getBasicItemId());
		}else{
			sql.append(" AND itemId= ?");
			param.add(cusStock.getItemId());
		}
		int upate = upate(sql.toString(),param);
		log.info("<StoreOrderDaoImpl--update--end>");
		if(upate>0){
			return true;
		}
		return false;
	}

	@Override
	public List<OrderDetailBean> getOrderDetail(String payCode) {
		log.info("<StoreOrderDaoImpl--getOrderDetail--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ib.pic,sod.id,sod.orderId,sod.itemId,sod.price,sod.num,sod.customerId,so.distributionModel,sod.itemName ");
		sql.append(" FROM `store_order_detile` AS sod ");
		sql.append(" INNER JOIN `store_order` AS so ON sod.orderId = so.id");
		sql.append(" left JOIN `item_basic` AS ib ON ib.id = sod.itemId");

		//so.distributionModel = 1 and 
		sql.append(" WHERE so.payCode = "+payCode);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<OrderDetailBean> orderList = new ArrayList<OrderDetailBean>();
		OrderDetailBean order = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				order = new OrderDetailBean();
				order.setId(rs.getLong("id"));
				order.setOrderId(rs.getLong("orderId"));
				order.setItemId(rs.getInt("itemId"));
				order.setPrice(rs.getBigDecimal("price"));
				order.setNum(rs.getInt("num"));
				order.setItemName(rs.getString("itemName"));
				order.setCustomerId(rs.getLong("customerId"));
				order.setDistributionModel(rs.getInt("distributionModel"));
				order.setPic("http://119.23.233.123:6662/ys_admin/files/"+rs.getString("pic"));
				orderList.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StoreOrderDaoImpl--getOrderDetail--end>");
		return orderList;
	}

	@Override
	public Integer getStoreItem(Integer itemId) {
		log.info("<StoreOrderDaoImpl--getMachinesItem--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT sg.id AS storeItemId FROM `vending_machines_item` AS vmi");
		sql.append(" INNER JOIN item_basic AS ib ON vmi.basicItemId = ib.id");
		sql.append(" INNER JOIN `shopping_goods` AS sg ON sg.basicItemId = ib.id");
		sql.append(" WHERE vmi.id = "+itemId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer storeItemId = -1;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				storeItemId = rs.getInt("storeItemId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StoreOrderDaoImpl--getMachinesItem--end>");
		return storeItemId;
	}

	@Override
	public Integer insertCouponLog(CouponLog coupouLog) {
		log.info("<StoreOrderDaoImpl--insertCouponLog--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO `coupon_use_log`(couponId,couponCustomerId,orderId,`type`,money,deductionMoney,createTime)");
		sql.append(" VALUES(?,?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(coupouLog.getCouponId());
		param.add(coupouLog.getCouponCustomerId());
		param.add(coupouLog.getOrderId());
		param.add(coupouLog.getType());
		param.add(coupouLog.getMoney());
		param.add(coupouLog.getDeductionMoney());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
		int insertGetID = insertGetID(sql.toString(), param);
		log.info("<StoreOrderDaoImpl--insertCouponLog--end>");
		return insertGetID;
	}

	@Override
	public Integer insertPickRecord(CustomerPickRecord pickRecord) {
		log.info("<StoreOrderDaoImpl--insertPickRecord--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO tbl_customer_record(customerId,basicItemId,pickNum,vmCode,wayNum,createTime)");
		sql.append(" VALUES(?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(pickRecord.getCustomerId());
		param.add(pickRecord.getBasicItemId());
		param.add(pickRecord.getPickNum());
		param.add(pickRecord.getVmCode());
		param.add(pickRecord.getWayNum());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
		int insertGetID = insertGetID(sql.toString(),param);
		log.info("<StoreOrderDaoImpl--insertPickRecord--end>");
		return insertGetID;
	}

	@Override
	public CustomerStockBean getStockByBasicItem(Long basicItemId, Long customerId) {
		log.info("<StoreOrderDaoImpl--getStockByBasicItem--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,customerId,itemId,itemName,stock,pickNum,createTime,updateTime,basicItemId");
		sql.append(" FROM `tbl_customer_stock` WHERE stock > pickNum AND customerId = "+customerId+" AND basicItemId = "+basicItemId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CustomerStockBean cusStock = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				cusStock = new CustomerStockBean();
				cusStock.setId(rs.getLong("id"));
				cusStock.setCreateTime(rs.getTimestamp("createTime"));
				cusStock.setCustomerId(rs.getLong("customerId"));
				cusStock.setItemId(rs.getInt("itemId"));
				cusStock.setItemName(rs.getString("itemName"));
				cusStock.setPickNum(rs.getInt("pickNum"));
				cusStock.setStock(rs.getInt("stock"));
				cusStock.setUpdateTime(rs.getTimestamp("updateTime"));
				cusStock.setBasicItemId(rs.getLong("basicItemId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StoreOrderDaoImpl--getStockByBasicItem--end>");
		return cusStock;
	}

	@Override
	public List<CustomerStockBean> getStockList(String basicItems, Long customerId) {
		log.info("<StoreOrderDaoImpl--getStockList--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,customerId,itemId,itemName,stock,pickNum,basicItemId ");
		sql.append(" FROM tbl_customer_stock WHERE stock>pickNum");
		sql.append(" AND customerId = "+customerId+" AND basicItemId IN ("+basicItems+")");
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CustomerStockBean cusStock = null;
		List<CustomerStockBean> stockList = new ArrayList<CustomerStockBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				cusStock = new CustomerStockBean();
				cusStock.setId(rs.getLong("id"));
				cusStock.setCustomerId(rs.getLong("customerId"));
				cusStock.setItemId(rs.getInt("itemId"));
				cusStock.setItemName(rs.getString("itemName"));
				cusStock.setPickNum(rs.getInt("pickNum"));
				cusStock.setStock(rs.getInt("stock"));
				cusStock.setBasicItemId(rs.getLong("basicItemId"));
				stockList.add(cusStock);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StoreOrderDaoImpl--getStockList--end>");
		return stockList;
	}
	

	
	@Override
	public OrderBean getStoreOrderbyOutTradeNo (String  outTradeNo) {
		log.info("<StoreOrderDaoImpl--getStoreOrderbyOutTradeNo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" select sg.typeId,sg.vouchersId,sg.vouchersIds,sg.name as goodsName,so.product,so.id,so.customerGroupId,so.customerId,so.openid,so.price,so.num,sgs.minimumGroupSize,tcs.participationCustomerId,timeLimit,tcs.startCustomerId from store_order so left join tbl_customer_spellGroup tcs on so.customerGroupId=tcs.id ");
		sql.append(" left join shopping_goods_spellGroup sgs on sgs.id=tcs.spellGroupId");
		sql.append(" left join shopping_goods sg on sg.id=sgs.goodsId");
		sql.append(" where so.payCode ='"+outTradeNo+"' ");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		OrderBean order = new OrderBean();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				order.setId(rs.getLong("id"));
				order.setCustomerGroupId(rs.getLong("customerGroupId"));
				order.setCustomerId(rs.getLong("customerId"));
				order.setOpenid(rs.getString("openid"));
				order.setPrice(rs.getBigDecimal("price"));
				order.setMinimumGroupSize(rs.getInt("minimumGroupSize"));
				order.setParticipationCustomerId(rs.getString("participationCustomerId"));
				order.setTimeLimit(rs.getInt("timeLimit"));
				order.setProduct(rs.getString("product"));
				order.setStartCustomerId(rs.getLong("startCustomerId"));
				order.setName(rs.getString("goodsName"));
				order.setTypeId(rs.getInt("typeId"));
				order.setVouchersId(rs.getLong("vouchersId"));
				order.setVouchersIds(rs.getString("vouchersIds"));
				order.setNum(rs.getInt("num"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StoreOrderDaoImpl--getStoreOrderbyOutTradeNo--end>");
		return order;
	}
	
	@Override
	public boolean updateParCustomerIdAndState(Long customerGroupId,String participationCustomerId,Integer state) {
		log.info("<StoreOrderDaoImpl--updateParCustomerIdAndState--start>");
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isNotBlank(participationCustomerId)){
			sql.append(" UPDATE `tbl_customer_spellGroup` SET participationCustomerId = '"+participationCustomerId+"'");
			if(state!=null) {
				sql.append(" ,state = "+state);
			}
			sql.append(" WHERE id =  "+customerGroupId);
		}else {
			sql.append(" UPDATE `tbl_customer_spellGroup` SET state = "+state);
			sql.append(" WHERE id =  "+customerGroupId);
		}

		log.info("sql语句："+sql);
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			int rowCount = ps.executeUpdate();
			if(rowCount>0) {
				return true;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StoreOrderDaoImpl--updateParCustomerIdAndState--end>");
		return false;
	}
	
	@Override
	public Integer insertTcSpellGroup(TblCustomerSpellGroupBean tcGroupBean) {
		log.info("<StoreOrderDaoImpl--insertTcSpellGroup--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO tbl_customer_spellGroup(goodsId,startCustomerId,endTime)");
		sql.append(" VALUES(?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(tcGroupBean.getGoodsId());
		param.add(tcGroupBean.getStartCustomerId());
		param.add(tcGroupBean.getEndTime());
		int insert = insertGetID(sql.toString(),param);
		log.info("<StoreOrderDaoImpl--insertTcSpellGroup--end>");
		return insert;
	}
	
	public boolean updateStoreOrderbyCustomerGroupId(Integer customerGroupId,Long orderId) {
		log.info("<StoreOrderDaoImpl--updateStoreOrderbyCustomerGroupId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE `store_order` SET customerGroupId = "+customerGroupId);
		sql.append(" where id ="+orderId);

		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			int rowCount = ps.executeUpdate();
			if(rowCount>0) {
				return true;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StoreOrderDaoImpl--updateStoreOrderbyCustomerGroupId--end>");
		return false;
	}

	@Override
	public List<ShoppingGoodsProductBean> getShoppingGoodsProductBean(Long shoppingGoodsId) {
		log.info("<StoreOrderDaoImpl>----<getShoppingGoodsProductBean>------start");
		StringBuilder sql = new StringBuilder();
		sql.append("select id,goodsId,itemId,itemName,createTime,createUser,updateTime,updateUser,deleteFlag,quantity  ");
		sql.append(" from shopping_goods_product where goodsId='"+shoppingGoodsId+"' ");
		List<ShoppingGoodsProductBean> list = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("根据商城商品id 查询绑定商品信息  sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				ShoppingGoodsProductBean bean = new ShoppingGoodsProductBean();
				bean.setId(rs.getLong("id"));
				bean.setGoodsId(rs.getLong("goodsId"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setItemName(rs.getString("itemName"));;
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setQuantity(rs.getLong("quantity"));
				list.add(bean);
			}
			log.info("<StoreOrderDaoImpl>----<getShoppingGoodsProductBean>------start");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsProductDaoImpl>----<getShoppingGoodsProductBean>------start");
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public List<OrderBean> getStoreOrderbyCustomerGroupId(Long customerGroupId) {
		log.info("<StoreOrderDaoImpl>----<getStoreOrderbyCustomerGroupId>----start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" select so.id,so.customerGroupId,so.customerId,so.openid,so.price,so.num,so.payCode,sg.typeId,sg.vouchersId,sg.vouchersIds  ");
		sql.append(" from store_order so ");
		sql.append(" left join tbl_customer_spellGroup tcs on so.customerGroupId=tcs.id");
		sql.append(" left join shopping_goods_spellGroup sgs on sgs.id=tcs.spellGroupId");
		sql.append(" left join shopping_goods sg on sg.id=sgs.goodsId");
		sql.append(" where so.state=10001 and so.customerGroupId ='"+customerGroupId+"' ");
		
		log.info("根据用户拼团id 查询订单信息sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<OrderBean> list=new ArrayList<OrderBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				OrderBean order = new OrderBean();
				order.setId(rs.getLong("id"));
				order.setCustomerGroupId(rs.getLong("customerGroupId"));
				order.setCustomerId(rs.getLong("customerId"));
				order.setOpenid(rs.getString("openid"));
				order.setPrice(rs.getBigDecimal("price"));
				order.setNum(rs.getInt("num"));
				order.setPayCode(rs.getString("payCode"));
				
				order.setTypeId(rs.getInt("typeId"));
				order.setVouchersId(rs.getLong("vouchersId"));
				order.setVouchersIds(rs.getString("vouchersIds"));
				order.setNum(rs.getInt("num"));
				list.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StoreOrderDaoImpl>----<getStoreOrderbyCustomerGroupId>----end>");
		return list;
	}
	
	@Override
	public void paySpellgroupStroeOrder(String outTradeNo, Integer type) {
		log.info("<CouponDaoImpl>---<paySpellgroupStroeOrder>----start");
		if(type==0) { //已完成状态
			String sql = "update store_order set spellgroupState=4  where id='" + outTradeNo + "'" ;
			log.info(sql);
			upate(sql);
		}else {
			String sql = " update store_order set spellgroupState="+type+"  where payCode='" + outTradeNo + "'" ;
			log.info(sql);
			upate(sql);
		}
	}
}

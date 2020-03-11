package com.server.module.trade.vendingMachinesWay;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.trade.vendingMachinesWayItem.MoreGoodsWayDto;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr 
 * create time: 2018-04-12 14:04:38
 */
@Repository
public class VendingMachinesWayDaoImpl extends BaseDao<VendingMachinesWayBean> implements VendingMachinesWayDao {

	private static Logger log = LogManager.getLogger(VendingMachinesWayDaoImpl.class);

	public ReturnDataUtil listPage(VendingMachinesWayCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,vendingMachinesCode,wayNumber,itemId,state,num,fullNum,updateTime,createTime from vending_machines_way where 1=1 ");
		List<Object> plist = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			List<VendingMachinesWayBean> list = Lists.newArrayList();
			while (rs.next()) {
				VendingMachinesWayBean bean = new VendingMachinesWayBean();
				bean.setId(rs.getLong("id"));
				bean.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				bean.setWayNumber(rs.getInt("wayNumber"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setState(rs.getInt("state"));
				bean.setNum(rs.getInt("num"));
				bean.setFullNum(rs.getInt("fullNum"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setCreateTime(rs.getDate("createTime"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
				log.info(plist.toString());
			}
			data.setCurrentPage(condition.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public VendingMachinesWayBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		VendingMachinesWayBean entity = new VendingMachinesWayBean();
		entity.setId((Long)id);
		return super.del(entity);
	}

	public boolean update(VendingMachinesWayBean entity) {
		return super.update(entity);
	}

	public VendingMachinesWayBean insert(VendingMachinesWayBean entity) {
		return super.insert(entity);
	}

	public List<VendingMachinesWayBean> list(VendingMachinesWayCondition condition) {
		return null;
	}

	@Override
	public List<WayDto> listAll(String vmCode) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select a.id as wayId,wayNumber,itemId,a.state,num,fullNum,b.price,b.costPrice,hot,c.name as itemName,c.pic,b.endTime  "
						+ " from vending_machines_way a left join vending_machines_item b on a.itemid=b.id "
						+ " left join item_basic c on b.basicItemId=c.id where vendingMachinesCode='"+vmCode+"' order by wayNumber ");
		
		log.info("vmCode==="+vmCode);
		log.info(sql.toString());
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			log.info(conn);
			pst = conn.prepareStatement(sql.toString());
			//pst.setObject(1, vmCode);
			rs = pst.executeQuery();
			List<WayDto> list = Lists.newArrayList();
			while (rs.next()) {

				WayDto bean = new WayDto();
				bean.setWayId(rs.getLong("wayId"));
				// bean.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				bean.setWayNumber(rs.getInt("wayNumber"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setState(rs.getInt("state"));
				bean.setNum(rs.getInt("num"));
				bean.setFullNum(rs.getInt("fullNum"));
				bean.setPrice(rs.getBigDecimal("price"));
				bean.setCostPrice(rs.getBigDecimal("costPrice"));
				bean.setHot(rs.getInt("hot"));
				bean.setItemName(rs.getString("itemName"));
				bean.setPic(rs.getString("pic"));
				bean.setEndTime(rs.getDate("endTime"));

				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		} finally {
			try {
				if(rs!=null)
				rs.close();
				if(pst!=null)
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 编辑货道及商品
	 */
	@SuppressWarnings("resource")
	public boolean editWayAndItem(BindItemDto dto) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			conn.setAutoCommit(false);

			String updateItemSql = "update vending_machines_item set price=? ,costPrice=?,hot=? ,updateTime=? where id=?";
			pst = conn.prepareStatement(updateItemSql);
			pst.setBigDecimal(1, new BigDecimal(dto.getPrice()));
			pst.setBigDecimal(2, new BigDecimal(dto.getCostPrice()));
			pst.setInt(3, dto.getHot());
			pst.setDate(4, new java.sql.Date(System.currentTimeMillis()));
			pst.setLong(5, dto.getId());
			pst.execute();
			String bindSql = "update vending_machines_way set itemId=? ,updateTime=? ,num=?, fullName=? where vendingMachinesCode=? and wayNumber=?";
			pst = conn.prepareStatement(bindSql);
			pst.setLong(1, dto.getId());
			pst.setDate(2, new Date(System.currentTimeMillis()));
			pst.setInt(3, dto.getNum());
			pst.setInt(4, dto.getFullNum());
			pst.setString(5, dto.getVmCode());
			pst.setInt(6, dto.getWayNumber());
			boolean b = pst.execute();
			conn.commit();
			return b;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		} finally {
			try {
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 统计货道的商品数量
	 */
	public ReturnDataUtil statisticsWayNum(StatisticsWayNumCondition condition) {

		List<Object> plist = Lists.newArrayList();

		StringBuilder sb = new StringBuilder();
		sb.append(
				"SELECT a.code as vmCode,a.companyId,a.`locatoinName` as address,d.name as itemName ,b.wayNumber,b.num,b.fullNum,b.state,b.id ");
		/*
		 * sb.
		 * append(" MAX(CASE b.wayNumber WHEN 1 THEN IFNULL(b.num,0) ELSE 0 END ) '1num', "
		 * ); sb.
		 * append(" MAX(CASE b.wayNumber WHEN 1 THEN IFNULL(b.fullNum,0) ELSE 0 END ) '1fulnum',"
		 * ); sb.
		 * append(" MAX(CASE b.wayNumber WHEN 2 THEN IFNULL(b.num,0) ELSE 0 END ) '2num', "
		 * ); sb.
		 * append(" MAX(CASE b.wayNumber WHEN 2 THEN IFNULL(b.fullNum,0) ELSE 0 END ) '2fulnum', "
		 * ); sb.
		 * append(" MAX(CASE b.wayNumber WHEN 3 THEN IFNULL(b.num,0) ELSE 0 END ) '3num', "
		 * ); sb.
		 * append(" MAX(CASE b.wayNumber WHEN 3 THEN IFNULL(b.fullNum,0) ELSE 0 END ) '3fulnum',"
		 * ); sb.
		 * append(" MAX(CASE b.wayNumber WHEN 4 THEN IFNULL(b.num,0) ELSE 0 END ) '4num', "
		 * ); sb.
		 * append(" MAX(CASE b.wayNumber WHEN 4 THEN IFNULL(b.fullNum,0) ELSE 0 END ) '4fulnum'"
		 * );
		 */
		sb.append(" FROM vending_machines_info a ");
		sb.append("INNER JOIN vending_machines_way b ");
			if (condition.getUnderNum() != null) {
				sb.append(" ON (a.code=b.vendingMachinesCode  AND b.num<? ) ");
				plist.add(condition.getUnderNum());
			}else{
				sb.append(" ON (a.code=b.vendingMachinesCode )");
			}
		sb.append(" LEFT JOIN vending_machines_item c ON c.id=b.`itemId`");
		sb.append(" JOIN item_basic d ON c.basicItemId=d.id");
		sb.append(" where 1=1 ");
		if (condition.getCompanyId() != null) {
			sb.append(" and  a.companyid=? ");
			plist.add(condition.getCompanyId());
		}
		if (condition.getIdIns() != null) {
			sb.append(" and  a.companyid in ? ");
			plist.add(condition.getIdIns());
		}
		
		if (condition.getState() != null) {
			sb.append(" and b.state=? ");
			plist.add(condition.getState());
		}
		sb.append("GROUP BY  a.code ");
		// SELECT * FROM company WHERE FIND_IN_SET(id, getChildCompany(66))
		ReturnDataUtil data = new ReturnDataUtil();

		List<StatisticsWayNumVo> list = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sb.toString());
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sb.toString() + " limit " + off + "," + condition.getPageSize());

			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			while (rs.next()) {
				StatisticsWayNumVo bean = new StatisticsWayNumVo();
				// bean.setWayId(rs.getLong("wayId"));
				// bean.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				bean.setWay(rs.getInt("wayNumber"));
				// .setItemId(rs.getLong("itemId"));
				bean.setState(rs.getInt("state"));
				bean.setNum(rs.getInt("num"));
				bean.setFullNum(rs.getInt("fullNum"));
				bean.setItemName(rs.getString("itemName"));
				bean.setAddress(rs.getString("address"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setId(rs.getLong("id"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sb.toString());
				log.info(plist);
			}
			data.setCurrentPage(condition.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			return data;

		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public long selectCount(String sql, List<Object> list) {
		return super.selectCountBySql(sql, list);
	}

	@Override
	public List<MoreGoodsWayDto> getWayInfo(String vmCode) {
		log.info("<VendingMachinesWayDaoImpl--getWayInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmwi.vendingMachinesCode,vmwi.wayNumber,ib.standard AS weight,ib.`id` AS basicItemId,");
		sql.append(" vmi.price,vmwi.num,vmwi.fullNum,ib.name as itemName,ib.pic,vmp.homeImg as img  FROM vending_machines_way AS vmwi ");
		sql.append(" INNER JOIN vending_machines_item AS vmi ON vmwi.`itemId` = vmi.`id`");
		sql.append(" INNER JOIN item_basic AS ib ON ib.`id` = vmi.`basicItemId`");
		
		sql.append(" left join vending_machines_pic vmp on vmp.id = vmwi.picId and vmp.state=0 ");

		sql.append(" WHERE vmwi.vendingMachinesCode = '"+vmCode+"'");
		sql.append(" order by vmwi.wayNumber");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MoreGoodsWayDto> wayList = new ArrayList<MoreGoodsWayDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				int wayNumber = rs.getInt("wayNumber");
				MoreGoodsWayDto moreGoodsWay = null;
				for(MoreGoodsWayDto way : wayList){
					if(way.getWayNumber().equals(wayNumber)){
						moreGoodsWay = way;
						break;
					}
				}
				if(moreGoodsWay == null){
					moreGoodsWay = new MoreGoodsWayDto();
					wayList.add(moreGoodsWay);
				}
				moreGoodsWay.setWayNumber(wayNumber);
				List<VmwayItemBean> wayItemList = moreGoodsWay.getWayItemList();
				if(wayItemList ==null){
					wayItemList = new ArrayList<VmwayItemBean>();
					moreGoodsWay.setWayItemList(wayItemList);
				}
				VmwayItemBean wayItem = new VmwayItemBean();
				wayItem.setBasicItemId(rs.getLong("basicItemId"));
				wayItem.setFullNum(rs.getInt("fullNum"));
				wayItem.setNum(rs.getInt("num"));
				wayItem.setOrderNumber(1);
				wayItem.setItemName(rs.getString("itemName"));
				wayItem.setPic(rs.getString("pic"));
				wayItem.setPrice(rs.getDouble("price"));
				wayItem.setVmCode(vmCode);
				wayItem.setWayNumber(wayNumber);
				wayItem.setWeight(rs.getInt("weight"));
				wayItem.setImg(rs.getString("img"));
				wayItemList.add(wayItem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<VendingMachinesWayDaoImpl--getWayInfo--end>");
		return wayList;
	}

	@Override
	public boolean updateWayNum(String vmCode, Integer wayNum, Integer replenishNum) {
		log.info("<VendingMachinesWayDaoImpl--updateWayNum--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE `vending_machines_way` SET num = '"+replenishNum+"',updateTime = NOW()");
		sql.append(" WHERE vendingMachinesCode = '"+vmCode+"' AND wayNumber = '"+wayNum+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int result = -1;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<VendingMachinesWayDaoImpl--updateWayNum--end>");
		if(result>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据售货机出厂编号 得到对应机器货道的商品规格 组成一个字符串 返回
	 */
	@Override
	public List<String> findItemStandard(String factnum) {
		log.info("<VendingMachinesBaseDaoImpl>----<findItemStandard>---start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select vmw.wayNumber,ib.standard,vmw.vendingMachinesCode from vending_machines_way vmw  ");
		sql.append(" left JOIN vending_machines_item vmit ON vmw.itemId=vmit.id ");
		sql.append(" left join item_basic ib  on vmit.basicItemId=ib.id ");
		sql.append(" left join vending_machines_info vmi on vmw.vendingMachinesCode=vmi.code ");
		sql.append(" left join vending_machines_base vmb on vmi.machinesBaseId=vmb.id ");
		sql.append(" where vmb.factoryNumber='"+factnum+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> list=new ArrayList<String>();
		
		log.info("根据售货机出厂编号 得到对应机器货道的商品规格sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next() && rs!=null) {
				String standard = rs.getString("standard");
				list.add(standard);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<VendingMachinesBaseDaoImpl>----<findItemStandard>---end");
		return list;
	}

}

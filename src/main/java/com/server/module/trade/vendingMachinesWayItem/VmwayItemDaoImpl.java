package com.server.module.trade.vendingMachinesWayItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.module.commonBean.ItemInfoDto;
import com.server.util.sqlUtil.MySqlFuns;
@Repository
public class VmwayItemDaoImpl extends MySqlFuns implements VmwayItemDao{

	private final static Logger log = LogManager.getLogger(VmwayItemDaoImpl.class);
	
	@Override
	public ItemChangeDto getChangeInfo(ItemChangeDto itemChangeDto) {
		//log.info("<VmwayItemDaoImpl--getChangeInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmwi.`basicItemId`,ib.`name` AS itemName,vmwi.price,ib.pic,vmwi.weight,vmwi.machineWayId,vmwi.num,vmwi.promotionPrice,ib.typeId FROM vending_machines_way_item AS vmwi ");
		sql.append(" INNER JOIN item_basic AS ib ON vmwi.`basicItemId` = ib.`id`");
		sql.append(" WHERE vmCode = '"+itemChangeDto.getVmCode()+"' AND wayNumber = '"+itemChangeDto.getWayNum()+"' AND orderNumber = '"+itemChangeDto.getOrderNum()+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isExists = false;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				isExists = true;
				itemChangeDto.setBasicItemId(rs.getLong("basicItemId"));
				itemChangeDto.setItemName(rs.getString("itemName"));
				itemChangeDto.setPic(rs.getString("pic"));
				itemChangeDto.setPrice(rs.getDouble("price"));
				itemChangeDto.setWeight(rs.getInt("weight"));
				itemChangeDto.setNum(rs.getInt("num"));
				itemChangeDto.setMachineWayId(rs.getLong("machineWayId"));
				itemChangeDto.setPromotionPrice(rs.getDouble("promotionPrice"));
				itemChangeDto.setItemTypeId(rs.getLong("typeId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<VmwayItemDaoImpl--getChangeInfo--end>");
		if(isExists){
			return itemChangeDto;
		}else{
			return null;
		}
	}

	@Override
	public List<MoreGoodsWayDto> getWayInfo(String vmCode) {
		//log.info("<VmwayItemDaoImpl--getWayInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmwi.vmCode,vmwi.wayNumber,vmwi.basicItemId,vmwi.weight,");
		sql.append(" vmwi.orderNumber,vmwi.price,vmwi.num,vmwi.fullNum,ib.`name` as itemName,ib.`pic`,vmp.homeImg as img ");
		sql.append(" FROM vending_machines_way_item AS vmwi");
		sql.append(" INNER JOIN item_basic AS ib ON ib.id = vmwi.`basicItemId`");
		
		sql.append(" left join vending_machines_pic vmp on vmp.id = vmwi.`picId` and vmp.state=0 ");
		
		sql.append(" WHERE vmwi.vmCode ='"+vmCode+"'");
		sql.append(" ORDER BY vmwi.wayNumber,vmwi.orderNumber");
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
				for(int i=0;i<wayList.size();i++){
					if(wayList.get(i).getWayNumber().equals(wayNumber)){
						moreGoodsWay = wayList.get(i);
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
				wayItem.setOrderNumber(rs.getInt("orderNumber"));
				wayItem.setPrice(rs.getDouble("price"));
				wayItem.setVmCode(vmCode);
				wayItem.setItemName(rs.getString("itemName"));
				wayItem.setPic(rs.getString("pic"));
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
		//log.info("<VmwayItemDaoImpl--getWayInfo--end>");
		return wayList;
	}

	@Override
	public boolean updateWayInfo(String vmCode, Integer wayNum, Integer orderNum, Integer num) {
		//log.info("<VmwayItemDaoImpl--updateWayInfo--start>");
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE `vending_machines_way_item` SET num = "+num);
		sql.append(" WHERE vmCode = '"+vmCode+"' AND wayNumber = "+wayNum+" AND orderNumber = "+orderNum);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int resultNum = -1;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			resultNum = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		if(resultNum>0){
			result = true;
		}
		//log.info("<VmwayItemDaoImpl--updateWayInfo--end>");
		return result;
	}

	@Override
	public WayValidatorDto getOneWayInfo(String vmCode, Integer wayNum) {
		//log.info("<VmwayItemDaoImpl--getOneWayInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT SUM(vmwi.num) AS num , vmw.`state`,MIN(vmwi.`price`) as minUnitPrice, MIN(vmwi.`weight`) AS minStandard FROM `vending_machines_way_item` AS vmwi ");
		sql.append(" INNER JOIN vending_machines_way AS vmw ON vmw.`id` = vmwi.`machineWayId`");
//		sql.append(" INNER JOIN vending_machines_info AS vmi ON vmi.`code` = vmw.`vendingMachinesCode`");
		sql.append(" WHERE  vmwi.`vmCode` = '"+vmCode+"' AND vmwi.`wayNumber` = '"+wayNum+"'");
		//vmi.`state` = 20001 AND
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		WayValidatorDto validator = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				validator = new WayValidatorDto();
				validator.setNum(rs.getInt("num"));
				validator.setState(rs.getInt("state"));
				validator.setMinUnitPrice(rs.getBigDecimal("minUnitPrice"));
				validator.setMinStandard(rs.getInt("minStandard"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<VmwayItemDaoImpl--getOneWayInfo--end>");
		return validator;
	}

	@Override
	public List<ItemInfoDto> getOneWayItmeInfo(String vmCode, Integer wayNum) {
		//log.info("<VmwayItemDaoImpl--getOneWayItmeInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT vmwi.basicItemId,vmwi.price AS unitPrice,vmwi.weight AS unitWeight,ib.pic,ib.`name` AS itemName,vmwi.num,vmwi.orderNumber");
		sql.append(" FROM vending_machines_way_item AS vmwi");
		sql.append(" INNER JOIN item_basic AS ib ON ib.`id` = vmwi.`basicItemId`");
		sql.append(" WHERE vmwi.vmCode = '"+vmCode+"' AND vmwi.wayNumber = '"+wayNum+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ItemInfoDto> itemInfoList = new ArrayList<ItemInfoDto>();
		ItemInfoDto itemInfo = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				itemInfo = new ItemInfoDto();
				itemInfo.setOrderNum(rs.getInt("orderNumber"));
				itemInfo.setBasicItemId(rs.getLong("basicItemId"));
				itemInfo.setItemName(rs.getString("itemName"));
				itemInfo.setPic(rs.getString("pic"));
				itemInfo.setUnitPrice(rs.getBigDecimal("unitPrice"));
				itemInfo.setUnitWeight(rs.getInt("unitWeight"));
				itemInfo.setNum(rs.getInt("num"));
				itemInfoList.add(itemInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<VmwayItemDaoImpl--getOneWayItmeInfo--end>");
		return itemInfoList;
	}
	
	@Override
	public List<WayStandardDto> getStandardInfo(String factnum) {
		//log.info("<VendingMachinesBaseDaoImpl>----<getStandardInfo>---start");
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT vmwi.wayNumber,ib.standard,vmwi.vmCode FROM vending_machines_way_item vmwi");
		sql.append(" LEFT JOIN item_basic ib  ON vmwi.basicItemId=ib.id ");
		sql.append(" LEFT JOIN vending_machines_info vmi ON vmwi.vmCode=vmi.code ");
		sql.append(" LEFT JOIN vending_machines_base vmb ON vmi.machinesBaseId=vmb.id");
		sql.append(" WHERE vmb.factoryNumber='"+factnum+"'");
		sql.append(" ORDER BY wayNumber,orderNumber");
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		WayStandardDto way = null;
		List<WayStandardDto> list = new ArrayList<WayStandardDto>();
		
		log.info("根据售货机出厂编号 得到对应机器货道的商品规格sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next() && rs!=null) {
				way = new WayStandardDto();
				way.setStandard(rs.getString("standard"));
				way.setVmCode(rs.getString("vmCode"));
				way.setWayNum(rs.getInt("wayNumber"));
				list.add(way);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		//log.info("<VendingMachinesBaseDaoImpl>----<getStandardInfo>---end");
		return list;
	}

	@Override
	public List<VmwayItemBean> findItemWeight(String factnum,Integer currWay) {
		log.info("<VendingMachinesBaseDaoImpl>----<findItemWeight>---start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select vmwi.num,vmwi.orderNumber,vmb.mainProgramVersion,vmwi.wayNumber,vmwi.weight from vending_machines_way_item vmwi  ");
		sql.append(" left JOIN vending_machines_info vmi ON vmwi.vmCode=vmi.code ");
		sql.append(" left join vending_machines_base vmb on vmi.machinesBaseId=vmb.id ");
		sql.append(" where vmb.factoryNumber='"+factnum+"' ");
		if(currWay !=null ) {
			sql.append(" and vmwi.wayNumber='"+currWay+"' ");
		}
		sql.append(" order by vmwi.wayNumber,vmwi.weight");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<VmwayItemBean> list=Lists.newArrayList();

		log.info("根据售货机出厂编号 得到对应机器货道的商品规格sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next() && rs!=null) {
				VmwayItemBean vmwib=new VmwayItemBean();
				vmwib.setWayNumber(rs.getInt("wayNumber"));
				vmwib.setWeight(rs.getInt("weight"));	
				vmwib.setOrderNumber(rs.getInt("orderNumber"));
				vmwib.setNum(rs.getInt("num"));

				vmwib.setMainProgramVersion(rs.getString("mainProgramVersion"));
				log.info(vmwib.getMainProgramVersion());
				if(vmwib.getMainProgramVersion().equals("Ver.1.002-20190507") ||vmwib.getMainProgramVersion().equals("Ver.2.002-20190507") || vmwib.getMainProgramVersion().equals("Ver.3.002-20190507") ) {
					list.add(vmwib);
				}else if(vmwib.getMainProgramVersion().contains("0812")) {
					list.add(vmwib);
				}
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<VendingMachinesBaseDaoImpl>----<findItemWeight>---end");
		return list;
	}
}

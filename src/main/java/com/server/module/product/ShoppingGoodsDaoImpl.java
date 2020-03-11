package com.server.module.product;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.machine.VendingMachinesInfoBean;
import com.server.util.ReturnDataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * author name: why create time: 2018-06-29 11:17:55
 */
@Repository
public class ShoppingGoodsDaoImpl extends BaseDao<ShoppingGoodsBean> implements ShoppingGoodsDao {

	private static Logger log = LogManager.getLogger(ShoppingGoodsDaoImpl.class);

	/**
	 * 商城商品详情查看
	 */
	public ShoppingGoodsBean get(Object id) {
		log.info("<ShoppingGoodsDaoImpl>-----<get>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select sg.id,sg.name,companyId,typeId,pic,barCode,sg.state,purchaseWay,brand,standard,unit,pack,details,sg.createTime,createUser,sg.updateTime,updateUser,");
		sql.append(
				" deleteFlag,quantity,purchaseNotes,commodityParameters,isHomeShow,it.name as typeName,c.name  as companyName,s.name as stateName ,s1.name as unitName,costPrice,salesPrice,preferentialPrice,basicItemId,advertisingPic,sg.target,sg.areaId,sg.vmCode,sg.areaName");
		sql.append(" ,sg.vouchersId,sg.vouchersIds,sg.isHelpOneself from shopping_goods  sg left join item_type it on sg.typeId=it.id ");
		sql.append(" left join company c on sg.companyId=c.id left join state_info s on sg.state=s.state ");
		sql.append(" left join state_info s1 on sg.unit=s1.state ");
		sql.append(" where sg.id ='" + id + "'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ShoppingGoodsBean bean = new ShoppingGoodsBean();
		log.info("商品详情查看sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setTypeId(rs.getLong("typeId"));
				bean.setPic(rs.getString("pic"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setState(rs.getLong("state"));
				bean.setPurchaseWay(rs.getString("purchaseWay"));
				bean.setBrand(rs.getString("brand"));
				bean.setStandard(rs.getString("standard"));
				bean.setUnit(rs.getLong("unit"));
				bean.setPack(rs.getString("pack"));
				bean.setDetails(rs.getString("details"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setCostPrice(rs.getBigDecimal("costPrice"));
				bean.setSalesPrice(rs.getBigDecimal("salesPrice"));
				bean.setPreferentialPrice(rs.getBigDecimal("preferentialPrice"));
				bean.setStateName(rs.getString("stateName"));
				bean.setUnitName(rs.getString("unitName"));
				bean.setTypeName(rs.getString("typeName"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setCommodityParameters(rs.getString("commodityParameters"));
				bean.setPurchaseNotes(rs.getString("purchaseNotes"));
				bean.setTarget(rs.getInt("target"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setAreaId(rs.getLong("areaId"));
				String basicItemId = rs.getString("basicItemId");
				if (basicItemId != null) {
					bean.setIsRelevance(0);
					bean.setBasicItemId(rs.getLong("basicItemId"));
				} else {
					bean.setIsRelevance(1);
				}
				bean.setIsHomeShow(rs.getInt("isHomeShow"));
				bean.setAdvertisingPic(rs.getString("advertisingPic"));
				bean.setVouchersId(rs.getLong("vouchersId"));
				bean.setVouchersIds(rs.getString("vouchersIds"));
				bean.setIsHelpOneself(rs.getInt("isHelpOneself"));
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<ShoppingGoodsDaoImpl>-----<get>----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsDaoImpl>-----<get>----end");
			return bean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 查询商品信息 给手机端显示  首页展示
	 */
	@Override
	public ReturnDataUtil list(ShoppingGoodsForm shoppingGoodsForm, VendingMachinesInfoBean vendingMachinesInfoBean) {
		log.info("<ShoppingGoodsDaoImpl>-----<list>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select sg.id,sg.name,sg.companyId,sg.typeId,sg.pic,sg.barCode,sg.state,sg.purchaseWay,sg.brand,sg.standard,sg.unit,sg.pack,sg.details,sg.createTime,sg.createUser,sg.updateTime,sg.updateUser,sg.deleteFlag  ");
		sql.append(",sg.costPrice,sg.salesPrice,sg.preferentialPrice,sg.basicItemId,sg.quantity,sg.purchaseNotes,sg.commodityParameters,sg.advertisingPic,sg.isHelpOneself,sgs.id spellgroupId,sgs.theme spellgroupName,sgs.spellGroupPrice,sg.target   ");
		sql.append(" from shopping_goods sg  inner join  shopping_goods_spellgroup sgs on sg.id=sgs.goodsId and endTime>now() ");
		sql.append("  where sg.deleteFlag=0 and sgs.deleteFlag=0 ");
		if(vendingMachinesInfoBean==null) {
			sql.append(" and sg.target = 0");
		}else{
			sql.append(" and (sg.target = 1 and FIND_IN_SET("+vendingMachinesInfoBean.getCompanyId()+",getChildList(companyId)) ");
			sql.append(" or sg.target = 2 and FIND_IN_SET("+vendingMachinesInfoBean.getCompanyId()+",getChildList(companyId))  and areaId='"+vendingMachinesInfoBean.getAreaId()+"'  ");
			sql.append(" or sg.target = 3 and  FIND_IN_SET("+vendingMachinesInfoBean.getCode()+",sg.vmCode)");
			sql.append(" or sg.target = 0 )");
		}
		sql.append(" and sg.state=5100 and sg.activityId=0 and (isHomeShow=3 or isHomeShow=4)  order by sg.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("查询拼团商品信息  结算页显示sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<ShoppingGoodsBean> list = Lists.newArrayList();
			while (rs.next()) {
				ShoppingGoodsBean bean = new ShoppingGoodsBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setTypeId(rs.getLong("typeId"));
				bean.setPic(rs.getString("pic"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setState(rs.getLong("state"));
				bean.setPurchaseWay(rs.getString("purchaseWay"));
				bean.setBrand(rs.getString("brand"));
				bean.setStandard(rs.getString("standard"));
				bean.setUnit(rs.getLong("unit"));
				bean.setPack(rs.getString("pack"));
				bean.setDetails(rs.getString("details"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setCostPrice(rs.getBigDecimal("costPrice"));
				bean.setSalesPrice(rs.getBigDecimal("salesPrice"));
				bean.setPreferentialPrice(rs.getBigDecimal("preferentialPrice"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setCommodityParameters(rs.getString("commodityParameters"));
				bean.setPurchaseNotes(rs.getString("purchaseNotes"));
				bean.setTarget(rs.getInt("target"));
				Long basicItemId = rs.getLong("basicItemId");
				if (basicItemId != null) {
					bean.setBasicItemId(rs.getLong("basicItemId"));
				}
				bean.setAdvertisingPic(rs.getString("advertisingPic"));
				bean.setIsHelpOneself(rs.getInt("isHelpOneself"));
				bean.setSpellgroupId(rs.getLong("spellgroupId"));
				bean.setSpellgroupName(rs.getString("spellgroupName"));
				bean.setGroupPurchasePrice(rs.getBigDecimal("spellGroupPrice"));
				
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(shoppingGoodsForm.getCurrentPage());
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ShoppingGoodsDaoImpl>-----<list>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsDaoImpl>-----<list>----end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public List<ShoppingGoodsBean> listPage(VendingMachinesInfoBean vmi) {
		log.info("<ShoppingGoodsDaoImpl>-----<listPage>----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select sg.id,sg.name,sg.pic,sg.salesPrice,sg.activityId,sg.target,sgs.id sId,sgs.theme,sgs.spellGroupPrice from shopping_goods sg  ");
		sql.append(" inner join shopping_goods_spellgroup sgs on sg.id=sgs.goodsId and endTime>now() and sgs.deleteFlag=0 where 1=1 and sg.deleteFlag=0  ");
		if(vmi==null) {
			sql.append(" and target = 0");
		}else{
			sql.append(" and (target = 1 and FIND_IN_SET("+vmi.getCompanyId()+",getChildList(companyId)) ");
			sql.append(" or target = 2 and FIND_IN_SET("+vmi.getCompanyId()+",getChildList(companyId))  and areaId='"+vmi.getAreaId()+"'  ");
			sql.append(" or target = 3 and  FIND_IN_SET("+vmi.getCode()+",sg.vmCode)");
			sql.append(" or target = 0 )");
		}
		sql.append(" and state=5100 and sg.activityId=0  order by sg.activityId asc, sg.createTime desc  ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ShoppingGoodsBean> list=new ArrayList<ShoppingGoodsBean>();
		log.info("查询拼团活动商品信息列表sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				ShoppingGoodsBean  bean=new ShoppingGoodsBean();
					bean.setId(rs.getLong("id"));
					bean.setName(rs.getString("name"));
					bean.setPic(rs.getString("pic"));
					bean.setSalesPrice(rs.getBigDecimal("salesPrice"));
					bean.setActivityId(rs.getInt("activityId"));
					bean.setSpellgroupId(rs.getLong("sId"));
					bean.setSpellgroupName(rs.getString("theme"));
					bean.setGroupPurchasePrice(rs.getBigDecimal("spellGroupPrice"));
					bean.setTarget(rs.getInt("target"));
					list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<ShoppingGoodsDaoImpl>-----<listPage>----end");
		return list;
	}

}

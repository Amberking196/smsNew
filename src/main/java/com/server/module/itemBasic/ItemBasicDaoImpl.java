package com.server.module.itemBasic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.server.common.bean.KeyValueBean;
import com.server.common.persistence.BaseDao;
import com.server.module.commonBean.ItemBasicBean;

/**
 * author name: why create time: 2018-04-10 14:22:54
 */
@Repository
public class ItemBasicDaoImpl extends BaseDao<ItemBasicBean> implements ItemBasicDao {

	public static Logger log = LogManager.getLogger(ItemBasicDaoImpl.class);
	@SuppressWarnings("unused")



	/**
	 * 通过商品ID 得到商品信息
	 */
	@Override
	public ItemBasicBean getItemBasic(Object id) {
		log.info("<ItemBasicDaoImpl>--<getItemBasic>--start");
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select i.id,i.name,barCode,unit,brand,item_type.name,i.state,standard,pack,purchaseWay from item_basic i,item_type ");
		sql.append("where i.typeId=item_type.id and i.id=" + id + "");
		log.info("<ItemBasicDaoImpl>--<getItemBasic>--sql" + sql.toString());
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			ItemBasicBean bean = null;
			while (rs.next()) {
				bean = new ItemBasicBean();
				bean.setId(rs.getInt(1));
				bean.setName(rs.getString(2));
				bean.setBarCode(rs.getString(3));
				// 通过查询出来的商品单位的 unit 去数据字典得到 商品单位的名称
				String unitName = getNameByState(Long.parseLong(rs.getString(4)));
				bean.setUnitName(unitName);
				bean.setStandard(rs.getString(5));
				bean.setType(rs.getString(6));
				// 通过查询出来的商品状态的 编号 state 去数据字典得到 商品状态的名称
				String nameByState = getNameByState(Long.parseLong(rs.getString(7)));
				bean.setStateName(nameByState);
				bean.setBrand(rs.getString(8));
				bean.setPack(rs.getString(9));
				bean.setPurchaseWay(rs.getString(10));
			}
			log.info("<ItemBasicDaoImpl>--<getItemBasic>--end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.info("<ItemBasicDaoImpl>--<getItemBasic>--SQLException" + e.getMessage());
			return null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 获取字典值字典的名称
	 */
	@Cacheable(value = "stateInfoName", key = "#state")
	public String getNameByState(Long state) {
		log.info("<StateInfoDaoImpl>--<getNameByState>--start");
		StateInfoBean bean= getStateInfoByState(state);
		log.info("<StateInfoDaoImpl>--<getNameByState>--end");
		if(bean!=null) {
			return bean.getName();
		}else {
		return null;
		}
	}
	
	@Override
	public StateInfoBean getStateInfoByState(Long state) {
		log.info("StateInfoDaoImpl---------getStateInfoByState------ start"); 
		String sql="select id,keyName,name,state from state_info where state ='"+state+"'";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StateInfoBean stateDto = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery(); 
			log.info("StateInfoDaoImpl---------getStateInfoByState------ sql:"+sql); 
			while (rs != null && rs.next()) {
				stateDto = new StateInfoBean();
				stateDto.setId(rs.getLong("id"));
				stateDto.setKeyName(rs.getString("keyName"));
				stateDto.setName(rs.getString("name"));
				stateDto.setState(rs.getLong("state"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StateInfoDaoImpl>--<getNameByState>--end");
		return stateDto;
	}
	@Override
	public List<KeyValueBean<String, String>> getItemByIds(String basicItemIds) {
		log.info("<StateInfoDaoImpl>--<getItemByIds>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT `name`,pic FROM item_basic WHERE id IN ("+basicItemIds+")");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<KeyValueBean<String, String>> itemList = new ArrayList<KeyValueBean<String, String>>();
		KeyValueBean<String, String> item = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				item = new KeyValueBean<String, String>();
				item.setKey(rs.getString("name"));
				item.setValue(rs.getString("pic"));
				itemList.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StateInfoDaoImpl>--<getItemByIds>--end");
		return itemList;
	}
}

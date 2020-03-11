package com.server.module.trade.order.dao;

import com.server.common.persistence.BaseDao;
import com.server.module.store.OrderBean;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.PayStateEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 处理跟购水相关的一些数据库相关方法
 * @author Administrator
 *
 */
@Repository
public class OrderDao extends BaseDao {
	public static Logger log = LogManager.getLogger( OrderDao.class);    

	@Autowired
	private SimpleBaseDao baseDao;


	/**
	 * 根据payCode查询订单的部分信息
	 * @author hhh
	 * @date 2019年3月16日 上午11:31:36
	 * @param payCode
	 * @return
	 */
	public OrderBean getMessageByPayCode(String payCode) {
		log.info("<OrderDaoImpl>----<pickUpAddress>----start>");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuffer sql = new StringBuffer();
		sql.append(" select id,state,nowprice,payCode,createTime,type,price,payType,ptCode,product,customerId,payTime from store_order where payCode='" + payCode + "' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		OrderBean bean = new OrderBean();
		log.info("根据payCode查询订单的部分信息：sql>>>:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean.setId(rs.getInt("id"));
				bean.setState(rs.getInt("state"));
				bean.setNowprice(rs.getBigDecimal("nowprice"));
				bean.setPayCode(rs.getString("payCode"));
				bean.setCreateTime(rs.getTime("createTime"));
				bean.setType(rs.getInt("type"));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setPrice(rs.getBigDecimal("price"));
				bean.setPayType(rs.getInt("payType"));
				bean.setPtCode(rs.getString("ptCode"));
				bean.setProduct(rs.getString("product"));
				bean.setPayTime(rs.getTime("payTime"));
				bean.setStateName(PayStateEnum.findStateName(rs.getInt("state")));
			}
			data.setStatus(1);
			data.setMessage("查询成功");
			log.info("<OrderDaoImpl>----<pickUpAddress>----end>");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			return bean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}


	@SuppressWarnings("unchecked")
	//@Cacheable(value = "vmCodeByFactoryNumber", key = "#factoryNumber")
	public String getVmCodeByFactoryNumber(String factoryNumber) {
		
		String sql = "select i.code as code from vending_machines_base b right join vending_machines_info i on  b.id=i.machinesBaseId where b.factoryNumber='"
				+ factoryNumber + "'";
		List<Map<String, String>> list = (List<Map<String, String>>) baseDao.baseSelectMap(sql, null);
		log.info("list=" + list);
		if (list.size() > 0) {
			Map<String, String> map = list.get(0);
			String code = map.get("code");
			return code;

		}
		return null;
	}

	//@Cacheable(value = "factoryNumberByVmCode", key = "#vmCode")
	public String getFactoryNumberByVmCode(String vmCode) {
		String sql = "select b.factoryNumber as factoryNumber from vending_machines_info i right join vending_machines_base b on  b.id=i.machinesBaseId where i.code='"
				+ vmCode + "'";
		List<Map<String, String>> list = (List<Map<String, String>>) baseDao.baseSelectMap(sql, null);
		//log.info("list=" + list);
		if (list.size() > 0) {
			Map<String, String> map = list.get(0);
			String factoryNumber = map.get("factoryNumber")!=null?map.get("factoryNumber").trim():null;
			return factoryNumber;

		}
		return null;
	}
	
	public Integer getVersionByFactoryNum(String factoryNum) {
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select i.machineVersion as ver from vending_machines_info i inner join vending_machines_base b on  b.id=i.machinesBaseId where b.factoryNumber='"+factoryNum+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer version = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				version = rs.getInt("ver");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		return version;
	}
	
	public Integer getVersionByVmCode(String vmCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT machineVersion AS ver FROM vending_machines_info WHERE `code` = '"+vmCode+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer version = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				version = rs.getInt("ver");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		return version;
	}

	/**
	 * 获取货道基础信息如：状态、商品id、当前数量、容量
	 * 
	 * @param vmCode
	 * @param way
	 * @return
	 */
	public Map<String, Object> getWay(String vmCode, Integer way) {

		String sql = "SELECT id, state,itemId,num,fullNum FROM vending_machines_way  WHERE wayNumber=" + way
				+ " AND vendingMachinesCode='" + vmCode + "'";
		log.info(sql);
		List<Map<String, Object>> list = (List<Map<String, Object>>) baseDao.baseSelectMap(sql, null);
		return list.get(0);

	}
	
	public List<Map<String, Object>> getWays(String vmCode) {

		String sql = "SELECT id, state,itemId,num,fullNum,wayNumber FROM vending_machines_way  WHERE "
				+ "  vendingMachinesCode='" + vmCode + "'";
		log.info(sql);
		List<Map<String, Object>> list = (List<Map<String, Object>>) baseDao.baseSelectMap(sql, null);
		return list;

	}

	/**
	 * 获取商品价格和单位重量
	 * 
	 * @param itemId
	 * @return
	 */
	public Map<String, Object> getItemInfo(Long itemId) {
		String sql = "SELECT i.price,i.costPrice,b.standard,b.name,b.id,b.typeId,b.pic FROM vending_machines_item  i left JOIN item_basic b ON i.basicItemId=b.id WHERE  i.id="
				+ itemId;
		log.info(sql);
		List<Map<String, Object>> list = (List<Map<String, Object>>) baseDao.baseSelectMap(sql, null);
		if(list.size()==0){
			log.info("数据有异常 没找到价格库对应的基础库信息  导致找不到对应的单桶水质量");
		}
		return list.get(0);
	}
   /**
    * 记录心跳日志
    * @param content
    * @param factoryNumber
    * @param type k 开门时心跳   g 关门时心跳  r 重设时心跳 j 校准重量  s 重启机器 o 开门命令  u 更新音量 m 同层多商品结算记录
    */
	public void saveHeartBeatLog(String content, String factoryNumber, char type,Long userId,Integer opType,Long footmarkId) {
		String sql = "insert into machines_heartbeat_log (factoryNumber,type,content,createTime,currentUserId,opType,footmarkId) values ('"
				+ factoryNumber + "','" + type + "','" + content + "',CURRENT_TIMESTAMP(),'"+userId+"',"+opType+","+footmarkId+")";
		log.info(sql);
		Connection conn = openConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(sql);
			st.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.info("记录心跳异常");
			e.printStackTrace();
		} finally {
			closeConnection(null, st, conn);
		}

	}
   /**
    * 修改货道存量及添加日志
    * @param wayId
    * @param vmCode
    * @param way
    * @param diffNum
    * @param preNum
    * @param userId
    * @param opType 1 客户买水   2 运维补水   3 重置水量
    */
	public void updateStockAndLog(Long wayId,String vmCode, int way, int num,int preNum, Long userId,int opType,Long basicItemId,Long itemId,Long footmarkId) {
		
		String sql = "UPDATE vending_machines_way SET num=" + num
				+ " ,updateTime=CURRENT_TIMESTAMP()  WHERE id="+wayId;
		log.info("修改货道水存量");
		log.info(sql);

		StringBuilder sqlLog = new StringBuilder("insert into vending_waynum_log (wayId,vmCode,wayNumber,preNum,num, opType, userId,itemId,basicItemId, createTime,footmarkId) values(");
		sqlLog.append(wayId+",'" + vmCode + "'," + way + "," + preNum + "," + num + ","+opType+"," + userId + ","+itemId+","+basicItemId+",CURRENT_TIMESTAMP(),"+footmarkId+" )");
		log.info(sqlLog.toString());
		Connection conn = openConnection();
		Statement st = null;
		try {
			conn.setAutoCommit(false);
			st = conn.createStatement();
			st.execute(sql);
			st.execute(sqlLog.toString());
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			closeConnection(conn);
		}
	}
	
	
	  /**
	    * 修改货道存量及添加日志
	    * @param wayId
	    * @param vmCode
	    * @param way
	    * @param diffNum
	    * @param preNum
	    * @param userId
	    * @param opType 1 客户买水   2 运维补水   3 重置水量
	    */
		public void updateVmwayStockAndLog(String vmCode, int way, int num,int preNum, 
				Long userId,int opType,Long basicItemId,Long itemId,Long footmarkId,Integer isRevise) {
			
			String sql = "UPDATE vending_machines_way SET num=" + num
					+ " ,updateTime=CURRENT_TIMESTAMP()  WHERE vendingMachinesCode='"+vmCode+"' AND wayNumber='"+way+"'";
			log.info("修改货道水存量");
			log.info(sql);

			StringBuilder sqlLog = new StringBuilder("insert into vending_waynum_log (wayId,vmCode,wayNumber,preNum,num, opType, userId,itemId,basicItemId, createTime,footmarkId,isRevise) values(");
			sqlLog.append(null+",'" + vmCode + "'," + way + "," + preNum + "," + num + ","+opType+"," + userId + ","+itemId+","+basicItemId+",CURRENT_TIMESTAMP(),"+footmarkId+" ,"+isRevise+")");
			log.info(sqlLog.toString());
			Connection conn = openConnection();
			Statement st = null;
			try {
				conn.setAutoCommit(false);
				st = conn.createStatement();
				st.execute(sql);
				st.execute(sqlLog.toString());
				conn.commit();
			} catch (SQLException e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			} finally {
				if (st != null)
					try {
						st.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				closeConnection(conn);
			}
		}
	
	
	public void updateVmwayItemAndLog(List<ItemChangeDto> itemChangeList,UserVo userVo,Long footmarkId,Integer isRevise){
//		String sql = "UPDATE vending_machines_way SET num=" + num
//				+ " ,updateTime=CURRENT_TIMESTAMP()  WHERE id="+wayId;
		
		Connection conn = openConnection();
		Statement st = null;
		try {
			conn.setAutoCommit(false);
			st = conn.createStatement();
			for (ItemChangeDto itemChangeDto : itemChangeList) {
				int num = Math.abs(itemChangeDto.getChangeNum());
				if(num>0){
					StringBuffer sql = new StringBuffer();
					sql.append(" UPDATE vending_machines_way_item SET num = num + "+itemChangeDto.getChangeNum()+",updateTime=CURRENT_TIMESTAMP()");
					sql.append(" where vmCode = '"+itemChangeDto.getVmCode()+"' and wayNumber = '"+itemChangeDto.getWayNum()+"' and orderNumber = '"+itemChangeDto.getOrderNum()+"'");
					log.info("修改货道水存量");
					log.info(sql);

					StringBuilder sqlLog = new StringBuilder("insert into vending_waynum_log (wayId,vmCode,wayNumber,preNum,num, opType, userId,basicItemId, createTime,footmarkId,isRevise) values(");
					sqlLog.append(itemChangeDto.getMachineWayId()+",'" + itemChangeDto.getVmCode() + "'," + itemChangeDto.getWayNum() + "," + itemChangeDto.getNum() + "," + (itemChangeDto.getNum()+itemChangeDto.getChangeNum()) + ","+userVo.getType()+"," + userVo.getId() +","+itemChangeDto.getBasicItemId()+",CURRENT_TIMESTAMP(),"+footmarkId+","+isRevise+" )");
					log.info(sqlLog.toString());
					st.execute(sql.toString());
					st.execute(sqlLog.toString());
				}
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			closeConnection(conn);
		}
	}
	
	
	/**
	 * 获取订单信息
	 * 
	 * @param orderId
	 * @return
	 */
	public Map<String, Object> getOrder(Long orderId) {
		String sql = "SELECT price,num,state,itemName,payCode,vendingMachinesCode,customerId FROM pay_record_vision  WHERE state= 10002 AND id="
				+ orderId;
		log.info(sql);
		List<Map<String, Object>> list = (List<Map<String, Object>>) baseDao.baseSelectMap(sql, null);	
		if(list != null && list.size()>0){
			return list.get(0);
		}else {
			 sql = "SELECT price,num,state,itemName,payCode,vendingMachinesCode,customerId FROM pay_record  WHERE state= 10002 AND id="
					+ orderId;
			 list = (List<Map<String, Object>>) baseDao.baseSelectMap(sql, null);	
			 if(list != null && list.size()>0){
					return list.get(0);
			 }
			 return null;
		}
	}
	/**
	 * 获取签约号
	 * @param openid
	 * @return
	 */
	public String getContractId(Integer companyId , String openid){
		String sql="select contractId from wechat_entrust where companyId = '"+companyId+"' and openid='"+openid+"' and changeType='ADD'";
		log.info(sql);
		List<Map<String, Object>> list = (List<Map<String, Object>>) baseDao.baseSelectMap(sql, null);		
		if(list.size()==0){
			return null;
		}
	
		Map<String,Object> map=(Map<String, Object>)list.get(0);
		return (String)map.get("contractId");
	}
	
	
	public boolean isHaveNotPayOrder(Long id) {
		
		String sql="select id from pay_record where state=10002 and customerId="+id ;
		//String sql="select id from pay_record where state=10002 and customerId="+id +" union select id from pay_record_vision where state=10002 and customerId="+id ;

		log.info(sql);
		List<Map<String, Object>> list = (List<Map<String, Object>>) baseDao.baseSelectMap(sql, null);		
		if(list.size()==0){
			return false;
		}
		return true;
	}
	
	public void addRegisterChannel(int channel,String vmCode,String openId){
		
		String sql="insert into customer_register_channel(channel,vmCode,openId,createTime) values ("+channel+",'"+vmCode+"','"+openId+"',current_timestamp())";
		baseDao.excute(sql);
	
	}
	/**
	 * 获取当前机器总货道数及出厂编号
	 * @author hebiting
	 * @date 2018年6月21日上午8:51:54
	 * @param vmCode
	 * @return
	 */
	public Map<String,Object> getBaseInfoByVmCode(String vmCode) {
		String sql = "select b.aisleConfiguration as totalWayNum,b.factoryNumber as factoryNumber,i.machineVersion  from vending_machines_info i inner join vending_machines_base b on  b.id=i.machinesBaseId where i.code='"
				+ vmCode + "'";
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				result.put("machineVersion", rs.getInt("machineVersion"));
				result.put("totalWayNum", rs.getString("totalWayNum"));
				result.put("factoryNumber", rs.getString("factoryNumber")!=null?rs.getString("factoryNumber").trim():null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		return result;
//		List<Map<String, Object>> list = (List<Map<String, Object>>) baseDao.baseSelectMap(sql, null);
//		System.out.println("list=" + list);
//		if (list.size() > 0) {
//			Map<String, Object> map = list.get(0);
//			return map;
//		}
//		return null;
	}

	public List<String> qury(String sql) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String>list=new ArrayList<>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				list.add(rs.getString("itemName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		return list;
		
		
	}
       
	public String queryShoppingGoodsName(String sql) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				return rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		return null;
	}


	public Integer getDistributionModelByPayCode(String payCode) {
		log.info("<OrderDaoImpl>-----<getDistributionModelByPayCode>--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" select distributionModel from store_order where payCode='" + payCode + "' ");
		log.info("根据payCode查询公司id sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer distributionModel = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				distributionModel = rs.getInt("distributionModel");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OrderDaoImpl>-----<getDistributionModelByPayCode>--end>");
		return distributionModel;
	}

	public boolean editDelivery(Long orderId) {
		log.info("<OrderDaoImpl--editDelivery--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("update store_order set state=" + PayStateEnum.Delivery_completed.getState() + " where id=" + orderId);
		Connection conn = null;
		PreparedStatement pst = null;
		log.info("sql>>>:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int sign = pst.executeUpdate();
			if (sign > 0) {
				log.info("<OrderDaoImpl--editDelivery----end>");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(null, pst, conn);
		}
		return false;
	}
}

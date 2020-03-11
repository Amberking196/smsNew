package com.server.module.trade.customer;

import com.server.common.persistence.BaseDao;
import com.server.module.trade.customer.wxcustomer.WxCustomerBean;
import com.server.module.trade.util.UserUtil;
import com.server.util.DateUtil;
import com.server.util.EmojiUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.CompanyEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * author name: yjr create time: 2018-04-16 15:06:05
 */
@Repository
public class TblCustomerDaoImpl extends BaseDao<TblCustomerBean> implements TblCustomerDao {

	private static Logger log = LogManager.getLogger(TblCustomerDaoImpl.class);

	public ReturnDataUtil listPage(TblCustomerCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT tc.id,tcw.openId,tc.type,tc.alipayUserId,tc.phone,");
		sql.append(" tc.nickname,tc.UserName,tc.sexId,tc.city,tc.province,tc.country,");
		sql.append(" tc.headImgUrl,tc.latitude,tc.longitude,tc.isCertified,tc.isStudentCertified,");
		sql.append(" tc.userStatus,tc.userType,tc.createTime,tc.createId,tc.updateTime,");
		sql.append(" tc.lastUpdateId,tc.deleteFlag FROM tbl_customer AS tc");
		sql.append(" LEFT JOIN tbl_customer_wx AS tcw ON tc.id = tcw.customerId where 1=1");
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
			List<TblCustomerBean> list = Lists.newArrayList();
			while (rs.next()) {
				TblCustomerBean bean = new TblCustomerBean();
				bean.setId(rs.getLong("id"));
				bean.setOpenId(rs.getString("openId"));
				bean.setType(rs.getInt("type"));
				bean.setAlipayUserId(rs.getString("alipayUserId"));
				bean.setPhone(rs.getString("phone"));
				bean.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
				bean.setUserName(rs.getString("UserName"));
				bean.setSexId(rs.getInt("sexId"));
				bean.setCity(rs.getString("city"));
				bean.setProvince(rs.getString("province"));
				bean.setCountry(rs.getString("country"));
				bean.setHeadImgUrl(rs.getString("headImgUrl"));
				bean.setLatitude(rs.getDouble("latitude"));
				bean.setLongitude(rs.getDouble("longitude"));
				bean.setIsCertified(rs.getString("isCertified"));
				bean.setIsStudentCertified(rs.getString("isStudentCertified"));
				bean.setUserStatus(rs.getString("userStatus"));
				bean.setUserType(rs.getString("userType"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setCreateId(rs.getString("createId"));
				bean.setUpdateTime(rs.getTimestamp("updateTime"));
				bean.setLastUpdateId(rs.getString("lastUpdateId"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
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

	public TblCustomerBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		TblCustomerBean entity = new TblCustomerBean();
		return super.del(entity);
	}

	public boolean update(TblCustomerBean entity) {
		entity.setNickname(EmojiUtil.getString(entity.getNickname()));
		return super.update(entity);
	}

	public TblCustomerBean insert(TblCustomerBean entity) {
		entity.setNickname(EmojiUtil.getString(entity.getNickname()));
		return super.insert(entity);
	}

	public List<TblCustomerBean> list(TblCustomerCondition condition) {
		return null;
	}
	
	public TblCustomerBean getCustomer(Integer type,String openId){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT tc.id,tcw.unionid,tcw.appOpenId,tcw.openId,tc.type,tc.alipayUserId,tc.phone,");
		sql.append(" tc.nickname,tc.UserName,tc.sexId,tc.city,tc.province,tc.country,");
		sql.append(" tc.headImgUrl,tc.latitude,tc.longitude,tc.isCertified,tc.isStudentCertified,");
		sql.append(" tc.userStatus,tc.userType,tc.createTime,tc.createId,tc.updateTime,");
		sql.append(" tc.lastUpdateId,tc.deleteFlag,tc.isSend,tc.follow FROM tbl_customer AS tc");
		sql.append(" LEFT JOIN tbl_customer_wx AS tcw ON tc.id = tcw.customerId where 1=1 ");
		List<Object> plist = Lists.newArrayList();

		if(type==2){
			sql.append(" and tc.alipayUserId=? ");
			plist.add(openId);
		}else if(type==1){
			sql.append(" and tcw.openId=? ");
			plist.add(openId);
		}else if(type==4) {
			sql.append(" and tcw.appOpenId=? ");
			plist.add(openId);
		}else {
			sql.append(" and tcw.unionId=? ");
			plist.add(openId);
		}
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());

			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			while (rs.next()) {
				TblCustomerBean bean = new TblCustomerBean();
				bean.setId(rs.getLong("id"));
				bean.setOpenId(rs.getString("openId"));
				bean.setType(rs.getInt("type"));
				bean.setAlipayUserId(rs.getString("alipayUserId"));
				bean.setPhone(rs.getString("phone"));
				bean.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
				bean.setUserName(rs.getString("UserName"));
				bean.setSexId(rs.getInt("sexId"));
				bean.setCity(rs.getString("city"));
				bean.setProvince(rs.getString("province"));
				bean.setCountry(rs.getString("country"));
				bean.setHeadImgUrl(rs.getString("headImgUrl"));
				bean.setLatitude(rs.getDouble("latitude"));
				bean.setLongitude(rs.getDouble("longitude"));
				bean.setIsCertified(rs.getString("isCertified"));
				bean.setIsStudentCertified(rs.getString("isStudentCertified"));
				bean.setUserStatus(rs.getString("userStatus"));
				bean.setUserType(rs.getString("userType"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setCreateId(rs.getString("createId"));
				bean.setUpdateTime(rs.getTimestamp("updateTime"));
				bean.setLastUpdateId(rs.getString("lastUpdateId"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setIsSend(rs.getInt("isSend"));
				bean.setFollow(rs.getInt("follow"));
				bean.setAppOpenId(rs.getString("appOpenId"));
				bean.setUnionId(rs.getString("unionId"));
				return bean;
			}
			if (showSql) {
				log.info(sql);
				//log.info(plist.toString());
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
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
	public TblCustomerBean getCustomerByPhone(String phone) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT tc.id,tcw.openId,tc.type,tc.alipayUserId,tc.phone,");
		sql.append(" tc.nickname,tc.UserName,tc.sexId,tc.city,tc.province,tc.country,");
		sql.append(" tc.headImgUrl,tc.latitude,tc.longitude,tc.isCertified,tc.isStudentCertified,");
		sql.append(" tc.userStatus,tc.userType,tc.createTime,tc.createId,tc.updateTime,");
		sql.append(" tc.lastUpdateId,tc.deleteFlag,tc.huafaAppOpenId FROM tbl_customer AS tc");
		sql.append(" LEFT JOIN tbl_customer_wx AS tcw ON tc.id = tcw.customerId where 1=1 ");
		List<Object> plist = Lists.newArrayList();
		sql.append(" and tc.phone=?");
		plist.add(phone);
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());

			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			while (rs.next()) {
				TblCustomerBean bean = new TblCustomerBean();
				bean.setId(rs.getLong("id"));
				bean.setOpenId(rs.getString("openId"));
				bean.setType(rs.getInt("type"));
				bean.setAlipayUserId(rs.getString("alipayUserId"));
				bean.setPhone(rs.getString("phone"));
				bean.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
				bean.setUserName(rs.getString("UserName"));
				bean.setSexId(rs.getInt("sexId"));
				bean.setCity(rs.getString("city"));
				bean.setProvince(rs.getString("province"));
				bean.setCountry(rs.getString("country"));
				bean.setHeadImgUrl(rs.getString("headImgUrl"));
				bean.setLatitude(rs.getDouble("latitude"));
				bean.setLongitude(rs.getDouble("longitude"));
				bean.setIsCertified(rs.getString("isCertified"));
				bean.setIsStudentCertified(rs.getString("isStudentCertified"));
				bean.setUserStatus(rs.getString("userStatus"));
				bean.setUserType(rs.getString("userType"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setCreateId(rs.getString("createId"));
				bean.setUpdateTime(rs.getTimestamp("updateTime"));
				bean.setLastUpdateId(rs.getString("lastUpdateId"));
				bean.setLastUpdateId(rs.getString("huafaAppOpenId"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				return bean;
			}
			if (showSql) {
				log.info(sql);
				log.info(plist.toString());
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
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
	public Integer isFirstBuy(Long customerId) {
		//log.info("<TblCustomerDaoImpl--isFirstBuy--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) as count from pay_record where customerId =" +customerId);
		sql.append(" and state!=10002 and state!=10008");

		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				return rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<TblCustomerDaoImpl--isFirstBuy--end>");
		return 1;
	}

	@Override
	public Integer isStoreFirstBuy(Long customerId) {
		//log.info("<TblCustomerDaoImpl--isStoreFirstBuy--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) as count from store_order where customerId =" +customerId);
		sql.append(" and state!=10002 ");

		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				return rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<TblCustomerDaoImpl--isStoreFirstBuy--end>");
		return 1;
	}
	
	@Override
	public boolean updateCusVmCode(Long customerId, String vmCode) {
		//log.info("<TblCustomerDaoImpl--updateCusVmCode--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE tbl_customer SET vmCode = '"+vmCode+"' WHERE (vmCode IS NULL OR vmCode = '1988000080') and id = "+customerId);
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
		//log.info("<TblCustomerDaoImpl--updateCusVmCode--end>");
		if(result>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean cusVmCodeIsNull(Long customerId) {
		//log.info("<TblCustomerDaoImpl--cusVmCodeIsNull--start>");
		boolean result = false;
		StringBuffer sql = new StringBuffer();
//		sql.append("SELECT 1 FROM tbl_customer WHERE id = "+customerId+" AND vmCode IS NULL ");
		sql.append("SELECT 1 FROM tbl_customer WHERE id = "+customerId+" AND (vmCode IS NULL OR vmCode = '1988000080')");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<TblCustomerDaoImpl--cusVmCodeIsNull--end>");
		return result;
	}

	@Override
	public List<TblCustomerBean> getCustomerByCompanyId(Integer companyId) {
			ReturnDataUtil data = new ReturnDataUtil();
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT tc.id,tcw.openId,tc.type,tc.alipayUserId,tc.phone,");
			sql.append(" tc.nickname,tc.UserName,tc.sexId,tc.city,tc.province,tc.country,");
			sql.append(" tc.headImgUrl,tc.latitude,tc.longitude,tc.isCertified,tc.isStudentCertified,");
			sql.append(" tc.userStatus,tc.userType,tc.createTime,tc.createId,tc.updateTime,");
			sql.append(" tc.lastUpdateId,tc.deleteFlag,tc.inviterId,tc.follow,tc.isSend FROM tbl_customer AS tc");
			sql.append(" inner JOIN tbl_customer_wx AS tcw ON tc.id = tcw.customerId ");
			//sql.append(" where openId is not NULL ");
			log.info("---"+sql);
			Connection conn = null;
			PreparedStatement pst = null;
			ResultSet rs = null;
			List<TblCustomerBean> list = Lists.newArrayList();
			try {
				conn = openConnection();
				pst = conn.prepareStatement(sql.toString());
				rs = pst.executeQuery();
				while (rs.next()) {
					TblCustomerBean bean = new TblCustomerBean();
					bean.setId(rs.getLong("id"));
					bean.setOpenId(rs.getString("openId"));
					bean.setType(rs.getInt("type"));
					bean.setAlipayUserId(rs.getString("alipayUserId"));
					bean.setPhone(rs.getString("phone"));
					bean.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
					bean.setUserName(rs.getString("UserName"));
					bean.setSexId(rs.getInt("sexId"));
					bean.setCity(rs.getString("city"));
					bean.setProvince(rs.getString("province"));
					bean.setCountry(rs.getString("country"));
					bean.setHeadImgUrl(rs.getString("headImgUrl"));
					bean.setLatitude(rs.getDouble("latitude"));
					bean.setLongitude(rs.getDouble("longitude"));
					bean.setIsCertified(rs.getString("isCertified"));
					bean.setIsStudentCertified(rs.getString("isStudentCertified"));
					bean.setUserStatus(rs.getString("userStatus"));
					bean.setUserType(rs.getString("userType"));
					bean.setCreateTime(rs.getTimestamp("createTime"));
					bean.setCreateId(rs.getString("createId"));
					bean.setUpdateTime(rs.getTimestamp("updateTime"));
					bean.setLastUpdateId(rs.getString("lastUpdateId"));
					bean.setDeleteFlag(rs.getInt("deleteFlag"));
					//bean.setCompanyId(rs.getInt("companyId"));
					bean.setFollow(rs.getInt("follow"));
					bean.setIsSend(rs.getInt("isSend"));
					list.add(bean);
				}
				if (showSql) {
					log.info(sql);
				}
				return list;
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				return list;
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
	
	
	public boolean deleteUser(Long id) {
		String deleteCustomerSql="delete from tbl_customer where id="+id;
		//log.info("删除用户"+deleteCustomerSql);
		Connection conn = null;
		PreparedStatement pst = null;
		conn = openConnection();	
		try {
			pst = conn.prepareStatement(deleteCustomerSql.toString());
			int flag = pst.executeUpdate();
			if(flag>0) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			this.closeConnection(null, pst, conn);
		}
		return false;
	}
	public boolean updateAliUser(TblCustomerBean customer,WxCustomerBean wxCustomerBean) {
		String updateCustomerSql="update  tbl_customer set openid ='"+customer.getOpenId()+"',updateTime ='"+DateUtil.formatYYYYMMDDHHMMSS(customer.getUpdateTime())+"' where id="+customer.getId();
		String updateWxCustomerSql="update  tbl_customer_wx set customerId ="+wxCustomerBean.getCustomerId()+",updateTime ='"+DateUtil.formatYYYYMMDDHHMMSS(wxCustomerBean.getUpdateTime())+"' where openId='"+customer.getOpenId()+"'";
		
		log.info("更新支付宝用户"+updateCustomerSql);
		log.info("更新tbl_customer_wx的updateWxCustomer"+updateWxCustomerSql);

		Connection conn = null;
		PreparedStatement pst = null;
		conn = openConnection();			
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(updateCustomerSql.toString());
			int updateCustomer = pst.executeUpdate();

			pst = conn.prepareStatement(updateWxCustomerSql.toString());
			int updateWxCustomer= pst.executeUpdate();
			conn.commit();
			if(updateCustomer>0 && updateWxCustomer>0 ) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}finally {
			this.closeConnection(null, pst, conn);
		}
		return false;
	}

	public boolean insertPhone(List<TblCustomerBean> phoneList) {
		//StringBuilder sql = new StringBuilder();
		Connection conn = null;
		PreparedStatement pst = null;
		conn = openConnection();			
		try {
			conn.setAutoCommit(false);
			for(TblCustomerBean tblCustomerBean:phoneList) {
				log.info(tblCustomerBean.getPhone());
				String sql="INSERT INTO phone(number) VALUES ("+tblCustomerBean.getPhone()+")";
				log.info(sql);
				pst = conn.prepareStatement(sql);
				int insertPhone = pst.executeUpdate();
				if(insertPhone<0) {
					break;
				}
			}
			conn.commit();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}finally {
			this.closeConnection(null, pst, conn);
		}
		return false;
	}

	
	public boolean updateUserFollow(List<String> openList) {
		//StringBuilder sql = new StringBuilder();
		Connection conn = null;
		PreparedStatement pst = null;
		conn = openConnection();			
		try {
			conn.setAutoCommit(false);
			for(String openId:openList) {
				String sql="update tbl_customer set follow =1 where  openId = "+openId;
				log.info(sql);
				pst = conn.prepareStatement(sql);
				int updateUserFollow = pst.executeUpdate();
				//updateUserFollow<0   已关注的用户可能没有生成用户账号
			}
			conn.commit();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(e);
		}finally {
			this.closeConnection(null, pst, conn);
		}
		return false;		
	}
	

	public TblCustomerBean getCustomerById(Long customerId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT tc.huafaAppOpenId,tcw.companyId,tc.id,tcw.openId,tc.type,tc.alipayUserId,tc.phone,");
		sql.append(" tc.UserName,tc.sexId,tc.city,tc.province,tc.country,");
		sql.append(" tc.latitude,tc.longitude,tc.isCertified,tc.isStudentCertified,");
		sql.append(" tc.userStatus,tc.userType,tc.createTime,tc.createId,tc.updateTime,");
		sql.append(" tc.lastUpdateId,tc.deleteFlag,tc.inviterId,tc.userBalance,tc.integral,tcw.nickname,tcw.headimgurl,tc.vmCode FROM tbl_customer AS tc");
		sql.append(" LEFT JOIN tbl_customer_wx AS tcw ON tc.id = tcw.customerId ");
		if(customerId!=null) {
			sql.append(" where tc.id="+customerId);
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		TblCustomerBean bean = new TblCustomerBean();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean.setId(rs.getLong("id"));
				bean.setOpenId(rs.getString("openId"));
				bean.setType(rs.getInt("type"));
				bean.setAlipayUserId(rs.getString("alipayUserId"));
				bean.setPhone(rs.getString("phone"));
				bean.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
				bean.setUserName(rs.getString("UserName"));
				bean.setSexId(rs.getInt("sexId"));
				bean.setCity(rs.getString("city"));
				bean.setProvince(rs.getString("province"));
				bean.setCountry(rs.getString("country"));
				bean.setHeadImgUrl(rs.getString("headImgUrl"));
				bean.setLatitude(rs.getDouble("latitude"));
				bean.setLongitude(rs.getDouble("longitude"));
				bean.setIsCertified(rs.getString("isCertified"));
				bean.setIsStudentCertified(rs.getString("isStudentCertified"));
				bean.setUserStatus(rs.getString("userStatus"));
				bean.setUserType(rs.getString("userType"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setCreateId(rs.getString("createId"));
				bean.setUpdateTime(rs.getTimestamp("updateTime"));
				bean.setLastUpdateId(rs.getString("lastUpdateId"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setCompanyId(rs.getInt("companyId"));
				bean.setInviterId(rs.getLong("inviterId"));
				bean.setIntegral(rs.getInt("integral"));
				bean.setUserBalance(rs.getBigDecimal("userBalance"));
				bean.setVmCode(rs.getString("vmCode"));
                bean.setHuafaAppOpenId(rs.getString("huafaAppOpenId"));

			}
			if (showSql) {
				log.info(sql);
			}
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return bean;
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

	/**
	 * 查询会员信息
	 */
	@Override
	public TblCustomerBean findBen(Long customerId) {
		//log.info("<TblCustomerDaoImpl>----<findBen>----start");
		StringBuilder sql = new StringBuilder();
		TblCustomerBean bean=null;
		sql.append(" SELECT tc.id,tc.phone,tc.startTime,tc.endTime,tc.userBalance,tc.isMember FROM tbl_customer tc ");
		sql.append(" where tc.id='"+customerId+"' and tc.isMember=1 and  tc.endTime >now() ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean = new TblCustomerBean();
				bean.setId(rs.getLong("id"));
				bean.setPhone(rs.getString("phone"));
				bean.setIsMember(rs.getInt("isMember"));
				if(rs.getTimestamp("startTime")!=null) {
					bean.setStartTime(rs.getTimestamp("startTime"));
				}
				if(rs.getTimestamp("endTime")!=null) {
					bean.setEndTime(rs.getTimestamp("endTime"));
				}
				bean.setUserBalance(rs.getBigDecimal("userBalance"));
			}
			if (showSql) {
				log.info(sql);
			}
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return bean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public String getPhoneByCustomerId(Long customerId) {
		//log.info("<TblCustomerDaoImpl>----<getPhoneByCustomerId>----start");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT phone FROM tbl_customer WHERE id = "+customerId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String phone = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				phone = rs.getString("phone");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		//log.info("<TblCustomerDaoImpl>----<getPhoneByCustomerId>----end");
		return phone;
	}

	public List<TblCustomerBean> myInviteRewards() {
		//log.info("<TblCustomerDaoImpl>----<myInviteRewards>----start");
		List<TblCustomerBean> list = Lists.newArrayList();
		Long customerId=UserUtil.getUser().getId();
		StringBuilder sql = new StringBuilder();
		sql.append(" select tcw.nickname,tc.phone,tc.createTime,tc.id from tbl_customer tc ");
		sql.append("left join tbl_customer_wx  tcw  on tc.id=tcw.customerId where tc.inviterId='"+customerId+"'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		TblCustomerBean bean = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean=new TblCustomerBean();
				bean.setId(rs.getLong("id"));
				bean.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
				bean.setPhone(rs.getString("phone"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				list.add(bean);
			}
			//log.info("<TblCustomerDaoImpl>----<myInviteRewards>----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<TblCustomerDaoImpl>----<myInviteRewards>----end");
			return list;
		} finally {
			this.closeConnection(rs,pst,conn);
		}


	}

	@Override
	public TblCustomerBean getCustomerBeanById(Long customerId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select tcw.id,tcw.customerId,tcw.openId,tcw.nickname,tcw.sex,tcw.province,tcw.city,tcw.country,tcw.headimgurl,tcw.privilege,  ");
		sql.append(" tcw.unionid,tcw.createTime,tcw.updateTime,tc.phone,tc.userBalance,tc.integral,tc.vmCode,vmi.companyId  from tbl_customer_wx  tcw left join  tbl_customer tc on tcw.customerId=tc.id ");
		sql.append(" left join vending_machines_info vmi on tc.vmCode=vmi.code and FIND_IN_SET(vmi.companyId,getChildList("+CompanyEnum.YOUSHUI.getCompanyId()+")) ");
		sql.append(" where 1=1 and tcw.customerId='"+customerId+"'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		TblCustomerBean bean = new TblCustomerBean();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getInt("companyId"));
				bean.setOpenId(rs.getString("openId"));
				bean.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
				bean.setSexId(rs.getInt("sex"));
				bean.setProvince(rs.getString("province"));
				bean.setCity(rs.getString("city"));
				bean.setCountry(rs.getString("country"));
				bean.setHeadImgUrl(rs.getString("headimgurl"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setPhone(rs.getString("phone"));
				bean.setUserBalance(rs.getBigDecimal("userBalance"));
				bean.setIntegral(rs.getInt("integral"));
			}
			if (showSql) {
				log.info(sql);
			}
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return bean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		
	}
	
	public String getCustomerLastVmcode(Long customerId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select vendingmachinescode from pay_record where customerId = "+customerId);
		sql.append(" order by createTime desc");
		sql.append(" limit 1");

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String vmCode=null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				vmCode = rs.getString("vendingmachinescode");
			}
			if (showSql) {
				log.info(sql);
			}
			return vmCode;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return vmCode;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public boolean updateCustomerBean(Long customerId, Long rewards,Integer type) {
		StringBuilder sql = new StringBuilder();
		if(type==1) {
			sql.append(" update tbl_customer set integral =integral+"+rewards+"  where  id = "+customerId+" ");
		}else {
			sql.append(" update tbl_customer set userBalance =userBalance+"+rewards+"  where  id = "+customerId+" ");
		}
		//log.info("游戏中奖后 更新用户积分或者余额---"+sql);
		Connection conn = null;
		PreparedStatement pst = null;
		boolean flag=false;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
		   int executeUpdate = pst.executeUpdate();
		   if(executeUpdate>0) {
			   flag=true;
		   }
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(null, pst, conn);
		}
		return flag;
	}
}

	
	
	
	
	

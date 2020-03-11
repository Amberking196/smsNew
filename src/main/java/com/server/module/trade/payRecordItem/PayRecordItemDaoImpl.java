package com.server.module.trade.payRecordItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;
@Repository
public class PayRecordItemDaoImpl extends MySqlFuns implements PayRecordItemDao{

	private final static Logger log = LogManager.getLogger(PayRecordItemDaoImpl.class);

	@Override
	public Long insert(PayRecordItemBean recordItem) {
		//log.info("<PayRecordItemDaoImpl--insert--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO pay_record_item(payRecordId,itemId,basicItemId,itemName,itemType,costPrice,price,num,createTime,realTotalPrice,finalNum,finalTotalPrice)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
		param.add(recordItem.getPayRecordId());
		param.add(recordItem.getItemId());
		param.add(recordItem.getBasicItemId());
		param.add(recordItem.getItemName());
		param.add(recordItem.getItemType());
		param.add(recordItem.getCostPrice());
		param.add(recordItem.getPrice());
		param.add(recordItem.getNum());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
		param.add(recordItem.getRealTotalPrice());
		param.add(recordItem.getNum());
		param.add(recordItem.getRealTotalPrice());
		int insertGetID = insertGetID(sql.toString(), param);


		//log.info("<PayRecordItemDaoImpl--insert--end>");
		return Long.valueOf(insertGetID);
	}
	
	public List<PayRecordItemBean> getListByPayRecordId(Long payRecordId){
		//log.info("<PayRecordItemDaoImpl--getListByPayRecordId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("select * from pay_record_item where payRecordId ="+payRecordId);
		log.info("sql语句："+sql);
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<PayRecordItemBean> list=Lists.newArrayList();
		try {
			conn=openConnection();
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while(rs.next()){
				PayRecordItemBean bean = new PayRecordItemBean();
				bean.setId(rs.getLong("id"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setBasicItemId(rs.getLong("basicItemId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setItemType(rs.getLong("itemType"));
				bean.setPrice(rs.getDouble("price"));
				bean.setNum(rs.getInt("num"));
				bean.setRealTotalPrice(rs.getDouble("realTotalPrice"));
				list.add(bean);
			}
			//log.info("<PayRecordItemDaoImpl--getListByPayRecordId--end>");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally{
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
	public boolean updatePayRecordItem(PayRecordItemBean payRecordItem) {
		//log.info("<PayRecordItemDaoImpl--updatePayRecordItem--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE pay_record_item SET num = ?,price=?,realTotalPrice =?,finalNum=?,finalTotalPrice=? WHERE id = ?");
		param.add(payRecordItem.getNum());
		param.add(payRecordItem.getPrice());
		param.add(payRecordItem.getRealTotalPrice());
		
		param.add(payRecordItem.getNum());
		param.add(payRecordItem.getRealTotalPrice());
		param.add(payRecordItem.getId());
		int upate = upate(sql.toString(),param);
		//log.info("<PayRecordItemDaoImpl--updatePayRecordItem--end>");
		if(upate>0){
			return true;
		}
		return false;
	}

}

package com.server.module.zfb_trade.module.creditwithheld;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;
@Repository("aliCreditWithheldDao")
public class AliCreditWithheldDaoImpl extends MySqlFuns implements AliCreditWithheldDao{

	public static Logger log = LogManager.getLogger(AliCreditWithheldDaoImpl.class); 
	@Override
	public CreditWithheldBean queryByAliId(CreditWithheldBean creditWithheld) {
		log.info("<AliCreditWithheldDaoImpl--queryByAliId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,agreementNo,productCode,scene,validTime,invalidTime,");
		sql.append(" alipayUserId,externalSignNo,isSuccess,signType,`SIGN`,`STATUS`,signTime,");
		sql.append(" signModifyTime,createTime,createId,updateTime,lastUpdateId,deleteFlag");
		sql.append(" FROM tbl_rfid_credit_withheld WHERE alipayUserId="+creditWithheld.getAlipayUserId());
		CreditWithheldBean  creditWithheldBean = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				creditWithheldBean = new CreditWithheldBean();
				creditWithheldBean.setAgreementNo(rs.getString("agreementNo"));;
				creditWithheldBean.setAlipayUserId(rs.getString("alipayUserId"));;
				creditWithheldBean.setCreateId(rs.getString("createId"));;
				creditWithheldBean.setCreateTime(rs.getDate("createTime"));;
				creditWithheldBean.setDeleteFlag(rs.getInt("deleteFlag"));;
				creditWithheldBean.setExternalSignNo(rs.getString("externalSignNo"));;
				creditWithheldBean.setId(rs.getLong("id"));;
				creditWithheldBean.setInvalidTime(rs.getDate("invalidTime"));;
				creditWithheldBean.setIsSuccess(rs.getString("isSuccess"));;
				creditWithheldBean.setLastUpdateId(rs.getString("lastUpdateId"));;
				creditWithheldBean.setProductCode(rs.getString("productCode"));;
				creditWithheldBean.setScene(rs.getString("scene"));;
				creditWithheldBean.setSign(rs.getString("sign"));;
				creditWithheldBean.setSignModifyTime(rs.getDate("signModifyTime"));;
				creditWithheldBean.setSignTime(rs.getDate("signTime"));;
				creditWithheldBean.setSignType(rs.getString("signType"));;
				creditWithheldBean.setStatus(rs.getString("status"));;
				creditWithheldBean.setUpdateTime(rs.getDate("updateTime"));;
				creditWithheldBean.setValidTime(rs.getDate("validTime"));;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeConnection(rs, ps, conn);
		}
		log.info("<AliCreditWithheldDaoImpl--queryByAliId--end>");
		return creditWithheldBean;
	}

	@Override
	public boolean save(CreditWithheldBean creditWithheld) {
		log.info("<AliCreditWithheldDaoImpl--save--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into tbl_rfid_credit_withheld (agreementNo,productCode,companyId,");
		sql.append(" scene,validTime,invalidTime,alipayUserId,externalSignNo,isSuccess,signType,");
		sql.append(" sign,status,signTime,createTime)");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(creditWithheld.getAgreementNo());
		param.add(creditWithheld.getProductCode());
		param.add(creditWithheld.getCompanyId());
		param.add(creditWithheld.getScene());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(creditWithheld.getValidTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(creditWithheld.getInvalidTime()));
		param.add(creditWithheld.getAlipayUserId());
		param.add(creditWithheld.getExternalSignNo());
		param.add(creditWithheld.getIsSuccess());
		param.add(creditWithheld.getSignType());
		param.add(creditWithheld.getSign());
		param.add(creditWithheld.getStatus());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(creditWithheld.getSignTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(creditWithheld.getCreateTime()));
		int result = insert(sql.toString(), param);
		log.info("<AliCreditWithheldDaoImpl--save--end>");
		if(result ==1){
			return true;
		}
		return false;
	}

	@Override
	public boolean update(CreditWithheldBean creditWithheld) {
		log.info("<AliCreditWithheldDaoImpl--update--start>");
//		StringBuffer sql = new StringBuffer();
		//未完成
		log.info("<AliCreditWithheldDaoImpl--update--end>");
		return false;
	}

}

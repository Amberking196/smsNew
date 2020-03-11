package com.server.module.zfb_trade.module.company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.sqlUtil.MySqlFuns;

@Repository("aliCompanyDao")
public class AliCompanyDaoImpl extends MySqlFuns implements AliCompanyDao{

	public static Logger log = LogManager.getLogger(AliCompanyDaoImpl.class);   
	
	@Override
	public List<CompanyBean> findAllSonCompany(Integer parentId) { 
		//log.info("<AliCompanyDaoImpl>--<findAllSonCompany>--start"); 
		List<CompanyBean> companyList = new ArrayList<CompanyBean>();
		StringBuffer sql = new StringBuffer();
		sql.append("select areaId,createTime,id,location,logoPic,mail,name,parentId,phone,principal,shortName,state from company where state = 2050 and parentId ="+parentId+" union "
				+ "select areaId,createTime,id,location,logoPic,mail,name,parentId,phone,principal,shortName,state from company where state = 2050 and id = "+parentId);
		sql.append(" UNION select areaId,createTime,id,location,logoPic,mail,name,parentId,phone,principal,shortName,state from company where state = 2050 and parentId in( SELECT id FROM company WHERE state = 2050 and parentId = "+parentId+")");
		
		log.info("<AliCompanyDaoImpl>--<findAllSonCompany>--sql:"+sql.toString()); 
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CompanyBean company = null;
		try {
			conn = openConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs != null && rs.next()){
				company = setCompanyInfo(rs);
				companyList.add(company);
			}
			//从第四层公司开始查询 直到此层无公司
			StringBuffer sql3=new StringBuffer();
			sql3.append("select id from company where state = 2050 and parentId in( SELECT id FROM company WHERE state = 2050 and parentId = "+parentId+")");
			while(rs != null) {
				log.info("<AliCompanyDaoImpl>--<findAllSonCompany>--sql3:"+sql3.toString());
				StringBuffer sql4=new StringBuffer();
				sql4.append("select areaId,createTime,id,location,logoPic,mail,name,parentId,phone,principal,shortName,state from company where parentId in("+sql3+") and  state = 2050");
				log.info("<AliCompanyDaoImpl>--<findAllSonCompany>--sql4:"+sql4.toString());
				ps = conn.prepareStatement(sql4.toString());
				rs = ps.executeQuery();
				while(rs != null && rs.next()){
					company = setCompanyInfo(rs);
					companyList.add(company);
				}
			    if(rs.first() == false) {
					break;
				}
			    //截取部分sql
				sql3=sql4.replace(7, 95, "id");
			}
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		//log.info("<AliCompanyDaoImpl>--<findAllSonCompany>--end"); 
		return companyList;
	}
	

	@Override
	public List<Integer> findAllSonCompanyId(Integer parentId) {
		//log.info("<AliCompanyDaoImpl>--<findAllSonCompanyId>--start");  
		List<Integer> resultList = new ArrayList<Integer>();
		//String sql = "SELECT id FROM company WHERE parentId="+parentId;
		StringBuilder sql=new StringBuilder();
		sql.append(" select id from company where state = 2050 and parentId  in( SELECT id FROM company WHERE state = 2050 and parentId = "+parentId+")");
		sql.append(" UNION ");
		sql.append(" SELECT id FROM company WHERE state = 2050 and parentId = "+parentId);
		
		log.info("<AliCompanyDaoImpl>--<findAllSonCompanyId>--sql:"+sql.toString());   
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		resultList.add(parentId);
		try {
			conn = openConnection();
            conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs != null && rs.next()){
				resultList.add(rs.getInt("id"));
			}
			//从第四层公司开始查询 直到此层无公司
			StringBuffer sql3=new StringBuffer();
			sql3.append("select id from company where state = 2050 and parentId  in( SELECT id FROM company  WHERE state = 2050 and parentId = "+parentId+")");
			log.info("<AliCompanyDaoImpl>--<findAllSonCompanyId>--sql3:"+sql3.toString());
			while(rs != null) {
				StringBuffer sql4=new StringBuffer();
				sql4.append("select id from company where state = 2050 and parentId in("+sql3+")");
				log.info("<AliCompanyDaoImpl>--<findAllSonCompanyId>--sql4:"+sql4.toString());
				ps = conn.prepareStatement(sql4.toString());
				rs = ps.executeQuery();
				while(rs != null && rs.next()){
					resultList.add(rs.getInt("id"));
				}
			    if(rs.first() == false) {
					break;
				}
				sql3=sql4;
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			this.closeConnection(rs,ps,conn);
		}
		//log.info("<AliCompanyDaoImpl>--<findAllSonCompanyId>--end");  
		
		return resultList;
	}
	


	public CompanyBean setCompanyInfo(ResultSet rs) throws SQLException{
		CompanyBean companyBean = new CompanyBean();
		companyBean.setAreaId(rs.getInt("areaId"));
		companyBean.setCreateTime(rs.getDate("createTime"));
		companyBean.setId(rs.getInt("id"));
		companyBean.setLocation(rs.getString("location"));
		companyBean.setLogoPic(rs.getString("logoPic"));
		companyBean.setMail(rs.getString("mail"));
		companyBean.setName(rs.getString("name"));
		companyBean.setParentId(rs.getInt("parentId"));
		companyBean.setPhone(rs.getString("phone"));
		companyBean.setPrincipal(rs.getString("principal"));
		companyBean.setShortName(rs.getString("shortName"));
		companyBean.setState(rs.getInt("state"));
		return companyBean;
	}
}

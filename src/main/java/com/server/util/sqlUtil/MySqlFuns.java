package com.server.util.sqlUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.server.dbpool.BaseDB;
 

public class MySqlFuns extends BaseDB{

	public static Logger log = LogManager.getLogger(MySqlFuns.class);
	public static final String SQL_GET_ID = "SELECT LAST_INSERT_ID() AS id";
	
	public int insert(String sql){
		
		Connection conn = null;
		PreparedStatement ps = null;
		int re=0;
		try {
			conn = this.openConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			re=ps.executeUpdate();    
			conn.commit();
		}  catch (SQLException e) {   
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
		return re;
	}
	 
	public int getId(Connection conn,PreparedStatement ps){
		ResultSet rs  = null;
		try {
			ps = conn.prepareStatement(SQL_GET_ID);
			rs  = ps.executeQuery();
			if(rs.next()){
				return rs.getInt("id");
			}
		} catch (SQLException e) {
			  e.printStackTrace(); 
		}
		return 0;
		
	}  

	public int insertGetID(String sql){
		int id = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			id = getId(conn,ps);
			conn.commit();
		}  catch (SQLException e) { 
			try {
				id = -1;
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
		return id;
	}
	
	

	 
	public int delete(String sql){
		int result = 0;
		Connection conn = null;
			PreparedStatement ps = null;
			try {
				conn = openConnection();
				conn.setAutoCommit(false);
				ps = conn.prepareStatement(sql);
				result = ps.executeUpdate(); 
				conn.commit();
			}  catch (SQLException e) {  
				try {
					conn.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} finally {
				this.closeConnection(null, ps, conn);
			}
			return result;
	} 
	
	public int upate(String sql) {
		Connection conn = null;
		PreparedStatement ps = null;
		int re=0;
		try {
			conn = openConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			re=ps.executeUpdate(); 
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		} finally {
			this.closeConnection(null, ps, conn);
		}
		return re;
	}
	
	public void excute(String sql) {
		Connection conn = null;
		Statement ps = null;
		try {
			conn = openConnection();
			conn.setAutoCommit(false);
			ps =  conn.createStatement();
			ps.execute(sql);
			conn.commit();
		}  catch (SQLException e) { 
			log.error(e.getMessage());
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		} finally {
			if(ps!=null)
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			closeConnection(conn);
			
		}
	}
	
	public void query(String sql){
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql);
            ResultSet rs =  ps.executeQuery();
            while(rs.next()){
            //	String name=rs.getString(1);
            	String companyId=rs.getString("companyId");
            	System.out.println(companyId);
            }
			//conn.commit();
		}  catch (SQLException e) { 
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	
public int insert(String sql,List<Object> param){
		
		Connection conn = null;
		PreparedStatement ps = null;
		int re=0;
		try {
			conn = this.openConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			String logSql = sql;
			for (int i = 0; i < param.size(); i++) {
				ps.setObject(i+1,param.get(i));
				if(param.get(i)!=null){
					if(param.get(i).getClass().getName().equals("java.lang.String")){
						logSql = logSql.replaceFirst("[\\?]", "'"+param.get(i).toString()+"'");
					}else{
						logSql = logSql.replaceFirst("[\\?]", param.get(i).toString());
					}
				}else{
					logSql = logSql.replaceFirst("[\\?]","null");
				}
				
			}
			log.info("sql语句："+logSql);
			re=ps.executeUpdate();    
			conn.commit();
		}  catch (SQLException e) {   
			try {
				re = -1;
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
		return re;
	}


	public int upate(String sql,List<Object> param) {
		Connection conn = null;
		PreparedStatement ps = null;
		int re=0;
		try {
			conn = openConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			String logSql = sql;
			for (int i = 0; i < param.size(); i++) {
				ps.setObject(i+1, param.get(i));
				if(param.get(i)!=null){
					if(param.get(i).getClass().getName().equals("java.lang.String")){
						logSql = logSql.replaceFirst("[\\?]", "'"+param.get(i).toString()+"'");
					}else{
						logSql = logSql.replaceFirst("[\\?]", param.get(i).toString());
					}
				}else{
					logSql = logSql.replaceFirst("[\\?]", "null");
				}
			}
			log.info("sql语句："+logSql);
			re=ps.executeUpdate(); 
			conn.commit();
		}  catch (SQLException e) { 
			try {
				re= -1;
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		} finally {
			this.closeConnection(null, ps, conn);
		}
		return re;
	}
	

	public int insertGetID(String sql,List<Object> param){
		System.out.println("我进来额");
		int id = -1;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			String logSql = sql;
			for (int i = 0; i < param.size(); i++) {
				ps.setObject(i+1,param.get(i));
				if(param.get(i)!=null){
					if(param.get(i).getClass().getName().equals("java.lang.String")){
						logSql = logSql.replaceFirst("[\\?]", "'"+param.get(i).toString()+"'");
					}else{
						logSql = logSql.replaceFirst("[\\?]", param.get(i).toString());
					}
				}else{
					logSql = logSql.replaceFirst("[\\?]","null");
				}
				
			}
			log.info("sql语句："+logSql);
			int result = ps.executeUpdate();    
			System.out.println("logSql==="+logSql);
			System.out.println("result==="+result);
			if(result == 1){
				id = getId(conn,ps);
			}
			conn.commit();
		}  catch (SQLException e) { 
			e.printStackTrace();
			try {
				id = -1;
				System.out.println("我回滚了");
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
		return id;
	}
}

package com.server.util.sqlUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.server.dbpool.BaseDB;
 
 
 
public class DeleteFuns extends BaseDB {
	public static Log log = LogFactory.getLog(DeleteFuns.class);
	public void delete(String sql){
		Connection conn = null;
			PreparedStatement ps = null;
			try {
				conn = openConnection();
				ps = conn.prepareStatement(sql);
				ps.execute();
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
	}
	
	public void delete(String sql,Object obj1){
		Connection conn = null;
			PreparedStatement ps = null;
			try {
				conn = openConnection();
				ps = conn.prepareStatement(sql);
				ps.setObject(1, obj1);
				ps.execute();
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
	}
	
	public void delete(String sql,Object obj1,Object obj2){
		Connection conn = null;
			PreparedStatement ps = null;
			try {
				conn = openConnection();
				ps = conn.prepareStatement(sql);
				int i=1;
				ps.setObject(i++, obj1);
				ps.setObject(i++, obj2);
				ps.execute();
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
	}
	
 
	
	public void delete(String sql,Object obj1,Object obj2,Object obj3){
		Connection conn = null;
			PreparedStatement ps = null;
			try {
				conn = openConnection();
				ps = conn.prepareStatement(sql);
				int i=1;
				ps.setObject(i++, obj1);
				ps.setObject(i++, obj2);
				ps.setObject(i++, obj3);
				ps.execute();
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
	}
	
	 
}

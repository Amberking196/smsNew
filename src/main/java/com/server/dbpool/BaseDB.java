package com.server.dbpool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.server.dbpool.DBPool;

/**
 * 数据库的连结接口 *******************************************************************
 * 说明：
 * <p> 
 */
public class BaseDB   {
	public static Log log = LogFactory.getLog(BaseDB.class);
	
	public static long count = 0;
	private static Object lock = new Object();
	private static int counter =0;
	private int i=1;
	private static PropManager prop = new PropManager("dbManager.properties");
	private static Properties config = prop.getProp();
	private static int limit = Integer.parseInt(config.getProperty("DB.MaxActive"));
	
	public Connection openConnection() { 
		Connection result=null;
		synchronized (lock) {
			while (counter >= limit) {
				log.info("当前数据库连接超过最大连接数，请等待连接释放！！！");
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			counter++;
			 	try {
				result = DBPool.getConnection();
			} catch (SQLException e) { 
				e.printStackTrace();
			}
		} 
		if(result==null){
			System.out.println("counter="+counter+"  limit  "+limit);
		}
		return result;
	}

	public void closeConnection(ResultSet rs, PreparedStatement ps,
			Connection conn) {
 
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) { 
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) { 
			}
		}
		if (conn != null) {
			try {
				conn.close();
				synchronized (lock) {
					counter--; 
					lock.notifyAll();
				}
			} catch (SQLException e) { 
			}
		}
 
	}
	public void closeConnection(Connection conn) {
 
		
		if (conn != null) {
			try {
				conn.close();
				synchronized (lock) {
					counter--; 
					lock.notifyAll();
				}
			} catch (SQLException e) { 
			}
		}
 
	}

	@Override
	public int hashCode() { 
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + i; 
		return result;
	}

	@Override
	public boolean equals(Object obj) { 
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BaseDB other = (BaseDB) obj;
		if (i != other.i)
			return false;
		return true;
	}

}


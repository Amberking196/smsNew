package com.server.dbpool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 
/**
 * 数据库连结池
 ********************************************************************
 * 说明：<p>
 *      所有的连结都在dbManager.properties里修改
 ******************************************************************** 
 */ 
public class DBPool  {

	 Log log = LogFactory.getLog(DBPool.class);  
	private static DBPool dbpoll=null;
	
	private static BasicDataSource datasource=null;
	private Properties config=null;
	
	private DBPool(){
		log.info("<DBPool>--<DBPool>--start");
		PropManager prop = new PropManager("dbManager.properties");
		config = prop.getProp();
		 
		datasource =new BasicDataSource();
		datasource.setDriverClassName(config.getProperty("DB.DriverClassName"));
		datasource.setUsername(config.getProperty("DB.Username").trim());
		datasource.setPassword(config.getProperty("DB.Password"));
		datasource.setUrl(config.getProperty("DB.ConnectionUrl")); 
		datasource.setMinIdle(Integer.parseInt(config.getProperty("DB.MinIdle")));
		datasource.setMaxIdle(Integer.parseInt(config.getProperty("DB.MaxIdle")));
		datasource.setInitialSize(Integer.parseInt(config.getProperty("DB.InitialSize")));
		datasource.setMaxWaitMillis(Integer.parseInt(config.getProperty("DB.MaxWait")));

		log.info("<DBPool>--<DBPool>--end");
	}
	
	public static DBPool getInstance(){
 
		if(dbpoll==null){
			synchronized (DBPool.class) {
				if(dbpoll==null){
					dbpoll=new DBPool();
				}
			}
		} 
		return dbpoll;
	}
	
	private Connection _getconn() throws SQLException{
 
		return datasource.getConnection();
	}
	public static Connection getConnection() throws SQLException{
		
		return getInstance()._getconn();
	}
	private void _show(){
 	}
	
	public static void show(){
		getInstance()._show();
	}
	
	private void _close() throws SQLException{
		datasource.close();
	}
	public static void close() throws SQLException{
		getInstance()._close();
	}

	public BasicDataSource getDatasource() {
		return datasource;
	}


	
	
}

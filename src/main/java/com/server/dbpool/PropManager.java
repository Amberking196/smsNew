package com.server.dbpool;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 管理所有的配置文件
 ********************************************************************
 * 说明：<p>
 * 
 */
public class PropManager {
	
	Log log = LogFactory.getLog(PropManager.class); 
	private Properties  prop = new Properties() ;

	/**
	 * 构造函数
	 ************************************************
	 * 给一个名称，实例化一个对象
	 * @param propName ：
	 ************************************************/
	public PropManager(String propName){
		log.info("<PropManager>--<PropManager>--start");
		ClassLoader cls = getClass().getClassLoader();
		URL url = cls.getResource(propName);
		try {
			log.debug("PropManager propties is load !");
			prop.load(url.openStream());
		} catch (IOException e) {
		}
		log.info("<PropManager>--<PropManager>--end");
	}
	
	public Properties getProp() {
		log.info("<PropManager>--<getProp>--start");
		log.info("<PropManager>--<getProp>--end");
		return prop;
	}

//  test --! 
	public static void main(String[] args) {
		PropManager m = new PropManager("dbManager.properties");
		Properties prop = m.getProp();
		System.out.println(prop.get("DB.DriverClassName"));
	}
	
}

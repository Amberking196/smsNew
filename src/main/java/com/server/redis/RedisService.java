package com.server.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {
	
	@Autowired
	JedisPool jedisPool;
	/**
	 * 获取字符串
	 * */
	public String getString(String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			 String  str = jedis.get(key);
			 return str;
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	public String setString(String key,String content) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			 String  str = jedis.set(key,content);
			 return str;
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	public long del(String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			 long   l = jedis.del(key);
			 return l;
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	
	/**
	 * 获取当个对象
	 * */
	public <T> T get(KeyPrefix prefix, String key,  Class<T> clazz) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			 //生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			 String  str = jedis.get(realKey);
			 T t =  stringToBean(str, clazz);
			 return t;
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	public String get(KeyPrefix prefix, String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			 //生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			 String  str = jedis.get(realKey);
			 return str;
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	
	/**
	 * 设置对象
	 * */
	public <T> boolean set(KeyPrefix prefix, String key,  T value) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			 String str = beanToString(value);
			 if(str == null || str.length() <= 0) {
				 return false;
			 }
			//生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			 int seconds =  prefix.expireSeconds();
			 if(seconds <= 0) {
				 jedis.set(realKey, str);
			 }else {
				 jedis.setex(realKey, seconds, str);
			 }
			 return true;
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	
	public  boolean set(KeyPrefix prefix, String key,  String content) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			 if(content == null || content.length() <= 0) {
				 return false;
			 }
			//生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			 int seconds =  prefix.expireSeconds();
			 if(seconds <= 0) {
				 jedis.set(realKey, content);
			 }else {
				 jedis.setex(realKey, seconds, content);
			 }
			 return true;
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	
	/**
	 * 设置对象
	 * */
	public <T> boolean set(String key,  T value ,int expires) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			 String str = beanToString(value);
			 if(str == null || str.length() <= 0) {
				 return false;
			 }
			//生成真正的key
			 if(expires <= 0) {
				 jedis.set(key, str);
			 }else {
				 jedis.setex(key, expires, str);
			 }
			 return true;
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	
	public  boolean set(String key,  String content , int expires) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			 if(content == null || content.length() <= 0) {
				 return false;
			 }
			//生成真正的key
			 if(expires <= 0) {
				 jedis.set(key, content);
			 }else {
				 jedis.setex(key, expires, content);
			 }
			 return true;
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	/**
	 * 获取当个对象
	 * */
	public <T> T get(String key,  Class<T> clazz) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			 //生成真正的key
			 String  str = jedis.get(key);
			 T t =  stringToBean(str, clazz);
			 return t;
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	
	/**
	 * 判断key是否存在
	 * */
	public  Boolean exists(KeyPrefix prefix, String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			return  jedis.exists(realKey);
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	/**
	 * 判断key是否存在
	 * */
	public  Boolean exists(String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			return  jedis.exists(key);
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	/**
	 * 增加值
	 * */
	public <T> Long incr(KeyPrefix prefix, String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			return  jedis.incr(realKey);
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	/**
	 * 减少值
	 * */
	public <T> Long decr(KeyPrefix prefix, String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			return  jedis.decr(realKey);
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	
	public  long del(KeyPrefix prefix, String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			return  jedis.del(realKey);
		 }finally {
			  returnToPool(jedis);
		 }
	}
	private <T> String beanToString(T value) {
		if(value == null) {
			return null;
		}
		Class<?> clazz = value.getClass();
		if(clazz == int.class || clazz == Integer.class) {
			 return ""+value;
		}else if(clazz == String.class) {
			 return (String)value;
		}else if(clazz == long.class || clazz == Long.class) {
			return ""+value;
		}else {
			return JSON.toJSONString(value);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T stringToBean(String str, Class<T> clazz) {
		if(str == null || str.length() <= 0 || clazz == null) {
			 return null;
		}
		if(clazz == int.class || clazz == Integer.class) {
			 return (T)Integer.valueOf(str);
		}else if(clazz == String.class) {
			 return (T)str;
		}else if(clazz == long.class || clazz == Long.class) {
			return  (T)Long.valueOf(str);
		}else {
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}
	}

	private void returnToPool(Jedis jedis) {
		 if(jedis != null) {
			 jedis.close();
		 }
	}
	
	/** 
     * 添加key value
     * @param key 
     * @param value 
     */  
    public void set(byte [] key,byte [] value){  
    	Jedis jedis = null;
    	try{
    		jedis =  jedisPool.getResource();
            jedis.set(key, value);  
    	}finally{
    		returnToPool(jedis);
    	}
    }
	
	
	/** 
     * 添加key value 并且设置存活时间(byte) 
     * @param key 
     * @param value 
     * @param liveTime 
     */  
    public void set(byte [] key,byte [] value,int liveTime){  
    	Jedis jedis = null;
    	try{
    		jedis =  jedisPool.getResource();
            jedis.set(key, value);  
            jedis.expire(key, liveTime); 
    	}finally{
    		returnToPool(jedis);
    	}
    }
    
    
    /** 
     * 获取redis value (byte [] )(反序列化) 
     * @param key 
     * @return 
     */  
    public byte[] get(byte [] key){  
    	Jedis jedis = null;
    	try{
    		jedis =  jedisPool.getResource();
    		byte[] value = jedis.get(key); 
            return value;  
    	}finally{
    		returnToPool(jedis);
    	}
    } 
    
    public boolean setIfNotExists(String key,String value,int expires){
    	Jedis jedis = null;
    	try{
    		jedis =  jedisPool.getResource();
            String back = jedis.set(key, value, "NX","EX",expires);
            if(back == null){
            	return false;
            }else if(back.equals("OK")){
            	return true;
            }else{
            	return false;
            }
    	}finally{
    		returnToPool(jedis);
    	}
    }
    
    public void setrange(String key,String value,int offset){
    	Jedis jedis = null;
    	try{
    		jedis =  jedisPool.getResource();
            jedis.setrange(key, offset, value);
    	}finally{
    		returnToPool(jedis);
    	}
    }

}

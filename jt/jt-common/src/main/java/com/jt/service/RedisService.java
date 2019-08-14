package com.jt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

@Service   //@Lazy	//表示当前类懒加载
public class RedisService {
	/**
	 * 	表示程序启动时必须注入该对象
	 * 	如果该注入操作有可能对其它的程序产生影响
	 * 这时需要配置懒加载.	
	 */
	@Autowired(required = false)	
	private JedisSentinelPool pool;
	
	public void set(String key,String value) {
		Jedis jedis = pool.getResource();
		jedis.set(key,value);
		jedis.close();
	}
	
	public void setex(String key,Integer seconds,String value) {
		Jedis jedis = pool.getResource();
		jedis.setex(key, seconds, value);
		jedis.close();
	}
	
	public String get(String key) {
		Jedis jedis = pool.getResource();
		String value = jedis.get(key);
		jedis.close();
		return value;
	}
}

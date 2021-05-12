package com.cafe24.auction.library;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

//@PropertySource({ "classpath:application.yml" })
//@Component
public class libRedis2 {
	//@Value("${spring.redis.host}")
	//private String redisHost;
	private String redisHost = "localhost";
	
	//@Value("${spring.redis.port}")
	//private int redisPort;
	private int redisPort = 6379;
	
	private final Set<Jedis> connectionList = new HashSet<Jedis>();
	private JedisPool pool;
	
	private libRedis2() {
		this.pool = new JedisPool(redisHost, redisPort);
	}
	
	private static class LazyHolder {
		private static final libRedis2 INSTANCE = new libRedis2();
	}
	
	public static libRedis2 getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	final public Jedis getConnection() {
		Jedis jedis = this.pool.getResource();
		this.connectionList.add(jedis);
		
		return jedis;
	}
	
	final public void returnResource(Jedis jedis) { 
		this.pool.returnBrokenResource(jedis);
	}

	final public void destroyPool() {
		Iterator<Jedis> jedisList = this.connectionList.iterator();
		while (jedisList.hasNext()) {
			Jedis jedis = jedisList.next();
			this.pool.returnResource(jedis);
		}
		
		this.pool.destroy();
	}
}

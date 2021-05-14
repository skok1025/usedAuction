package com.cafe24.auction.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cafe24.auction.library.libRedis;
import com.cafe24.auction.library.libRedis2;
import com.cafe24.auction.service.RedisService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Controller
@RequestMapping("/redis")
public class TestRedisController {

	@Autowired
	private RedisService service;
	
	@Autowired
	private libRedis libredis;
	
	@ResponseBody
	@GetMapping("/set")
	public String setTest() { 
		service.set("hello_redis", "value");
		return service.get("hello_redis");
	}
	
	@ResponseBody
	@GetMapping("/set1")
	public String jedisTest() {
		Jedis jedis = new Jedis("localhost", 6379);
		
		String result = jedis.set("key1", "val1");
		
		Map<String, Double> map = new HashMap<>();
		map.put("member1", (double) 1);
		jedis.zadd("zsettest", map);
		System.out.println(result);
		
		return jedis.get("key1");
	}
	
	@ResponseBody
	@GetMapping("/insert")
	public String redisInsertTest() {
		final float TOTAL_OP = 10000f;
		JedisPool pool = new JedisPool("localhost", 6379);
		Jedis jedis = pool.getResource();
		String key, value;
		long start = System.currentTimeMillis();
		long loopTime = System.currentTimeMillis();
		
		for(int i = 1; i <= TOTAL_OP; i++) {
			key = value = String.valueOf("key" + (10000 + i));
			jedis.set(key, value);
			//jedis.del(key);
		}
		
		long elapsed = System.currentTimeMillis() - start;
		System.out.println("초당 처리 건수 : " + TOTAL_OP / elapsed * 1000f);
		System.out.println("소요 시간 : " + elapsed / 1000f + "초");
		jedis.disconnect();
		
		return "end";
	}
	
	@ResponseBody
	@GetMapping("/libTest")
	public String redisLibTest() {
//		Jedis jedis = libRedis2.getInstance().getConnection();
//		jedis.set("libKey", "libVal");
//		
//		return jedis.get("libKey");
		
		libredis.set("lib_key", "lib_val");
		
		return libredis.get("lib_key");
	}
	
}

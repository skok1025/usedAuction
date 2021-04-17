package com.cafe24.auction.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cafe24.auction.service.RedisService;

import redis.clients.jedis.Jedis;

@Controller
@RequestMapping("/redis")
public class TestRedisController {

	@Autowired
	private RedisService service;
	
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
}

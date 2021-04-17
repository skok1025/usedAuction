package com.cafe24.auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cafe24.auction.repository.RedisDao;

@Service
public class RedisService {

	@Autowired
	private RedisDao dao;

	public void set(String key, String val) {
		dao.set(key, val);
	}

	public String get(String key) {
		return dao.get(key);
	}
	
}

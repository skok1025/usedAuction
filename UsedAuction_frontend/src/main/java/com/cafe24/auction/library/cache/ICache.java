package com.cafe24.auction.library.cache;

public interface ICache {
	void write(String cacheKey, String str);
	String read(String cacheKey	);
}

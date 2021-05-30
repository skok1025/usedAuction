package com.cafe24.auction.library.cache;

public class libCache {
	private ICache cacheMethod;
	
	public void setCacheMethod(ICache cacheMethod) {
		this.cacheMethod = cacheMethod;
	}
	
	public void write(String cacheKey, String str) {
		if (cacheMethod == null) {
			cacheMethod = new libFileCache();
		}
		cacheMethod.write(cacheKey, str);
	}
	
	public String read(String cacheKey) {
		if (cacheMethod == null) {
			cacheMethod = new libFileCache();
		}
		
		return cacheMethod.read(cacheKey);
	}

}

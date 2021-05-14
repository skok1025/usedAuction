package com.cafe24.auction.library;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class libCart {
	@Autowired
	private libRedis redis;
	
	private JSONObject cartInfo;
	private static final String KEY_CART_LIST = ":cart:product";
	private static final String KEY_CART_PRODUCT = ":cart:productid:";
	private static final String JSON_PRODUCT_LIST = "products";
	private static final int EXPIRE = 60 * 60 * 24 * 3;
	
	
	/**
	 * 장바구니가 존재하지 않는 사용자를 위한 빈 장바구니 정보를 생성한다.
	 * @return 빈 장바구니 정보
	 */
	private JSONObject makeEmptyCart() {
		JSONObject cart = new JSONObject();
		cart.put(JSON_PRODUCT_LIST, new JSONArray());
		return cart;
	}
	
	/**
	 * 레디스에 저장된 장바구니 정보를 조회하여 JSON 객체로 변환한다.
	 * @param userNo 유저번호
	 * @return 장바구니 정보가 저장된 JSONObject
	 */
	private JSONObject getCartInfo(String userNo) {
		String productInfo = redis.get(userNo + KEY_CART_LIST);
		if (null == productInfo || "".equals(productInfo)) {
			return makeEmptyCart();
		}
		try {
			JSONParser parser = new JSONParser();
			return (JSONObject) parser.parse(productInfo);
		} catch (Exception e) {
			return makeEmptyCart();
		}
	}
	
	/**
	 * 장바구니에 저장된 상품을 삭제한다.
	 * @param userNo 유저번호
	 * @return 삭제되 상품개수
	 */
	public int flushCart(String userNo) {
		this.cartInfo = getCartInfo(userNo);
		JSONArray products = (JSONArray) this.cartInfo.get(JSON_PRODUCT_LIST);
		for (int i=0; i < products.size(); i++) {
			redis.del(userNo + KEY_CART_PRODUCT + products.get(i));
		}
		redis.set(userNo + KEY_CART_LIST, "");
		return products.size();
	}
	
	/**
	 * 장바구니 상품 추가
	 * @param userNo
	 * @param productNo
	 * @param productName
	 * @param quantity
	 */
	public void addProduct(String userNo, String productNo, String productName, int quantity) {
		this.cartInfo = getCartInfo(userNo);
		JSONArray products = (JSONArray) this.cartInfo.get(JSON_PRODUCT_LIST);
		products.add(productNo);
		
		redis.set(userNo + KEY_CART_LIST, this.cartInfo.toJSONString());
		
		JSONObject product = new JSONObject();
		product.put("productNo", productNo);
		product.put("productName", productName);
		product.put("quantity", quantity);
		
		String productKey = userNo + KEY_CART_PRODUCT + productNo;
		redis.set(productKey, product.toJSONString());
	}

	public int deleteProduct(String userNo, String[] productNo) {
		this.cartInfo = getCartInfo(userNo);
		JSONArray products = (JSONArray) this.cartInfo.get(JSON_PRODUCT_LIST);
		int result = 0;
		
		for (String item : productNo) {
			products.remove(item);
			result += (redis.del(userNo + KEY_CART_PRODUCT + item) == true ? 1 : 0);
		}
		
		redis.set(userNo + KEY_CART_LIST, this.cartInfo.toJSONString());
		return result;
	}
	
	public JSONArray getProductList(String userNo) {
		this.cartInfo = getCartInfo(userNo);
		boolean isChanged = false;
		JSONArray products = (JSONArray) this.cartInfo.get(JSON_PRODUCT_LIST);
		JSONArray result = new JSONArray();
		String value = null;
		
		for (int i=0; i <products.size(); i++) {
			value = redis.get(userNo + KEY_CART_PRODUCT + products.get(i));
			
			if (value == null) {
				isChanged = true;
			}
			else {
				result.add(value);
			}
		}
		if (isChanged) {
			redis.set(userNo + KEY_CART_LIST, cartInfo.toJSONString());
		}
		
		return result;
	}
}

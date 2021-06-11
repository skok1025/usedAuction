package com.cafe24.auction.library;

public class libLog {
	
	/**
	 * 인스턴스 변수
	 */
	private static libLog instance;
	
	/**
	 * 클래스 외부에서 생성자를 통해 인스턴스 생성하지 못하도록 작업
	 */
	private libLog() {}
	
	/**
	 * 하나의 인스턴스 (싱글톤 객체) 만 리턴하는 함수
	 * @return 싱글톤 객체
	 */
	public static libLog getInstance() {
		if (instance == null) {
			instance = new libLog();
		} 
		
		return instance;
	}
	
	/**
	 * 파일 로그 작성
	 * @param key 로그키
	 * @param value 로그에 저장할 값
	 */
	public void write(String key, String value) {
		
	}

}

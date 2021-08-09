package com.cafe24.auction.service;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
public class SecurityServiceImpl implements SecurityService {
	public static final String secretKey = "4C8kum4LxyKWYLM78sKdXrzbBjDCFyfX";

	@Override 
	public String createToken(String subject, long ttlMillis) { 
		if (ttlMillis == 0) { 
			throw new RuntimeException("토큰 만료기간은 0 이상 이어야 합니다."); 	
		} 
		
		// HS256 방식으로 암호화 방식 설정 
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; 
		
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey); 
		
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName()); 
		JwtBuilder builder = Jwts.builder().setSubject(subject) // User를 구분할 수 있는 값. 
				.signWith(signatureAlgorithm, signingKey); 
		long nowMillis = System.currentTimeMillis(); 
		builder.setExpiration(new Date(nowMillis + ttlMillis)); 
		return builder.compact(); 
	} 
	
	@Override 
	public String getSubject(String token) { 
		Claims claims = Jwts.parser() 
				.setSigningKey(DatatypeConverter.parseBase64Binary(secretKey)) 
				.parseClaimsJws(token).getBody(); 
		return claims.getSubject(); 
		
	} 
	
	public String get(String token, String key) { 
		String value = Jwts.parser() 
				.setSigningKey((DatatypeConverter.parseBase64Binary(secretKey))) 
				.parseClaimsJwt(token) 
				.getBody() 
				.get(key, String.class); 
		return value; 
		
	} 
	
}

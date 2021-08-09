package com.cafe24.auction.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cafe24.auction.dto.SecuritySubjectDTO;
import com.cafe24.auction.service.SecurityService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired 
	private SecurityService securityService;

	@ResponseBody
	@PostMapping("/token") 
	public String generateToken(@RequestBody SecuritySubjectDTO securitySubjectDTO) { 
	    String userId = securitySubjectDTO.getUserId();
	    String userPassword = securitySubjectDTO.getUserPassword();
	    String subject = userId + "::" + userPassword;
	    
		String token = securityService.createToken(subject, 1000 * 60 * 60 * 2L); // 2시간 
		
		return token;
	}
	
	@GetMapping("/subject") 
	public String getSubject(@RequestHeader String AccessToken) { 
		String subject = securityService.getSubject(AccessToken); 
		return subject; 
	}


}

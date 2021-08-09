package com.cafe24.auction.controller.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired 
	private SecurityService securityService;

	@ResponseBody
	@PostMapping("/token") 
	public String generateToken(@RequestBody SecuritySubjectDTO securitySubjectDTO) throws JsonProcessingException { 
	    ObjectMapper mapper = new ObjectMapper();
	    String subject = mapper.writeValueAsString(securitySubjectDTO);
	    
		String token = securityService.createToken(subject, 1000 * 60 * 60 * 2L); // 2시간 
		
		return token;
	}
	
	@GetMapping("/subject") 
	public SecuritySubjectDTO getSubject(@RequestHeader String AccessToken) throws JsonParseException, JsonMappingException, IOException { 
		String subject = securityService.getSubject(AccessToken); 
		ObjectMapper mapper = new ObjectMapper();
		SecuritySubjectDTO dto = mapper.readValue(subject, SecuritySubjectDTO.class);
		
		return dto; 
	}


}

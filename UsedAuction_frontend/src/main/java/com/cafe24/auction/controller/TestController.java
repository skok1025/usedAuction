package com.cafe24.auction.controller;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/test")
public class TestController {

	@GetMapping("")
	public ModelAndView test() {
		ModelAndView modelAndView = new ModelAndView();
	     
        modelAndView.setViewName("home");
        
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Bamdule");
        map.put("date", LocalDateTime.now());
        
        modelAndView.addObject("data", map);
        
        return modelAndView;
	}
	
	@GetMapping("/template")
	public String template() {
		return "photo";
	}
	
}

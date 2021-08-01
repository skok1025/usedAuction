package com.cafe24.auction.controller;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cafe24.auction.library.libCart;
import com.cafe24.auction.library.libRedis;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired
	private libRedis redis;
	
	@Autowired
	private libCart cart;
	
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
	
	@GetMapping("/sample")
	public String sample(Model model) {
		model.addAttribute("name", "kim");
		
        return "sample/index";
	}
	
	@GetMapping("/template")
	public String template() {
		return "photo";
	}
	
	/**
	 * 페이지 조회 수 카운트 하는 컨트롤러
	 * cookie 를 이용하여 중복 카운트 방지.
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@GetMapping("/page/count")
	public String pageVisitCount(HttpServletRequest request, HttpServletResponse response) {
		
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		Cookie[] cookies = request.getCookies();
		String id = "skok1025";
		
		// 비교하기 위해 새로운 쿠키
        Cookie viewCookie = null;
        
        // 쿠키가 있을 경우 
        if (cookies != null && cookies.length > 0) 
        {
            for (int i = 0; i < cookies.length; i++)
            {
                // Cookie의 name이 cookie + reviewNo와 일치하는 쿠키를 viewCookie에 넣어줌 
                if (cookies[i].getName().equals("first_visit"))
                { 
                    System.out.println("처음 쿠키가 생성한 뒤 들어옴. (already visit)");
                    viewCookie = cookies[i];
                    break;
                }
            }
        }
        
        if (viewCookie == null) {    
            System.out.println("cookie 없음");
            // 쿠키 생성
            Cookie newCookie = new Cookie("first_visit", "val");
            response.addCookie(newCookie);
            
            redis.incr("test_page_count");
            
            // 특정 회원(id) 의 해당날짜 페이지 접근 횟수 
            redis.hincrBy(
            	"daily:click:" + id, 
            	today,
            	1
            );
            
        }
        
        String result = "전체 count : " + redis.get("test_page_count");
    	
        Set<String> dateList = redis.hkeys("daily:click:" + id);
        
        for (String date : dateList) {
        	String idDateCount = String.format("- %s 의 %s 페이지 접근 횟수 : %s", id, date, redis.hget("daily:click:" + id, date));
        	result += "<br>" + idDateCount;
		}
		
		return result;
	}
	
	@ResponseBody
	@GetMapping("/json/get")
	public String getJsonString() {
		JsonObject obj = new JsonObject();
		obj.addProperty("name", "kim");
		obj.addProperty("age", "29");
	
		return new Gson().toJson(obj);
	}
	
	@ResponseBody
	@GetMapping("/basket/add")
	public String getBasketAdd() {
		cart.addProduct("1", "235", "test product", 1);
		return cart.getProductList("1").toJSONString();
	}
}

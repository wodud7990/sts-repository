package kr.top2blue.boot3.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.top2blue.boot3.service.TestService;

@Controller
public class HomeController {

	@Autowired
	private TestService testService;
	
	@GetMapping(value = {"/","/index","/home"})
	public String index(Principal principal, Model model) {
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일(EEEE) hh:mm:ss");
		model.addAttribute("today", localDateTime.format(dateTimeFormatter));
		model.addAttribute("dbTime", testService.today());
		
		if(principal!= null)
			model.addAttribute("userName", principal.getName());
		else
			model.addAttribute("userName", "Guest(손님)");
		return "index";
	}
	
	@GetMapping(value = {"/hello"})
	public String hello(Principal principal, Model model) {
		model.addAttribute("userName",principal.getName());
		return "hello";
	}
	
	@GetMapping(value = {"/admin"})
	public String admin(Principal principal, Model model) {
		model.addAttribute("userName",principal.getName());
		return "admin";
	}

	@GetMapping(value = {"/login"})
	public String login(@RequestParam(required = false) String error,
						@RequestParam(required = false) String logout,
						Model model ) {
		if(error!=null) model.addAttribute("error","error");
		if(logout!=null) model.addAttribute("logout","logout");
		return "login";
	}

	// 첫번째 오류: 0으로 나눴으므로 500 오류를 발생시킨다.
	@GetMapping("/divide")
	public String problem(Model model) {
		int result = 1 / 0;
		model.addAttribute("serverTime", result);
		return "home";
	}

	// 두 번째 오류: @PostMapping이므로 Get 방식으로 요청 시 405 오류를 발생시킨다.
	@PostMapping("/method")
	public String method(Model model) {
		return "home";
	}	
}

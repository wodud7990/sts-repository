package kr.top2blue.boot3.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import kr.top2blue.boot3.service.EmployeeService;
import kr.top2blue.boot3.service.TestService;
import kr.top2blue.boot3.vo.Employee;

@Controller
public class HomeController {

	@Autowired
	private TestService testService;

	@Autowired
	private EmployeeService employeeService;

	
	//@Autowired
	//private DataSource dataSource;
	
	
	@GetMapping(value = {"/","/index","/home"})
	public String index(Principal principal, Model model) {
		/*
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			pstmt = connection.prepareStatement("update users set password=? where username=?");
			pstmt.setString(1, encoder.encode("123456"));
			pstmt.setString(2, "admin");
			pstmt.executeUpdate();
			pstmt.setString(1, encoder.encode("123456"));
			pstmt.setString(2, "user");
			pstmt.executeUpdate();
			connection.commit();
		}catch (Exception e) {
			try {
				connection.rollback();
			}catch (Exception e1) {
				;
			}
		} finally {
			try {
				pstmt.close();
				connection.close();
			}catch (Exception e1) {
				;
			}
		}
		*/
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


	@GetMapping("/welcome")
	public ModelAndView firstPage() {
		return new ModelAndView("welcome");
	}

	@GetMapping(value = "/addNewEmployee")
	public ModelAndView show() {
		return new ModelAndView("addEmployee", "emp", new Employee());
	}

	@PostMapping(value = "/addNewEmployee")
	public ModelAndView processRequest(@ModelAttribute("emp") Employee emp) {

		employeeService.insertEmployee(emp);
		List<Employee> employees = employeeService.getAllEmployees();
		ModelAndView model = new ModelAndView("getEmployees");
		model.addObject("employees", employees);
		return model;
	}

	@GetMapping("/getEmployees")
	public ModelAndView getEmployees() {
		List<Employee> employees = employeeService.getAllEmployees();
		ModelAndView model = new ModelAndView("getEmployees");
		model.addObject("employees", employees);
		return model;
	}

	@GetMapping(value = "/login")
	public String login(@RequestParam(value = "error", required = false) String error,
						@RequestParam(value = "logout", required = false) String logout, Model model ) {
		if(error!=null) model.addAttribute("error","error");
		if(logout!=null) model.addAttribute("logout","logout");
		return "login";
	}
	
	@GetMapping(value = {"/accessDenied"})
	public String accessDenied() {
		return "accessDenied";
	}

	// 첫번째 오류: 0으로 나눴으므로 500 오류를 발생시킨다.
	@GetMapping("/div")
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

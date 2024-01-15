package kr.top2blue.boot3.controller;

import java.util.Date;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String error(HttpServletRequest request, Model model) {
        Object status = request.getAttribute("jakarta.servlet.error.status_code");

        if(status!=null) {
	        model.addAttribute("status", "상태:" + status);
	        model.addAttribute("path", request.getAttribute("jakarta.servlet.error.request_uri"));
	        model.addAttribute("timestamp", new Date());
	
	        Object exceptionObj = request.getAttribute("jakarta.servlet.error.exception");
	        if (exceptionObj != null) {
	            Throwable e = ((Exception) exceptionObj).getCause();
	            model.addAttribute("exception", e.getClass().getName());
	            model.addAttribute("message", e.getMessage());
	        }
	
	        if (status.equals(HttpStatus.NOT_FOUND.value())) {
	            return "/errors/404";
	        } else if (status.equals(405)) {
	            return "/errors/405";
	        } else if (status.equals(403)) {
	            return "/errors/accessDenied";
	        } else {
	            return "/errors/500";
	        }
        }else {
        	return "/errors/500";
        }
    }

}
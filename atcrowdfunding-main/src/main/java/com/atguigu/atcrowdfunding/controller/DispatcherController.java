package com.atguigu.atcrowdfunding.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DispatcherController {
	//跳转到首页中的方法
	@RequestMapping(value= {"/" , "/index.html" , "/index" , "/default.html"})
	public String toIndexPage() {
		return "index";
	}
	//跳转到登录页面的方法
	@RequestMapping("/login.html")
	public String toLoginPage() {
		return "login";
	}
	//处理访问无权限页面跳转的请求
	@RequestMapping("/error.html")
	public String toErrorPage() {
		return "error/403";
	}
	
}

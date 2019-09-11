package com.atguigu.scw.ui.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.atguigu.scw.common.bean.TMember;

@Controller
public class ThmeleafController {
	
	
	//跳转到首页的方法
	//@GetMapping("/index")
	public String toIndexPage(HttpServletRequest request , HttpSession session) {
		request.setAttribute("reqKey", "reqVal");
		//返回的字符串 会交给Thmeleaf的视图解析器，拼接前缀后缀
		ServletContext application = request.getServletContext();
		session.setAttribute("sessionKey", "sessionVal");
		application.setAttribute("appKey", "appVal");
		
		
		List<TMember> list = new ArrayList<TMember>();
		list.add(new TMember(1, "11", "111", "安妮", "xasx", null, null, null, null, null));
		list.add(new TMember(2, "12", "111", "环环", "xasx", null, null, null, null, null));
		list.add(new TMember(3, "13", "111", null, "xasx", null, null, null, null, null));
		list.add(new TMember(4, "14", "111", "柴柴", "xasx", null, null, null, null, null));
		list.add(new TMember(5, "15", "111", "婷姐", null, null, null, null, null, null));
		request.setAttribute("list", list);
		
		return "index";
	}
}

package com.atguigu.scw.ui.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.atguigu.scw.common.bean.AppResonse;
import com.atguigu.scw.ui.feign.ProjectFeign;
import com.atguigu.scw.ui.vo.response.ProjectVO;

@Controller
public class DispatcherController {
	@Autowired
	ProjectFeign projectFeign;
	@GetMapping(value= {"/index" , "/" , "/index.html"})
	public String index(HttpServletRequest request) {
		System.out.println(".....");
		//查询项目列表
		AppResonse<List<ProjectVO>> all = projectFeign.all();
		//共享到域中
		request.setAttribute("all", all.getData());
		//首页需要显示项目列表
		return "index";
	}
	/*@GetMapping("/login.html")
	public String toLoginPage() {
		//没有任何业务逻辑 
		//如果不需要处理业务逻辑：springmvc中提供了 mvc:view-controller标签  给视图映射一个访问路径不需要提供方法
		//springboot中 也提供了配置类 可以编写一样的功能
		return "login";
	}*/
	
	
}

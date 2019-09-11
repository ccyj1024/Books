package com.atguigu.scw.ui.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.atguigu.scw.common.bean.AppResonse;
import com.atguigu.scw.ui.feign.ProjectFeign;
import com.atguigu.scw.ui.vo.response.ProjectDetailsVO;

@Controller
public class ProjectController {

	@Autowired
	ProjectFeign projectFeign;
	
	
	//查询项目详情的方法
	@GetMapping("/project/{projectid}")
	public String getProjectDetails(HttpServletRequest request , @PathVariable("projectid")Integer projectid) {
		//远程调用project服务查询id对应的项目详情
		AppResonse<ProjectDetailsVO> appResonse = projectFeign.detailsInfo(projectid);
		//设置到域中共享
		request.setAttribute("project", appResonse.getData());
		
		//跳转到project页面显示项目详情
		return "project/project";
	}
	
}

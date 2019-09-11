package com.atguigu.atcrowdfunding.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcorwdfunding.consts.AppConsts;
import com.atguigu.atcrowdfunding.bean.TRole;
import com.atguigu.atcrowdfunding.service.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

//处理角色操作相关的Controller
@Controller
public class TRoleController {
	
	@Autowired
	RoleService roleService;
	//处理批量删除的请求
	@ResponseBody
	@RequestMapping("/role/deleteRoles")
	public String deleteRoles(@RequestParam("ids")List<Integer> ids) {// 如果使用集合接受多个参数值  必须通过@RequestParam指定要绑定的name属性值
		roleService.deleteRoles(ids);
		return "ok";
	}
	
	
	
	
	//处理更新角色的请求
	@ResponseBody
	@RequestMapping("/role/updateRole")
	public String updateRole(TRole role) {
		roleService.updateRole(role);
		return "ok";
	}
	
	
	
	
	//根据id查询指定角色
	@ResponseBody
	@RequestMapping("/role/getRole")
	public TRole getRole(Integer id) {
		TRole role = roleService.getRole(id);
		return role;
	}
	
	
	//保存角色的方法
	@ResponseBody
	@PostMapping("/role/saveRole")
	public String saveRole(TRole role) {
		roleService.saveRole(role);
		return "ok";
	}
	
	
	
	
	
	//处理删除角色请求的方法
	@ResponseBody
	@PostMapping("/role/deleteRole")
	public String deleteRole(Integer id) {
		roleService.deleteRole(id);
		return "ok";
	}
	
	
	
	//处理查询所有角色的json字符串ajax请求的方法
	@PreAuthorize("hasRole('MANAGER')")
	@ResponseBody  //springmvc会自动使用JACKSON将返回值处理为json字符串响应给ajax请求
	@RequestMapping("/role/listRoles")
	public PageInfo<TRole> listRoles(@RequestParam(required=false , defaultValue="") String condition,@RequestParam(required=false , defaultValue="1")Integer pageNum) {
		//分页查询
		PageHelper.startPage(pageNum, AppConsts.PAGE_SIZE);
		List<TRole> roles = roleService.listRoles(condition);
		//获取详细的分页信息
		PageInfo<TRole> pageInfo = new PageInfo<TRole>(roles, AppConsts.PAGE_NAV_NUM);
		return pageInfo;
	}
	//跳转到角色维护首页的页面
	@GetMapping("/role/index.html")  //等价于  RequestMapping ， 设置了请求方式为get
	public String toRoleIndexPage() {
		return "role/role";
	}
}

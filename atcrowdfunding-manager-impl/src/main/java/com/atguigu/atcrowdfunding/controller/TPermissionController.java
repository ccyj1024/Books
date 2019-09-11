package com.atguigu.atcrowdfunding.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.bean.TPermission;
import com.atguigu.atcrowdfunding.service.TPermissionService;

@Controller
public class TPermissionController {

	@Autowired
	TPermissionService permissionService;
	
	@ResponseBody
	@RequestMapping("/permission/listPermissions")
	public List<TPermission> listPermissions(){
		List<TPermission> permissions = permissionService.listPermissions();
		return permissions;
	}
	@ResponseBody
	@RequestMapping("/permission/getPermissionIdsByRoleId")
	public List<Integer> getPermissionIdsByRoleId(Integer roleId){
		List<Integer> ids = permissionService.getPermissionIdsByRoleId(roleId);
		return ids;
	}
	//分配权限给角色的方法
	@ResponseBody
	@RequestMapping("/permission/assignPermissionsToRole")
	public String assignPermissionsToRole(Integer roleId , Integer[] pids){
		//删除roleId角色拥有的所有权限
		permissionService.deletePermissionsByRoleId(roleId);
		// 再将页面中勾选的权限分配给角色
		permissionService.assignPermissionsToRole(roleId , pids);
		return "ok";
	}
	
	
}

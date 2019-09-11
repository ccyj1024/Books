package com.atguigu.atcrowdfunding.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.atcrowdfunding.bean.TPermission;
import com.atguigu.atcrowdfunding.bean.TRolePermissionExample;
import com.atguigu.atcrowdfunding.mapper.TPermissionMapper;
import com.atguigu.atcrowdfunding.mapper.TRolePermissionMapper;
import com.atguigu.atcrowdfunding.service.TPermissionService;
@Service
public class TPermissionServiceImpl implements TPermissionService {
	
	@Autowired
	TPermissionMapper permissionMapper;
	@Autowired
	TRolePermissionMapper rolePermissionMapper;
	@Override
	public List<TPermission> listPermissions() {
		return permissionMapper.selectByExample(null);
	}

	@Override
	public List<Integer> getPermissionIdsByRoleId(Integer roleId) {
		List<Integer> ids = permissionMapper.selectPermissionIdsByRoleId(roleId);
		return ids;
	}

	@Override
	public void assignPermissionsToRole(Integer roleId, Integer[] pids) {
		//保存数据到t_role_permission表中
		permissionMapper.insertRoleAndPermissions(roleId, pids); //insert into t_role_permission(roleid , permissionid) values(1,1) , (1,2)...
	}

	@Override
	public void deletePermissionsByRoleId(Integer roleId) {
		TRolePermissionExample e = new TRolePermissionExample();
		e.createCriteria().andRoleidEqualTo(roleId);
		//删除t_role_permission表
		rolePermissionMapper.deleteByExample(e);
	}

}

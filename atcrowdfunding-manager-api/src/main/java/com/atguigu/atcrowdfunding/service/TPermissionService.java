package com.atguigu.atcrowdfunding.service;

import java.util.List;

import com.atguigu.atcrowdfunding.bean.TPermission;

public interface TPermissionService {

	List<TPermission> listPermissions();

	List<Integer> getPermissionIdsByRoleId(Integer roleId);

	void assignPermissionsToRole(Integer roleId, Integer[] pids);

	void deletePermissionsByRoleId(Integer roleId);

}

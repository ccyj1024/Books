package com.atguigu.atcrowdfunding.service;

import java.util.List;

import com.atguigu.atcrowdfunding.bean.TRole;

public interface RoleService {

	List<TRole> listRoles(String condition);

	void deleteRole(Integer id);

	void saveRole(TRole role);

	TRole getRole(Integer id);

	void updateRole(TRole role);

	void deleteRoles(List<Integer> ids);

}

package com.atguigu.atcrowdfunding.service;

import java.util.List;

import com.atguigu.atcrowdfunding.bean.TRole;

public interface TRoleService {

	List<TRole> listRoles();

	List<Integer> getRoleIdsByAdminId(Integer adminId);

}

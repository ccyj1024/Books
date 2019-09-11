package com.atguigu.atcrowdfunding.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.atcrowdfunding.bean.TRole;
import com.atguigu.atcrowdfunding.mapper.TAdminRoleMapper;
import com.atguigu.atcrowdfunding.mapper.TRoleMapper;
import com.atguigu.atcrowdfunding.service.TRoleService;
@Service
public class TRoleServiceImpl implements TRoleService {
	@Autowired
	TRoleMapper roleMapper;
	@Autowired
	TAdminRoleMapper adminRoleMapper;
	
	
	@Override
	public List<TRole> listRoles() {
		return roleMapper.selectByExample(null);
	}
	//查询 指定的admin拥有的角色id的集合
	@Override
	public List<Integer> getRoleIdsByAdminId(Integer adminId) {
		List<Integer> list = adminRoleMapper.selectRoleIdsByAdminId(adminId);
		return list;
	}

}

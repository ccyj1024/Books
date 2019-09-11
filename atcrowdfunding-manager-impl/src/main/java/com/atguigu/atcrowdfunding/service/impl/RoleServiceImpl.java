package com.atguigu.atcrowdfunding.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.atguigu.atcrowdfunding.bean.TRole;
import com.atguigu.atcrowdfunding.bean.TRoleExample;
import com.atguigu.atcrowdfunding.mapper.TRoleMapper;
import com.atguigu.atcrowdfunding.service.RoleService;
@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	TRoleMapper roleMapper;
	
	
	@Override
	public List<TRole> listRoles(String condition) {
		
		TRoleExample e = null;
		if(!StringUtils.isEmpty(condition)) {
			//条件不为空，封装查询时需要使用的example条件
			e = new TRoleExample();
			e.createCriteria().andNameLike("%"+condition+"%");
		}
		
		List<TRole> list = roleMapper.selectByExample(e);
		return list;
	}


	@Override
	public void deleteRole(Integer id) {
		roleMapper.deleteByPrimaryKey(id);
	}


	@Override
	public void saveRole(TRole role) {
		roleMapper.insertSelective(role);
	}


	@Override
	public TRole getRole(Integer id) {
		TRole role = roleMapper.selectByPrimaryKey(id);
		return role;
	}


	@Override
	public void updateRole(TRole role) {
		roleMapper.updateByPrimaryKeySelective(role);
	}


	@Override
	public void deleteRoles(List<Integer> ids) {
		TRoleExample e = new TRoleExample();
		e.createCriteria().andIdIn(ids);
		roleMapper.deleteByExample(e );
	}

}

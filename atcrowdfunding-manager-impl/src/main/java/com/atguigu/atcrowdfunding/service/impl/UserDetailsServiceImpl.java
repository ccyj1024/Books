package com.atguigu.atcrowdfunding.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.atguigu.atcrowdfunding.bean.TAdmin;
import com.atguigu.atcrowdfunding.bean.TAdminExample;
import com.atguigu.atcrowdfunding.bean.TAdminUserDetails;
import com.atguigu.atcrowdfunding.mapper.TAdminMapper;
import com.atguigu.atcrowdfunding.mapper.TPermissionMapper;
import com.atguigu.atcrowdfunding.mapper.TRoleMapper;
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
	TAdminMapper adminMapper;
	@Autowired
	TRoleMapper roleMapper;
	@Autowired
	TPermissionMapper permissionMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		TAdminExample e = new TAdminExample();
		//1、根据登录提交的账号查询数据库中该用户的信息
		e.createCriteria().andLoginacctEqualTo(username);
		List<TAdmin> list = adminMapper.selectByExample(e);
		if(CollectionUtils.isEmpty(list) || list.size()>1) {
			return null; //查询失败
		}
		//2、查询该用户的角色和权限 名称 集合
		TAdmin admin = list.get(0);
		List<String> roleNames = roleMapper.selectRoleNamesByAdminId(admin.getId());
		List<String> permissionNames =permissionMapper.selectPermissionNamesByAdminId(admin.getId());
		//3、将角色和权限名称转为  SpringSecurity权限集合
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (String role : roleNames) {//遍历所有的角色集合，转为角色权限
			authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
		}
		for (String permission : permissionNames) {//遍历所有的权限集合，转为权限
			authorities.add(new SimpleGrantedAuthority(permission));
		}
		System.out.println("权限集合："+authorities);
		//4、将登录成功的账号密码+权限集合 封装为UserDetails对象
		TAdminUserDetails userDetails = new TAdminUserDetails(admin, authorities);
		System.out.println("登录并设置了权限的用户对象："+userDetails);
		
		//User是UserDtails的实现类
		return userDetails;
	}

}

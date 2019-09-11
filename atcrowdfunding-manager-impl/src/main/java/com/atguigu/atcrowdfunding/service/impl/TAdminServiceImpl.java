package com.atguigu.atcrowdfunding.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.atguigu.atcorwdfunding.exception.AdminException;
import com.atguigu.atcorwdfunding.utils.MD5Util;
import com.atguigu.atcrowdfunding.bean.TAdmin;
import com.atguigu.atcrowdfunding.bean.TAdminExample;
import com.atguigu.atcrowdfunding.bean.TAdminExample.Criteria;
import com.atguigu.atcrowdfunding.bean.TAdminRoleExample;
import com.atguigu.atcrowdfunding.mapper.TAdminMapper;
import com.atguigu.atcrowdfunding.mapper.TAdminRoleMapper;
import com.atguigu.atcrowdfunding.service.TAdminService;
@Service
public class TAdminServiceImpl implements TAdminService {

	@Autowired
	TAdminMapper adminMapper;
	@Autowired
	TAdminRoleMapper adminRoleMapper;
	
	@Override
	public TAdmin doLogin(TAdmin loginAdmin) {
		//将用户登录的密码加密处理
		loginAdmin.setUserpswd(MD5Util.digest(loginAdmin.getUserpswd()));
		TAdminExample e = new TAdminExample();
		e.createCriteria().andLoginacctEqualTo(loginAdmin.getLoginacct())
							.andUserpswdEqualTo(loginAdmin.getUserpswd());
		//调用mapper查询 账号和密码对应的用户
		List<TAdmin> list = adminMapper.selectByExample(e);
		if(list==null || list.size()!=1) {
			//登录失败
			return null;
		}
		//为了安全性，登录成功的用户  一般不保存密码
		list.get(0).setUserpswd("密码已被加密");
		return list.get(0);//返回查找到的管理员用户对象
		
	}
	@Override
	public List<TAdmin> listAdmins(String condition) {
		List<TAdmin> list = null;
		TAdminExample e = null;
		if(!StringUtils.isEmpty(condition)) {//如果没有条件，还是查询所有
			//如果有条件，按照条件查询
			e = new TAdminExample();
			Criteria c1 = e.createCriteria();//账号 或者  名称 或者邮箱地址  任意一个包含条件  都可以查询
			c1.andLoginacctLike("%"+condition+"%");//默认条件
			Criteria c2 = e.createCriteria();
			c2.andUsernameLike("%"+condition+"%");
			Criteria c3 = e.createCriteria();
			c3.andEmailLike("%"+condition+"%");
			e.or(c2);
			e.or(c3);
		}
		list = adminMapper.selectByExample(e);
		
		return list;
	}
	@Autowired
	PasswordEncoder passwordEncoder;	
	@Override
	public void saveAdmin(TAdmin admin) {
		TAdminExample e = new TAdminExample();
		Criteria c1 = e.createCriteria();
		c1.andLoginacctEqualTo(admin.getLoginacct());
		Criteria c2 = e.createCriteria();
		c2.andEmailEqualTo(admin.getEmail());
		e.or(c2);
		//账号  邮箱地址不能重复
		long count = adminMapper.countByExample(e);
		if(count>0) {
			throw new AdminException("登录账号或邮箱地址必须唯一");
		}
		//密码通过MD5加密之后才能保存----
		//admin.setUserpswd(MD5Util.digest(admin.getUserpswd()));
		//使用了SpringSecurity之后，保存管理员  所有的密码都需要使用BCryptPasswordEncoder处理
		admin.setUserpswd(passwordEncoder.encode(admin.getUserpswd()));
		adminMapper.insertSelective(admin);
	}
	@Override
	public void deleteAdmin(Integer id) {
		adminMapper.deleteByPrimaryKey(id);
	}
	@Override
	public void deleteAdmins(Integer[] ids) {
		TAdminExample e = new TAdminExample();
		e.createCriteria().andIdIn(Arrays.asList(ids));
		adminMapper.deleteByExample(e);
	}
	@Override
	public TAdmin getAdmin(Integer id) {
		return adminMapper.selectByPrimaryKey(id);
	}
	@Override
	public void updateAdmin(TAdmin admin) {//修改之后的管理员
		//判断 管理员账号和邮箱是否修改过
		TAdmin oldAdmin = getAdmin(admin.getId());
		if(!oldAdmin.getLoginacct().equals(admin.getLoginacct())) {
			TAdminExample e = new TAdminExample();
			e.createCriteria().andLoginacctEqualTo(admin.getLoginacct());
			//如果账号修改了 ，判断是不是唯一的，如果不唯一，抛出异常
			long l = adminMapper.countByExample(e );
			if(l>0) {
				throw new AdminException("登录账号已存在!!!");
			}
			
		}
		if(!oldAdmin.getEmail().equals(admin.getEmail())) {
			TAdminExample e = new TAdminExample();
			e.createCriteria().andEmailEqualTo(admin.getEmail());
			//如果邮箱地址修改了 ，判断是不是唯一的，如果不唯一，抛出异常
			long l = adminMapper.countByExample(e );
			if(l>0) {
				throw new AdminException("邮箱已存在!!!");
			}
			
		}
		//账号邮箱可以使用
		//密码加密处理
		if(!StringUtils.isEmpty(admin.getUserpswd()))
		admin.setUserpswd(passwordEncoder.encode(admin.getUserpswd()));
		adminMapper.updateByPrimaryKeySelective(admin);
	}
	@Override
	public void saveRolesToAdmin(Integer adminId, List<Integer> roleIdList) {
		//给管理员分配角色本质就是将管理员id和 角色id绑定存到 t_admin_role表中
		// INSERET INTO t_admin_role(adminid , roleid) VALUES(2,1) , (2 , 2) , (2,4);
		adminRoleMapper.insertRolesToAdmin(adminId  , roleIdList);
	}
	@Override
	public void deleteRolesToAdmin(Integer adminId, List<Integer> roleIds) {
		TAdminRoleExample e = new TAdminRoleExample();
		// DELETE FROM t_admin_role where  adminid=2 and roleid in(1,2,3,4);
		e.createCriteria().andAdminidEqualTo(adminId).andRoleidIn(roleIds);
		adminRoleMapper.deleteByExample(e );
	}

}

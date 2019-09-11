package com.atguigu.atcrowdfunding.service;

import java.util.List;

import com.atguigu.atcrowdfunding.bean.TAdmin;

public interface TAdminService {

	TAdmin doLogin(TAdmin loginAdmin);

	List<TAdmin> listAdmins(String condition);

	void saveAdmin(TAdmin admin);

	void deleteAdmin(Integer id);

	void deleteAdmins(Integer[] ids);

	TAdmin getAdmin(Integer id);

	void updateAdmin(TAdmin admin);

	void saveRolesToAdmin(Integer adminId, List<Integer> roleIdList);

	void deleteRolesToAdmin(Integer adminId, List<Integer> roleIds);

}

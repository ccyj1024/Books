package com.atguigu.scw.user.service;

import java.util.List;

import com.atguigu.scw.common.bean.AppResonse;
import com.atguigu.scw.common.bean.TMember;
import com.atguigu.scw.user.vo.request.UserRequestVO;
import com.atguigu.scw.user.vo.response.UserAddressVo;
import com.atguigu.scw.user.vo.response.UserResponseVO;

public interface UserService {

	void saveUser(UserRequestVO vo);

	AppResonse<Object>  getUser(String loginacct, String userpswd);

	List<UserAddressVo> getUserAddresses(String accessToken);

	TMember getMemberById(Integer memberid);
	
}

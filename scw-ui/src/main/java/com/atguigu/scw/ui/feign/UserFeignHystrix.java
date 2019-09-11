package com.atguigu.scw.ui.feign;


import java.util.List;

import org.springframework.stereotype.Service;

import com.atguigu.scw.common.bean.AppResonse;
import com.atguigu.scw.ui.vo.response.UserAddressVo;
@Service
public class UserFeignHystrix implements UserFeign {

	@Override
	public AppResonse<Object> login(String loginacct, String userpswd) {
		return AppResonse.fail("服务器繁忙，请稍后再试!!");
	}

	@Override
	public AppResonse<List<UserAddressVo>> address(String accessToken) {
		return AppResonse.fail(null);
	}

}

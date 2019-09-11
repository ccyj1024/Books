package com.atguigu.scw.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.scw.common.bean.AppResonse;
import com.atguigu.scw.common.bean.TMember;
@FeignClient(value="SCW-USER")
public interface UserServiceFeign {

	@GetMapping("/user/info/memberInfo")
	public AppResonse<TMember> getMemberInfo(@RequestParam("id")Integer memberid);

}

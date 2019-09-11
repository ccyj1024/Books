package com.atguigu.scw.ui.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.scw.common.bean.AppResonse;
import com.atguigu.scw.ui.vo.response.UserAddressVo;
@FeignClient(value="SCW-USER" , fallback=UserFeignHystrix.class)
public interface UserFeign {
	/**
	 * 远程调用时   方法中必须给形参通过哦注解标注
	 * 	1、传递请求参数     @RequestParam("accessToken")
	 * 	2、通过路径传递参数   @PathVariable("pathVar")
	 * 	3、传递对象(JSON传递)  @RequestBody("xx")
	 * @param accessToken
	 * @return
	 */
	@GetMapping("/user/info/address")
	public AppResonse<List<UserAddressVo>> address(@RequestParam("accessToken")String accessToken);
	@PostMapping("/user/login")
	public AppResonse<Object> login(@RequestParam("loginacct")String loginacct , @RequestParam("userpswd")String userpswd);
}

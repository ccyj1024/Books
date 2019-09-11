package com.atguigu.scw.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.scw.common.bean.AppResonse;
import com.atguigu.scw.common.bean.TMember;
import com.atguigu.scw.user.service.UserService;
import com.atguigu.scw.user.vo.response.UserAddressVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;



@Api(tags = "用户个人信息模块")
@RestController
public class UserInfoController {
	
	@Autowired
	UserService userService;
	
	
	
	@ApiOperation(value="查询项目发起人信息") 
	@GetMapping("/user/info/memberInfo")
	public AppResonse<TMember> getMemberInfo(@RequestParam("id")Integer memberid){
		return AppResonse.ok(userService.getMemberById(memberid));
	} 
	
	@ApiOperation(value="获取用户收货地址") 
	@ApiImplicitParams(value={
			@ApiImplicitParam(value="访问令牌",name="accessToken",required=true)
	})
	@GetMapping("/user/info/address")
	public AppResonse<List<UserAddressVo>> address(@RequestParam("accessToken")String accessToken){
		//根据accessToken获取redis中存储的用户的id
		List<UserAddressVo> list = userService.getUserAddresses(accessToken);
		if(list==null ||list.size()==0) {
			return AppResonse.fail(null);
		}
//		if(list.size()==0) {
//			return AppResonse.ok("没有查询到地址信息!");
//		}
		//再根据用户id去数据库中查询自己的地址集合
		return AppResonse.ok(list);
	} 
	

}

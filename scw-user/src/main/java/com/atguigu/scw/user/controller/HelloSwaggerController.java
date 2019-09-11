package com.atguigu.scw.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.scw.user.bean.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@Api(tags="测试Swagger的controller") //给controller添加接口文档的内容
@RestController
public class HelloSwaggerController {

	@ApiOperation(value="hello方法")
	@ApiImplicitParams(    //描述请求参数
			value= {
				@ApiImplicitParam(name="username" , required=false , value="登录账号"),  //描述一个请求参数
				@ApiImplicitParam(name="password" , required=true , value="登录密码")
			}
		)
	@ApiResponses(
			value= {
					@ApiResponse(code=400 , message="Bad Request 请求参数问题"),
					@ApiResponse(code=10000 , message="用户查询失败")
			}
		)
	@GetMapping("/hello")
	public User hello(String username , String password) {
		return new User(1000, username, password);
	}
}

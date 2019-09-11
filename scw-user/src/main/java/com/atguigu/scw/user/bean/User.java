package com.atguigu.scw.user.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * 逆向工程只能将数据库中所有的表生成javabean
 * 但是开发中的需求较多，还会有其他的非常多的javabean(例如：reids和java交互的数据、浏览器和服务器请求传递的数据)需要我们手动编写
 * 		- lombok可以简化开发，通过注解让javabean自动生成 get/set方法、构造器、toString....
 *
 */
@Data   //get  set方法
@NoArgsConstructor  //生成无参构造器
@AllArgsConstructor //生成全部参数的构造器
@ToString //生成toSting方法
@ApiModel(value="用户类")   //给controller响应类型添加文档描述
public class User {
	@ApiModelProperty("主键id，用户的编号")
	private Integer id;
	@ApiModelProperty("登录账号")
	private String username;
	@ApiModelProperty("登录密码")
	private String password;
	public  void test() {
	}
}

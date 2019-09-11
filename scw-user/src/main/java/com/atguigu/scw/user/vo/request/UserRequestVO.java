package com.atguigu.scw.user.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequestVO {
	//账号(手机号码)、密码、验证码、邮箱地址
	private String loginacct;
	private String userpswd;
	private String code;
	private String email;
	
	
}

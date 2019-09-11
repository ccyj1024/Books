package com.atguigu.scw.user.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponseVO {
	private String token;
	private String loginacct;
	private String username;
	private String email;
}

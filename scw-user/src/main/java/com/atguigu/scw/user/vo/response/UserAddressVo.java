package com.atguigu.scw.user.vo.response;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserAddressVo implements Serializable{
	private Integer id;
	private String address;
}

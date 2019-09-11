package com.atguigu.scw.common.bean;

import lombok.Data;

//后台系统的多个服务中接受请求时，需要使用请求参数中的token识别用户的身份，所以每个VO都需要有token属性
@Data
public class BaseVO {
	String accessToken;
}

package com.atguigu.scw.common.bean;

import com.atguigu.scw.common.consts.AppConsts;

import lombok.Data;

@Data   //如果javabean没有get set方法  那么 jackson也不能将其转为json字符串
public class AppResonse<T> {

	private Integer code;//响应状态码：  200 代表成功 ， 10001 代表xxx错误  10002 ...
	private String msg; //响应信息：
	private T data; //响应数据
	
	public AppResonse() {
		super();
	}
	private AppResonse(Integer code, String msg, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	public static <T> AppResonse<T> ok(T data){
		return new AppResonse<T>(AppConsts.RESPONSE_SUCCESS_CODE, AppConsts.RESPONSE_SUCCESS_MSG, data);
	}
	public static <T> AppResonse<T> fail(T data){
		return new AppResonse<T>(AppConsts.RESPONSE_ERROR_CODE, AppConsts.RESPONSE_ERROR_MSG, data);
	}
	
}

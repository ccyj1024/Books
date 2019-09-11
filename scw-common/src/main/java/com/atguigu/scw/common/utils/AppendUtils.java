package com.atguigu.scw.common.utils;
//给字符串拼接前缀后缀的工具类
public class AppendUtils {

	
	public static String appendPhoneNumCodeKey(String phoneNum) {
		return "code:"+phoneNum+":expire";
	}
}

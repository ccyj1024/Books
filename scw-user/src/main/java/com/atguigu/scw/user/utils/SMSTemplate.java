package com.atguigu.scw.user.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;

import com.atguigu.scw.common.utils.HttpUtils;

/**
 * 发送短信的模板类
 *
 */
public class SMSTemplate {
	@Value("${sms.host}")
	String host;
	@Value("${sms.path}")
	String path;
	@Value("${sms.method}")
	String method;
	@Value("${sms.appcode}")
	String appcode;
	/**
	 * Map<String, String> querys = new HashMap<String, String>();
		querys.put("mobile", "13936795572");
		querys.put("param", "code:6666666");
		querys.put("tpl_id", "TP1711063");
	 */
	public boolean sendSms(Map<String, String> querys) {
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appcode);
		Map<String, String> bodys = new HashMap<String, String>();

		try {
			HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
			//System.out.println(response.toString());
			int code = response.getStatusLine().getStatusCode();
			return code==200;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}

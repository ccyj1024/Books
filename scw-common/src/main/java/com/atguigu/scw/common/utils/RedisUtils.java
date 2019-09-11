package com.atguigu.scw.common.utils;

import java.util.UUID;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;

public class RedisUtils {
	
	//根据key从redis中获取json字符串并还原为对应的对象的方法
	public static<T> T getJson2Bean(StringRedisTemplate stringRedisTemplate , String token ,Class<T> type) {
		String jsonStr = stringRedisTemplate.opsForValue().get(token);
		if(StringUtils.isEmpty(jsonStr)) {
			return null;//没有查询到数据
		}
		//将查询到数据转T类型的对象
		T t = JSON.parseObject(jsonStr, type);
		return t;
	}
	
	//将对象转为json字符串存到redis中的方法
	public static<T> String saveBean2Redis(T t , StringRedisTemplate stringRedisTemplate ) {
		//生成唯一的key：token
		String token = UUID.randomUUID().toString().replace("-", "");
		//将对象转为json字符串
		String tJsonStr = JSON.toJSONString(t);
		//保存到redis中
		stringRedisTemplate.opsForValue().set(token, tJsonStr);
		return token;
	}
	
}

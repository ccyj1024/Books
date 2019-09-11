package com.atguigu.scw.project.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.atguigu.scw.common.bean.AppResonse;
import com.atguigu.scw.common.bean.TMember;
import com.atguigu.scw.common.consts.AppConsts;
import com.atguigu.scw.common.utils.RedisUtils;
import com.atguigu.scw.project.bean.TReturn;
import com.atguigu.scw.project.service.ProjectService;
import com.atguigu.scw.project.vo.request.ProjectBaseInfoVO;
import com.atguigu.scw.project.vo.request.ProjectConfirmVO;
import com.atguigu.scw.project.vo.request.ProjectRedisStorageVO;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ProjectCreateController {
	@Autowired
	ProjectService projectService;
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	//处理第三步： 提交：将redis中的bigVo持久化保存到数据库中
	@PostMapping("/project/confirmProject")
	public AppResonse<Object> createProjectStep3(ProjectConfirmVO vo){
		TMember member = RedisUtils.getJson2Bean(stringRedisTemplate, vo.getAccessToken(), TMember.class);
		if(member==null) {
			return AppResonse.fail("发起众筹必须先登录!!!");
		}
		//持久化保存redis中的bigVo到数据库中: service+mapper
		projectService.createProject(vo);
		
		return AppResonse.ok("提交项目成功");
	}
	
	// ResponseBody:可以将响应德 对象转为json ， RequestBody：可以将请求的json转为java对象
	//*****第二步中保存回报时  可以添加多个回报，可以创建一个方法，接受每一个提交的回报信息到服务器保存到数据库中，方便以后增删改查
	//第二步提交数据中包含了多个回报的信息，使用List<回报>集合接受，前提：请求参数必须以json的方式提交，接受时必须使用@RequestBody 注解接受参数的形参
	@PostMapping("/project/createProjectStep2")
	public AppResonse<Object> createProjectStep2(@RequestBody List<TReturn> returns){
		if(CollectionUtils.isEmpty(returns)) {
			return AppResonse.fail("请上传正确的回报信息");
		}
		//判断登录状态
		TMember member = RedisUtils.getJson2Bean(stringRedisTemplate, returns.get(0).getAccessToken(), TMember.class);
		if(member==null) {
			return AppResonse.fail("发起众筹必须先登录!!!");
		}
		log.warn("接受的到请求参数Return集合: {}", returns);
		//接受请求参数：回报集合
		//获取redis中的bigVo
		ProjectRedisStorageVO bigVo = RedisUtils.getJson2Bean(stringRedisTemplate, returns.get(0).getProjectToken(), ProjectRedisStorageVO.class);
		//将回报集合设置给bigVo
		bigVo.setProjectReturns(returns);
		//将bigVo存到redis中
		String bigVoJsonStr = JSON.toJSONString(bigVo);
		stringRedisTemplate.opsForValue().set(bigVo.getProjectToken(), bigVoJsonStr);
		return AppResonse.ok(bigVoJsonStr);
	}
	
	//处理点击阅读同意协议的请求：初始化BigVo保存到redis中，并将bigVo和发起众筹的登录用户绑定
	@PostMapping("/project/init")
	public AppResonse<Object> initProject(String accessToken){//accessToken
		//判断登录状态
		TMember member = RedisUtils.getJson2Bean(stringRedisTemplate, accessToken, TMember.class);
		if(member==null) {
			return AppResonse.fail("发起众筹必须先登录!!!");
		}
		//创建BigVo对象
		ProjectRedisStorageVO bigVo = new ProjectRedisStorageVO();
		bigVo.setAccessToken(accessToken);
		bigVo.setMemberid(member.getId());
		//为bigVo创建一个唯一的token
		bigVo.setProjectToken(AppConsts.PROJECT_CREATE_TOKEN_PREFIX+UUID.randomUUID().toString().replace("-", ""));
		//将bigVo 暂存到redis中
		//RedisUtils.saveBean2Redis(bigVo, stringRedisTemplate);
		stringRedisTemplate.opsForValue().set(bigVo.getProjectToken(), JSON.toJSONString(bigVo));
		
		return AppResonse.ok(bigVo);
	}
	
	//处理第一步：收集项目及发起人信息的请求:将收集到的数据更新给reids中的bigvo
	//提供一个VO
	@PostMapping("/project/createProjectStep1")
	public AppResonse<Object> createProjectStep1(ProjectBaseInfoVO vo){
		//判断用户是否登录
		//判断登录状态
		TMember member = RedisUtils.getJson2Bean(stringRedisTemplate, vo.getAccessToken(), TMember.class);
		if(member==null) {
			return AppResonse.fail("登录超时，请重新登录!!!");
		}
		//获取redis中缓存的项目bigVo对象
		ProjectRedisStorageVO bigVo = RedisUtils.getJson2Bean(stringRedisTemplate, vo.getProjectToken(), ProjectRedisStorageVO.class);
		//将本次收集到的数据 拷贝到bigVo中:bean的属性名和值的类型如果一样，可以直接拷贝
		BeanUtils.copyProperties(vo, bigVo);
		log.debug("接受的到请求参数ProjectBaseInfoVO: {}", vo);
		log.debug("拷贝参数后的ProjectRedisStorageVO: {}", bigVo);
		//再将bigVo存到redis中替换之前的
		//RedisUtils.saveBean2Redis(bigVo, stringRedisTemplate)
		String bigVoJsonStr = JSON.toJSONString(bigVo);
		stringRedisTemplate.opsForValue().set(bigVo.getProjectToken(), bigVoJsonStr);
		
		return AppResonse.ok(bigVo);
		
	}
	//处理第二步：收集回报信息的请求:将收集到的数据更新给reids中的bigvo
	
	
	
}

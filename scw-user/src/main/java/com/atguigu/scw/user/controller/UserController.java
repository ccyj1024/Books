package com.atguigu.scw.user.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.scw.common.bean.AppResonse;
import com.atguigu.scw.common.utils.AppendUtils;
import com.atguigu.scw.user.service.UserService;
import com.atguigu.scw.user.utils.SMSTemplate;
import com.atguigu.scw.user.vo.request.UserRequestVO;
import com.atguigu.scw.user.vo.response.UserResponseVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Api(tags="处理用户注册、登录、个人信息查询、发送验证码")
@RestController
@Slf4j
public class UserController {
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	@Autowired
	SMSTemplate smsTemplate;
	@Autowired
	UserService userService;
	//登录
	@PostMapping("/user/login")
	public AppResonse<Object> login(@RequestParam("loginacct")String loginacct , @RequestParam("userpswd")String userpswd){ //loginacct userpswd
		//处理登录请求
		AppResonse<Object> response = userService.getUser(loginacct , userpswd);
		//4、将key作为token响应[以后ui项目再来访问user或订单或项目服务时  可以通过此token判断用户是否登录  记录用户的登录状态]
		return response;
	}
	
	
	
	
	
	
	
	
	
	//发送短信验证码
	// 如果手机号码已经存在对应的验证码，让用户等待[发送验证码之前根据手机号码拼接key去查询验证码是否存在]
	// 如果该用户手机号码24小时内已经申请了3次验证码 ， 列入黑名单[发送验证码之前 获取redis中存储的该手机号码获取验证码的次数，如果存在判断次数，如果不存在，则记录一次]
	@ApiOperation("发送短信验证码")
	@ApiImplicitParams(
			value= {
					@ApiImplicitParam(name="phoneNum" , value="手机号码" , required=true)
			})
	@PostMapping("/user/sendCode")
	public AppResonse<Object> sendCode(String phoneNum) {
		//===== 先判断该手机号码 时段内获取验证码的次数
		String getCodeCountKey = "code:"+phoneNum+":count";
		String countStr = stringRedisTemplate.opsForValue().get(getCodeCountKey);
		Integer count = 0;
		if(!StringUtils.isEmpty(countStr)) {
			count = Integer.parseInt(countStr);
			if(count>=3) {
				return AppResonse.fail("获取验证码次数超出范围");
			}
		}
		//======  在判断手机号码是否还存在未过期的验证码
		String codeKey = AppendUtils.appendPhoneNumCodeKey(phoneNum);
		//如果当前键 在reids中仍然存在  ，那么存在验证码
		Boolean hasKey = stringRedisTemplate.hasKey(codeKey);
		if(hasKey) {
			return AppResonse.fail("请不要频繁获取验证码!!!");
		}
		//封装map传递参数
		Map<String, String> querys = new HashMap<String, String>();
		//发送短信验证码逻辑
		//1、接受发送验证码请求的手机号参数
		querys.put("mobile", phoneNum);
		//2、生成验证码[用户提交注册请求时还需要验证验证码是否正确、验证码有效期15分钟]
		String code = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
		querys.put("param", "code:"+code);
		querys.put("tpl_id", "TP1711063");
		//3、调用短信平台给用户发送验证码
		boolean b = smsTemplate.sendSms(querys);
		if(b) {
			count++;
			//4、将短信验证码在服务器中保存15分钟:通过redis缓存
			//4.1 拼接每个手机号存储验证码的key
			//4.2 存储验证码到redis中
			stringRedisTemplate.opsForValue().set(codeKey, code, 15, TimeUnit.MINUTES);
			//4.3 更新手机号码获取验证码的次数
			Long expire = stringRedisTemplate.getExpire(getCodeCountKey , TimeUnit.HOURS);
			if(expire==null || expire<0) {
				expire = 24L;
			}
			stringRedisTemplate.opsForValue().set(getCodeCountKey, count+"", expire, TimeUnit.HOURS);
			return AppResonse.ok("验证码发送成功");
		}
		return AppResonse.fail("失败!!!");
		
	}
	
	//注册
	@PostMapping("/user/regist")
	public AppResonse<String> doRegist(UserRequestVO vo) {//处理注册请求：账号(手机号码)、密码、验证码、邮箱地址
		//---------------------- 逆向工程将 user项目需要的javabean和mapper生成、
		//为了接受用户提交的注册参数，可以创建VO用来接受
		//1、接受请求参数
		//2、检查验证码是否正确
		if(StringUtils.isEmpty(vo.getCode())) {
			return AppResonse.fail("验证码不能为空");
		}
		String phoneNum = vo.getLoginacct();
		//获取redis中的验证码
		String redisCode = stringRedisTemplate.opsForValue().get(AppendUtils.appendPhoneNumCodeKey(phoneNum));
		
		log.info("注册：请求参数验证码-{} , redis存储验证码-{}", vo.getCode() , redisCode);
		if(StringUtils.isEmpty(redisCode) || !redisCode.equals(vo.getCode())) {
			return  AppResonse.fail("验证码错误，请重新获取");
		}
		
		//3、如果验证码正确，处理注册请求
		//将用户信息存到数据库中,删除redis中的验证码
		try {
			userService.saveUser(vo);
		} catch (Exception e) {
			e.printStackTrace();
			return AppResonse.fail(e.getMessage());
		}
		
		stringRedisTemplate.delete(AppendUtils.appendPhoneNumCodeKey(phoneNum));
		
		//4、响应注册成功或失败消息
		return AppResonse.ok("注册成功");
	}
	//为了方便其他调用当前项目的请求分辨请求成功与否，服务器中可以约束响应的数据格式：{code:200 , msg:"响应状态信息"  , data:{"响应给调用者的数据"} }
}

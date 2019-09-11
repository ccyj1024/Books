package com.atguigu.scw.ui.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.atguigu.scw.common.bean.AppResonse;
import com.atguigu.scw.ui.feign.UserFeign;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {
/**
 * 使用openfeign远程调用：
 * 	1、创建feignclient 接口：
 * 			定义和要远程调用的controller的方法一样的抽象方法
 * 	2、在接口上使用@FeignClient标注 需要远程绑定的项目在注册中心的名称
 * 	3、只要主程序上使用了@EnableFeignClients注解，会自动扫描@FeignClient并注入到容器中可以通过自动装配的方式使用
 * 
 * 注意：
 * 		如果远程调用时传递了多个参数，必须使用@RequestParam标注参数绑定关系
 * @param loginacct
 * @param userpswd
 * @return
 */
	@Autowired
	UserFeign userFeign;
	@PostMapping("/user/login")
	public String doLogin(HttpServletRequest  request , HttpSession session,String loginacct , String userpswd) {
		//用户提交登录请求给scw-ui前台项目-- 如果需要处理用户请求，远程调用scw-user项目处理
		//如果登录成功，当前项目只需要保存返回的accessToken即可
		AppResonse<Object> resonse = userFeign.login(loginacct, userpswd);
		System.out.println(resonse.getCode()+" , "+resonse.getMsg()+", " +resonse.getData());
		if(resonse.getCode()==200) {
			//登录成功后的跳转有两个情况：1、如果不是去结算跳转过来让登录的，那么可以直接跳转到首页 ， 2、如果是去结算跳转过来的，登录成功应该跳转到结算页面
			String path = (String) session.getAttribute("path");
			session.setAttribute("user", resonse.getData());//{token=715eefd2db1046b6b84caeddc7895774, loginacct=17326324589, username=17316324589, email=an@atguigu.com}
			if(path==null) {
				//重定向到登录成功页面
				//将成功的信息设置到session域中，页面中可以获取显示
				log.debug("登录成功返回的用户信息:{}", resonse.getData());
				return "redirect:/index";
			}else {
				//移除session中已经使用过的路径
				session.removeAttribute("path");
				return "redirect:"+path;
			}
		}
		//设置错误消息到request域中
		Object result = resonse.getData();
		if(result==null) {
			request.setAttribute("errorMsg", "远程调用失败");
		}else {
			request.setAttribute("errorMsg",result);
			
		}
		log.debug("登录失败的错误信息:{}", result);
		//转发到登录页面让用户继续登录
		return "login";
	}
	
}

package com.atguigu.atcrowdfunding.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.StringUtils;
@Configuration //配置类注解
@EnableWebSecurity //启用web权限控制
@EnableGlobalMethodSecurity(prePostEnabled=true) //细粒度的权限控制注解
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter{
	/**
	 * SpringSecurity是 web权限框架，处理web相关的验证的
	 * 	，设计到web 组件相关内容，如果使用 spring容器扫描装配，不能用来管理springmvc的资源的访问权限
	 * 
	 * 暂时将Config类放在  main工程中
	 */
	//web相关的配置： 表单配置、注销配置 、 资源权限配置
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//首页和静态资源 认证
		http.authorizeRequests() //开始资源认证
			.antMatchers("/static/**","/" , "/index.html" , "/index" , "/default.html").permitAll() //首页和静态资源授权所有人都可以访问
			.anyRequest().authenticated(); //其他的所有请求都需要认证[登录成功无论拥有什么权限都可以访问]
		//登录表单配置
		http.formLogin()
			.usernameParameter("loginacct")// 登录表单提交的账号name属性值
			.passwordParameter("userpswd")//登录表单提交的密码的name属性值
			.loginPage("/login.html").permitAll() //自定义登录页面  所有人都可以访问
			.loginProcessingUrl("/login")  //自定义处理登录请求的url地址[ 登录请求会自动交给SpringSecurity处理]，需要修改登录表单提交路径，之前的处理登录请求的方法作废了
			.defaultSuccessUrl("/main.html"); 
		//注销路径配置  
		http.logout()
			.logoutUrl("/logout") //注销url 
			.logoutSuccessUrl("/index"); //注销成功跳转的页面
		
		//禁用csrf：
		http.csrf().disable();
		
		//记住我功能
		//http.rememberMe();
		//登录成功后访问未授权页面处理器
		http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
			
			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException accessDeniedException) throws IOException, ServletException {
				//如果是ajax请求，请求头中会有X-Requested-With: XMLHttpRequest
				String header = request.getHeader("X-Requested-With");
				if(!StringUtils.isEmpty(header)&& header.equals("XMLHttpRequest") ) {
					//异步请求被权限管理了
					//异步操作 ：响应结果由js代码解析，响应异常页面没有意义 ，ajax请求可以根据响应的字符串或者状态码判断请求成功还是失败
					//如何判断请求是同步的还是异步的，响应异常字符串(需要设置到响应体中)给ajax请求
					response.getWriter().write("unauth");
				}else {
					//同步访问：可以直接响应一个异常页面给浏览器  响应给用户查看
					//将异常信息存到域中共享
					request.setAttribute("errorMsg", accessDeniedException.getMessage());
					//转发到未授权页面给用户提示
					request.getRequestDispatcher("/error.html").forward(request, response);
				}
				
			}
		});
		
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Autowired
	UserDetailsService userDetaisService;
	@Autowired
	PasswordEncoder passwordEncoder;
	//认证配置：  用户登录操作、密码加密验证方式
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//userDetailsService:从数据库中查询用户并绑定该用户权限的业务类
		//自己创建userDetailsService实现类 并实现具体查询数据绑定权限的方法
		//passwordEncoder: 判断提交的登录密码和 数据库中查询到的用户密码是否一样的工具类
		//passwordEncoder 一般使用 BCryptPasswordEncoder  ， 也可以自定义MD5PasswordEncoder
		auth.userDetailsService(userDetaisService).passwordEncoder(passwordEncoder);
	}
	
}

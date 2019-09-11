package com.atguigu.scw.ui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//只要写一个配置类，实现了WebMvcConfigurer接口，就可以定制SpringMVC里面的细节功能
@Configuration
public class AppWebMvcConfig implements WebMvcConfigurer { // JDK1.8接口支持默认的空方法，可以不用实现接口所有方法
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login.html").setViewName("login");// mvc:view-controller
		// registry.addViewController("/index.html").setViewName("index");
	}
}

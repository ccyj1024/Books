package com.atguigu.scw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import feign.Retryer;
@EnableEurekaClient
@EnableFeignClients
@EnableHystrix
@EnableHystrixDashboard
@SpringBootApplication
public class ScwUiApplication {

	@Bean
	@ConditionalOnMissingBean
	public Retryer feignRetryer() {
		return Retryer.NEVER_RETRY;// feign在远程调用时就不会进行重试  ， feign如果不设置重试机制 默认会连续5次进行重试
	}

	
	public static void main(String[] args) {
		SpringApplication.run(ScwUiApplication.class, args);
	}

}

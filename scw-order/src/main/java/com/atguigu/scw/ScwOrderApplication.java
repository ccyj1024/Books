package com.atguigu.scw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableDiscoveryClient
@EnableHystrix
@EnableFeignClients
@EnableHystrixDashboard
@SpringBootApplication
@MapperScan("com.atguigu.scw.order.mapper") //指定需要扫描的mapper接口所在的包名
public class ScwOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScwOrderApplication.class, args);
	}

}

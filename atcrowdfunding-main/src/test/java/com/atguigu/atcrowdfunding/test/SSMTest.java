package com.atguigu.atcrowdfunding.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


//junit 不能直接测试  ssm的环境，因为没有办法使用容器
// spring框架提供了测试包，可以在junit中去加载容器使用容器环境
//1、使用Spring单元测试来驱动普通的测试方法
@RunWith(value=SpringJUnit4ClassRunner.class)
//2、加载Spring配置文件
@ContextConfiguration(locations={"classpath:spring/spring-bean.xml",
"classpath:spring/spring-mybatis.xml","classpath:spring/spring-tx.xml"})
public class SSMTest {
//	@Autowired
//	TAdminMapper mapper;
//	//Logger logger = Logger.getLogger(getClass());
//	Logger logger = LoggerFactory.getLogger(getClass());
	@Test
	public void test() {
		//slf4j支持带占位符{} 的  日志输出 
//		logger.debug("正在测试：{}", System.currentTimeMillis());
////		long startTime = System.currentTimeMillis();
////		logger.debug("正在执行测试方法，起始时间："+startTime);
//		TAdminExample example = new TAdminExample();
//		example.createCriteria().andLoginacctEqualTo("zhangsan").andUserpswdEqualTo("123456");
//		
//		logger.info("正在执行测试方法，即将开始查询：{}" , System.currentTimeMillis());
//		List<TAdmin> list = mapper.selectByExample(example );
//		
//		logger.error("正在执行测试方法，查询完毕：{} , 时间：{}" , list , System.currentTimeMillis());
//		
////		//System.out.println(list);
////		
////		logger.warn("正在执行测试方法，查询结束："+System.currentTimeMillis()+" , 结果是："+list);
////		
////		
////		logger.error("测试方法执行完毕,耗时："+ (System.currentTimeMillis()-startTime));
		
	}

}

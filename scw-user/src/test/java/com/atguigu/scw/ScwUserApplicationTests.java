package com.atguigu.scw;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.atguigu.scw.user.bean.User;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

//spring测试环境，可以使用自动装配 获取容器中的对象
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ScwUserApplicationTests {
	// 测试HttpClient:java中用来发送网络请求的客户端 双方都需要遵循http协议
	@Test
	public void testHttpClient() throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://www.atguigu.com/");
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the network socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally clause.
		// Please note that if response content is not fully consumed the underlying
		// connection cannot be safely re-used and will be shut down and discarded
		// by the connection manager. 
		try {
		    System.out.println(response1.getStatusLine());
		    HttpEntity entity1 = response1.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    System.out.println(EntityUtils.toString(entity1,"UTF-8"));
		    EntityUtils.consume(entity1);
		} finally {
		    response1.close();
		}
	}

	// 测试UUID
	@Test
	public void testUUID() {
		// java中为了方便获取一个唯一字符串，提供的工具类, 底层使用的是时间戳+机器码 随机生成的 32位的由16进制数字组成的字符串
		UUID uuid = UUID.randomUUID();
		String uuidStr = uuid.toString();
		// System.out.println(uuidStr);
		log.warn("获取的UUID字符串：{}", uuidStr);
		log.warn(uuidStr.replace("-", ""));
	}

	// 测试短信平台的调用
	@Test
	public void testSMS() {
		// String host = "http://dingxin.market.alicloudapi.com";
		// String path = "/dx/sendSms";
		// String method = "POST";
		// String appcode = "75cb9e7f5fc94db9a7b7ac1524d05f6a";
		// Map<String, String> headers = new HashMap<String, String>();
		// // 最后在header中的格式(中间是英文空格)为Authorization:APPCODE
		// 83359fd73fe94948385f570e3c139105
		// headers.put("Authorization", "APPCODE " + appcode);
		// Map<String, String> querys = new HashMap<String, String>();
		// querys.put("mobile", "13936795572");
		// querys.put("param", "code:6666666");
		// querys.put("tpl_id", "TP1711063");
		// Map<String, String> bodys = new HashMap<String, String>();
		//
		// try {
		// /**
		// * 重要提示如下: HttpUtils请从
		// *
		// https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
		// * 下载
		// *
		// * 相应的依赖请参照
		// * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
		// */
		// HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys,
		// bodys);
		// System.out.println(response.toString());
		// // 获取response的body
		// // System.out.println(EntityUtils.toString(response.getEntity()));
		// int code = response.getStatusLine().getStatusCode();
		// System.out.println("短信平台的响应码: "+code);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	// 从容器中获取redisTemplate操作redis： 只要引入了redis场景启动器+配置文件中指定redis的连接地址端口号
	// springboot会自动创建template对象
	@Autowired
	RedisTemplate<Object, Object> redisTemplate; // 用来操作redis 存储对象类型的数据 不用
	@Autowired
	StringRedisTemplate stringRedisTemplate; // 用来在redis中操作字符串类型数据的redis模板类
	// JSON: 简单数据 直接使用字符串存储：存储手机验证码
	// 复杂类型： 对象、集合 ：可以通过Gson或者fastJson或者Jackson将对象转为json字符串
	// 存到redis中，读取时java代码中可以将json再转为对象或对象集合

	// 使用SLF4J+logback打印日志
	// Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void contextLoads() {
		log.info("开始测试redis.............");
		// opsForValue 模板类用来向redis中操作数据的方法
		redisTemplate.opsForValue().set("key1", "xxxxxx", 10, TimeUnit.MINUTES);
		Long expire = redisTemplate.getExpire("key1");
		log.info("获取key1的过期时间：{}.............", expire);

		Object object = redisTemplate.opsForValue().get("key1");
		log.info("获取key1的值：{}.............", object);

	}

	// 测试json和对象转换存储到redis中
	@Test
	public void contextLoads2() {
		// User user = new User(1001, "田思超", "123456");

		Gson gson = new Gson();
		// String userJson = gson.toJson(user);
		// stringRedisTemplate.opsForValue().set("user", userJson, 100,
		// TimeUnit.SECONDS);

		String userJsonStr = stringRedisTemplate.opsForValue().get("user");
		System.err.println(userJsonStr);

		User user = gson.fromJson(userJsonStr, User.class);
		System.out.println(user);

	}
}

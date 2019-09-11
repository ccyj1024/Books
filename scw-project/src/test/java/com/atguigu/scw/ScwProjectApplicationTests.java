package com.atguigu.scw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScwProjectApplicationTests {

	@Test
	public void contextLoads() throws Exception {
		// Endpoint以杭州为例，其它Region请按实际情况填写。
		String endpoint = "http://oss-cn-shanghai.aliyuncs.com"; //oss-cn-shanghai.aliyuncs.com
		// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
		String accessKeyId = "LTAIx3EFAtSueNAd"; //使用阿里云oss的用户身份id
		String accessKeySecret = "OvxqLNHsAAwhTyPrtGtoQ1jCvVMTxU";

		// 创建OSSClient实例。 阿里云OSSapi 
		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

		// 上传文件流。
		InputStream inputStream = new FileInputStream(new File("C:\\Users\\86185\\Desktop\\2.jpg"));
		String fileName = System.currentTimeMillis()+"_"+UUID.randomUUID().toString().replace("-", "")+"_2.jpg";
		ossClient.putObject("scw-190508", "images/"+fileName, inputStream);

		// 关闭OSSClient。
		ossClient.shutdown();
	}

}

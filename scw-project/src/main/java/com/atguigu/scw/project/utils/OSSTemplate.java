package com.atguigu.scw.project.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import lombok.Data;

//在阿里云OSS中上传文件使用的工具类
@Data
public class OSSTemplate {
	// Endpoint以杭州为例，其它Region请按实际情况填写。
	String scheme;
	String endpoint; //oss-cn-shanghai.aliyuncs.com
	String bucketName;
	String imgFolder;
	// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
	String accessKeyId; //使用阿里云oss的用户身份id
	String accessKeySecret;

	//上传图片并返回图片的url地址的方法
	public String uploadImg(MultipartFile img) throws Exception {
		String fileName = img.getOriginalFilename();
		InputStream inputStream = img.getInputStream();
		// 创建OSSClient实例。 阿里云OSSapi 
		OSS ossClient = new OSSClientBuilder().build(scheme+endpoint, accessKeyId, accessKeySecret);
		
		fileName = System.currentTimeMillis()+"_"+UUID.randomUUID().toString().replace("-", "")+"_"+fileName;
		ossClient.putObject(bucketName, imgFolder+fileName, inputStream);
		// 关闭OSSClient。
		ossClient.shutdown();
		//返回上传成功的图片地址
		//https://scw-190508.oss-cn-shanghai.aliyuncs.com/images/1566370177265_b8e8b777549d4e6c933b7a3745205358_2.jpg
		//协议://bucketName.endPoint/ObjectName(存储图片的路径+文件名)
		return scheme+bucketName+"."+endpoint+"/"+imgFolder+fileName;
	}
	
}

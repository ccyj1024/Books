package com.atguigu.scw.ui.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016092900621804";//沙箱应用id
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDg6RsuVpU3k7vQy9NL0kFP7PEsg7FZxiouYDXsW+1g+iYPpNzJVyqP+zSQSocRrDxgZQjst9GQE7VXetgje6Idr+ciZIGRRC+m7KEglabQpNPqxk5mNfY2RWUfYCYCCriFmKF+vdhFi8RPgw1pN2cpSonaD1BotYHXIi7/3nT/uOd++vP4KefqQ8cJTtd/Z1GKZiGefOR30HDkBN3ili8/yFk7rSct7EzbYZM6ZMMg7hLH7SKlKNQxtmR43WJ8+OCNxGJPMEYclmmMAXw6h/H/xuBMLNNniEbks9a4oVVnKFg9fwNgJ0dN3ByWT527JQdxnKhlUNh928HQESNkeSq3AgMBAAECggEAGFP+ta916pIXYcl6bJwspmmZ2NfR6kUOMQAFNTDoZ3IHhe+uVjUQjIx+rhDxtU6tJTwoj/p+N5h6KCFImeX4rj9lwAmhHkaC5Qp5j13AspbxuVaECnhRzqMJAjDTS6s/NGxDI8ODTlqE/JjPbY8poITIjobTis//YHb5III+o553rYL42BSSb1mddr45yC17JwNM1My8aCyuKweOgyDnYqESvmrzV4UNMUO9F6WIQ+wKV0V36vXT1NhAMjWuNxBv6zNPDpPJ2jP6s/EDEraRm24dXAdmw3qnQxOtRqV8X876IdqXIwH0R/jLpsUjcjqC58Adp8hfarh2oi96mnGlaQKBgQD653yquURlMfsgRlbY6hmxo3H236Ie9BLXTnT3fj8SsCY3iJxHttPqDckMxjEyDsdRqoZZ44cnFA5dGzxUiVVaAAazl9hEyynLEboie7uHujfMzI4Fn/2tZ7w0fKUc08wbSsfEUK8ZaQGHykPoxNc3E0i/at+BRZ4nT3Rg/P9TqwKBgQDleni/gvQCV+81P2xBz0pBFu5ADFTAWpHIimnRbA2j/77T0tsTBD51bPY5RJ52VNb5CqFHfrmEktZ3uwUbNtH3dCpMh+8VXFYWoEd1WFMjdJ+NDBsS3df/XuNTbPZMPwaCeGnbqGbVvx0dGbsYsOXOQ7ZWZvWKYy62ZM1EDNM5JQKBgQCMSBUZ1w/4jGNd/jXcfbbz7PBxGUQpjklifAlFvx5AsqALeKP4LK57NE3uiHlFBaJHdNI9O8t6++m0AOBQW6CO+2jDzOgsPnMViMnLMeb90g4UwWG+n19ULqW5qt8eKQT7VWovxvS/fkhOxbYGVNgHgQ6xh22Dd4sfa3skJxASlQKBgD4rVukzdh3oatL3MsIFga1uo+9QJ+nQ36v/67kj1/I1SCKk/lsBjFZNxcswDuoqA++GdLkk2OfUzCu1zCVaTboHi6oeLaSQ1nXcyF8/JY72en9A51jOJ0V1FIs2euefL4lLTAqJbgq8cwu+O9Ttn+SorY5s6Fqskiwx1PZ5rwPNAoGBAIU8ZMoIXWOJd9TlUqjwlaZ1RV/b+H8ZhezbRX/RaapBbFtqkfwRLeKaTwNhNDMY01VmCo5bS3v1nEwb+pWIzZ353XDv0xdA3b6n4PyfOwoJohsmuF9FcK7GotgPSIBMss2YUVTNnxCKUiiawOt1NDKC7iNz+uDYgN7mHRhlrdiW";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAibeLGhTLL3yfHCCtlVz9H538HI3xkL9UEriXsiX6rTaE+Tvx1uALV1IAkIDYTiLQEz1YhCfdsNL4VypcxaHCjF6Z7kRVIM/bYhdBJXESKpGUKP1ASFOiytdhdeYGxXEQk5NYfLAdRN/COFOAcWianJC2vao2woNbLyIK4wX8G+x4OostJi3kA00pUh/rYdiJbLdcpF7KMKhtTbEUHKKHeXRlZ7ItFko7VSwVOAkKe3a/70uoTKVwn7MAvYa7gP8/NLPboKEaQC+Zu02QZtq7r2w8+Jha+tn4vHPHlSYTLVl+bmZupiDzx02/qIgioatqj7st8rjVmDvwYj3Ph8OLBwIDAQAB";

    //支付宝同步处理请求的响应有可能有错误:用户支付成功哟可能有异常，客服处理
	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //http://2x46f41192.qicp.vip
	public static String notify_url = "http://2x46f41192.qicp.vip/order/notify";

	//支付宝接受用户支付成功后会自动跳转的给用户通知的由我们自己编写的页面：用户支付后的直接响应
	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	// 当支付成功时，将数据库中的订单状态修改为已支付 ，再跳转到结账成功页面 给用户提示
	public static String return_url = "http://2x46f41192.qicp.vip/order/return";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关  https://openapi.alipaydev.com/gateway.do
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝日志保存的路径
	public static String log_path = "D:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


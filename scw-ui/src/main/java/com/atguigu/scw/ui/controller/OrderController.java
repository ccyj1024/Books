package com.atguigu.scw.ui.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atguigu.scw.common.bean.AppResonse;
import com.atguigu.scw.ui.config.AlipayConfig;
import com.atguigu.scw.ui.feign.OrderFeign;
import com.atguigu.scw.ui.feign.ProjectFeign;
import com.atguigu.scw.ui.feign.UserFeign;
import com.atguigu.scw.ui.vo.request.OrderFormInfoSubmitVo;
import com.atguigu.scw.ui.vo.request.OrderInfoSubmitVo;
import com.atguigu.scw.ui.vo.response.ReturnPayConfirmVo;
import com.atguigu.scw.ui.vo.response.UserAddressVo;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class OrderController {
	
	@Autowired
	ProjectFeign projectFeign;
	@Autowired
	OrderFeign orderFeign;
	@Autowired
	UserFeign userFeign;
	//支付宝处理支付后回调的异步方法: 最终决定订单支付状态的方法
	@PostMapping("/order/notify")
	public String alipayNotify( HttpServletRequest request) throws Exception {
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

		//——请在这里编写您的程序（以下代码仅作参考）——
		
		/* 实际验证过程建议商户务必添加以下校验：
		1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
		4、验证app_id是否为该商户本身。
		*/
		if(signVerified) {//验证成功
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
			
			if(trade_status.equals("TRADE_FINISHED")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
					
				//注意：
				//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
			}else if (trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				
				//注意：
				//付款完成后，支付宝系统发送该交易状态通知
			}
			
			
		}else {//验证失败
		
			//调试用，写文本函数记录程序运行情况是否正常
			//String sWord = AlipaySignature.getSignCheckContentV1(params);
			//AlipayConfig.logResult(sWord);
		}
		return  null;
	}
	
	//处理支付成功后的同步请求的方法：  尽快的给用户响应，但是结果可能不精确
	@GetMapping("/order/return")
	public String alipayReturn(HttpServletRequest request) throws Exception {
		//获取支付宝GET过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

		//——请在这里编写您的程序（以下代码仅作参考）——
		if(signVerified) {
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//付款金额
			String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
			//代表支付成功
			//1、判断支付是否成功
			
			//2、如果成功。更新订单的状态
			orderFeign.updateOrderState(out_trade_no, "1");
			//跳转到支付成功的页面
			return "order/success";
			//3、给用户成功或失败的响应
			//out.println("trade_no:"+trade_no+"<br/>out_trade_no:"+out_trade_no+"<br/>total_amount:"+total_amount);
		}else {
			//out.println("验签失败");
			//支付失败
			return "order/error";
		}
		
	}
	
	
	//立即付款的方法:接受+整理付款对应的订单数据、将订单数据存到数据库中、调用支付宝将需要的订单信息传递过去跳转到支付页面让用户支付
	@ResponseBody  //响应字符串时必须使用此注解
	@PostMapping(value="/order/checkout", produces="text/html")//produces springmvc会自动将响应体的数据写入到响应体中并设置contenttype告诉浏览器响应体是一个页面
	public String checkout(HttpSession session,OrderFormInfoSubmitVo vo) {//VO: 创建一个javabean描述页面提交的请求参数
		//1、获取请求参数
		log.debug("接受到的订单信息：{}", vo);
		ReturnPayConfirmVo returnPayConfirmVo = (ReturnPayConfirmVo) session.getAttribute("returnPayConfirmVo");
		log.debug("确认订单信息：{}", returnPayConfirmVo);
		Map map = (Map) session.getAttribute("user");
		//2、将请求的数据转为远程调用order服务时需要的OrderInfoSubmitVo
		OrderInfoSubmitVo submitVo = new OrderInfoSubmitVo();
		String accessToken = (String) map.get("token");
		submitVo.setAccessToken(accessToken);
		submitVo.setProjectid(returnPayConfirmVo.getProjectId());
		submitVo.setReturnid(returnPayConfirmVo.getReturnId());
		submitVo.setRtncount(vo.getNum());
		//根据提交的信息计算总金额
		Integer totalMoney = vo.getNum()*returnPayConfirmVo.getPrice()+returnPayConfirmVo.getFreight();
		submitVo.setMoney(totalMoney);
		submitVo.setAddress(vo.getAddress());
		submitVo.setInvoice(vo.getInvoice()+"");
		submitVo.setInvoictitle(vo.getInvoictitle());
		submitVo.setRemark(vo.getRemark());
		
		
		//3、远程调用order服务
		AppResonse<Object> resonse = orderFeign.createOrder(submitVo);
		if(resonse.getCode()!=200) {
			//订单保存失败
			return "错误页面";
		}
		String orderNum = (String) resonse.getData();
		
		//4、调用支付宝让用户支付
		//获得初始化的AlipayClient： 需要使用AlipayConfig类中配置的参数
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
		
		//设置请求参数：alipayRequest aplipey客户端SDK用来向alipay服务端发请求的对象
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig.return_url);//配置同步和异步的响应页面地址[商户自己编写的页面地址]
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = orderNum;//new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//付款金额，必填
		String total_amount = submitVo.getMoney().toString();//new String(request.getParameter("WIDtotal_amount").getBytes("ISO-8859-1"),"UTF-8");
		//订单名称，必填
		String subject = returnPayConfirmVo.getProjectName();//new String(request.getParameter("WIDsubject").getBytes("ISO-8859-1"),"UTF-8");
		//商品描述，可空
		String body = submitVo.getRemark();//new String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8");
		
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
				+ "\"total_amount\":\""+ total_amount +"\"," 
				+ "\"subject\":\""+ subject +"\"," 
				+ "\"body\":\""+ body +"\"," 
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		
		//若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
		//alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
		//		+ "\"total_amount\":\""+ total_amount +"\"," 
		//		+ "\"subject\":\""+ subject +"\"," 
		//		+ "\"body\":\""+ body +"\"," 
		//		+ "\"timeout_express\":\"10m\"," 
		//		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		//请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节
		
		//请求
		String result = "";
		try {
			result = alipayClient.pageExecute(alipayRequest).getBody();
			//response.getWriter().println(result);//将reslut写入到响应报文的响应体中，交给浏览器解析
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	
	//跳转到订单页面的第二步： 确认订单  选择收货地址、开票方式、确认回报信息
	@GetMapping("/order/pay-step-2/{num}")
	public String payStep2(HttpServletRequest request,HttpSession session , @PathVariable("num")Integer num) {
		//0、判断用户是否登录，如果已登录可以获取收货地址，如果没有登录跳转到登录页面让用户登录
		Map map = (Map) session.getAttribute("user");
		if(map==null) {
			String requestURI = request.getRequestURI();
			session.setAttribute("path", requestURI);
			request.setAttribute("errorMsg","去结算必须先登录!!");
			//转发到登录页面让用户继续登录
			return "login";
		}
		//1、准备当前登录用户的收货地址
		String accessToken = (String) map.get("token");
		AppResonse<List<UserAddressVo>> resonse = userFeign.address(accessToken);
		//2、修改存在session中的项目信息
		ReturnPayConfirmVo returnPayConfirmVo = (ReturnPayConfirmVo) session.getAttribute("returnPayConfirmVo");
		//修改购买的回报数量
		returnPayConfirmVo.setNum(num);
		//修改总价
		BigDecimal totalPrice = new BigDecimal(0+"");
		BigDecimal priceBd = new BigDecimal(returnPayConfirmVo.getPrice()+"");
		BigDecimal numBd = new BigDecimal(num+"");
		BigDecimal freightBd = new BigDecimal(returnPayConfirmVo.getFreight()+"");
		totalPrice = priceBd.multiply(numBd).add(freightBd);
		returnPayConfirmVo.setTotalPrice(totalPrice);
		log.error("地址列表：{}", resonse.getData());
		log.error("returnPayConfirmVo：{}", returnPayConfirmVo);
		
		//将获取到的地址设置到域中共享
		request.setAttribute("addresses", resonse.getData());
		return "order/pay-step-2";
	}
	//跳转到订单页面的第一步： 确认回报页面pay-step-1.html
	@GetMapping("/order/pay-step-1/{returnId}/{projectId}")
	public String payStep1(HttpServletRequest request , HttpSession session,@PathVariable("projectId")Integer projectId, @PathVariable("returnId")Integer returnId) {
		//调用远程服务  查询 页面显示需要的数据
		AppResonse<ReturnPayConfirmVo> resonse = projectFeign.confirmProjectReturnPayInfo(projectId, returnId);
		ReturnPayConfirmVo returnPayConfirmVo = resonse.getData();
		//需要的是创建项目的那个人的id和 用户名
//		returnPayConfirmVo.setMemberId((Integer)map.get("id"));//登录者的id和账号
//		returnPayConfirmVo.setMemberName((String)map.get("username"));
		log.debug("payStep1查询到回报确认： {}", resonse.getData());
		
		//将查询到的回报确认信息存到域中
		session.setAttribute("returnPayConfirmVo", returnPayConfirmVo);
		
		//转发到页面中显示
		return "order/pay-step-1";
	}
	

}

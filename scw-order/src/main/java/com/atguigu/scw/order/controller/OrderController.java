package com.atguigu.scw.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.scw.common.bean.AppResonse;
import com.atguigu.scw.order.service.OrderService;
import com.atguigu.scw.order.vo.request.OrderInfoSubmitVo;

@RestController
public class OrderController {

	@Autowired
	OrderService orderService;
	
	@GetMapping("/order/updateOrder")
	public AppResonse<Object> updateOrderState(@RequestParam("orderNum")String orderNum , @RequestParam("state")String state){
		
		orderService.updateOrderState(orderNum , state);
		
		return AppResonse.ok("更新成功");
	}
	
	//创建订单
	@PostMapping("/order/createOrder")
	public AppResonse<Object> createOrder(@RequestBody OrderInfoSubmitVo vo){
		//获取需要保存的订单数据
		//调用service处理保存订单的业务
		String orderNum = orderService.createOrder(vo);
		if(orderNum==null) {
			return AppResonse.fail("订单创建失败");
		}
		
		return AppResonse.ok(orderNum);
	}
	
}

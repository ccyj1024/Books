package com.atguigu.scw.ui.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.scw.common.bean.AppResonse;
import com.atguigu.scw.ui.vo.request.OrderInfoSubmitVo;
@FeignClient("SCW-ORDER")
public interface OrderFeign {

	@GetMapping("/order/updateOrder")
	public AppResonse<Object> updateOrderState(@RequestParam("orderNum")String orderNum , @RequestParam("state")String state);
	
	@PostMapping("/order/createOrder")
	public AppResonse<Object> createOrder(@RequestBody OrderInfoSubmitVo vo);
}

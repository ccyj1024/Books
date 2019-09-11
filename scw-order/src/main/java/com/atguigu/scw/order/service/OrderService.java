package com.atguigu.scw.order.service;

import com.atguigu.scw.order.vo.request.OrderInfoSubmitVo;

public interface OrderService {

	String createOrder(OrderInfoSubmitVo vo);

	void updateOrderState(String orderNum, String state);

}

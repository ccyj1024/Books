package com.atguigu.scw.order.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.atguigu.scw.common.bean.TMember;
import com.atguigu.scw.common.utils.AppDateUtils;
import com.atguigu.scw.common.utils.RedisUtils;
import com.atguigu.scw.order.bean.TOrder;
import com.atguigu.scw.order.bean.TOrderExample;
import com.atguigu.scw.order.mapper.TOrderMapper;
import com.atguigu.scw.order.service.OrderService;
import com.atguigu.scw.order.vo.request.OrderInfoSubmitVo;
@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	TOrderMapper orderMapper;
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Override
	public String createOrder(OrderInfoSubmitVo vo) {
		//1、判断用户是否登录
		TMember member = RedisUtils.getJson2Bean(stringRedisTemplate, vo.getAccessToken(), TMember.class);
		if(member == null) {
			return null;
		}
		//2、将订单数据转换为数据库中需要使用的订单信息
		TOrder order = new TOrder();
		/**
		 * private String accessToken;
			private Integer projectid;  ==
			private Integer returnid;   ==
			private Integer rtncount;  == 
		
			private String address;  == 
			private String invoice; ==
			private String invoictitle; == 
			private String remark; == 

		 */
		BeanUtils.copyProperties(vo, order);
		//ordernum、status(0)、memberid(根据accessToken查询)、createDate、money(自己计算)
		order.setMemberid(member.getId());
		order.setStatus("0");
		order.setCreatedate(AppDateUtils.getFormatTime());
		//创建唯一的订单编号
		String orderNum = System.currentTimeMillis()+UUID.randomUUID().toString().replace("-", "")+member.getId();
		order.setOrdernum(orderNum);
		
		//3、调用service将订单信息存到数据库中
		orderMapper.insertSelective(order);
		//4、如果成功返回订单编号
		return orderNum;
	}

	@Override
	public void updateOrderState(String orderNum, String state) {
		TOrderExample e = new TOrderExample();
		e.createCriteria().andOrdernumEqualTo(orderNum);
		List<TOrder> list = orderMapper.selectByExample(e );
		TOrder order = list.get(0);
		order.setStatus(state);
		orderMapper.updateByPrimaryKeySelective(order);
	}

	
}

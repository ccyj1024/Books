package com.atguigu.scw.order.vo.request;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderInfoSubmitVo implements Serializable{ // 用于保存订单的数据
	private String accessToken;
	private Integer projectid;
	private Integer returnid;
	private Integer rtncount;
	private Integer money;
	private String address;
	private String invoice;
	private String invoictitle;
	private String remark;
}

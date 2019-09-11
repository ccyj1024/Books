package com.atguigu.scw.project.vo.request;

import com.atguigu.scw.common.bean.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class BaseProjectVO extends BaseVO{
	@ApiModelProperty("项目之前的临时token")
	String projectToken;// 项目的临时token
}

package com.atguigu.scw.project.vo.request;

import lombok.Data;

@Data
public class ProjectConfirmVO extends BaseProjectVO{
	
	//用户确认保存项目时操作的类型: 0 ：代表提交保存到项目表中 ， 1：代表保存草稿，保存到临时表中
	private String opration;
}

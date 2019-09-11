package com.atguigu.scw.project.vo.request;

import java.util.List;

import com.atguigu.scw.common.bean.BaseVO;
import com.atguigu.scw.project.bean.TProjectInitiator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
//用来接受第一步 项目及发起人信息的 数据
@ApiModel
@Data
@ToString
public class ProjectBaseInfoVO extends BaseProjectVO {


	@ApiModelProperty("项目的分类id")
	private List<Integer> typeids; // 项目的分类id

	@ApiModelProperty("项目的标签id")
	private List<Integer> tagids; // 项目的标签id

	@ApiModelProperty("项目名称")
	private String name;// 项目名称

	@ApiModelProperty("项目简介")
	private String remark;// 项目简介

	@ApiModelProperty("筹资金额")
	private Integer money;// 筹资金额

	@ApiModelProperty("筹资天数")
	private Integer day;// 筹资天数

	@ApiModelProperty("项目头部图片")
	private String headerImage;// 项目头部图片 在oss中图片的url地址

	@ApiModelProperty("项目详情图片")
	private List<String> detailsImage;// 项目详情图片
	@ApiModelProperty("项目发起人信息")
	private TProjectInitiator initiator; //项目发起人信息
}

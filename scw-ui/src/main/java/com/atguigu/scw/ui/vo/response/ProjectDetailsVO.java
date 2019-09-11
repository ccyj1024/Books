package com.atguigu.scw.ui.vo.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
//项目详情VO类
@ApiModel
@Data
public class ProjectDetailsVO implements Serializable{
	@ApiModelProperty("项目id")
	private Integer id;
	@ApiModelProperty("项目名")
	private String name;
	@ApiModelProperty("项目备注")
	private String remark;
	@ApiModelProperty("众筹金额")
	private Long money;
	@ApiModelProperty("天数")
	private Integer day;
	@ApiModelProperty("项目状态")
	private Byte status;
	@ApiModelProperty("项目部署日期")
	private String deploydate;
	@ApiModelProperty("已支持金额")
	private Long supportmoney;
	@ApiModelProperty("项目支持人数")
	private Integer supporter;
	@ApiModelProperty("完成度")
	private Integer completion;
	@ApiModelProperty("项目创建日期")
	private String createdate;
	@ApiModelProperty("关注人数")
	private Integer follower;
	@ApiModelProperty("项目发起人信息")
	private TProjectInitiator initiator;
	@ApiModelProperty("项目头部图")
	private String headerImage;
	@ApiModelProperty("项目详情图")
	private List<String> detailsImage = new ArrayList<>();

	@ApiModelProperty("项目回报档位详情")
	private List<TReturn> returns = new ArrayList<>();
}

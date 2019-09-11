package com.atguigu.scw.ui.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.scw.common.bean.AppResonse;
import com.atguigu.scw.ui.vo.response.ProjectDetailsVO;
import com.atguigu.scw.ui.vo.response.ProjectVO;
import com.atguigu.scw.ui.vo.response.ReturnPayConfirmVo;

@FeignClient(value="SCW-PROJECT")
public interface ProjectFeign {
	@GetMapping("/project/all")
	public AppResonse<List<ProjectVO>> all();
	//查询项目详情
	@GetMapping("/details/info/{projectId}")
	public AppResonse<ProjectDetailsVO> detailsInfo(@PathVariable("projectId")Integer projectId);
	//查询确认回报信息
	@GetMapping("/project/returnConfirm")
	public AppResonse<ReturnPayConfirmVo> confirmProjectReturnPayInfo(@RequestParam("projectId") Integer projectId,
			@RequestParam("retId") Integer retId) ;

}

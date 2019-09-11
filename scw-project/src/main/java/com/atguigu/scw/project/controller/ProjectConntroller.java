package com.atguigu.scw.project.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.atguigu.scw.common.bean.AppResonse;
import com.atguigu.scw.common.bean.TMember;
import com.atguigu.scw.project.bean.TProject;
import com.atguigu.scw.project.bean.TProjectImages;
import com.atguigu.scw.project.bean.TProjectInitiator;
import com.atguigu.scw.project.bean.TReturn;
import com.atguigu.scw.project.feign.UserServiceFeign;
import com.atguigu.scw.project.service.ProjectService;
import com.atguigu.scw.project.utils.OSSTemplate;
import com.atguigu.scw.project.vo.response.ProjectDetailsVO;
import com.atguigu.scw.project.vo.response.ProjectVO;
import com.atguigu.scw.project.vo.response.ReturnPayConfirmVo;

import io.swagger.annotations.ApiOperation;

@RestController
public class ProjectConntroller {

	@Autowired
	OSSTemplate ossTemplate;
	@Autowired
	ProjectService projectInfoService;
	@Autowired
	UserServiceFeign userServiceFeign;
	
	
	//点击支持回报，跳转到回报确认页面需要的数据
	@GetMapping("/project/returnConfirm")
	public AppResonse<ReturnPayConfirmVo> confirmProjectReturnPayInfo(@RequestParam("projectId") Integer projectId,
			@RequestParam("retId") Integer retId) {
		// 1、项目的信息
		TProject project = projectInfoService.getProjectInfo(projectId);
		//2、查询项目发起人信息
		Integer memberid = project.getMemberid();
		AppResonse<TMember> memberInfo = userServiceFeign.getMemberInfo(memberid);
		TMember member = memberInfo.getData();

		// 3、回报的信息
		TReturn returnInfo = projectInfoService.getProjectReturnById(retId);
		// 4、封装数据并返回
		ReturnPayConfirmVo confirmVo = new ReturnPayConfirmVo();
		confirmVo.setFreight(returnInfo.getFreight());
		confirmVo.setNum(1);
		confirmVo.setPrice(returnInfo.getSupportmoney());
		confirmVo.setProjectId(project.getId());
		confirmVo.setProjectName(project.getName());
		confirmVo.setMemberId(member.getId());
		confirmVo.setMemberName(member.getUsername());
		confirmVo.setProjectRemark(project.getRemark());
		confirmVo.setReturnId(returnInfo.getId());
		confirmVo.setReturnContent(returnInfo.getContent());
		confirmVo.setSignalpurchase(returnInfo.getSignalpurchase());

		Integer totalMoney = returnInfo.getSupportmoney() * 1 + returnInfo.getFreight();
		BigDecimal bigDecimal = new BigDecimal(totalMoney.toString());
		confirmVo.setTotalPrice(bigDecimal);

		return AppResonse.ok(confirmVo);
	}

	@ApiOperation("[+]获取项目信息详情")
	@GetMapping("/details/info/{projectId}")
	public AppResonse<ProjectDetailsVO> detailsInfo(@PathVariable("projectId") Integer projectId) {
		TProject p = projectInfoService.getProjectInfo(projectId);
		ProjectDetailsVO projectVo = new ProjectDetailsVO();

		// 1、查出这个项目的所有图片
		List<TProjectImages> projectImages = projectInfoService.getProjectImages(p.getId());
		for (TProjectImages tProjectImages : projectImages) {
			if (tProjectImages.getImgtype() == 0) {
				projectVo.setHeaderImage(tProjectImages.getImgurl());
			} else {
				List<String> detailsImage = projectVo.getDetailsImage();
				detailsImage.add(tProjectImages.getImgurl());
			}
		}

		// 2、项目的所有支持档位；
		List<TReturn> returns = projectInfoService.getProjectReturns(p.getId());
		projectVo.setReturns(returns);
		// 3、项目的发起人信息
		TProjectInitiator initiator = projectInfoService.getprojectInitiator(p.getId());
		projectVo.setInitiator(initiator);

		BeanUtils.copyProperties(p, projectVo);
		return AppResonse.ok(projectVo);
	}

	// 查询项目列表的方法
	@ApiOperation("[+]获取系统所有的项目")
	@GetMapping("/project/all")
	public AppResonse<List<ProjectVO>> all() {
		// 1、分步查询，先查出所有项目
		// 2、再查询这些项目图片
		List<ProjectVO> prosVo = new ArrayList<>();

		// 1、连接查询，所有的项目left join 图片表，查出所有的图片
		// left join：笛卡尔积 A*B 1000万*6 = 6000万
		// 大表禁止连接查询；
		List<TProject> pros = projectInfoService.getAllProjects();

		for (TProject tProject : pros) {
			Integer id = tProject.getId();
			List<TProjectImages> images = projectInfoService.getProjectImages(id);
			ProjectVO projectVo = new ProjectVO();
			BeanUtils.copyProperties(tProject, projectVo);

			for (TProjectImages tProjectImages : images) {
				if (tProjectImages.getImgtype() == 0) {
					projectVo.setHeaderImage(tProjectImages.getImgurl());
				} else {
					List<String> detailsImage = projectVo.getDetailsImage();
					detailsImage.add(tProjectImages.getImgurl());
				}
			}
			prosVo.add(projectVo);
		}
		return AppResonse.ok(prosVo);
	}

	// 处理文件上传的请求
	// 注意：需要在properties配置文件中设置服务器处理上传文件的大小限制
	@PostMapping("/project/uploadImgs")
	public AppResonse<Object> uploadProjectImgs(MultipartFile[] imgs) {// 接受上传的文件： 一个文件对应一个MultiPartFile
		List<String> imgPathList = new ArrayList<String>();
		// 遍历将多个图片上传到OSS中
		if (imgs != null && imgs.length > 0) {
			for (int i = 0; i < imgs.length; i++) {
				MultipartFile img = imgs[i];
				if (img.getSize() > 100) {
					// OSSTemplate上传图片
					try {
						String imgPath = ossTemplate.uploadImg(img);
						imgPathList.add(imgPath);
					} catch (Exception e) {
						e.printStackTrace();
						return AppResonse.fail("上传失败!!!");
					}
				}
			}
		}

		return AppResonse.ok(imgPathList);
	}

}

package com.atguigu.scw.project.service;

import java.util.List;

import com.atguigu.scw.project.bean.TProject;
import com.atguigu.scw.project.bean.TProjectImages;
import com.atguigu.scw.project.bean.TProjectInitiator;
import com.atguigu.scw.project.bean.TReturn;
import com.atguigu.scw.project.vo.request.ProjectConfirmVO;

public interface ProjectService {

	void createProject(ProjectConfirmVO vo);

	List<TProject> getAllProjects();

	List<TProjectImages> getProjectImages(Integer id);

	TProject getProjectInfo(Integer projectId);

	List<TReturn> getProjectReturns(Integer id);

	TProjectInitiator getprojectInitiator(Integer id);

	TReturn getProjectReturnById(Integer retId);

}

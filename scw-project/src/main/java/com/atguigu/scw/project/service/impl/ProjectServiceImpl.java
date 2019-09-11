package com.atguigu.scw.project.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.scw.common.utils.AppDateUtils;
import com.atguigu.scw.common.utils.RedisUtils;
import com.atguigu.scw.project.bean.TProject;
import com.atguigu.scw.project.bean.TProjectImages;
import com.atguigu.scw.project.bean.TProjectImagesExample;
import com.atguigu.scw.project.bean.TProjectInitiator;
import com.atguigu.scw.project.bean.TProjectTag;
import com.atguigu.scw.project.bean.TProjectType;
import com.atguigu.scw.project.bean.TReturn;
import com.atguigu.scw.project.bean.TReturnExample;
import com.atguigu.scw.project.mapper.TProjectImagesMapper;
import com.atguigu.scw.project.mapper.TProjectInitiatorMapper;
import com.atguigu.scw.project.mapper.TProjectMapper;
import com.atguigu.scw.project.mapper.TProjectTagMapper;
import com.atguigu.scw.project.mapper.TProjectTypeMapper;
import com.atguigu.scw.project.mapper.TReturnMapper;
import com.atguigu.scw.project.service.ProjectService;
import com.atguigu.scw.project.vo.request.ProjectConfirmVO;
import com.atguigu.scw.project.vo.request.ProjectRedisStorageVO;
@Transactional
@Service
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	@Autowired
	TProjectImagesMapper tProjectImagesMapper;
	@Autowired
	private TProjectMapper projectMapper;
	@Autowired
	TProjectTagMapper projectTagMapper;
	@Autowired
	TProjectTypeMapper projectTypeMapper;
	@Autowired
	TProjectInitiatorMapper tProjectInitiatorMapper;
	@Autowired
	TReturnMapper tReturnMapper;
	@Override
	public void createProject(ProjectConfirmVO vo) {
		//从redis中获取bigVo
		ProjectRedisStorageVO bigVo = RedisUtils.getJson2Bean(stringRedisTemplate, vo.getProjectToken(), ProjectRedisStorageVO.class);
		//将bigVo转为javabean、TProject.java 存到数据库中
		//bigVo中包含了项目信息、回报集合信息、项目发起人信息、项目图片信息
		//数据需要拆分成不同的javabean存到不同的表中
		//1、拆分出project信息 保存并返回project的id（后续保存其他数据到表中时需要使用projectid关联）
		TProject tProject = new TProject();
		BeanUtils.copyProperties(bigVo, tProject);
		
		tProject.setStatus("0");// 0 - 即将开始， 1 - 众筹中， 2 - 众筹成功， 3 - 众筹失败
		tProject.setDeploydate(AppDateUtils.getFormatTime());//发布日期
		projectMapper.insertSelective(tProject);//mybatis支持插入数据后返回主键id，必须在插入的映射文件中 使用useGenretedKey 使用生成的主键值
		Integer id = tProject.getId();//获取到项目对象在数据库中存储之后的主键id
		
		//2、保存project的图片信息
		//2.1存储头图
		TProjectImages projectImages = new TProjectImages();
		projectImages.setProjectid(id);
		projectImages.setImgurl(bigVo.getHeaderImage());
		projectImages.setImgtype((byte)0);
		tProjectImagesMapper.insertSelective(projectImages);
		//2.2存储详情图
		for (int i = 0; i < bigVo.getDetailsImage().size(); i++) {
			projectImages.setProjectid(id);
			projectImages.setImgurl(bigVo.getDetailsImage().get(i));
			projectImages.setImgtype((byte)1);
			//每次遍历获取到一个详情图
			tProjectImagesMapper.insertSelective(projectImages);
			
		}
		
		//3、保存project的tag信息  
		for (int i = 0; i < bigVo.getTagids().size(); i++) {
			TProjectTag projectTag = new TProjectTag();
			projectTag.setProjectid(id);
			projectTag.setTagid(bigVo.getTagids().get(i));//或者正在遍历的tagid
			projectTagMapper.insertSelective(projectTag);
		}
		//4、保存project的type信息
		for (int i = 0; i < bigVo.getTypeids().size(); i++) {
			TProjectType projectType = new TProjectType();
			projectType.setProjectid(id);
			projectType.setTypeid(bigVo.getTypeids().get(i));//或者正在遍历的tagid
			projectTypeMapper.insertSelective(projectType);
		}
		//5、保存project的 发起人信息
		/*TProjectInitiator initiator = bigVo.getInitiator();
		initiator.set*/
		tProjectInitiatorMapper.insertSelective(bigVo.getInitiator());
		//6、保存project的回报信息
		for (int i = 0; i < bigVo.getProjectReturns().size(); i++) {
			TReturn tReturn = bigVo.getProjectReturns().get(i);
			//return和项目关联：通过projectid
			tReturn.setProjectid(id);
			tReturnMapper.insertSelective(tReturn);
		}
		//清空redis中缓存的项目信息
		stringRedisTemplate.delete(vo.getProjectToken());
	}
	@Override
	public List<TProject> getAllProjects() {
		return projectMapper.selectByExample(null);
	}
	@Override
	public List<TProjectImages> getProjectImages(Integer id) {
		TProjectImagesExample e = new TProjectImagesExample();
		e.createCriteria().andProjectidEqualTo(id);
		List<TProjectImages> list = tProjectImagesMapper.selectByExample(e );
		return list;
	}
	//根据项目id查询指定项目的详情
	@Override
	public TProject getProjectInfo(Integer projectId) {
		return projectMapper.selectByPrimaryKey(projectId);
	}
	//根据项目id查询项目的回报列表
	@Override
	public List<TReturn> getProjectReturns(Integer id) {
		TReturnExample e = new TReturnExample();
		e.createCriteria().andProjectidEqualTo(id);
		return tReturnMapper.selectByExample(e );
	}
	@Override
	public TProjectInitiator getprojectInitiator(Integer id) {
		//tProjectInitiatorMapper.selectByExample(example) 不能使用projectid
		return tProjectInitiatorMapper.selectInitiatorByProjectId(id);
	}
	@Override
	public TReturn getProjectReturnById(Integer retId) {
		return tReturnMapper.selectByPrimaryKey(retId);
	}

}

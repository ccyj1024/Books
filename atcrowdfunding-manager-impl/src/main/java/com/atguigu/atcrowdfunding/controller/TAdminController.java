package com.atguigu.atcrowdfunding.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcorwdfunding.consts.AppConsts;
import com.atguigu.atcrowdfunding.bean.TAdmin;
import com.atguigu.atcrowdfunding.bean.TMenu;
import com.atguigu.atcrowdfunding.bean.TRole;
import com.atguigu.atcrowdfunding.service.TAdminService;
import com.atguigu.atcrowdfunding.service.TMenuService;
import com.atguigu.atcrowdfunding.service.TRoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
public class TAdminController {
	
	@Autowired
	TAdminService adminService;
	@Autowired
	TMenuService menuService;
	@Autowired
	TRoleService roleService;
	//删除管理员角色的方法
	@ResponseBody
	@RequestMapping("/admin/deleteRolesToAdmin")
	public String deleteRolesToAdmin(Integer adminId , @RequestParam("roleIds")List<Integer>roleIds) {
		adminService.deleteRolesToAdmin(adminId , roleIds);
		return "ok";
		
	}
	
	
	//给管理员分配角色的方法
	@ResponseBody
	@RequestMapping("/admin/assignRolesToAdmin")
	public String assignRolesToAdmin(Integer adminId , String roleIdsStr) {
		//解析参数：将roleIds转为 role id的集合
		String[] roleIds = roleIdsStr.split(",");
		List<Integer> roleIdList = new ArrayList<Integer>();
		for (String roleIdStr : roleIds) {
			roleIdList.add(Integer.parseInt(roleIdStr));
		}
		//将adminId和roleId存到t_admin_role表中
		adminService.saveRolesToAdmin(adminId , roleIdList);
		//响应结果
		
		return "ok";
	}
	
	
	
	//跳转到角色分配的页面：可以显示管理员已经拥有的角色，还显示管理员未分配的角色列表 
	@RequestMapping("/admin/assignRole.html")
	public String toAssignRolePage(Integer adminId , Model model) {
		//准备页面需要显示的数据
		//1、查询所有的角色集合
		List<TRole> roles = roleService.listRoles();
		//2、查询指定管理员所拥有的角色id集合:  select roleid from t_admin_role where adminid = ? 
		List<Integer> assignRoleIds = roleService.getRoleIdsByAdminId(adminId);
		//3、遍历所有的角色集合 ：如果角色的id 在管理员用户角色id集合中存在，则保存为已分配角色  否则保存为未分配角色
		//创建两个集合  用来存储  角色
		List<TRole> assignRoles = new ArrayList<TRole>();
		List<TRole> unAssignRoles = new ArrayList<TRole>();
		for (TRole role : roles) {
			//如果assignRoleIds中包含正在遍历的role的id，那么该角色用户已拥有
			if(assignRoleIds.contains(role.getId())) {
				assignRoles.add(role);
			}else {
				unAssignRoles.add(role);
			}
		}
		//存到域中
		model.addAttribute("assignRoles", assignRoles);
		model.addAttribute("unAssignRoles", unAssignRoles);
		logger.debug("已分配的角色集合：{} , 未分配的角色集合：{}", assignRoles , unAssignRoles);
		//到页面中获取显示[需要两个集合：一个已分配集合  一个未分配角色集合]
		return "admin/assignRole";
	}
	
	//处理修改管理员数据的请求
	@RequestMapping("/admin/editAdmin")
	public String editAdmin(Model model , TAdmin admin , HttpSession session) {//id  username  userpswd  email  loginacct
		//调用业务层处理修改的业务
		try {
			adminService.updateAdmin(admin);
		} catch (Exception e) {
			e.printStackTrace();
			
			model.addAttribute("errorMsg", e.getMessage());
			TAdmin editAdmin = adminService.getAdmin(admin.getId());
			//隐藏密码
			editAdmin.setUserpswd("******");
			//将查询到的管理员对象存到域中
			model.addAttribute("editAdmin", editAdmin);
			return "admin/edit";
		}
		//跳转到修改之前的页面: 修改之前的页面 在/admin/edit.html/可以获取到，当前方法中不能直接获取
		String ref = (String) session.getAttribute("ref");
		//移除session域中使用完毕的ref
		session.removeAttribute("ref");
		return "redirect:"+ref;
	}
	
	//跳转到编辑管理员的页面
	@RequestMapping("/admin/edit.html/{adminId}")
	public String toEditPage(HttpSession session,@PathVariable("adminId")Integer id  , Model model , @RequestHeader("referer")String referer) {
		//获取修改之前的页面路径，修改完成之后（/admin/editAdmin）需要使用
		session.setAttribute("ref", referer);
		
		//查询id对应的管理员数据：所有的从前端提交的数据都是不安全的
		TAdmin editAdmin = adminService.getAdmin(id);
		//隐藏密码
		editAdmin.setUserpswd("******");
		//将查询到的管理员对象存到域中
		model.addAttribute("editAdmin", editAdmin);
		//转发到编辑页面
		return "admin/edit";
	}
	
	
	//批量删除管理员
	@RequestMapping("/admin/deleteAdmins")
	public String deleteAdmins(String ids , @RequestHeader("Referer") String referer) {//如果浏览器提交的参数  ?ids=1,2,3   ，springmvc就会自动将参数封装为数组传入
		logger.debug("接受到的id数组参数： {}", ids);
		String[] idsStr = ids.split(",");
		Integer[] idsInt = new Integer[idsStr.length];
		for (int i = 0; i < idsInt.length; i++) {
			idsInt[i] = Integer.parseInt(idsStr[i]);
		}
		adminService.deleteAdmins(idsInt);
		//跳转到删除之前的页面
		return "redirect:"+referer;
	}
	
	//处理批量删除管理员的请求
//	@RequestMapping("/admin/deleteAdmins")
//	public String deleteAdmins(Integer[] ids , @RequestHeader("Referer") String referer) {//如果浏览器提交的参数  ?ids=1&ids=2&ids=3   ，springmvc就会自动将参数封装为数组传入
		//@ReqeustParam("ids")List<Integer> ids
 	
	//		logger.debug("接受到的id数组参数： {}", Arrays.toString(ids));
//		adminService.deleteAdmins(ids);
//		//跳转到删除之前的页面
//		return "redirect:"+referer;
//	}
	
	
	//处理删除管理员的请求
	@RequestMapping("/admin/deleteAdmin")
	public String deleteAdmin(Integer id , @RequestHeader("Referer") String referer) {
		
		adminService.deleteAdmin(id);
		
		//跳转到删除之前的页面
		return "redirect:"+referer;
	}
	
	
	//处理添加管理员的请求
	@RequestMapping("/admin/add")
	public String addAdmin(TAdmin admin) {
		try {
			adminService.saveAdmin(admin);
		} catch (Exception e) {
			e.printStackTrace();
			//设置错误消息
			return "redirect:/admin/add.html";
		}
		//跳转到 管理员列表的最后一页
		return "redirect:/admin/index.html?pageNum=10000";
	}
	
	//转发到管理员的添加页面
	@RequestMapping("/admin/add.html")
	public String toAddPage() {
		return "admin/add";
	}
	
	
	
	//处理main.html页面中  用户维护超链接  查询  管理员列表的请求
	@PreAuthorize("hasRole('MANAGER') OR hasAuthority('user:get')")
	@RequestMapping("/admin/index.html")
	public String listAdmins(Model model ,@RequestParam(required=false , defaultValue="") String condition, @RequestParam(required=false , defaultValue="1")Integer pageNum) {
		
		//像经常使用的key  或固定的值  一般使用常量存储:  interface中 的属性 默认是静态final的
		PageHelper.startPage(pageNum, AppConsts.PAGE_SIZE);
		//查询管理员列表
		List<TAdmin> admins = adminService.listAdmins(condition);//查询时可能传入了条件
		//页面中需要使用更加详细的分页详情
		PageInfo<TAdmin> pageInfo = new PageInfo<TAdmin>(admins , AppConsts.PAGE_NAV_NUM);
		//共享到域中:request域
		model.addAttribute("pageInfo", pageInfo);
		logger.debug("查询到的管理员分页列表：{}",pageInfo);
		//转发到admin文件夹下 显示数据的页面
		return "admin/user";
	}
//	@RequestMapping("/admin/index.html")  不带条件的查询分页数据的方法
//	public String listAdmins(Model model , @RequestParam(required=false , defaultValue="1")Integer pageNum) {
//		//像经常使用的key  或固定的值  一般使用常量存储:  interface中 的属性 默认是静态final的
//		PageHelper.startPage(pageNum, AppConsts.PAGE_SIZE);
//		//查询管理员列表
//		List<TAdmin> admins = adminService.listAdmins();
//		//页面中需要使用更加详细的分页详情
//		PageInfo<TAdmin> pageInfo = new PageInfo<TAdmin>(admins , AppConsts.PAGE_NAV_NUM);
//		//共享到域中:request域
//		model.addAttribute("pageInfo", pageInfo);
//		logger.debug("查询到的管理员分页列表：{}",pageInfo);
//		//转发到admin文件夹下 显示数据的页面
//		return "admin/user";
//	}
	
	
	
	
	//处理登录请求
//	@RequestMapping("/doLogin")
//	public String doLogin(TAdmin loginAdmin  , Model model , HttpSession session) {
//		//获取账号密码
//		//调用业务层处理登录业务
//		TAdmin  admin = adminService.doLogin(loginAdmin);
//		if(admin==null) {
//			//登录失败，转发到登录页面给出错误提示继续登录
//			model.addAttribute("errorMsg", "账号或密码错误!!");
//			return "login";
//		}else {
//			//登录成功，重定向到登录成功页面main.html，并显示登录成功的用户信息
//			//将用户对象共享到session域中：
//			session.setAttribute("admin", admin);
//			return "redirect:main.html";//重定向响应一般响应另外一个controller方法的访问地址，页面存放在web-inf下浏览器不能直接访问
//		}
//	}
	
	Logger logger = LoggerFactory.getLogger(getClass());
	@PreAuthorize("hasRole('PM - 项目经理')")  //设置访问当前路径时 需要的权限和角色
	//登录成功跳转到管理员主页面
	@RequestMapping("/main.html")
	public String  toTAdminMainPage(HttpSession session) {
		//main页面需要 显示数据库中的菜单
		//准备菜单数据
		List<TMenu> parentMenus = menuService.listParentsMenu();
		logger.debug("查询菜单集合：{}", parentMenus);
		//共享到域中:页面中点击多个连接跳转后的页面  仍然需要使用菜单，存到session域中
		session.setAttribute("parentMenus", parentMenus);
		//到页面中获取显示
		return "admin/main";
	}
	//处理注销同步请求
//	@RequestMapping("/logout")
//	public String  doLogout(HttpSession session) {
//		//销毁session域或者移除session域中保存的admin
//		session.invalidate();
//		//跳转到首页index页面
//		return "redirect:index";
//	}
	//处理异步注销请求的方法:  返回字符串 
//	@ResponseBody
//	@RequestMapping("/logout")
//	public String  doLogout(HttpSession session) {
//		//销毁session域或者移除session域中保存的admin
//		session.invalidate();
//		//跳转到首页index页面
//		return "ok";
//	}
	
	
	
}

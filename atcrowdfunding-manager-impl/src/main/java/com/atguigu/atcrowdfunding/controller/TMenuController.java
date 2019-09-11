package com.atguigu.atcrowdfunding.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.bean.TMenu;
import com.atguigu.atcrowdfunding.service.TMenuService;

@Controller
@RequestMapping("/menu")
public class TMenuController {
	@Autowired
	TMenuService menuService;
	@PreAuthorize("hasRole('MANAGER')")
	//跳转到 菜单维护页面的请求
	@RequestMapping("/index.html")
	public String toMenuIndexPage() {
		return "menu/menu";
	}
	//异步查询  菜单列表的请求
	@ResponseBody
	@RequestMapping("/listMenus")
	public List<TMenu> listMenus(){
		List<TMenu> menus = menuService.listMenus();
		return menus;
	}
	@ResponseBody
	@RequestMapping("/saveMenu")
	public String saveMenu(TMenu menu){
		menuService.saveMenu(menu);
		return "ok";
	}
	@ResponseBody
	@RequestMapping("/deleteMenu")
	public String deleteMenu(Integer id){
		menuService.deleteMenu(id);
		return "ok";
	}
	//查询指定的menu对象
	@ResponseBody
	@RequestMapping("/getMenu")
	public TMenu getMenu(Integer id){
		TMenu menu = menuService.getMenu(id);
		return menu;
	}
	@ResponseBody
	@RequestMapping("/updateMenu")
	public String getMenu(TMenu menu){
		menuService.updateMenu(menu);
		return "ok";
	}
}

package com.atguigu.atcrowdfunding.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.atcrowdfunding.bean.TMenu;
import com.atguigu.atcrowdfunding.mapper.TMenuMapper;
import com.atguigu.atcrowdfunding.service.TMenuService;
@Service
public class TMenuServiceImpl implements TMenuService {

	@Autowired
	TMenuMapper menuMapper;
	
	@Override
	public List<TMenu> listParentsMenu() {
		//查询所有的菜单集合
		List<TMenu> menus = menuMapper.selectByExample(null);
		//将菜单封装为父子菜单集合
		//1、挑选父菜单map集合： pid == 0
		//List<TMenu> parentMenus = new ArrayList<TMenu>();
		Map<Integer , TMenu> parentMap = new HashMap<Integer , TMenu>();
		for (TMenu tMenu : menus) {
			if(tMenu.getPid()==0) {
				//parentMenus.add(tMenu);
				parentMap.put(tMenu.getId(), tMenu);
			}
		}
		//2、遍历菜单集合：判断如果一个菜单的pid！=0  && pid 在父菜单集合中有 菜单和它一样，那么该菜单就是 此菜单的子菜单(将子菜单设置给父菜单的children集合)
		for (TMenu tMenu : menus) {
			if(tMenu.getPid()!=0) {
				//判断当前菜单的 pid  是否等于 parentMap中 某个父菜单的 id
				TMenu parentMenu = parentMap.get(tMenu.getPid());
				if(parentMenu!=null) {
					//将正在遍历的子菜单设置给父菜单
					parentMenu.getChildren().add(tMenu);
				}
			}
		}
		
		//3、将封装完毕的父菜单返回
		return new ArrayList<TMenu>(parentMap.values());
	}

	@Override
	public List<TMenu> listMenus() {
		return menuMapper.selectByExample(null);
	}

	@Override
	public void saveMenu(TMenu menu) {
		menuMapper.insertSelective(menu);
	}

	@Override
	public void deleteMenu(Integer id) {
		menuMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TMenu getMenu(Integer id) {
		return menuMapper.selectByPrimaryKey(id);
	}

	@Override
	public void updateMenu(TMenu menu) {
		menuMapper.updateByPrimaryKeySelective(menu);
	}

}

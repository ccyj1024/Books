package com.atguigu.atcrowdfunding.service;

import java.util.List;

import com.atguigu.atcrowdfunding.bean.TMenu;

public interface TMenuService {

	List<TMenu> listParentsMenu();

	List<TMenu> listMenus();

	void saveMenu(TMenu menu);

	void deleteMenu(Integer id);

	TMenu getMenu(Integer id);

	void updateMenu(TMenu menu);

}

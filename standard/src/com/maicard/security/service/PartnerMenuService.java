package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.MenuCriteria;
import com.maicard.security.domain.Menu;

public interface PartnerMenuService {

	int insert(Menu partnerMenu);

	int update(Menu partnerMenu);

	int delete(int menuId);
	
	Menu select(int menuId);

	List<Menu> list(MenuCriteria partnerMenuCriteria);

	List<Menu> listOnPage(MenuCriteria partnerMenuCriteria);


}

package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.MenuCriteria;
import com.maicard.security.domain.Menu;

public interface PartnerMenuDao {

	int insert(Menu partnerMenu) throws DataAccessException;

	int update(Menu partnerMenu) throws DataAccessException;

	int delete(int menuId) throws DataAccessException;

	Menu select(int menuId) throws DataAccessException;

	List<Menu> list(MenuCriteria partnerMenuCriteria) throws DataAccessException;
	
	List<Menu> listOnPage(MenuCriteria partnerMenuCriteria) throws DataAccessException;
	
	int count(MenuCriteria partnerMenuCriteria) throws DataAccessException;

}

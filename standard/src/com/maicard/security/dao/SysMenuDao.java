package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.MenuCriteria;
import com.maicard.security.domain.Menu;

public interface SysMenuDao {

	int insert(Menu menu) throws DataAccessException;

	int update(Menu menu) throws DataAccessException;

	int delete(int menuId) throws DataAccessException;

	Menu select(int menuId) throws DataAccessException;

	List<Menu> list(MenuCriteria menuCriteria) throws DataAccessException;
	
	List<Menu> listOnPage(MenuCriteria menuCriteria) throws DataAccessException;
	
	int count(MenuCriteria menuCriteria) throws DataAccessException;

}

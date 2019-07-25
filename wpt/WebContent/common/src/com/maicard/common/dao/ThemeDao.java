package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.ThemeCriteria;
import com.maicard.common.domain.Theme;

public interface ThemeDao {

	int insert(Theme theme) throws DataAccessException;
	void updateWrong(Theme theme) throws DataAccessException;

	int update(Theme theme) throws DataAccessException;

	int delete(int themeId) throws DataAccessException;

	Theme select(int themeId) throws DataAccessException;

	List<Theme> list(ThemeCriteria themeCriteria) throws DataAccessException;
	
	List<Theme> listOnPage(ThemeCriteria themeCriteria) throws DataAccessException;
	
	int count(ThemeCriteria themeCriteria) throws DataAccessException;
	List<Integer> listPk(ThemeCriteria themeCriteria);
	List<Integer> listPkOnPage(ThemeCriteria themeCriteria);

}

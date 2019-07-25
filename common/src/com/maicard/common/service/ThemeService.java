package com.maicard.common.service;

import java.util.List;

import com.maicard.common.criteria.ThemeCriteria;
import com.maicard.common.domain.Theme;

public interface ThemeService {

	int insert(Theme theme);
	
	int update(Theme theme);

	int delete(int themeId);
	
	Theme select(int themeId);

	List<Theme> list(ThemeCriteria themeCriteria);

	List<Theme> listOnPage(ThemeCriteria themeCriteria);
	
	int count(ThemeCriteria themeCriteria);

	Theme selectByUser(long uuid);
	
}

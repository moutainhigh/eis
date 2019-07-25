package com.maicard.site.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.site.criteria.DisplayTypeCriteria;
import com.maicard.site.domain.DisplayType;

public interface DisplayTypeDao {

	int insert(DisplayType displayType) throws DataAccessException;

	int update(DisplayType displayType) throws DataAccessException;

	int delete(int displayTypeId) throws DataAccessException;

	DisplayType select(int displayTypeId) throws DataAccessException;

	List<DisplayType> list(DisplayTypeCriteria displayTypeCriteria) throws DataAccessException;
	
	List<DisplayType> listOnPage(DisplayTypeCriteria displayTypeCriteria) throws DataAccessException;
	
	int count(DisplayTypeCriteria displayTypeCriteria) throws DataAccessException;

	List<Integer> listPkOnPage(DisplayTypeCriteria displayTypeCriteria)
			throws DataAccessException;

	List<Integer> listPk(DisplayTypeCriteria displayTypeCriteria)
			throws DataAccessException;

}

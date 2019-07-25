package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.AreaCriteria;
import com.maicard.common.domain.Area;

public interface AreaDao {

	int insert(Area area) throws DataAccessException;

	int update(Area area) throws DataAccessException;

	int delete(long areaId) throws DataAccessException;

	
	Area select(long areaId) throws DataAccessException;

	List<Area> list(AreaCriteria areaCriteria) throws DataAccessException;
	
	List<Area> listOnPage(AreaCriteria areaCriteria) throws DataAccessException;
	
	int count(AreaCriteria areaCriteria) throws DataAccessException;

	List<Long> listPk(AreaCriteria areaCriteria)
			throws DataAccessException;

	List<Long> listPkOnPage(AreaCriteria areaCriteria)
			throws DataAccessException;

}

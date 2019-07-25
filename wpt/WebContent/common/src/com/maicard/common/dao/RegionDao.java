package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.RegionCriteria;
import com.maicard.common.domain.Region;

public interface RegionDao {

	int insert(Region region) throws DataAccessException;

	int update(Region region) throws DataAccessException;

	int delete(int regionId) throws DataAccessException;

	Region select(int regionId) throws DataAccessException;

	List<Region> list(RegionCriteria regionCriteria) throws DataAccessException;
	
	List<Region> listOnPage(RegionCriteria regionCriteria) throws DataAccessException;
	
	int count(RegionCriteria regionCriteria) throws DataAccessException;

	List<Integer> listPkOnPage(RegionCriteria regionCriteria);

	List<Integer> listPk(RegionCriteria regionCriteria);

}

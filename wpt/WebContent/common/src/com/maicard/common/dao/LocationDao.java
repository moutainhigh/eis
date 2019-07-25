package com.maicard.common.dao;

import java.util.List;

import com.maicard.common.criteria.LocationCriteria;
import com.maicard.common.domain.Location;

public interface LocationDao {

	int insert(Location location) throws Exception;

	int update(Location location) throws Exception;

	Location select(LocationCriteria locationCriteria) throws Exception;

	List<Location> list(LocationCriteria locationCriteria) throws Exception;
	
	List<Location> listOnPage(LocationCriteria locationCriteria) throws Exception;
	
	int count(LocationCriteria locationCriteria) throws Exception;
	
	int delete(LocationCriteria locationCriteria);

	int updateBatch(List<Location> flushLocationList);

	

}

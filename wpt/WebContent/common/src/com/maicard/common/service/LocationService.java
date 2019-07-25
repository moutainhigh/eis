package com.maicard.common.service;

import java.util.List;

import com.maicard.common.criteria.LocationCriteria;
import com.maicard.common.domain.Location;

public interface LocationService {

	int insert(Location location);

	int update(Location location);
	
	Location select(LocationCriteria locationCriteria);

	int delete(LocationCriteria locationCriteria);
	
	List<Location> list(LocationCriteria locationCriteria);

	List<Location> listOnPage(LocationCriteria locationCriteria);
	
	int count(LocationCriteria locationCriteria);

	int updateBatch(List<Location> flushLocationList);
}

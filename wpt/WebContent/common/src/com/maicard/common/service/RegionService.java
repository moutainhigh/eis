package com.maicard.common.service;

import java.util.HashMap;
import java.util.List;

import com.maicard.common.criteria.RegionCriteria;
import com.maicard.common.domain.Region;

public interface RegionService {

	int insert(Region region);

	int update(Region region);

	int delete(int regionId);
	
	Region select(int regionId);

	List<Region> list(RegionCriteria regionCriteria);

	int count(RegionCriteria regionCriteria);

	List<Region> listOnPage(RegionCriteria regionCriteria);

	HashMap<String, Region> mapInTree(RegionCriteria regionCriteria);


}

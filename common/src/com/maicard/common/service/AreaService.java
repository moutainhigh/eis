package com.maicard.common.service;

import java.util.List;

import com.maicard.common.criteria.AreaCriteria;
import com.maicard.common.domain.Area;

public interface AreaService {

	int insert(Area area);

	int update(Area area);

	int delete(long areaId);
	
	Area select(long areaId);
	
	List<Area> list(AreaCriteria areaCriteria);

	List<Area> listOnPage(AreaCriteria areaCriteria);
	
	int count(AreaCriteria areaCriteria);


}

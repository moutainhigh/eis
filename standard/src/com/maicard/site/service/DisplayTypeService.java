package com.maicard.site.service;

import java.util.List;

import com.maicard.site.criteria.DisplayTypeCriteria;
import com.maicard.site.domain.DisplayType;

public interface DisplayTypeService {

	int insert(DisplayType displayType);

	int update(DisplayType displayType);

	int delete(int displayTypeId);
	
	DisplayType select(int displayTypeId);
	
	List<DisplayType> list(DisplayTypeCriteria displayTypeCriteria);

	List<DisplayType> listOnPage(DisplayTypeCriteria displayTypeCriteria);

}

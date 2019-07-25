package com.maicard.wpt.service;

import java.util.List;

import com.maicard.wpt.criteria.AutoResponseModelCriteria;
import com.maicard.wpt.domain.AutoResponseModel;

public interface AutoResponseModelService {
	int insert(AutoResponseModel autoResponseModel);

	int update(AutoResponseModel autoResponseModel);

	int delete(long autoResponseModelId);
	
	AutoResponseModel select(long autoResponseModelId);
	
	List<AutoResponseModel> list(AutoResponseModelCriteria autoResponseModelCriteria);

	List<AutoResponseModel> listOnPage(AutoResponseModelCriteria autoResponseModelCriteria);

	int count(AutoResponseModelCriteria autoResponseModelCriteria);

}

package com.maicard.common.service;

import java.util.HashMap;
import java.util.List;

import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;

public interface DataDefineService {

	int insert(DataDefine dataDefine);

	int update(DataDefine dataDefine);

	int delete(int dataDefineId);
	
	DataDefine select(int dataDefineId);
	
	DataDefine select(String dataCode);

	DataDefine select(DataDefineCriteria dataDefineCriteria);

	List<DataDefine> list(DataDefineCriteria dataDefineCriteria);

	List<DataDefine> listOnPage(DataDefineCriteria dataDefineCriteria);

	int count(DataDefineCriteria dataDefineCriteria);

	void delete(DataDefineCriteria dataDefineCriteria);

	HashMap<String, DataDefine> map(DataDefineCriteria dataDefineCriteria);

}

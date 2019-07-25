package com.maicard.common.service;

import java.util.List;

import com.maicard.common.criteria.DictCriteria;
import com.maicard.common.domain.Dict;

public interface DictService {

	int insert(Dict dictionary);

	int update(Dict dictionary);

	int delete(int dictId);
	
	Dict select(int dictId);
	
	Dict select(String dictData);
	
	List<Dict> list(DictCriteria dictionaryCriteria);

	List<Dict> listOnPage(DictCriteria dictionaryCriteria);
	
	int count(DictCriteria dictionaryCriteria);
}

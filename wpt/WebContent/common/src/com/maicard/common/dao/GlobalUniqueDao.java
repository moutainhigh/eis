package com.maicard.common.dao;


import java.util.List;

import com.maicard.common.criteria.GlobalUniqueCriteria;
import com.maicard.common.domain.GlobalUnique;

public interface GlobalUniqueDao {

	
	boolean exist(GlobalUnique gu) throws Exception;

	boolean create(GlobalUnique globalUnique) throws Exception;
	
	List<GlobalUnique> list(GlobalUniqueCriteria globalUniqueCriteria) throws Exception;

	int insertIgnore(GlobalUnique globalUnique);

	int delete(GlobalUnique globalUnique);

}

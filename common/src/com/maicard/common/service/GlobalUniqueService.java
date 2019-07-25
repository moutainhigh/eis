package com.maicard.common.service;

import com.maicard.common.criteria.GlobalUniqueCriteria;
import com.maicard.common.domain.GlobalUnique;


public interface GlobalUniqueService {
	
	boolean create(GlobalUnique globalUnique);

	boolean exist(GlobalUnique globalUnique);

	void syncDbToDistributed() throws Exception;

	void syncDistributedToDb();

	int getDistributedCount();

	long plusDistributedCount(int count);

	int count(GlobalUniqueCriteria globalUniqueCriteria);

	int delete(GlobalUnique globalUnique);

	long incrOrderSequence(int count);



}

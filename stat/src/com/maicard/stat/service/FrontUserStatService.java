package com.maicard.stat.service;

import java.util.List;

import com.maicard.stat.criteria.FrontUserStatCriteria;
import com.maicard.stat.domain.FrontUserStat;

public interface FrontUserStatService {
	
	List<FrontUserStat> listOnPage(FrontUserStatCriteria frontUserStatCriteria);

	int count(FrontUserStatCriteria frontUserStatCriteria);

}

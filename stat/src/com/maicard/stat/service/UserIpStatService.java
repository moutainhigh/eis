package com.maicard.stat.service;

import java.util.List;

import com.maicard.stat.criteria.UserIpStatCriteria;
import com.maicard.stat.domain.UserIpStat;

public interface UserIpStatService {
	
	List<UserIpStat> listOnPage(UserIpStatCriteria userIpStatCriteria) throws Exception;

	int count(UserIpStatCriteria userIpStatCriteria) throws Exception;
	
	

	
}


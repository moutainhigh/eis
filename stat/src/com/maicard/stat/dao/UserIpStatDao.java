package com.maicard.stat.dao;

import java.util.List;

import com.maicard.stat.criteria.UserIpStatCriteria;
import com.maicard.stat.domain.UserIpStat;

public interface UserIpStatDao {	
	

	List<UserIpStat> listOnPage(UserIpStatCriteria statCriteria) throws Exception;

	
	int count(UserIpStatCriteria statCriteria) throws Exception;

}

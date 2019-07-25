package com.maicard.common.service;

import java.util.List;

import com.maicard.common.criteria.SecurityLevelCriteria;
import com.maicard.common.domain.SecurityLevel;

public interface SecurityLevelService {

	int insert(SecurityLevel securityLevel);

	int update(SecurityLevel securityLevel) ;	

	int delete(int  level);

	SecurityLevel select(int level);

	List<SecurityLevel> list(SecurityLevelCriteria securityLevelCriteria);
	
	List<SecurityLevel> listOnPage(SecurityLevelCriteria securityLevelCriteria);

	int count(SecurityLevelCriteria securityLevelCriteria);

	
}

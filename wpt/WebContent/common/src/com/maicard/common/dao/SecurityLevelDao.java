package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.SecurityLevelCriteria;
import com.maicard.common.domain.SecurityLevel;

public interface SecurityLevelDao {

	int insert(SecurityLevel securityLevel) throws DataAccessException;

	int update(SecurityLevel securityLevel) throws DataAccessException;

	int delete(int level) throws DataAccessException;

	SecurityLevel select(int level) throws DataAccessException;

	List<SecurityLevel> list(SecurityLevelCriteria securityLevelCriteria) throws DataAccessException;
	
	List<SecurityLevel> listOnPage(SecurityLevelCriteria securityLevelCriteria) throws DataAccessException;
	
	int count(SecurityLevelCriteria securityLevelCriteria) throws DataAccessException;
	

	List<Long> listPk(SecurityLevelCriteria securityLevelCriteria)
			throws DataAccessException;

	List<Long> listPkOnPage(SecurityLevelCriteria securityLevelCriteria)
			throws DataAccessException;



}

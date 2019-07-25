package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.PasswordLogCriteria;
import com.maicard.security.domain.PasswordLog;

public interface PasswordLogDao {

	int insert(PasswordLog passwordLog) throws DataAccessException;

	PasswordLog select(int passwordLogId) throws DataAccessException;

	List<PasswordLog> list(PasswordLogCriteria passwordLogCriteria) throws DataAccessException;
	
	List<PasswordLog> listOnPage(PasswordLogCriteria passwordLogCriteria) throws DataAccessException;
	
	int count(PasswordLogCriteria passwordLogCriteria) throws DataAccessException;
		
	

}

package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.PasswordLogCriteria;
import com.maicard.security.domain.PasswordLog;

public interface PasswordLogService {

	int insert(PasswordLog passwordLog);	
	
	PasswordLog select(int passwordLogId);
	
	List<PasswordLog> list(PasswordLogCriteria passwordLogCriteria);

	List<PasswordLog> listOnPage(PasswordLogCriteria passwordLogCriteria);

	int count(PasswordLogCriteria passwordLogCriteria);

}

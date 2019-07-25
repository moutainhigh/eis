package com.maicard.site.service;

import java.util.List;

import com.maicard.site.criteria.UserReadLogCriteria;
import com.maicard.site.domain.UserReadLog;

public interface UserReadLogService {

	int insert(UserReadLog userReadLog);

	int update(UserReadLog userReadLog);

	int delete(int userReadLogId);
	
	int count(UserReadLogCriteria userReadLogCriteria);	
	
	UserReadLog select(int userReadLogId);
	
	List<UserReadLog> list(UserReadLogCriteria userReadLogCriteria);
	
	List<UserReadLog> listOnPage(UserReadLogCriteria userReadLogCriteria);

	UserReadLog readed(UserReadLogCriteria userReadLogCriteria);	
}

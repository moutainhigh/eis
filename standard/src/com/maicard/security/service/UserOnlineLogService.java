package com.maicard.security.service;

import java.util.Date;
import java.util.List;

import com.maicard.security.criteria.UserOnlineLogCriteria;
import com.maicard.security.domain.UserOnlineLog;

public interface UserOnlineLogService {

	int insert(UserOnlineLog userOnlineLog);

	int update(UserOnlineLog userOnlineLog);

	int delete(int userOnlineLogId);
	
	UserOnlineLog select(int userOnlineLogId);

	List<UserOnlineLog> list(UserOnlineLogCriteria userOnlineLogCriteria);

	List<UserOnlineLog> listOnPage(UserOnlineLogCriteria userOnlineLogCriteria);
	
	
	int count(UserOnlineLogCriteria userOnlineLogCriteria);

	void sync(long uuid, int onlineMode, Date date);

	void flushUserOnlineLogCache();

	int syncToDb(UserOnlineLog userOnlineLog);

	void listCache();
	
	UserOnlineLog getLastOnlineLog(long uuid);

	long getTotalOnlineTime(UserOnlineLogCriteria userOnlineLogCriteria);

}

package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.UserOnlineLogCriteria;
import com.maicard.security.domain.UserOnlineLog;

public interface UserOnlineLogDao {

	int insert(UserOnlineLog userOnlineLog) throws DataAccessException;

	int update(UserOnlineLog userOnlineLog) throws DataAccessException;

	int delete(int userOnlineLogId) throws DataAccessException;

	UserOnlineLog select(int userOnlineLogId) throws DataAccessException;

	List<UserOnlineLog> list(UserOnlineLogCriteria userOnlineLogCriteria) throws DataAccessException;
	
	List<UserOnlineLog> listOnPage(UserOnlineLogCriteria userOnlineLogCriteria) throws DataAccessException;
	
	int count(UserOnlineLogCriteria userOnlineLogCriteria) throws DataAccessException;


	int updateSameUserAndOnlineTimeLog(UserOnlineLog userOnlineLog);

	UserOnlineLog getLastOnlineLog(long uuid);

	long getTotalOnlineTime(UserOnlineLogCriteria userOnlineLogCriteria);

}

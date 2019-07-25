package com.maicard.site.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.site.criteria.UserReadLogCriteria;
import com.maicard.site.domain.UserReadLog;

public interface UserReadLogDao {

	int insert(UserReadLog userReadLog) throws DataAccessException;

	int update(UserReadLog userReadLog) throws DataAccessException;

	int delete(int userReadLogId) throws DataAccessException;

	UserReadLog select(int userReadLogId) throws DataAccessException;

	List<UserReadLog> list(UserReadLogCriteria userReadLogCriteria) throws DataAccessException;
	
	List<UserReadLog> listOnPage(UserReadLogCriteria userReadLogCriteria) throws DataAccessException;
	
	int count(UserReadLogCriteria userReadLogCriteria) throws DataAccessException;

	List<Integer> listPkOnPage(UserReadLogCriteria userReadLogCriteria)
			throws DataAccessException;

	List<Integer> listPk(UserReadLogCriteria userReadLogCriteria)
			throws DataAccessException;

}

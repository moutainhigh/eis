package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.UserLevelConditionCriteria;
import com.maicard.security.domain.UserLevelCondition;

public interface UserLevelConditionDao {

	int insert(UserLevelCondition userLevelCondition) throws DataAccessException;

	int update(UserLevelCondition userLevelCondition) throws DataAccessException;

	int delete(int userLevelConditionId) throws DataAccessException;

	UserLevelCondition select(int userLevelConditionId) throws DataAccessException;

	List<UserLevelCondition> list(UserLevelConditionCriteria userLevelConditionCriteria) throws DataAccessException;
	
	List<UserLevelCondition> listOnPage(UserLevelConditionCriteria userLevelConditionCriteria) throws DataAccessException;
	
	int count(UserLevelConditionCriteria userLevelConditionCriteria) throws DataAccessException;

}

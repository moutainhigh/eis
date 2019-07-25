package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.UserLevelProjectCriteria;
import com.maicard.security.domain.UserLevelProject;

public interface UserLevelProjectDao {

	int insert(UserLevelProject userLevelProject) throws DataAccessException;

	int update(UserLevelProject userLevelProject) throws DataAccessException;

	int delete(int userLevelProjectId) throws DataAccessException;

	UserLevelProject select(int userLevelProjectId) throws DataAccessException;

	List<UserLevelProject> list(UserLevelProjectCriteria userLevelProjectCriteria) throws DataAccessException;
	
	List<UserLevelProject> listOnPage(UserLevelProjectCriteria userLevelProjectCriteria) throws DataAccessException;
	
	int count(UserLevelProjectCriteria userLevelProjectCriteria) throws DataAccessException;

}

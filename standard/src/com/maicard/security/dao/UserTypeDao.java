package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.UserTypeCriteria;
import com.maicard.security.domain.UserType;

public interface UserTypeDao {

	int insert(UserType userType) throws DataAccessException;

	int update(UserType userType) throws DataAccessException;

	UserType select(int userTypeId) throws DataAccessException;

	List<UserType> list(UserTypeCriteria userTypeCriteria) throws DataAccessException;
	
	List<UserType> listOnPage(UserTypeCriteria userTypeCriteria) throws DataAccessException;
	
	int count(UserTypeCriteria userTypeCriteria) throws DataAccessException;
	
	int delete(int id);

	void deleteByCriteria(UserTypeCriteria userTypeCriteria);

}

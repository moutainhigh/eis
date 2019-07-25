package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.UserTypeCriteria;
import com.maicard.security.domain.UserType;

public interface UserTypeService {

	int insert(UserType userType);

	int update(UserType userType);

	int delete(int id);
	
	//void delete(UserTypeCriteria userTypeCriteria);
	
	UserType select(int id);
	
	UserType select(UserTypeCriteria userTypeCriteria);

	List<UserType> list(UserTypeCriteria userTypeCriteria);

	List<UserType> listOnPage(UserTypeCriteria userTypeCriteria);

	int count(UserTypeCriteria userTypeCriteria);

}

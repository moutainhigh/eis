package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.UserLevelProjectCriteria;
import com.maicard.security.domain.UserLevelProject;

public interface UserLevelProjectService {

	int insert(UserLevelProject userLevelProject);

	int update(UserLevelProject userLevelProject);

	int delete(int userLevelProjectId);
	
	UserLevelProject select(int userLevelProjectId);

	List<UserLevelProject> list(UserLevelProjectCriteria userLevelProjectCriteria);

	List<UserLevelProject> listOnPage(UserLevelProjectCriteria userLevelProjectCriteria);
	int count(UserLevelProjectCriteria userLevelProjectCriteria);

	UserLevelProject selectByLevel(long userLevelId);


}

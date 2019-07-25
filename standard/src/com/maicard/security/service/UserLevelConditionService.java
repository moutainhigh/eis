package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.UserLevelConditionCriteria;
import com.maicard.security.domain.UserLevelCondition;

public interface UserLevelConditionService {

	int insert(UserLevelCondition userLevelCondition);

	int update(UserLevelCondition userLevelCondition);

	int delete(int userLevelConditionId);
	
	UserLevelCondition select(int userLevelConditionId);

	List<UserLevelCondition> list(UserLevelConditionCriteria userLevelConditionCriteria);

	List<UserLevelCondition> listOnPage(UserLevelConditionCriteria userLevelConditionCriteria);

}

package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.UserRelation;

public interface FrontUserRelationService {

	int insert(UserRelation userRelation);

	int update(UserRelation userRelation);

	int delete(int fuuid);
	
	UserRelation select(int fuuid);

	List<UserRelation> list(UserRelationCriteria userRelationCriteria);

	List<UserRelation> listOnPage(UserRelationCriteria userRelationCriteria);
	
	int count(UserRelationCriteria userRelationCriteria);
	
}

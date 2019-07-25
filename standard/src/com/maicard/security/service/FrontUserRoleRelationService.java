package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.UserRoleRelationCriteria;
import com.maicard.security.domain.UserRoleRelation;

public interface FrontUserRoleRelationService {

	int insert(UserRoleRelation userRoleRelation);

	int update(UserRoleRelation userRoleRelation);

	int delete(int frontUserRoleRelationId);
	
	UserRoleRelation select(int frontUserRoleRelationId);

	List<UserRoleRelation> list(UserRoleRelationCriteria frontUserRoleRelationCriteria);

	List<UserRoleRelation> listOnPage(UserRoleRelationCriteria frontUserRoleRelationCriteria);
	
	void deleteByFuuid(long fuuid);	

}

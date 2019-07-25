package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.UserRoleRelationCriteria;
import com.maicard.security.domain.UserRoleRelation;

public interface PartnerRoleRelationService {

	int insert(UserRoleRelation partnerRoleRelation);

	int update(UserRoleRelation partnerRoleRelation);

	int delete(int partnerRoleRelationId);
	void deleteByUuid(long uuid);	
	
	UserRoleRelation select(int partnerRoleRelationId);

	List<UserRoleRelation> list(UserRoleRelationCriteria partnerRoleRelationCriteria);

	List<UserRoleRelation> listOnPage(UserRoleRelationCriteria partnerRoleRelationCriteria);

}

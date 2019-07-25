package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.UserRelation;

public interface PartnerRelationService {

	int insert(UserRelation partnerRelation);

	int update(UserRelation partnerRelation);

	int delete(int partnerRelationId);
	
	UserRelation select(int partnerRelationId);

	List<UserRelation> list(UserRelationCriteria partnerRelationCriteria);

	List<UserRelation> listOnPage(UserRelationCriteria partnerRelationCriteria);
	
	int count(UserRelationCriteria userRelationCriteria);


}

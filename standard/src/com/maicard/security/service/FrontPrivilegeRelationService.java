package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.PrivilegeRelationCriteria;
import com.maicard.security.domain.PrivilegeRelation;

public interface FrontPrivilegeRelationService {

	int insert(PrivilegeRelation frontPrivilegeRelation);

	int update(PrivilegeRelation frontPrivilegeRelation);

	int delete(int privilegeId);
	
	void deleteByFrontPrivilegeId(int frontPrivilegeId);

	PrivilegeRelation select(int privilegeId);

	List<PrivilegeRelation> list(PrivilegeRelationCriteria frontPrivilegeRelationCriteria);

	List<PrivilegeRelation> listOnPage(PrivilegeRelationCriteria frontPrivilegeRelationCriteria);

}

package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.PrivilegeRoleRelationCriteria;
import com.maicard.security.domain.PrivilegeRoleRelation;

public interface FrontPrivilegeRoleRelationService {

	int insert(PrivilegeRoleRelation privilegeRoleRelation);

	int update(PrivilegeRoleRelation privilegeRoleRelation);

	int delete(int frontPrivilegeRoleRelationId);
	
	PrivilegeRoleRelation select(int frontPrivilegeRoleRelationId);

	List<PrivilegeRoleRelation> list(PrivilegeRoleRelationCriteria frontPrivilegeRoleRelationCriteria);

	List<PrivilegeRoleRelation> listOnPage(PrivilegeRoleRelationCriteria frontPrivilegeRoleRelationCriteria);
	void deleteByRoleId(int frontRoleId);
}

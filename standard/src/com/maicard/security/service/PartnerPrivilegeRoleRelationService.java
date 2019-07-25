package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.PrivilegeRoleRelationCriteria;
import com.maicard.security.domain.PrivilegeRoleRelation;

public interface PartnerPrivilegeRoleRelationService {

	void insert(PrivilegeRoleRelation privilegeRoleRelation);

	int update(PrivilegeRoleRelation privilegeRoleRelation);

	int delete(int privilegeRoleRelationId);
	
	
	PrivilegeRoleRelation select(int PrivilegeRoleRelationId);

	List<PrivilegeRoleRelation> list(PrivilegeRoleRelationCriteria privilegeRoleRelationCriteria);

	List<PrivilegeRoleRelation> listOnPage(PrivilegeRoleRelationCriteria privilegeRoleRelationCriteria);

}

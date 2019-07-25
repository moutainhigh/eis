package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.MenuRoleRelationCriteria;
import com.maicard.security.domain.Menu;
import com.maicard.security.domain.MenuRoleRelation;

public interface PartnerMenuRoleRelationService {

	int insert(MenuRoleRelation partnerMenuRoleRelation);

	int update(MenuRoleRelation partnerMenuRoleRelation);

	int delete(int partnerMenuRoleRelationId);
	
	void deleteByGroupId(int groupId);
	
	MenuRoleRelation select(int partnerMenuRoleRelationId);

	List<MenuRoleRelation> list(MenuRoleRelationCriteria partnerMenuRoleRelationCriteria);

	List<MenuRoleRelation> listOnPage(MenuRoleRelationCriteria partnerMenuRoleRelationCriteria);
	
	List<Menu> listInTree(MenuRoleRelationCriteria partnerMenuRoleRelationCriteria);

}

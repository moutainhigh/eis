package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.MenuRoleRelationCriteria;
import com.maicard.security.domain.MenuRoleRelation;

public interface PartnerMenuRoleRelationDao {

	int insert(MenuRoleRelation partnerMenuRoleRelation) throws DataAccessException;

	int update(MenuRoleRelation partnerMenuRoleRelation) throws DataAccessException;

	int delete(int partnerMenuRoleRelationId) throws DataAccessException;
	void deleteByGroupId(int groupId) throws DataAccessException;

	MenuRoleRelation select(int partnerMenuRoleRelationId) throws DataAccessException;

	List<MenuRoleRelation> list(MenuRoleRelationCriteria partnerMenuRoleRelationCriteria) throws DataAccessException;
	
	List<MenuRoleRelation> listOnPage(MenuRoleRelationCriteria partnerMenuRoleRelationCriteria) throws DataAccessException;
	
	int count(MenuRoleRelationCriteria partnerMenuRoleRelationCriteria) throws DataAccessException;

}

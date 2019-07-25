package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.MenuRoleRelationCriteria;
import com.maicard.security.domain.MenuRoleRelation;

public interface SysMenuRoleRelationDao {

	int insert(MenuRoleRelation menuRoleRelation) throws DataAccessException;

	int update(MenuRoleRelation menuRoleRelation) throws DataAccessException;

	int delete(int id) throws DataAccessException;
	
	void deleteByGroupId(int groupId) throws DataAccessException;

	MenuRoleRelation select(int id) throws DataAccessException;

	List<MenuRoleRelation> list(MenuRoleRelationCriteria menuRoleRelationCriteria) throws DataAccessException;
	
	List<MenuRoleRelation> listOnPage(MenuRoleRelationCriteria menuRoleRelationCriteria) throws DataAccessException;
	
	int count(MenuRoleRelationCriteria menuRoleRelationCriteria) throws DataAccessException;

}

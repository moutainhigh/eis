package com.maicard.site.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.site.criteria.DocumentNodeRelationCriteria;
import com.maicard.site.domain.DocumentNodeRelation;

public interface DocumentNodeRelationDao {

	int insert(DocumentNodeRelation documentNodeRelation) throws DataAccessException;

	int update(DocumentNodeRelation documentNodeRelation) throws DataAccessException;

	DocumentNodeRelation select(int documentNodeRelationId) throws DataAccessException;

	List<DocumentNodeRelation> list(DocumentNodeRelationCriteria documentNodeRelationCriteria) throws DataAccessException;
	
	List<DocumentNodeRelation> listOnPage(DocumentNodeRelationCriteria documentNodeRelationCriteria) throws DataAccessException;
	
	int count(DocumentNodeRelationCriteria documentNodeRelationCriteria) throws DataAccessException;

	int delete(DocumentNodeRelationCriteria documentNodeRelationCriteria);

}

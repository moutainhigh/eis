package com.maicard.site.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.site.criteria.DocumentTypeCriteria;
import com.maicard.site.domain.DocumentType;

public interface DocumentTypeDao {

	int insert(DocumentType documentType) throws DataAccessException;

	int update(DocumentType documentType) throws DataAccessException;

	int delete(int documentTypeId) throws DataAccessException;

	DocumentType select(int documentTypeId) throws DataAccessException;

	List<DocumentType> list(DocumentTypeCriteria documentTypeCriteria) throws DataAccessException;
	
	List<DocumentType> listOnPage(DocumentTypeCriteria documentTypeCriteria) throws DataAccessException;
	
	int count(DocumentTypeCriteria documentTypeCriteria) throws DataAccessException;

	List<Integer> listPk(DocumentTypeCriteria documentTypeCriteria)
			throws DataAccessException;

	List<Integer> listPkOnPage(DocumentTypeCriteria documentTypeCriteria)
			throws DataAccessException;

}

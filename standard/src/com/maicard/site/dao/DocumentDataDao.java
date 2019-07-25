package com.maicard.site.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.site.criteria.DocumentDataCriteria;
import com.maicard.site.domain.DocumentData;

public interface DocumentDataDao {

	int insert(DocumentData documentData) throws DataAccessException;

	int update(DocumentData documentData) throws DataAccessException;

	int delete(int documentDataId) throws DataAccessException;
	
	DocumentData select(int documentDataId) throws DataAccessException;

	List<DocumentData> list(DocumentDataCriteria documentDataCriteria) throws DataAccessException;
	
	List<DocumentData> listOnPage(DocumentDataCriteria documentDataCriteria) throws DataAccessException;
	
	DocumentData matchByContent(DocumentDataCriteria documentDataCriteria) throws DataAccessException;
	
	int count(DocumentDataCriteria documentDataCriteria) throws DataAccessException;

	void delete(DocumentDataCriteria documentDataCriteria);

	List<Integer> listPk(DocumentDataCriteria documentDataCriteria)
			throws DataAccessException;

	List<Integer> listPkOnPage(DocumentDataCriteria documentDataCriteria)
			throws DataAccessException;

}

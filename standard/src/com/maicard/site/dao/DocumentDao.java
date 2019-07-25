package com.maicard.site.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.domain.Document;

public interface DocumentDao {

	int insert(Document document) throws DataAccessException;

	int update(Document document) throws DataAccessException;

	int delete(int udid) throws DataAccessException;

	Document select(int udid) throws DataAccessException;

	List<Document> list(DocumentCriteria documentCriteria) throws DataAccessException;
	
	List<Document> listOnPage(DocumentCriteria documentCriteria) throws DataAccessException;
		
	List<Integer> listPk(DocumentCriteria documentCriteria) throws DataAccessException;
	
	List<Integer> listPkOnPage(DocumentCriteria documentCriteria) throws DataAccessException;
	
	
	int count(DocumentCriteria documentCriteria) throws DataAccessException;

	Document selectNoCache(int udid) throws DataAccessException;
	

	


}

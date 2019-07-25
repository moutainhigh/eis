package com.maicard.site.service;

import java.util.List;

import com.maicard.site.criteria.DocumentNodeRelationCriteria;
import com.maicard.site.domain.DocumentNodeRelation;

public interface DocumentNodeRelationService {

	int insert(DocumentNodeRelation documentNodeRelation);

	int update(DocumentNodeRelation documentNodeRelation);
	
	DocumentNodeRelation select(int documentNodeRelationId);

	List<DocumentNodeRelation> list(DocumentNodeRelationCriteria documentNodeRelationCriteria);

	List<DocumentNodeRelation> listOnPage(DocumentNodeRelationCriteria documentNodeRelationCriteria);

	int delete(DocumentNodeRelationCriteria documentNodeRelationCriteria);

}

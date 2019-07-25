package com.maicard.site.service;

import java.util.List;

import com.maicard.site.criteria.DocumentTypeCriteria;
import com.maicard.site.domain.DocumentType;

public interface DocumentTypeService {

	int insert(DocumentType documentType);

	int update(DocumentType documentType);

	int delete(int documentTypeId);
	
	DocumentType select(int documentTypeId);

	DocumentType select(int documentTypeId, String fetchMode);
	
	DocumentType select(String documentTypeCode, String fetchMode);

	List<DocumentType> list(DocumentTypeCriteria documentTypeCriteria);

	List<DocumentType> listOnPage(DocumentTypeCriteria documentTypeCriteria);
	
	int count(DocumentTypeCriteria documentTypeCriteria);

}

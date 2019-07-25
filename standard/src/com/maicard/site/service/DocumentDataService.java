package com.maicard.site.service;

import java.util.HashMap;
import java.util.List;

import com.maicard.site.criteria.DocumentDataCriteria;
import com.maicard.site.domain.DocumentData;

public interface DocumentDataService {

	int insert(DocumentData documentData);

	int update(DocumentData documentData);

	int delete(int documentDataId);
		
	DocumentData select(int documentDataId);
	//DocumentData selectExtra(int udid, int documentColumnId);

	List<DocumentData> list(DocumentDataCriteria documentDataCriteria);

	List<DocumentData> listOnPage(DocumentDataCriteria documentDataCriteria);
	HashMap<String, DocumentData> map(DocumentDataCriteria documentDataCriteria);

	
	//以content匹配方式查找数据
	DocumentData matchByContent(DocumentDataCriteria documentDataCriteria);

	void delete(DocumentDataCriteria documentDataCriteria);
}

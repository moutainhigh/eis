package com.maicard.site.service;

import java.util.List;

import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.domain.Document;

public interface SpiderService {

	//根据条件抓取并返回指定的外部文档
	List<Document> list(DocumentCriteria documentCriteria);
	

}

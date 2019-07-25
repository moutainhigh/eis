package com.maicard.site.service;

import java.util.List;

import javax.jms.ObjectMessage;

import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.domain.Document;

public interface DocumentService {
		
	int insert(Document document) throws Exception;

	int update(Document document) throws Exception;

	int delete(int udid) throws Exception;
	
	Document select(int udid);
	
	void rebuildPdf(Document document);
	
	int changeStatus(int udid, int status) throws Exception;
		
	List<Document> list(DocumentCriteria documentCriteria) throws Exception;
	
	List<Document> listOnPage(DocumentCriteria documentCriteria) throws Exception;		
	
	int count(DocumentCriteria documentCriteria);	

	boolean postProcess(ObjectMessage objectMessage) throws Exception;

	Document select(String documentCode, long ownerId);

	List<Document> listFreshDocument();

	Document selectNoCache(int udid);

	/**
	 * 将文档扩展数据中的文件路径修改为带下载前缀的URL绝对路径以供显示
	 * @param document
	 */
	//void processDataPath(Document document);
}

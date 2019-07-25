package com.maicard.site.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.annotation.IgnoreJmsDataSync;
import com.maicard.common.base.BaseService;
import com.maicard.common.service.ConfigService;
import com.maicard.exception.RequiredAttributeIsNullException;
import com.maicard.site.criteria.DocumentNodeRelationCriteria;
import com.maicard.site.dao.DocumentNodeRelationDao;
import com.maicard.site.domain.DocumentNodeRelation;
import com.maicard.site.service.DocumentNodeRelationService;
import com.maicard.standard.BasicStatus;

@Service
public class DocumentNodeRelationServiceImpl extends BaseService implements DocumentNodeRelationService {

	@Resource
	private DocumentNodeRelationDao documentNodeRelationDao;
	
	@Resource
	private ConfigService configService;
	


	@IgnoreJmsDataSync
	public int insert(DocumentNodeRelation documentNodeRelation) {
		return documentNodeRelationDao.insert(documentNodeRelation);
		
	}

	@IgnoreJmsDataSync
	public int update(DocumentNodeRelation documentNodeRelation) {
		int actualRowsAffected = 0;
		
		int documentNodeRelationId = documentNodeRelation.getDocumentNodeRelationId();

		DocumentNodeRelation _oldDocumentNodeRelation = documentNodeRelationDao.select(documentNodeRelationId);
		
		if (_oldDocumentNodeRelation != null) {
			actualRowsAffected = documentNodeRelationDao.update(documentNodeRelation);
		}
		
		return actualRowsAffected;
	}

	@Override
	@IgnoreJmsDataSync
	public int delete(DocumentNodeRelationCriteria documentNodeRelationCriteria) {
		
		if(documentNodeRelationCriteria == null){
			throw new RequiredAttributeIsNullException("删除条件为空");
		}
		if(documentNodeRelationCriteria.getDocumentNodeRelationId() == 0 && documentNodeRelationCriteria.getUdid() == 0 && documentNodeRelationCriteria.getNodeId() == 0){
			throw new RequiredAttributeIsNullException("缺少必要的删除条件");
		}
		
		int allCount = documentNodeRelationDao.count(new DocumentNodeRelationCriteria());		
		List<DocumentNodeRelation> _oldDocumentNodeRelationList = documentNodeRelationDao.list(documentNodeRelationCriteria);
		if(_oldDocumentNodeRelationList == null || _oldDocumentNodeRelationList.size() < 1){
			return 0;
		}
		if(_oldDocumentNodeRelationList.size() == allCount){
			logger.error("按删除条件操作将删除所有记录");
			return 0;
		}
		int realDeleted = documentNodeRelationDao.delete(documentNodeRelationCriteria);
		logger.debug("应删除[" + _oldDocumentNodeRelationList.size() + "]条记录，实际删除[" + realDeleted + "]条记录");
		int[]affectedNodeIds = new int[_oldDocumentNodeRelationList.size()];
		for(int i = 0; i < _oldDocumentNodeRelationList.size(); i++){
			affectedNodeIds[i] = _oldDocumentNodeRelationList.get(i).getNodeId();
		}		
		return realDeleted;
	}

	
	public DocumentNodeRelation select(int documentNodeRelationId) {
		DocumentNodeRelation documentNodeRelation  = documentNodeRelationDao.select(documentNodeRelationId);
		documentNodeRelation.setStatusName(BasicStatus.normal.findById(documentNodeRelation.getCurrentStatus()).getName());
		return documentNodeRelation;

	}

	public List<DocumentNodeRelation> list(DocumentNodeRelationCriteria documentNodeRelationCriteria) {
		List<DocumentNodeRelation> documentNodeRelationList = documentNodeRelationDao.list(documentNodeRelationCriteria);
		for(int i = 0; i < documentNodeRelationList.size(); i++){
			documentNodeRelationList.get(i).setStatusName(BasicStatus.normal.findById(documentNodeRelationList.get(i).getCurrentStatus()).getName());
		}
		return documentNodeRelationList;
	}
	
	public List<DocumentNodeRelation> listOnPage(DocumentNodeRelationCriteria documentNodeRelationCriteria) {
		List<DocumentNodeRelation> documentNodeRelationList = documentNodeRelationDao.listOnPage(documentNodeRelationCriteria);
		for(int i = 0; i < documentNodeRelationList.size(); i++){
			documentNodeRelationList.get(i).setStatusName(BasicStatus.normal.findById(documentNodeRelationList.get(i).getCurrentStatus()).getName());
		}
		return documentNodeRelationList;
	}

	

}

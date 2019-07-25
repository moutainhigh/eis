package com.maicard.site.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.service.DataDefineService;
import com.maicard.site.criteria.*;
import com.maicard.site.dao.*;
import com.maicard.site.domain.DocumentType;
import com.maicard.site.service.DocumentTypeService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.ObjectType;
import com.maicard.standard.CommonStandard.DataFetchMode;


@Service
public class DocumentTypeServiceImpl extends BaseService implements DocumentTypeService {

	@Resource
	private DocumentTypeDao documentTypeDao;
	@Resource
	private DataDefineService dataDefineService;

	public int insert(DocumentType documentType) {
		int rs = 0;
		try{
			rs = documentTypeDao.insert(documentType);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		if(rs != 1){
			logger.error("新增文档类型失败,数据操作未返回1");
			return -1;
		}

		if(documentType.getDataDefineMap() != null){
			if(documentType.getDataDefineMap().size() > 0){
				//保险起见，即使是新增也先尝试删除一遍
				DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
				dataDefineCriteria.setObjectType(ObjectType.document.toString());
				dataDefineCriteria.setObjectId(documentType.getDocumentTypeId());
				dataDefineService.delete(dataDefineCriteria);
				for(int i = 0; i < documentType.getDataDefineMap().size(); i++){
					documentType.getDataDefineMap().get(i).setObjectType(ObjectType.document.toString());
					documentType.getDataDefineMap().get(i).setObjectId(documentType.getDocumentTypeId());
					documentType.getDataDefineMap().get(i).setCurrentStatus(BasicStatus.normal.getId());
					dataDefineService.insert(documentType.getDataDefineMap().get(i));
				}
			}
		}

		return 1;
	}

	public int update(DocumentType documentType) {
		//logger.info("更新时的大小:" + documentType.getDocumentDataDefineMap().size());
		int actualRowsAffected = 0;

		int documentTypeId = documentType.getDocumentTypeId();

		DocumentType _oldDocumentType = documentTypeDao.select(documentTypeId);

		if (_oldDocumentType == null) {
			return 0;
		}
		try{
			actualRowsAffected = documentTypeDao.update(documentType);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());

		}

		logger.info("update documentColumnList:" + documentType.getDataDefineMap().size());
		if(documentType.getDataDefineMap() == null || documentType.getDataDefineMap().size() < 1){
			//没有任何关联关系，应当清空
			DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
			dataDefineCriteria.setObjectType(ObjectType.document.toString());
			dataDefineCriteria.setObjectId(documentType.getDocumentTypeId());
			dataDefineService.delete(dataDefineCriteria);
		} else if(documentType.getDataDefineMap() != null && documentType.getDataDefineMap().size() > 0){
			//FIXME 	dataDefineService.sync(documentType.getDataDefineMap());

			//			for(int i = 0; i < documentType.getDocumentDataDefineMap().size(); i++){
			//				documentType.getDocumentDataDefineMap().get(i).setDocumentTypeId(documentType.getDocumentTypeId());
			//				if(documentType.getDocumentDataDefineMap().get(i).getCurrentStatus() != CommonStandard.Operate.delete.getId()){
			//					documentType.getDocumentDataDefineMap().get(i).setCurrentStatus(CommonStandard.BasicStatus.normal.getId());
			//				}
			//				documentDataDefineService.sync(documentType.getDocumentDataDefineMap().get(i));
			//			}

		} 
		return actualRowsAffected;
	}

	public int delete(int documentTypeId) {
		int actualRowsAffected = 0;

		DocumentType _oldDocumentType = documentTypeDao.select(documentTypeId);

		if (_oldDocumentType != null) {
			actualRowsAffected = documentTypeDao.delete(documentTypeId);
		}
		return actualRowsAffected;
	}


	public DocumentType select(int documentTypeId){
		return select(documentTypeId, DataFetchMode.full.toString());
	}

	public List<DocumentType> list(DocumentTypeCriteria documentTypeCriteria) {
		List<Integer> idList = documentTypeDao.listPk(documentTypeCriteria);

		if(idList == null || idList.size() < 1){
			return Collections.emptyList();
		}
		List<DocumentType> documentTypeList =  new ArrayList<DocumentType> ();		
		for(Integer id : idList){
			DocumentType documentType = documentTypeDao.select(id);
			if(documentType != null){
				afterFetch(documentType);
				documentTypeList.add(documentType);
			}
		}
		idList = null;
		return documentTypeList;

		/*
		List<DocumentType> documentTypeList = documentTypeDao.list(documentTypeCriteria);
		if(documentTypeList == null){
			return null;
		}
		for(int i = 0; i < documentTypeList.size(); i ++){
			documentTypeList.get(i).setIndex(i+1);		
			afterFetch(documentTypeList.get(i));
		}
		return documentTypeList;
		 */
	}

	public List<DocumentType> listOnPage(DocumentTypeCriteria documentTypeCriteria) {
		List<Integer> idList = documentTypeDao.listPkOnPage(documentTypeCriteria);

		if(idList == null || idList.size() < 1){
			return Collections.emptyList();
		}
		List<DocumentType> documentTypeList =  new ArrayList<DocumentType> ();		
		for(Integer id : idList){
			DocumentType documentType = documentTypeDao.select(id);
			if(documentType != null){
				afterFetch(documentType);
				documentTypeList.add(documentType);
			}
		}
		idList = null;
		return documentTypeList;

		/*
		List<DocumentType> documentTypeList = documentTypeDao.listOnPage(documentTypeCriteria);
		if(documentTypeList == null){
			return null;
		}
		for(int i = 0; i < documentTypeList.size(); i ++){
			documentTypeList.get(i).setIndex(i+1);
			afterFetch(documentTypeList.get(i));
		}
		return documentTypeList;
		 */
	}

	public int count(DocumentTypeCriteria documentTypeCriteria){
		return documentTypeDao.count(documentTypeCriteria);
	}

	private void afterFetch(DocumentType documentType){
		documentType.setId(documentType.getDocumentTypeId());
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria(); 
		dataDefineCriteria.setObjectType(ObjectType.document.toString());
		dataDefineCriteria.setObjectId(documentType.getDocumentTypeId());
		HashMap<String, DataDefine> documentDataDefineMap = dataDefineService.map(dataDefineCriteria);
		if(documentDataDefineMap != null){
			logger.debug("该文档类型[" + documentType.getDocumentTypeId() + "]包含" + documentDataDefineMap.size() + "个自定义字段");
			documentType.setDataDefineMap(documentDataDefineMap);
		}
	}

	@Override
	public DocumentType select(int documentTypeId, String fetchMode) {
		DocumentType documentType =  documentTypeDao.select(documentTypeId);
		if(fetchMode != null && fetchMode.equals(DataFetchMode.simple.toString())){
			return documentType;
		}
		afterFetch(documentType);
		return documentType;
	}

	@Override
	public DocumentType select(String documentTypeCode, String fetchMode) {
		DocumentTypeCriteria documentTypeCriteria = new DocumentTypeCriteria();
		documentTypeCriteria.setDocumentTypeCode(documentTypeCode);
		List<DocumentType> documentTypeList = list(documentTypeCriteria);
		if(documentTypeList != null && documentTypeList.size() > 0){
			return documentTypeList.get(0);
		}
		return null;
	}


}

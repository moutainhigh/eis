package com.maicard.site.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.annotation.IgnoreJmsDataSync;
import com.maicard.common.base.BaseService;
import com.maicard.common.service.ConfigService;
import com.maicard.exception.RequiredAttributeIsNullException;
import com.maicard.site.criteria.DocumentDataCriteria;
import com.maicard.site.dao.DocumentDataDao;
import com.maicard.site.domain.DocumentData;
import com.maicard.site.service.DocumentDataService;

@Service
public class DocumentDataServiceImpl extends BaseService implements DocumentDataService {
	@Resource
	private DocumentDataDao documentDataDao;

	@Resource
	private ConfigService configService;

	/*private String documentDownloadFakePath;

	@PostConstruct
	public void init(){
		documentDownloadFakePath = configService.getValue(DataName.documentDownloadFakePath.toString(),0);
		if(StringUtils.isBlank(documentDownloadFakePath)){
			documentDownloadFakePath = "/static";
		}
	}*/

	@IgnoreJmsDataSync
	public int insert(DocumentData documentData) {
		if(documentData == null){
			return 0;
		}

		/*if(documentData.getDataValue() != null && documentData.getDataValue().startsWith(documentDownloadFakePath)){
			documentData.setDataValue(
					documentData.getDataValue().replaceFirst(documentDownloadFakePath + "/" + CommonStandard.documentForLoginSaveDir + "/", "")
					.replaceFirst(documentDownloadFakePath + "/" + CommonStandard.documentForSubscribeSaveDir + "/", "")
					.replaceFirst(documentDownloadFakePath + "/" + CommonStandard.documentOpenSaveDir + "/", "")
					);
			logger.info("去除数据[" + documentData.getDataCode() + "]中的前缀:" + documentData.getDataValue());
		} else {
			logger.info("无需去除");
		}*/



		try{
			return documentDataDao.insert(documentData);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;		
	}

	@IgnoreJmsDataSync
	public int update(DocumentData documentData) {
		if(documentData == null){
			logger.error("尝试更新的documentData为空");
			return 0;
		}
		/*if(documentData.getDataValue() != null && documentData.getDataValue().startsWith(documentDownloadFakePath)){
			documentData.setDataValue(
					documentData.getDataValue().replaceFirst(documentDownloadFakePath + "/" + CommonStandard.documentForLoginSaveDir + "/", "")
					.replaceFirst(documentDownloadFakePath + "/" + CommonStandard.documentForSubscribeSaveDir + "/", "")
					.replaceFirst(documentDownloadFakePath + "/" + CommonStandard.documentOpenSaveDir + "/", "")
					);
		}*/
		try{
			return  documentDataDao.update(documentData);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	}

	@IgnoreJmsDataSync
	public int delete(int documentDataId) {
		try{
			return documentDataDao.delete(documentDataId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}

	@IgnoreJmsDataSync
	@Override
	public void delete(DocumentDataCriteria documentDataCriteria){
		if(documentDataCriteria == null){
			throw new RequiredAttributeIsNullException("documentDataCriteria为空");
		}
		if(documentDataCriteria.getDataDefineId() == 0 && documentDataCriteria.getUdid() ==0){
			throw new RequiredAttributeIsNullException("documentDataCriteria至少要指定dataDefineId和udid中的一个");		
		}
		try{
			documentDataDao.delete(documentDataCriteria);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}

	}


	public DocumentData select(int documentDataId) {
		DocumentData documentData = documentDataDao.select(documentDataId);
		//afterFetch(documentData);
		return documentData;
	}

	public 	DocumentData selectExtra(int udid, int dataDefineId){
		DocumentDataCriteria documentDataCriteria = new DocumentDataCriteria();
		documentDataCriteria.setUdid(udid);
		documentDataCriteria.setDataDefineId(dataDefineId);		
		List<DocumentData> documentDataList = list(documentDataCriteria);
		if(documentDataList == null){
			return null;
		}
		if(documentDataList.size() == 1){
			return documentDataList.get(0);
		}
		return null;

	}

	/*缓存模式
	 public List<DocumentData> list(DocumentDataCriteria documentDataCriteria) {
		List<Integer> idList = documentDataDao.listPk(documentDataCriteria);
		if(logger.isDebugEnabled()){
			logger.debug("根据条件[udid=" + documentDataCriteria.getUdid() + "]查询到的文档数据PK是" + (idList == null ? "空" : idList.size()));
		}
		if(idList == null || idList.size() < 1){
			return null;
		}
		List<DocumentData> documentDataList = new ArrayList<DocumentData>();

		for(int i = 0; i < idList.size(); i++){
			DocumentData dd = documentDataDao.select(idList.get(i));
			if(dd != null){
				dd.setIndex(i+1);
				afterFetch(dd);
				documentDataList.add(dd);
			}
		}
		return documentDataList;
	}*/

	@Override
	//非缓存模式
	public List<DocumentData> list(DocumentDataCriteria documentDataCriteria) {

		List<DocumentData> documentDataList = documentDataDao.list(documentDataCriteria);
		if(documentDataList == null || documentDataList.size() < 1){
			return Collections.emptyList();
		}
		for(int i = 0; i < documentDataList.size(); i++){
			documentDataList.get(i).setIndex(i+1);
			//	afterFetch(documentDataList.get(i));

		}

		return documentDataList;
	}


	/* 缓存模式
	 public List<DocumentData> listOnPage(DocumentDataCriteria documentDataCriteria) {
		List<Integer> idList = documentDataDao.listPkOnPage(documentDataCriteria);
		if(idList == null || idList.size() < 1){
			return null;
		}
		List<DocumentData> documentDataList = new ArrayList<DocumentData>();

		for(int i = 0; i < idList.size(); i++){
			DocumentData dd = documentDataDao.select(idList.get(i));
			if(dd != null){
				dd.setIndex(i+1);
				afterFetch(dd);
				documentDataList.add(dd);
			}
		}
		return documentDataList;
	}*/

	@Override
	//非缓存模式
	public List<DocumentData> listOnPage(DocumentDataCriteria documentDataCriteria) {

		List<DocumentData> documentDataList = documentDataDao.listOnPage(documentDataCriteria);
		if(documentDataList != null && documentDataList.size() > 0){
			for(int i = 0; i < documentDataList.size(); i++){
				documentDataList.get(i).setIndex(i+1);
				//		afterFetch(documentDataList.get(i));

			}
		}
		return documentDataList;
	}
	public HashMap<String, DocumentData> map(DocumentDataCriteria documentDataCriteria) {
		List<DocumentData> documentDataList = list(documentDataCriteria);
		if(documentDataList == null){
			return null;
		}
		HashMap<String,DocumentData> documentDataMap = new HashMap<String,DocumentData>();
		for(int i = 0; i < documentDataList.size(); i++){
			documentDataMap.put(documentDataList.get(i).getDataCode(), documentDataList.get(i));
		}
		return documentDataMap;
	}

	public DocumentData matchByContent(DocumentDataCriteria documentDataCriteria){		
		return documentDataDao.matchByContent(documentDataCriteria);
	}



	/*private void afterFetch(DocumentData documentData) {
		if(documentData == null){
			return;
		}
		documentData.setCurrentStatusName(BasicStatus.normal.findById(documentData.getCurrentStatus()).getName());
		documentData.setId(documentData.getDocumentDataId());
		if(documentData != null){
			logger.debug("XXXX documentData[" + documentData.getDocumentDataId() + "==>inputMethod=" + documentData.getInputMethod() + ",dataValue=" + documentData.getDataValue());
			if(documentData.getInputMethod() != null && documentData.getInputMethod().equals("file")){
				if(documentData.getDataValue() != null && !documentData.getDataValue().startsWith(documentDownloadFakePath)){
					if(documentData.getDisplayLevel() == null){
						documentData.setDataValue(documentDownloadFakePath + "/" + CommonStandard.documentOpenSaveDir + "/" + documentData.getDataValue());
					} else {
						if(documentData.getDisplayLevel().equals(DisplayLevel.subscriber.toString())){
							documentData.setDataValue(documentDownloadFakePath + "/" + CommonStandard.documentForSubscribeSaveDir + "/" + documentData.getDataValue());
						} else if(documentData.getDisplayLevel().equals(DisplayLevel.login.toString())){
							documentData.setDataValue(documentDownloadFakePath + "/" + CommonStandard.documentForLoginSaveDir + "/" + documentData.getDataValue());
						} else {
							documentData.setDataValue(documentDownloadFakePath + "/" + CommonStandard.documentOpenSaveDir + "/" + documentData.getDataValue());
						}
					}
				}
			}
		}
	}*/

}

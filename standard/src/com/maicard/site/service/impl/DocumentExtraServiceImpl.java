package com.maicard.site.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.LanguageService;
import com.maicard.site.criteria.*;
import com.maicard.site.dao.*;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.DocumentData;
import com.maicard.site.domain.DocumentNodeRelation;
import com.maicard.site.domain.Node;
import com.maicard.site.domain.Tag;
import com.maicard.site.domain.TagObjectRelation;
import com.maicard.site.service.DocumentDataService;
import com.maicard.site.service.DocumentExtraService;
import com.maicard.site.service.DocumentNodeRelationService;
import com.maicard.site.service.DocumentTypeService;
import com.maicard.site.service.NodeService;
import com.maicard.site.service.TagObjectRelationService;
import com.maicard.site.service.TagService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.ObjectType;

@Service
public class DocumentExtraServiceImpl extends BaseService implements DocumentExtraService{
	@Resource
	private ConfigService configService;
	@Resource
	private DocumentDao documentDao;
	@Resource
	private DocumentTypeService documentTypeService;
	@Resource
	private DocumentDataService documentDataService;
	@Resource
	private DocumentNodeRelationService documentNodeRelationService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private LanguageService languageService;
	@Resource
	private TagObjectRelationService tagObjectRelationService;
	@Resource
	private TagService tagService;
	@Resource
	private NodeService nodeService;


	
	@Override
	@Cacheable(value = CommonStandard.cacheNameDocument, key = "'Document#' + #udid")
	public Document select(int udid){
		return selectNoCache(udid);
		
	}
	
	@Override
	public Document selectNoCache(int udid){
		if(logger.isDebugEnabled()){
			logger.debug("从数据库中查找文档[" + udid + "]");
		}
		Document document =  documentDao.select(udid);
		if(document == null){
			return null;
		}
		document.setId(udid);
		afterFetch(document);
		return document;
		
	}
	
	
	public void afterFetch(Document document) {

		//查找对应的自定义数据
		DocumentDataCriteria  documentDataCriteria = new DocumentDataCriteria();
		documentDataCriteria.setUdid(document.getUdid());
		documentDataCriteria.setDocumentTypeId(document.getDocumentTypeId());
		HashMap<String,DocumentData> documentDataMap  = documentDataService.map(documentDataCriteria);
		if(logger.isDebugEnabled()){
			logger.debug("为文档[" + document.getUdid() + "]找到的自定义数据有[" + (documentDataMap == null ? -1 : documentDataMap.size()) + "]条");
		}
		if(documentDataMap != null){
			
			
			//把发货地、原产地等信息进行处理
			DocumentData dd = documentDataMap.get(DataName.productOrigin.toString());
			if(dd != null && dd.getDataValue() != null){
					if(dd.getDataValue().startsWith("北京") || dd.getDataValue().startsWith("天津") || dd.getDataValue().startsWith("重庆") || dd.getDataValue().startsWith("上海")){
						dd.setDataValue(dd.getDataValue().split("#")[0]);
					} else {
						dd.setDataValue(dd.getDataValue().replaceAll("#", " "));
					}
			}
			dd = documentDataMap.get(DataName.deliveryFromArea.toString());
			if(dd != null && dd.getDataValue() != null){
					if(dd.getDataValue().startsWith("北京") || dd.getDataValue().startsWith("天津") || dd.getDataValue().startsWith("重庆") || dd.getDataValue().startsWith("上海")){
						dd.setDataValue(dd.getDataValue().split("#")[0]);
					} else {
						dd.setDataValue(dd.getDataValue().replaceAll("#", " "));
					}
			}
			dd = documentDataMap.get(DataName.defaultFromArea.toString());
			if(dd != null && dd.getDataValue() != null){
					if(dd.getDataValue().startsWith("北京") || dd.getDataValue().startsWith("天津") || dd.getDataValue().startsWith("重庆") || dd.getDataValue().startsWith("上海")){
						dd.setDataValue(dd.getDataValue().split("#")[0]);
					} else {
						dd.setDataValue(dd.getDataValue().replaceAll("#", " "));
					}
			}
			document.setDocumentDataMap(documentDataMap);
		}
	

		// End of update 2012-11-13
		//查找关联节点
		DocumentNodeRelationCriteria documentNodeRelationCriteria = new DocumentNodeRelationCriteria();
		documentNodeRelationCriteria.setUdid((int)document.getUdid());
		List<DocumentNodeRelation> documentNodeRelationList = documentNodeRelationService.list(documentNodeRelationCriteria);
		ArrayList<Node> nodeList = new ArrayList<Node>();
		if(documentNodeRelationList != null){
			for(int i = 0; i < documentNodeRelationList.size(); i++){
				Node node = nodeService.select(documentNodeRelationList.get(i).getNodeId());
				if(node == null){
					continue;
				}
				//用对应关系表中的状态取代输出的节点状态
				node.setCurrentStatus(documentNodeRelationList.get(i).getCurrentStatus());
				if(node.getCurrentStatus()  == BasicStatus.relation.getId()){
					document.setDefaultNode(node);
				}
				nodeList.add(node);			
			}
			document.setRelatedNodeList(nodeList);
		}
		
		//处理tags
		StringBuffer sb = new StringBuffer();

		TagObjectRelationCriteria tagObjectRelationCriteria = new TagObjectRelationCriteria();
		tagObjectRelationCriteria.setObjectType(ObjectType.document.toString());
		tagObjectRelationCriteria.setObjectId(document.getUdid());
		List<TagObjectRelation> tagObjectRelationList = tagObjectRelationService.list(tagObjectRelationCriteria);
		if(tagObjectRelationList != null){
			for(TagObjectRelation tagObjectRelation : tagObjectRelationList){
				if(tagObjectRelation != null){
					Tag tag = tagService.select(tagObjectRelation.getTagId());
					if(tag != null){
						sb.append(tag.getTagName());
						sb.append(",");
					}
				}
			}
			try{sb.deleteCharAt(sb.length() - 1);}catch(Exception e){}
			document.setTags(sb.toString());

		}
		/*
		if(document.getDocumentTypeId() == 171004){
			pdfProcess(document);
		}*/

		return;
	}


	

}

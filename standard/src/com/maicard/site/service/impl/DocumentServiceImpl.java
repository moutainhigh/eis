package com.maicard.site.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.ObjectMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.LanguageService;
import com.maicard.exception.DataInvalidException;
import com.maicard.exception.EisException;
//import com.maicard.flow.service.WorkflowInstanceService;
import com.maicard.security.domain.User;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerService;
import com.maicard.site.criteria.DocumentCriteria;
import com.maicard.site.criteria.DocumentDataCriteria;
import com.maicard.site.criteria.DocumentNodeRelationCriteria;
import com.maicard.site.criteria.TagObjectRelationCriteria;
import com.maicard.site.dao.DocumentDao;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.DocumentData;
import com.maicard.site.domain.DocumentNodeRelation;
import com.maicard.site.domain.DocumentType;
import com.maicard.site.domain.Node;
import com.maicard.site.service.AdvertService;
import com.maicard.site.service.DocumentDataService;
import com.maicard.site.service.DocumentExtraService;
import com.maicard.site.service.DocumentNodeRelationService;
import com.maicard.site.service.DocumentPostProcessor;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.DocumentTypeService;
import com.maicard.site.service.NodeService;
import com.maicard.site.service.TagObjectRelationService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.SiteStandard.DocumentStatus;

@Service
public class DocumentServiceImpl extends BaseService implements DocumentService{

	@Resource 
	DocumentDao documentDao;

	@Resource
	private AdvertService advertService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	DocumentExtraService documentExtraService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private ConfigService configService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private DocumentTypeService documentTypeService;
	@Resource
	private DocumentDataService documentDataService;
	@Resource
	private DocumentNodeRelationService documentNodeRelationService;
	@Resource
	private LanguageService languageService;
	@Resource
	private NodeService nodeService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private TagObjectRelationService tagObjectRelationService;

	private String EXTRA_DATA_URL_PREFIX;


	@PostConstruct
	public void init(){
		EXTRA_DATA_URL_PREFIX = configService.getValue(DataName.EXTRA_DATA_URL_PREFIX.toString(),0);
		if(StringUtils.isBlank(EXTRA_DATA_URL_PREFIX)){
			EXTRA_DATA_URL_PREFIX = "/static";
		}
		logger.debug("附加数据下载URL前缀是:" + EXTRA_DATA_URL_PREFIX);
	}

	@Override
	public int insert(Document document) throws Exception {
		/*if(document.getDocumentTypeId() == 171004){
			document.setFlag(document.getCurrentStatus());
			document.setCurrentStatus(DocumentStatus.inProgress.getId());
		}*/

		if(document.getCreateTime() == null){
			document.setCreateTime(new Date());
		}
		if(document.getValidTime() == null){
			document.setValidTime(new Date());
		}
		if(StringUtils.isBlank(document.getDocumentCode())){
			document.setDocumentCode(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		}
		int rs = 0;
		rs = documentDao.insert(document);
		
		if(rs != 1){
			logger.error("无法写入新文档");
			return -1;
		}
		DocumentType documentType = documentTypeService.select(document.getDocumentTypeId());		
		String postProcessorDefine = documentType.getExtraValue(DataName.documentPostProcessor.name());
		if (StringUtils.isNotBlank(postProcessorDefine)) {
			logger.info("新增文档[" + document.getUdid() + "/" + document.getDocumentTypeId() + "]定义了后期处理器:" + postProcessorDefine);
			DocumentPostProcessor documentPostProcessor = null;
			documentPostProcessor = (DocumentPostProcessor)applicationContextService.getBean(postProcessorDefine);
			
			if(documentPostProcessor != null){
				int postResult = documentPostProcessor.process(document, Operate.create.name());
				if(postResult == 1) {
					//需要更新文档
					documentDao.update(document);
					
				}
			} else {
				String msg = "新增文档[" + document.getUdid() + "/" + document.getDocumentTypeId() + "]定义了后期处理器，但找不到该bean:" + postProcessorDefine;
				logger.error(msg);
				documentDao.delete(document.getUdid());
				throw new EisException(msg);
				

			}
		} 

		//更新tags
		if(StringUtils.isBlank(document.getTags())){
			//删除跟之关联的所有tag
			TagObjectRelationCriteria tagObjectRelationCriteria = new TagObjectRelationCriteria();
			tagObjectRelationCriteria.setObjectType(ObjectType.document.toString());
			tagObjectRelationCriteria.setObjectId(document.getUdid());
			tagObjectRelationService.delete(tagObjectRelationCriteria);
		} else {
			tagObjectRelationService.sync(document.getOwnerId(), ObjectType.document.toString(), document.getUdid(), document.getTags());
		}


		//先删除所有对应的自定义数据
		DocumentDataCriteria  documentDataCriteria = new DocumentDataCriteria();
		documentDataCriteria.setUdid(document.getUdid());
		documentDataService.delete(documentDataCriteria);

		DocumentNodeRelationCriteria documentNodeRelationCriteria = new DocumentNodeRelationCriteria();
		documentNodeRelationCriteria.setUdid(document.getUdid());
		documentNodeRelationService.delete(documentNodeRelationCriteria);
		//然后插入对应的自定义数据
		if(document.getDocumentDataMap() != null){
			for(DocumentData documentData : document.getDocumentDataMap().values()){
				documentData.setUdid(document.getUdid());				
				documentData.setCurrentStatus(BasicStatus.normal.getId());
				logger.debug("尝试插入自定义文档数据[" + documentData.getDataCode() + "/" + documentData.getDataDefineId() + "]，数据内容:[" + documentData.getDataValue() + "]");
				if(documentData.getDataDefineId() <= 0 ){
					if(documentData.getDataCode() != null){
						DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
						dataDefineCriteria.setDataCode(documentData.getDataCode());
						dataDefineCriteria.setObjectType(ObjectType.document.name());
						dataDefineCriteria.setObjectId(document.getDocumentTypeId());
						DataDefine dataDefine = dataDefineService.select(dataDefineCriteria);
						if(dataDefine == null){
							logger.error("找不到文档扩展数据定义[objectType=" + dataDefineCriteria.getObjectType() + ",objectId=" + dataDefineCriteria.getObjectId() + ",dataCode=" + dataDefineCriteria.getDataCode() + "]");
							continue;
						}
						documentData.setDataDefineId(dataDefine.getDataDefineId());
					} else {
						throw new DataInvalidException("尝试新增的文档数据[" + documentData + "]既没有dataDefineId也没有dataCode");
					}
				}
				documentDataService.insert(documentData);
			}
		} else {
			logger.warn("新增的文档不包含自定义数据[udid=" + document.getUdid());
		}
		if(document.getRelatedNodeList() != null && document.getRelatedNodeList().size() > 0){
			List<Node> nodeList = document.getRelatedNodeList();
			//然后插入关联节点
			for(int i = 0; i < nodeList.size(); i++){
				DocumentNodeRelation documentNodeRelation = new DocumentNodeRelation();
				documentNodeRelation.setUdid(document.getUdid());
				documentNodeRelation.setNodeId(nodeList.get(i).getNodeId());
				documentNodeRelation.setCurrentStatus(nodeList.get(i).getCurrentStatus());
				documentNodeRelationService.insert(documentNodeRelation);			
				logger.debug("为新增文档插入与节点[" + documentNodeRelation.getNodeId() + "]的关联关系:" + documentNodeRelation.getCurrentStatus());
			}
		} else {
			logger.warn("新增的文档不包含关联节点,udid=" + document.getUdid());
		}
		logger.info("新增文档和扩展数据完毕[" + document.getUdid() + "],该文档类型是:" + document.getDocumentTypeId());
		
		String asyncPpostProcessorDefine = documentType.getExtraValue(DataName.asyncDocumentPostProcessor.name());
		if (StringUtils.isNotBlank(asyncPpostProcessorDefine)) {
			logger.info("新增文档[" + document.getUdid() + "/" + document.getDocumentTypeId() + "]定义了异步后期处理器:" + asyncPpostProcessorDefine);
			DocumentPostProcessor documentPostProcessor = null;
			documentPostProcessor = (DocumentPostProcessor)applicationContextService.getBean(postProcessorDefine);
			
			if(documentPostProcessor != null){
				documentPostProcessor.asyncProcess(document, Operate.create.name());
				
			} else {
				logger.error("新增文档[" + document.getUdid() + "/" + document.getDocumentTypeId() + "]定义了后期处理器，但找不到该bean" + postProcessorDefine);

			}
		} 
		
		return 1;

	}

	public void rebuildPdf(Document document){
		if(document == null){
			logger.error("文档不存在，无法进行下一步转换PDF工作");
			return;
		}
		logger.info("发送消息至转换器，进行PDF转换[udid=" + document.getUdid() + "]");
		//jmsTemplate.send(this.destination, new MessageCreatorImpl(document));
		/*try{
			Connection connection = topicSendConnectionFactory.createConnection();
			connection.setClientID(String.valueOf(new java.util.Date().getTime()));
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);  
			// destination = session.createQueue(shortMessageQueueName); 
			Topic topic = session.createTopic(configService.getValue(DataName.messageBusUser.toString()));
			MessageProducer messageProducer = session.createProducer(topic);
			ObjectMessage message = session.createObjectMessage(document);
			message.setIntProperty("operate", DocumentStatus.inProgress.getId());
			messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
			messageProducer.setPriority(1);
			messageProducer.setTimeToLive(0);
			messageProducer.send(message);
			logger.info("文档后期处理事件已发送至管理消息总线[" + configService.getValue(DataName.messageBusUser.toString()) + "]");
		}catch(Exception e){
			logger.error(e.getMessage());
		}*/

	}


	@Override
	@CacheEvict(value = CommonStandard.cacheNameDocument, key = "'Document#' + #document.udid")
	public int update(Document document) throws Exception{
		int actualRowsAffected = 0;
		if(document == null){
			return -1;
		}
		int udid = document.getUdid();
		if(udid < 1){
			return -1;
		}
		document.setValidTime(document.getPublishTime());

		Document _oldDocument = documentDao.select(udid);

		if (_oldDocument != null) {
			try{
				actualRowsAffected = documentDao.update(document);
			}catch(Exception e){
				logger.error("更新文档时失败:" + e.getMessage());
			}
			if(actualRowsAffected != 1){
				return -1;
			}
		} else {
			return -1;
		}
		DocumentType documentType = documentTypeService.select(document.getDocumentTypeId());		
		String postProcessorDefine = documentType.getExtraValue(DataName.documentPostProcessor.name());
		if (StringUtils.isNotBlank(postProcessorDefine)) {
			logger.info("修改文档[" + document.getUdid() + "/" + document.getDocumentTypeId() + "]定义了后期处理器:" + postProcessorDefine);
			DocumentPostProcessor documentPostProcessor = null;
			documentPostProcessor = (DocumentPostProcessor)applicationContextService.getBean(postProcessorDefine);
			
			if(documentPostProcessor != null){
				int postResult = documentPostProcessor.process(document, Operate.update.name());
				if(postResult == 1) {
					//需要更新文档
					documentDao.update(document);
					
				}
			} else {
				logger.error("修改文档[" + document.getUdid() + "/" + document.getDocumentTypeId() + "]定义了后期处理器，但找不到该bean" + postProcessorDefine);

			}
		} 

		//更新tags
		if(document.getTags() == null || document.getTags().equals("")){
			//删除跟之关联的所有tag
			TagObjectRelationCriteria tagObjectRelationCriteria = new TagObjectRelationCriteria();
			tagObjectRelationCriteria.setObjectType(ObjectType.document.toString());
			tagObjectRelationCriteria.setObjectId(document.getUdid());
			tagObjectRelationService.delete(tagObjectRelationCriteria);
		} else {
			tagObjectRelationService.sync(document.getOwnerId(), ObjectType.document.toString(), document.getUdid(), document.getTags());
		}
		//先删除所有对应的自定义数据
		DocumentDataCriteria  documentDataCriteria = new DocumentDataCriteria();
		documentDataCriteria.setUdid(document.getUdid());
		documentDataService.delete(documentDataCriteria);
		//然后插入对应的自定义数据

		if(document.getDocumentDataMap() != null){
			for(DocumentData documentData : document.getDocumentDataMap().values()){
				if(documentData.getDataDefineId() <= 0 ){
					if(documentData.getDataCode() != null){
						DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
						dataDefineCriteria.setDataCode(documentData.getDataCode());
						dataDefineCriteria.setObjectType(ObjectType.document.name());
						dataDefineCriteria.setObjectId(document.getDocumentTypeId());
						DataDefine dataDefine = dataDefineService.select(dataDefineCriteria);
						if(dataDefine == null){
							logger.error("找不到文档扩展数据定义[objectType=" + dataDefineCriteria.getObjectType() + ",objectId=" + dataDefineCriteria.getObjectId() + ",dataCode=" + dataDefineCriteria.getDataCode() + "]");
							continue;
						}
						documentData.setDataDefineId(dataDefine.getDataDefineId());
					} else {
						throw new DataInvalidException("尝试新增的文档数据[" + documentData + "]既没有dataDefineId也没有dataCode");
					}
				}
				documentData.setUdid(document.getUdid());
				documentDataService.insert(documentData);
			}
		}else {
			logger.warn("更新的文档不包含自定义数据udid=" + document.getUdid());
		}

		if(document.getRelatedNodeList() != null && document.getRelatedNodeList().size() > 0){
			logger.warn("更新的文档[" + document.getUdid() + "]需要更新" + document.getRelatedNodeList().size() + "个关联节点");

			List<Node> nodeList = document.getRelatedNodeList();
			DocumentNodeRelationCriteria documentNodeRelationCriteria = new DocumentNodeRelationCriteria();
			documentNodeRelationCriteria.setUdid(document.getUdid());
			documentNodeRelationService.delete(documentNodeRelationCriteria);
			//然后插入关联节点
			for(int i = 0; i < nodeList.size(); i++){
				DocumentNodeRelation documentNodeRelation = new DocumentNodeRelation();
				documentNodeRelation.setUdid(document.getUdid());
				documentNodeRelation.setNodeId(nodeList.get(i).getNodeId());
				documentNodeRelation.setCurrentStatus(nodeList.get(i).getCurrentStatus());
				documentNodeRelationService.insert(documentNodeRelation);			
			}
		}else {
			logger.warn("更新的文档不包含关联节点udid=" + document.getUdid());
		}

		logger.info("更新文档和扩展数据完毕");
		String asyncPpostProcessorDefine = documentType.getExtraValue(DataName.asyncDocumentPostProcessor.name());
		if (StringUtils.isNotBlank(asyncPpostProcessorDefine)) {
			logger.info("更新文档[" + document.getUdid() + "/" + document.getDocumentTypeId() + "]定义了异步后期处理器:" + asyncPpostProcessorDefine);
			DocumentPostProcessor documentPostProcessor = null;
			documentPostProcessor = (DocumentPostProcessor)applicationContextService.getBean(postProcessorDefine);
			
			if(documentPostProcessor != null){
				documentPostProcessor.asyncProcess(document, Operate.create.name());
				
			} else {
				logger.error("更新文档[" + document.getUdid() + "/" + document.getDocumentTypeId() + "]定义了后期处理器，但找不到该bean" + postProcessorDefine);

			}
		} 
		return actualRowsAffected;		
	}

	@Override
	@CacheEvict(value = CommonStandard.cacheNameDocument, key = "'Document#' + #udid")
	public int delete(int udid) throws Exception {
		int actualRowsAffected = 1;

		Document _oldDocument = documentDao.select(udid);

		if (_oldDocument != null) {
			//删除所有对应的自定义数据和关联节点
			logger.info("删除文档[" + udid + "]的关联数据...");
			DocumentDataCriteria  documentDataCriteria = new DocumentDataCriteria();
			documentDataCriteria.setUdid(udid);
			documentDataService.delete(documentDataCriteria);
			logger.info("删除文档[" + udid + "]的节点关联...");
			DocumentNodeRelationCriteria documentNodeRelationCriteria = new DocumentNodeRelationCriteria();
			documentNodeRelationCriteria.setUdid(udid);
			documentNodeRelationService.delete(documentNodeRelationCriteria);
			logger.info("删除文档[" + udid + "]自身...");
			try{
				actualRowsAffected = documentDao.delete(udid);
			}catch(Exception e){
				logger.error("删除文档时出错:" + e.getMessage());
			}
			logger.info("文档[" + udid + "]删除结果:" + actualRowsAffected);
			
			DocumentType documentType = documentTypeService.select(_oldDocument.getDocumentTypeId());		
			String postProcessorDefine = documentType.getExtraValue(DataName.documentPostProcessor.name());
			if (StringUtils.isNotBlank(postProcessorDefine)) {
				logger.info("被删除的文档[" + udid + "/" + _oldDocument.getDocumentTypeId() + "]定义了后期处理器:" + postProcessorDefine);
				DocumentPostProcessor documentPostProcessor = null;
				documentPostProcessor = (DocumentPostProcessor)applicationContextService.getBean(postProcessorDefine);
				
				if(documentPostProcessor != null){
					documentPostProcessor.process(_oldDocument, Operate.delete.name());
					
				} else {
					String msg = "被删除的文档[" + _oldDocument.getUdid() + "/" + _oldDocument.getDocumentTypeId() + "]定义了后期处理器，但找不到该bean:" + postProcessorDefine;
					logger.error(msg);
					throw new EisException(msg);
					

				}
			} 


		} else {
			logger.info("找不到UDID=" + udid + "的文章");
			return -1;
		}

		return actualRowsAffected;
	}

	@Override
	@CacheEvict(value = CommonStandard.cacheNameDocument, key = "'Document#' + #udid")
	public 	int changeStatus(int udid, int status) throws Exception{
		Document document = documentExtraService.select(udid);
		if(document == null){
			return 0;
		}
		document.setCurrentStatus(status);
		return update(document);		

	}




	public List<Document> list(DocumentCriteria documentCriteria) {
		List<Integer> udidList= documentDao.listPk(documentCriteria);
		if(udidList == null || udidList.size() < 1){
			return Collections.emptyList();
		}	
		List<Document> documentList = new ArrayList<Document>();
		for(int i =0; i < udidList.size(); i++){
			Document document = documentExtraService.select(udidList.get(i));
			if(document != null){
				document.setIndex(i);
				documentList.add(document);
			}
		}
		return documentList;
	}


	@Override
	public List<Document> listOnPage(DocumentCriteria documentCriteria) throws Exception{
		long startTime = System.currentTimeMillis();
		List<Integer> udidList= documentDao.listPkOnPage(documentCriteria);
		logger.debug("根据当前条件的到的主键数量有:" + (udidList == null ? "空" : udidList.size()));
		if(udidList == null || udidList.size() < 1){
			return Collections.emptyList();
		}	
		long offsetTime = System.currentTimeMillis();
		List<Document> documentList = new ArrayList<Document>();
		for(int i =0; i < udidList.size(); i++){
			Document document = documentExtraService.select(udidList.get(i));
			if(document != null){
				document.setIndex(i);
				documentList.add(document);
			}
		}
		long endTime = System.currentTimeMillis();
		logger.info("时间统计:获取udid用时:" + (offsetTime - startTime) / 1000f + "秒，获取[" + documentList.size() + "]个文档用时:" + (endTime - offsetTime) / 1000f + "秒");


		return documentList;
	}

	@Override
	public boolean postProcess(ObjectMessage objectMessage) throws Exception{
		Document document = null;
		try{
			document = (Document)objectMessage.getObject();
		}catch(Exception e){}
		if(document == null){
			logger.error("无法对空对象文档进行后期处理.");
			return false;
		}
		/*if(document.getCurrentStatus() == DocumentStatus.inProgress.getId()){
			if(pdf2swf(document)){
				//处理成功，将状态转换为之前的状态
				document.setCurrentStatus(document.getFlag());
			} else {
				document.setCurrentStatus(CommonStandard.Error.dataError.getId());
			}		
			changeStatus(document.getUdid(), document.getCurrentStatus());
		}*/
		return false;
	}


	@SuppressWarnings("unused")
	private void listPostProcess(List<Document> documentList, DocumentCriteria documentCriteria){
		/*
		 * 文档过滤
		 * 1、发布者是自己的可以显示
		 * 2、若当前用户拥有审批权限(review)，则发布的node与自己的审批权限关联node一致的可以显示
		 * 3、已发布的文档全部可以显示
		 */
		List<Document> filteredDocumentList = new ArrayList<Document>();
		for(Document document : documentList){
			if(document.getPublisherId() == documentCriteria.getCurrentUuid()){
				filteredDocumentList.add(document);
				continue;
			}
			if(document.getCurrentStatus() == DocumentStatus.published.getId()){
				filteredDocumentList.add(document);
				continue;
			}
			boolean fetched = false;
			if(documentCriteria.getCurrentUuid() > 0){
				User currentSysUser = partnerService.select(documentCriteria.getCurrentUuid());
				if(currentSysUser != null && document.getRelatedNodeList() != null){
					for(int i = 0; i < document.getRelatedNodeList().size(); i++){
						if(fetched){
							break;
						}
						for(int j = 0; j < currentSysUser.getRelatedRoleList().size(); j++){
							if(fetched){
								break;
							}
							for(int k = 0; k < currentSysUser.getRelatedRoleList().get(j).getRelatedPrivilegeList().size(); k++){
								if(fetched){
									break;
								}
								/*FIXME for(int m = 0; m <  currentSysUser.getRelatedRoleList().get(j).getRelatedPrivilegeList().get(k).getPrivilegeRelationList().size(); m++){
									if(currentSysUser.getRelatedRoleList().get(j).getRelatedPrivilegeList().get(k).getPrivilegeRelationList().get(m).getObjectTypeId() == Constants.ObjectType.node.toString()){
										if(Integer.parseInt(currentSysUser.getRelatedRoleList().get(j).getRelatedPrivilegeList().get(k).getPrivilegeRelationList().get(m).getObjectId()) == document.getRelatedNodeList().get(i).getNodeId()){
											filteredDocumentList.add(document);
											fetched = true;
											break;
										}
									}
								}*/
							}
						}
					}

				}
			}
		}
		documentList = filteredDocumentList;
	}



	@Override
	public int count(DocumentCriteria documentCriteria) {
		return documentDao.count(documentCriteria);

	}

	@Override
	public Document select(int udid) {
		try{
			return documentExtraService.select(udid);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Document selectNoCache(int udid) {
		try{
			return documentExtraService.selectNoCache(udid);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Document select(String documentCode, long ownerId) {
		DocumentCriteria documentCriteria = new DocumentCriteria();
		documentCriteria.setDocumentCode(documentCode);
		documentCriteria.setOwnerId(ownerId);
		List<Integer> udidList= documentDao.listPk(documentCriteria);
		logger.debug("[" + documentCode + "]得到的文章数量是:" + (udidList == null ? -1 : udidList.size()));
		if(udidList == null || udidList.size() != 1){
			return null;
		}	
		try{
			return documentExtraService.select(udidList.get(0));
		}catch(Exception e){}
		return null;
	}

	@Override
	//列出实时资讯
	public List<Document> listFreshDocument() {

		return null;
	}

	/*@Override
	public void processDataPath(Document document) {
		if(document == null){
			return;
		}
		if(document.getDocumentDataMap() == null || document.getDocumentDataMap().size() < 1){
			return;
		}
		for(DocumentData documentData : document.getDocumentDataMap().values()){
			if(documentData == null){
				continue;
			}
			documentData.setId(documentData.getDocumentDataId());
			if(documentData.getInputMethod() != null && documentData.getInputMethod().equals("file")){
				if(documentData.getDataValue() != null && !documentData.getDataValue().startsWith(EXTRA_DATA_URL_PREFIX)){
					if(documentData.getDisplayLevel() == null){
						documentData.setDataValue(EXTRA_DATA_URL_PREFIX + "/" + CommonStandard.EXTRA_DATA_OPEN_PATH + "/" + documentData.getDataValue());
					} else {
						if(documentData.getDisplayLevel().equals(DisplayLevel.subscriber.toString())){
							documentData.setDataValue(EXTRA_DATA_URL_PREFIX + "/" + CommonStandard.EXTRA_DATA_SUBSCRIBE_PATH + "/" + documentData.getDataValue());
						} else if(documentData.getDisplayLevel().equals(DisplayLevel.login.toString())){
							documentData.setDataValue(EXTRA_DATA_URL_PREFIX + "/" + CommonStandard.EXTRA_DATA_LOGIN_PATH + "/" + documentData.getDataValue());
						} else {
							documentData.setDataValue(EXTRA_DATA_URL_PREFIX + "/" + CommonStandard.EXTRA_DATA_OPEN_PATH + "/" + documentData.getDataValue());
						}
					}
				}
			}

		}
	}*/


}

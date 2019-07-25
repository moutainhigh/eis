package com.maicard.security.service.impl.authorizeProcessor;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.annotation.AuthorizeProcess;
import com.maicard.common.base.BaseService;
import com.maicard.exception.DataInvalidException;
import com.maicard.security.domain.User;
import com.maicard.security.processor.AuthorizeProcessor;
import com.maicard.security.service.AuthorizeService;
import com.maicard.site.domain.Document;
import com.maicard.standard.ObjectType;

@AuthorizeProcess(Document.class)
@Service
public class DocumentProcessor extends BaseService implements AuthorizeProcessor {

	@Resource
	private AuthorizeService authorizeService;


	@Override
	public Object filter(User user, Object targetObject, String operateCode) {
		
		logger.debug("DocumentData处理...");
		if(user == null){
			throw new DataInvalidException("User对象为空");
		}
		Document document = null;
		try{
			document = (Document)targetObject;
		}catch(Exception e){
			e.printStackTrace();
		}
		if(document == null){
			throw new DataInvalidException("无法将对象转换为document");
		}
		
		if(document.getDocumentDataMap() == null || document.getDocumentDataMap().size() < 1){
			logger.debug("文档[" + document.getUdid() + "]没有任何扩展数据，无需处理");
			return document;
		}
		String validDataDefineIds = authorizeService.listValidObjectId(user, ObjectType.document.name(), document.getDocumentTypeId(), operateCode);
		if(validDataDefineIds == null || validDataDefineIds.equals("")){
			logger.info("针对对象" +  ObjectType.document.name() + "[" + document.getDocumentTypeId() + "]的操作[" + operateCode + "]的数据定义为空");
			document.setDocumentDataMap(null);
			return document;
		}
		if(validDataDefineIds.equals("*")){
			logger.debug("针对对象" +  ObjectType.document.name() + "[" + document.getDocumentTypeId() + "]的操作[" + operateCode + "]的数据定义是*，直接返回");
			return document;
		}
		String[] ids = validDataDefineIds.split(",");
		List<String> invalidDocumentDataKeys = new ArrayList<String>();
		for(String key : document.getDocumentDataMap().keySet()){
			boolean valid = false;
			for(String id : ids ){
				if(document.getDocumentDataMap().get(key).getDataDefineId() == Integer.parseInt(id)){
					valid = true;
					break;
				}
			}
			if(!valid){
				invalidDocumentDataKeys.add(key);
			}
		}
		for(String key : invalidDocumentDataKeys){
			document.getDocumentDataMap().remove(key);
		}
		return document;
	}

}

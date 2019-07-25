package com.maicard.mb.service.impl;

import java.util.List;

import javax.annotation.Resource;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.mb.criteria.EisTopicCriteria;
import com.maicard.mb.dao.EisTopicDao;
import com.maicard.mb.domain.EisTopic;
import com.maicard.mb.service.EisTopicService;
import com.maicard.standard.BasicStatus;

@Service
public class EisTopicServiceImpl extends BaseService implements EisTopicService {

	@Resource
	private EisTopicDao topicDao;

	public int insert(EisTopic eisTopic) {
		int newTopicId = -1;
		try{
			newTopicId = topicDao.insert(eisTopic);
		}catch(DataIntegrityViolationException e){
			logger.error("必须的字段不完整，或关联数据不完整，无法插入数据库");
			return -1;
		}
		if(newTopicId < 1){
			return -1;
		}
		return newTopicId;
	}
	
	


	public int update(EisTopic eisTopic){
		int actualRowsAffected = 0;
		if(eisTopic == null){
			return -1;
		}
		int messageId = eisTopic.getTopicId();
		if(messageId < 1){
			return -1;
		}

		EisTopic _oldTopic = topicDao.select(messageId);

		if (_oldTopic != null) {
			actualRowsAffected = topicDao.update(eisTopic);
		} else {
			return -1;
		}
		
		
		return actualRowsAffected;		
	}

	public int delete(int udid) {
		int actualRowsAffected = 0;

		EisTopic _oldTopic = topicDao.select(udid);

		if (_oldTopic != null) {
			actualRowsAffected = topicDao.delete(udid);
		} else {
			return -1;
		}
		
		return actualRowsAffected;
	}
	
	public 	int changeStatus(int udid, int status){
		EisTopic eisTopic = topicDao.select(udid);
		if(eisTopic == null){
			return 0;
		}
		eisTopic.setCurrentStatus(status);
		return update(eisTopic);		
		
	}


	public EisTopic select(int topicId) {
		EisTopic eisTopic =  topicDao.select(topicId);
		if(eisTopic == null){
			return null;
		}
		eisTopic.setStatusName(BasicStatus.disable.findById(eisTopic.getCurrentStatus()).getName());
		
		return eisTopic;

	}

	public List<EisTopic> list(EisTopicCriteria topicCriteria) {
		List<EisTopic> topicList= topicDao.list(topicCriteria);
		if(topicList == null){
			return null;
		}	
		for(int i =0; i < topicList.size(); i++){
			topicList.get(i).setIndex(i+1);
			topicList.get(i).setId(topicList.get(i).getTopicId());
			topicList.get(i).setStatusName(BasicStatus.disable.findById(topicList.get(i).getCurrentStatus()).getName());
		}
		return topicList;
	}

	public List<EisTopic> listOnPage(EisTopicCriteria topicCriteria) {
		List<EisTopic> topicList =  topicDao.listOnPage(topicCriteria);
		if(topicList == null){
			return null;
		}
		for(int i =0; i < topicList.size(); i++){
			topicList.get(i).setIndex(i+1);
			topicList.get(i).setId(topicList.get(i).getTopicId());
			topicList.get(i).setStatusName(BasicStatus.disable.findById(topicList.get(i).getCurrentStatus()).getName());

			
		}
		return topicList;
	}


	
	public  int count(EisTopicCriteria topicCriteria){
		return topicDao.count(topicCriteria);
	}




	@Override
	public EisTopic select(String topicCode) {
		EisTopicCriteria eisTopicCriteria = new EisTopicCriteria();
		eisTopicCriteria.setTopicCode(topicCode);
		List<EisTopic> topicList = list(eisTopicCriteria);
		if(topicList != null && topicList.size() == 1){
			return topicList.get(0);
		}
		return null;
	}

}

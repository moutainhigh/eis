package com.maicard.mb.service.impl;

import java.util.List;

import javax.annotation.Resource;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.mb.criteria.UserSubscribeMessageRelationCriteria;
import com.maicard.mb.dao.UserSubscribeMessageRelationDao;
import com.maicard.mb.domain.UserMessageRelation;
import com.maicard.mb.service.UserSubscribeMessageRelationService;
import com.maicard.standard.BasicStatus;

@Service
public class UserSubscribeMessageRelationServiceImpl extends BaseService implements UserSubscribeMessageRelationService {

	@Resource
	private UserSubscribeMessageRelationDao userSubscribeMessageRelationDao;


	@Override
	public int insert(UserMessageRelation userMessageRelation) {
		int newUserSubscribeMessageRelationId = -1;
		try{
			newUserSubscribeMessageRelationId = userSubscribeMessageRelationDao.insert(userMessageRelation);
		}catch(DataIntegrityViolationException e){
			logger.error("必须的字段不完整，或关联数据不完整，无法插入数据库");
			return -1;
		}
		if(newUserSubscribeMessageRelationId < 1){
			return -1;
		}
		return newUserSubscribeMessageRelationId;
	}
	
	


	@Override
	public int update(UserMessageRelation userMessageRelation){
		if(userMessageRelation == null){
			return -1;
		}
		int messageId = userMessageRelation.getUserSubscribeMessageRelationId();
		if(messageId < 1){
			return -1;
		}

		try{
			return userSubscribeMessageRelationDao.update(userMessageRelation);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
			
	}

	@Override
	public int delete(int userSubScribeMessageRelationId) {
		try{
			return  userSubscribeMessageRelationDao.delete(userSubScribeMessageRelationId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;

		
	}
	
	public 	int changeStatus(int udid, int status){
		UserMessageRelation userMessageRelation = userSubscribeMessageRelationDao.select(udid);
		if(userMessageRelation == null){
			return 0;
		}
		userMessageRelation.setCurrentStatus(status);
		return update(userMessageRelation);		
		
	}


	public UserMessageRelation select(int udid) {
		UserMessageRelation userMessageRelation =  userSubscribeMessageRelationDao.select(udid);
		if(userMessageRelation == null){
			return null;
		}
		
		return userMessageRelation;

	}

	public List<UserMessageRelation> list(UserSubscribeMessageRelationCriteria userSubscribeMessageRelationCriteria) {
		List<UserMessageRelation> userSubscribeMessageRelationList= userSubscribeMessageRelationDao.list(userSubscribeMessageRelationCriteria);
		if(userSubscribeMessageRelationList == null){
			return null;
		}	
		/*for(int i =0; i < userSubscribeMessageRelationList.size(); i++){
			userSubscribeMessageRelationList.get(i).setIndex(i+1);
			userSubscribeMessageRelationList.get(i).setId(userSubscribeMessageRelationList.get(i).getUserSubscribeMessageRelationId());
			userSubscribeMessageRelationList.get(i).setCurrentStatusName(BasicStatus.disable.findById(userSubscribeMessageRelationList.get(i).getCurrentStatus()).getName());
		}*/
		return userSubscribeMessageRelationList;
	}

	public List<UserMessageRelation> listOnPage(UserSubscribeMessageRelationCriteria userSubscribeMessageRelationCriteria) {
		List<UserMessageRelation> userSubscribeMessageRelationList =  userSubscribeMessageRelationDao.listOnPage(userSubscribeMessageRelationCriteria);
		if(userSubscribeMessageRelationList == null){
			return null;
		}
		/*for(int i =0; i < userSubscribeMessageRelationList.size(); i++){
			userSubscribeMessageRelationList.get(i).setIndex(i+1);
			userSubscribeMessageRelationList.get(i).setId(userSubscribeMessageRelationList.get(i).getUserSubscribeMessageRelationId());

			
		}*/
		return userSubscribeMessageRelationList;
	}


	
	public  int count(UserSubscribeMessageRelationCriteria userSubscribeMessageRelationCriteria){
		return userSubscribeMessageRelationDao.count(userSubscribeMessageRelationCriteria);
	}

}

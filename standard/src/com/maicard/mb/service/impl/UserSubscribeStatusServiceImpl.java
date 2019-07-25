package com.maicard.mb.service.impl;

import java.util.List;

import javax.annotation.Resource;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.mb.criteria.UserSubscribeStatusCriteria;
import com.maicard.mb.dao.UserSubscribeStatusDao;
import com.maicard.mb.domain.UserSubscribeStatus;
import com.maicard.mb.service.UserSubscribeStatusService;
import com.maicard.standard.BasicStatus;

@Service
public class UserSubscribeStatusServiceImpl extends BaseService implements UserSubscribeStatusService {

	@Resource
	private UserSubscribeStatusDao userSubscribeStatusDao;

	@Override
	public int insert(UserSubscribeStatus userSubscribeStatus) {
		int newUserSubscribeStatusId = -1;
		try{
			newUserSubscribeStatusId = userSubscribeStatusDao.insert(userSubscribeStatus);
		}catch(DataIntegrityViolationException e){
			logger.error("必须的字段不完整，或关联数据不完整，无法插入数据库");
			return -1;
		}
		if(newUserSubscribeStatusId < 1){
			return -1;
		}
		return newUserSubscribeStatusId;
	}
	
	


	@Override
	public int update(UserSubscribeStatus userSubscribeStatus){
		if(userSubscribeStatus == null){
			return -1;
		}
		int messageId = userSubscribeStatus.getUserSubscribeStatusId();
		if(messageId < 1){
			return -1;
		}

		try{
			return  userSubscribeStatusDao.update(userSubscribeStatus);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
	}

	@Override
	public int delete(int userSubscribeStatusId) {
		try{
			return  userSubscribeStatusDao.delete(userSubscribeStatusId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;

		
	}
	
	public 	int changeStatus(int udid, int status){
		UserSubscribeStatus userSubscribeStatus = userSubscribeStatusDao.select(udid);
		if(userSubscribeStatus == null){
			return 0;
		}
		userSubscribeStatus.setCurrentStatus(status);
		return update(userSubscribeStatus);		
		
	}


	public UserSubscribeStatus select(int udid) {
		UserSubscribeStatus userSubscribeStatus =  userSubscribeStatusDao.select(udid);
		if(userSubscribeStatus == null){
			return null;
		}
		
		return userSubscribeStatus;

	}

	public List<UserSubscribeStatus> list(UserSubscribeStatusCriteria userSubscribeStatusCriteria) {
		List<UserSubscribeStatus> userSubscribeStatusList= userSubscribeStatusDao.list(userSubscribeStatusCriteria);
		if(userSubscribeStatusList == null){
			return null;
		}	
		/*for(int i =0; i < userSubscribeStatusList.size(); i++){
			userSubscribeStatusList.get(i).setIndex(i+1);
			userSubscribeStatusList.get(i).setId(userSubscribeStatusList.get(i).getUserSubscribeStatusId());
			userSubscribeStatusList.get(i).setCurrentStatusName(BasicStatus.disable.findById(userSubscribeStatusList.get(i).getCurrentStatus()).getName());
		}*/
		return userSubscribeStatusList;
	}

	public List<UserSubscribeStatus> listOnPage(UserSubscribeStatusCriteria userSubscribeStatusCriteria) {
		List<UserSubscribeStatus> userSubscribeStatusList =  userSubscribeStatusDao.listOnPage(userSubscribeStatusCriteria);
		if(userSubscribeStatusList == null){
			return null;
		}
	/*	for(int i =0; i < userSubscribeStatusList.size(); i++){
			userSubscribeStatusList.get(i).setIndex(i+1);
			userSubscribeStatusList.get(i).setId(userSubscribeStatusList.get(i).getUserSubscribeStatusId());
			userSubscribeStatusList.get(i).setCurrentStatusName(BasicStatus.disable.findById(userSubscribeStatusList.get(i).getCurrentStatus()).getName());
		}*/
		return userSubscribeStatusList;
	}


	
	public  int count(UserSubscribeStatusCriteria userSubscribeStatusCriteria){
		return userSubscribeStatusDao.count(userSubscribeStatusCriteria);
	}



}

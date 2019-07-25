package com.maicard.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.criteria.UserLevelConditionCriteria;
import com.maicard.security.dao.UserLevelConditionDao;
import com.maicard.security.domain.UserLevelCondition;
import com.maicard.security.service.UserLevelConditionService;

@Service
public class UserLevelConditionServiceImpl extends BaseService implements UserLevelConditionService {

	@Resource
	private UserLevelConditionDao userLevelConditionDao;

	public int insert(UserLevelCondition userLevelCondition) {
		try{
			return userLevelConditionDao.insert(userLevelCondition);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(UserLevelCondition userLevelCondition) {
		try{
			return  userLevelConditionDao.update(userLevelCondition);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int delete(int userLevelConditionId) {
		try{
			return  userLevelConditionDao.delete(userLevelConditionId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;		
	}
	
	public UserLevelCondition select(int userLevelConditionId) {
		return userLevelConditionDao.select(userLevelConditionId);
	}

	public List<UserLevelCondition> list(UserLevelConditionCriteria userLevelConditionCriteria) {
		return userLevelConditionDao.list(userLevelConditionCriteria);
	}
	
	public List<UserLevelCondition> listOnPage(UserLevelConditionCriteria userLevelConditionCriteria) {
		return userLevelConditionDao.listOnPage(userLevelConditionCriteria);
	}

}

package com.maicard.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.dao.FrontUserRelationDao;
import com.maicard.security.domain.UserRelation;
import com.maicard.security.service.FrontUserRelationService;

@Service
public class FrontUserRelationServiceImpl extends BaseService implements FrontUserRelationService {

	@Resource
	private FrontUserRelationDao frontUserRelationDao;



	public int insert(UserRelation userRelation) {
		return frontUserRelationDao.insert(userRelation);
	}

	public int update(UserRelation userRelation) {
		int actualRowsAffected = 0;

		long front_user_focus_id = userRelation.getUuid();

		UserRelation _oldFrontUserRelation = frontUserRelationDao.select(front_user_focus_id);

		if (_oldFrontUserRelation != null) {
			actualRowsAffected = frontUserRelationDao.update(userRelation);
		}

		return actualRowsAffected;
	}

	public int delete(int front_user_focus_id) {
		int actualRowsAffected = 0;

		UserRelation _oldFrontUserRelation = frontUserRelationDao.select(front_user_focus_id);

		if (_oldFrontUserRelation != null) {
			actualRowsAffected = frontUserRelationDao.delete(front_user_focus_id);
		}

		return actualRowsAffected;
	}

	public UserRelation select(int front_user_focus_id) {
		return frontUserRelationDao.select(front_user_focus_id);
	}

	public List<UserRelation> list(UserRelationCriteria userRelationCriteria) {
		List<UserRelation> frontUserRelationList =  frontUserRelationDao.list(userRelationCriteria);
		if(frontUserRelationList == null){
			return null;
		}
		for(int i = 0; i < frontUserRelationList.size(); i++){
			frontUserRelationList.get(i).setId(frontUserRelationList.get(i).getUserRelationId());
			frontUserRelationList.get(i).setIndex(i+1);
		}
		return frontUserRelationList;
	}

	public List<UserRelation> listOnPage(UserRelationCriteria userRelationCriteria) {
		List<UserRelation> frontUserRelationList =  frontUserRelationDao.listOnPage(userRelationCriteria);
		if(frontUserRelationList == null){
			return null;
		}
		for(int i = 0; i < frontUserRelationList.size(); i++){
			frontUserRelationList.get(i).setId(frontUserRelationList.get(i).getUserRelationId());
			frontUserRelationList.get(i).setIndex(i+1);
		}
		return frontUserRelationList;
	}
	
	public int count(UserRelationCriteria userRelationCriteria){
		return frontUserRelationDao.count(userRelationCriteria);
	}

}

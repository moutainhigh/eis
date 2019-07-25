package com.maicard.security.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;

import com.maicard.common.domain.EisObject;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.UserRelation;

public interface UserRelationService {

	int insert(UserRelation userRelation);

	int update(UserRelation userRelation);

	int delete(long userRelationId);
		
	UserRelation select(long userRelationId);
	
	List<UserRelation> list(UserRelationCriteria userRelationCriteria);

	List<UserRelation> listOnPage(UserRelationCriteria userRelationCriteria);

	int count(UserRelationCriteria userRelationCriteria);

	int delete(UserRelationCriteria userRelationCriteria);

	@Async
	void insertAsync(UserRelation userRelation);

	int getRelationCount(UserRelationCriteria userRelationCriteria);

	@Async
	void plusCachedRelationCount(UserRelation userRelation);


	/**
	 * 写入文档或其他对象的实时动态数据，如阅读次数、点赞次数和收藏次数
	 */
	void setDynamicData(EisObject object);


	

}

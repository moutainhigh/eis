package com.maicard.mb.service;

import java.util.List;

import com.maicard.mb.criteria.UserSubscribeMessageRelationCriteria;
import com.maicard.mb.domain.UserMessageRelation;

public interface UserSubscribeMessageRelationService {

	int insert(UserMessageRelation userMessageRelation);

	int update(UserMessageRelation userMessageRelation);

	int delete(int udid);
		
	UserMessageRelation select(int ugid);

	List<UserMessageRelation> list(UserSubscribeMessageRelationCriteria userSubscribeMessageRelationCriteria);

	List<UserMessageRelation> listOnPage(UserSubscribeMessageRelationCriteria userSubscribeMessageRelationCriteria);
		
	int count(UserSubscribeMessageRelationCriteria userSubscribeMessageRelationCriteria);
	
		


}

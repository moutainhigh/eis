package com.maicard.mb.service;

import java.util.List;

import com.maicard.mb.criteria.UserSubscribeStatusCriteria;
import com.maicard.mb.domain.UserSubscribeStatus;

public interface UserSubscribeStatusService {

	int insert(UserSubscribeStatus userSubscribeStatus);

	int update(UserSubscribeStatus userSubscribeStatus);

	int delete(int udid);
		
	UserSubscribeStatus select(int ugid);

	List<UserSubscribeStatus> list(UserSubscribeStatusCriteria userSubscribeStatusCriteria);

	List<UserSubscribeStatus> listOnPage(UserSubscribeStatusCriteria userSubscribeStatusCriteria);
		
	int count(UserSubscribeStatusCriteria userSubscribeStatusCriteria);
	
		


}

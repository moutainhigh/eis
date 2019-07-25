package com.maicard.security.service;


import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;

public interface FrontUserExtraService {


	int deleteLocalByUuid(long uuid);
	
	int insertLocal(User frontUser) throws Exception;
	
	int updateLocal(User frontUser) throws Exception;
	
	User select(long uuid);

	void changeUuid(User frontUser);

	int deleteLocalByName(UserCriteria userCriteria);


	
	
}

package com.maicard.security.service;

import java.util.HashMap;
import java.util.List;

import com.maicard.security.criteria.UserDataCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserData;

public interface UserDataService {

	int insert(UserData userData);

	int update(UserData userData);
	
	UserData select(UserDataCriteria userDataCriteria);

	List<UserData> list(UserDataCriteria userDataCriteria);
	
	int count(UserDataCriteria userDataCriteria);
	
	//void deleteByUser(int uuid);
	
	int delete(UserDataCriteria userDataCriteria);

	void processUserConfig(User user) throws Exception;

	//UserData selectByValue(UserDataCriteria userDataCriteria);

	HashMap<String, UserData> map(UserDataCriteria userDataCriteria);
	
	HashMap<String, UserData> generateStandardMap(
			UserDataCriteria userDataCriteria);

}

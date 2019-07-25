package com.maicard.security.service;

import com.maicard.common.domain.Location;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserDynamicData;

public interface UserDynamicDataService {

	UserDynamicData insert(UserDynamicData user);

	UserDynamicData update(UserDynamicData user);	

	int delete(UserDynamicData frontUser);
	
	UserDynamicData select(int userTypeId, long uuid);
	
	void flushUserVariableDataCache();

	UserDynamicData generateFromUser(User user);

	void applyToUser(User user);

	void applyLocation(long uuid, Location location);

	
}

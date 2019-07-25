package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.UserLocationCriteria;
import com.maicard.security.domain.UserLocation;

public interface UserLocationService {

	
	void deleteUserLocation(long uuid, long ownerId, String locationType);

	UserLocation getUserLocation(long uuid, long ownerId, String locationType);

	int count(long ownerId);

	List<UserLocation> listOnPage(UserLocationCriteria userLocationCriteria);

	void setUserLocation(UserLocation userLocation);
	
	

	
	

}

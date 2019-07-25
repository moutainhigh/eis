package com.maicard.security.dao;

import java.util.List;
import java.util.Map;

import com.maicard.security.criteria.UserDataCriteria;
import com.maicard.security.domain.UserData;

public interface UserDataDao {

	int insert(UserData userData) throws Exception;

	int update(UserData userData) throws Exception;

	UserData select(UserDataCriteria userDataCriteria) throws Exception;

	List<UserData> list(UserDataCriteria userDataCriteria) throws Exception;

	List<UserData> listOnPage(UserDataCriteria userDataCriteria) throws Exception;

	int count(UserDataCriteria userDataCriteria) throws Exception;

	int delete(UserDataCriteria userDataCriteria);

	List<String> listPk(UserDataCriteria userDataCriteria) throws Exception;

	List<String> listPkOnPage(UserDataCriteria userDataCriteria)
			throws Exception;

	Map<String, UserData> map(UserDataCriteria userDataCriteria);


}

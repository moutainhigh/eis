package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserDynamicData;

public interface PartnerDao {

	int insert(User partner) throws DataAccessException;

	int update(User partner) throws DataAccessException;

	int delete(long uuid) throws DataAccessException;

	User select(long uuid) throws DataAccessException;

	List<User> list(UserCriteria partnerCriteria) throws DataAccessException;
	
	List<User> listOnPage(UserCriteria partnerCriteria) throws DataAccessException;
	
	int count(UserCriteria partnerCriteria) throws DataAccessException;
	
	int updateDynamicData(UserDynamicData userDynamicData);

	List<Long> listPk(UserCriteria partnerCriteria)
			throws DataAccessException;

	List<Long> listPkOnPage(UserCriteria partnerCriteria)
			throws DataAccessException;

	String listProgeny(long rootUuid);

	int updateNoNull(User user);



}

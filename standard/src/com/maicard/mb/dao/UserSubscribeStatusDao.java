package com.maicard.mb.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.mb.criteria.UserSubscribeStatusCriteria;
import com.maicard.mb.domain.UserSubscribeStatus;

public interface UserSubscribeStatusDao {

	int insert(UserSubscribeStatus userSubscribeStatus) throws DataAccessException;

	int update(UserSubscribeStatus userSubscribeStatus) throws DataAccessException;

	int delete(int ugid) throws DataAccessException;

	UserSubscribeStatus select(int ugid) throws DataAccessException;

	List<UserSubscribeStatus> list(UserSubscribeStatusCriteria userSubscribeStatusCriteria) throws DataAccessException;
	
	List<UserSubscribeStatus> listOnPage(UserSubscribeStatusCriteria userSubscribeStatusCriteria) throws DataAccessException;
	
	int count(UserSubscribeStatusCriteria userSubscribeStatusCriteria) throws DataAccessException;
		


}

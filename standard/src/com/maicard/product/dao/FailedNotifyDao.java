package com.maicard.product.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.product.criteria.FailedNotifyCriteria;
import com.maicard.product.domain.FailedNotify;

public interface FailedNotifyDao {

	int insert(FailedNotify failedNotify) throws DataAccessException;
	

	List<FailedNotify> list(FailedNotifyCriteria failedNotifyCriteria) throws DataAccessException;
	
	List<FailedNotify> listOnPage(FailedNotifyCriteria failedNotifyCriteria) throws DataAccessException;
	
	int count(FailedNotifyCriteria failedNotifyCriteria) throws DataAccessException;


	int delete(String transactionId);

	int update(FailedNotify failedNotify);


	int replace(FailedNotify failedNotify);

}

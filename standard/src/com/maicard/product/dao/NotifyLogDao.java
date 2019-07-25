package com.maicard.product.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.product.criteria.NotifyLogCriteria;
import com.maicard.product.domain.NotifyLog;

public interface NotifyLogDao {

	int insert(NotifyLog notifyLog) throws DataAccessException;
	

	List<NotifyLog> list(NotifyLogCriteria notifyLogCriteria) throws DataAccessException;
	
	List<NotifyLog> listOnPage(NotifyLogCriteria notifyLogCriteria) throws DataAccessException;
	
	int count(NotifyLogCriteria notifyLogCriteria) throws DataAccessException;

	List<NotifyLog> getFailedNotify(NotifyLogCriteria notifyLogCriteria);


	int cleanOldNotifyLog(NotifyLogCriteria notifyLogCriteria);


	List<NotifyLog> getUnSendNotify(NotifyLogCriteria notifyLogCriteria);

}

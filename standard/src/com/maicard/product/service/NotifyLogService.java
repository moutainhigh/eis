package com.maicard.product.service;

import java.util.List;

import com.maicard.product.criteria.NotifyLogCriteria;
import com.maicard.product.domain.NotifyLog;


public interface NotifyLogService {
	
	int insert(NotifyLog notifyLog);
	List<NotifyLog> list(NotifyLogCriteria notifyLogCriteria);
	List<NotifyLog> getFailedNotify(NotifyLogCriteria notifyLogCriteria);
	int count(NotifyLogCriteria notifyLogCriteria);
	int insertLocal(NotifyLog notifyLog);
	void cleanOldNotifyLog();
	List<NotifyLog> getUnSendNotify(NotifyLogCriteria notifyLogCriteria);

}

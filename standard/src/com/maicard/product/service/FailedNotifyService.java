package com.maicard.product.service;

import java.util.List;

import com.maicard.product.criteria.FailedNotifyCriteria;
import com.maicard.product.domain.FailedNotify;


public interface FailedNotifyService {
	
	int insert(FailedNotify failedNotify);
	List<FailedNotify> list(FailedNotifyCriteria failedNotifyCriteria);
	int count(FailedNotifyCriteria failedNotifyCriteria);
	int delete(String transactionId);
	int update(FailedNotify failedNotify);
	int replace(FailedNotify failedNotify);

}

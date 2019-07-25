package com.maicard.product.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.product.criteria.FailedNotifyCriteria;
import com.maicard.product.dao.FailedNotifyDao;
import com.maicard.product.domain.FailedNotify;
import com.maicard.product.service.FailedNotifyService;

@Service
public class FailedNotifyServiceImpl extends BaseService implements FailedNotifyService{

	@Resource
	private FailedNotifyDao failedNotifyDao;




	@Override
	public int insert(FailedNotify failedNotify) {		
		return failedNotifyDao.insert(failedNotify);
	}
	
	@Override
	public int update(FailedNotify failedNotify) {		
		return failedNotifyDao.update(failedNotify);
	}

	
	@Override
	public List<FailedNotify> list(FailedNotifyCriteria failedNotifyCriteria) {
		return failedNotifyDao.list(failedNotifyCriteria);
	}



	@Override
	public int count(FailedNotifyCriteria failedNotifyCriteria) {
		return failedNotifyDao.count(failedNotifyCriteria);
	}

	@Override
	public int delete(String transactionId) {
		return failedNotifyDao.delete(transactionId);
	}

	@Override
	public int replace(FailedNotify failedNotify) {
		return failedNotifyDao.replace(failedNotify);
	}

	

}

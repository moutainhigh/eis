package com.maicard.product.service.impl;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.product.criteria.ActivityLogCriteria;
import com.maicard.product.dao.ActivityLogDao;
import com.maicard.product.domain.ActivityLog;
import com.maicard.product.service.ActivityLogService;

@Service
public class ActivityLogServiceImpl extends BaseService implements ActivityLogService{

	@Resource
	private ActivityLogDao activityLogDao;
	
	
	@Override
	public int insert(ActivityLog activityLog) {
		return activityLogDao.insert(activityLog);
	}

	@Override
	public int update(ActivityLog activityLog) {
		return activityLogDao.update(activityLog);

	}

	@Override
	public int delete(int activityLogId) {
		return activityLogDao.delete(activityLogId);

	}

	@Override
	public ActivityLog select(int activityLogId) {
		return activityLogDao.select(activityLogId);
	}

	@Override
	public List<ActivityLog> list(ActivityLogCriteria activityLogCriteria) {
		return activityLogDao.list(activityLogCriteria);
	}
	
	@Override
	public List<ActivityLog> like(ActivityLogCriteria activityLogCriteria) {
		return activityLogDao.like(activityLogCriteria);
	}

	@Override
	public List<ActivityLog> listOnPage(ActivityLogCriteria activityLogCriteria) {
		return activityLogDao.listOnPage(activityLogCriteria);

	}

	@Override
	public int count(ActivityLogCriteria activityLogCriteria) {
		return activityLogDao.count(activityLogCriteria);

	}

}

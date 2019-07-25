package com.maicard.product.service;

import java.util.List;

import com.maicard.product.criteria.ActivityLogCriteria;
import com.maicard.product.domain.ActivityLog;

public interface ActivityLogService {

	int insert(ActivityLog activityLog);

	int update(ActivityLog activityLog);

	int delete(int activityLogId);

	ActivityLog select(int activityLogId);

	List<ActivityLog> list(ActivityLogCriteria activityLogCriteria);
	
	List<ActivityLog> like(ActivityLogCriteria activityLogCriteria);

	List<ActivityLog> listOnPage(ActivityLogCriteria activityLogCriteria);

	int count(ActivityLogCriteria activityLogCriteria);

}

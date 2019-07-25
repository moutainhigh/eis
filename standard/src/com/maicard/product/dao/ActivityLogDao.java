package com.maicard.product.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.product.criteria.ActivityLogCriteria;
import com.maicard.product.domain.ActivityLog;

public interface ActivityLogDao {

	int insert(ActivityLog activityLog) throws DataAccessException;

	int update(ActivityLog activityLog) throws DataAccessException;

	int delete(int activityLogId) throws DataAccessException;

	ActivityLog select(int activityLogId) throws DataAccessException;

	int count(ActivityLogCriteria activityLogCriteria) throws DataAccessException;

	List<ActivityLog> list(ActivityLogCriteria activityLogCriteria);
	
	List<ActivityLog> like(ActivityLogCriteria activityLogCriteria);

	List<ActivityLog> listOnPage(ActivityLogCriteria activityLogCriteria);

}

package com.maicard.product.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.product.criteria.ActivityLogCriteria;
import com.maicard.product.dao.ActivityLogDao;
import com.maicard.product.domain.ActivityLog;

@Repository
public class ActivityLogDaoImpl extends BaseDao implements ActivityLogDao {

	@Override
	public int insert(ActivityLog activityLog) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("com.maicard.product.sql.ActivityLog.insert", activityLog);

	}

	@Override
	public int update(ActivityLog activityLog) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.product.sql.ActivityLog.update", activityLog);
	}

	@Override
	public int delete(int activityLogId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.product.sql.ActivityLog.delete", new Integer(activityLogId));

	}

	@Override
	public ActivityLog select(int activityLogId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("com.maicard.product.sql.ActivityLog.select", new Integer(activityLogId));

	}

	@Override
	public int count(ActivityLogCriteria activityLogCriteria)
			throws DataAccessException {

		Assert.notNull(activityLogCriteria, "activityLogCriteria must not be null");
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.product.sql.ActivityLog.count", activityLogCriteria)).intValue();
	}

	@Override
	public List<ActivityLog> list(ActivityLogCriteria activityLogCriteria) {
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.ActivityLog.list", activityLogCriteria);
	}
	
	@Override
	public List<ActivityLog> like(ActivityLogCriteria activityLogCriteria) {
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.ActivityLog.like", activityLogCriteria);
	}

	@Override
	public List<ActivityLog> listOnPage(ActivityLogCriteria activityLogCriteria) {
		Assert.notNull(activityLogCriteria, "activityLogCriteria must not be null");
		Assert.notNull(activityLogCriteria.getPaging(), "paging must not be null");
		int totalResults = count(activityLogCriteria);
		Paging paging = activityLogCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.ActivityLog.list", activityLogCriteria, rowBounds);
	}

}

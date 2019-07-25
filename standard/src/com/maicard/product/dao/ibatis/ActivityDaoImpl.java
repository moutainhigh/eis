package com.maicard.product.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.product.criteria.ActivityCriteria;
import com.maicard.product.dao.ActivityDao;
import com.maicard.product.domain.Activity;

@Repository
public class ActivityDaoImpl extends BaseDao implements ActivityDao {	
	//private final String cacheName = CommonStandard.cacheNameProduct;
	@Override
	public int insert(Activity activity) throws DataAccessException {
		activity.setActivityCode(activity.getActivityCode().trim());
		return (Integer)getSqlSessionTemplate().insert("com.maicard.product.sql.Activity.insert", activity);
	}
	@Override
	//@CacheEvict(value = cacheName, key = "'Activity#' + #activity.activityId")
	public int update(Activity activity) throws DataAccessException {
		activity.setActivityCode(activity.getActivityCode().trim());
		return getSqlSessionTemplate().update("com.maicard.product.sql.Activity.update", activity);
	}
	@Override
	//@CacheEvict(value = cacheName, key = "'Activity#' + #activityId")
	public int delete(long activityId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.product.sql.Activity.delete", activityId);
	}
	@Override
	//@Cacheable(value = cacheName, key = "'Activity#' + #activityId")
	public Activity select(long activityId) throws DataAccessException {
		if(logger.isDebugEnabled()){
			logger.debug("从数据库中获取活动[" + activityId + "]");
		}
		return getSqlSessionTemplate().selectOne("com.maicard.product.sql.Activity.select", activityId);
	}
	@Override
	public int count(ActivityCriteria activityCriteria) throws DataAccessException {
		Assert.notNull(activityCriteria, "activityCriteria must not be null");
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.product.sql.Activity.count", activityCriteria)).intValue();
	}
	@Override
	public List<Long> listPk(ActivityCriteria activityCriteria) {
		Assert.notNull(activityCriteria, "activityCriteria must not be null");

		return getSqlSessionTemplate().selectList("com.maicard.product.sql.Activity.listPk", activityCriteria);
	}

	@Override
	public List<Long> listPkOnPage(ActivityCriteria activityCriteria) {
		Assert.notNull(activityCriteria, "activityCriteria must not be null");
		Assert.notNull(activityCriteria.getPaging(), "paging must not be null");
		int totalResults = count(activityCriteria);
		Paging paging = activityCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.Activity.listPk", activityCriteria, rowBounds);
	}

	
}

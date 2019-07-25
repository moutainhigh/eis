package com.maicard.product.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.product.criteria.ActivityCriteria;
import com.maicard.product.domain.Activity;

public interface ActivityDao {

	int insert(Activity activity) throws DataAccessException;

	int update(Activity activity) throws DataAccessException;

	int delete(long activityId) throws DataAccessException;

	Activity select(long activityId) throws DataAccessException;

	int count(ActivityCriteria activityCriteria) throws DataAccessException;

	List<Long> listPk(ActivityCriteria activityCriteria);

	List<Long> listPkOnPage(ActivityCriteria activityCriteria);

}

package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.UserLevelConditionCriteria;
import com.maicard.security.dao.UserLevelConditionDao;
import com.maicard.security.domain.UserLevelCondition;

@Repository
public class UserLevelConditionDaoImpl extends BaseDao implements UserLevelConditionDao {

	public int insert(UserLevelCondition userLevelCondition) throws DataAccessException {
		return getSqlSessionTemplate().insert("UserLevelCondition.insert", userLevelCondition);
	}

	public int update(UserLevelCondition userLevelCondition) throws DataAccessException {


		return getSqlSessionTemplate().update("UserLevelCondition.update", userLevelCondition);


	}

	public int delete(int userLevelConditionId) throws DataAccessException {


		return getSqlSessionTemplate().delete("UserLevelCondition.delete", new Integer(userLevelConditionId));


	}

	public UserLevelCondition select(int userLevelConditionId) throws DataAccessException {
		return (UserLevelCondition) getSqlSessionTemplate().selectOne("UserLevelCondition.select", new Integer(userLevelConditionId));
	}


	public List<UserLevelCondition> list(UserLevelConditionCriteria userLevelConditionCriteria) throws DataAccessException {
		Assert.notNull(userLevelConditionCriteria, "userLevelConditionCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("UserLevelCondition.list", userLevelConditionCriteria);
	}


	public List<UserLevelCondition> listOnPage(UserLevelConditionCriteria userLevelConditionCriteria) throws DataAccessException {
		Assert.notNull(userLevelConditionCriteria, "userLevelConditionCriteria must not be null");
		Assert.notNull(userLevelConditionCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(userLevelConditionCriteria);
		Paging paging = userLevelConditionCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("UserLevelCondition.list", userLevelConditionCriteria, rowBounds);
	}

	public int count(UserLevelConditionCriteria userLevelConditionCriteria) throws DataAccessException {
		Assert.notNull(userLevelConditionCriteria, "userLevelConditionCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("UserLevelCondition.count", userLevelConditionCriteria)).intValue();
	}

}

package com.maicard.mb.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.mb.criteria.UserSubscribeMessageRelationCriteria;
import com.maicard.mb.dao.UserSubscribeMessageRelationDao;
import com.maicard.mb.domain.UserMessageRelation;

@Repository
public class UserSubscribeMessageRelationDaoImpl extends BaseDao implements UserSubscribeMessageRelationDao {

	public int insert(UserMessageRelation userMessageRelation) throws DataAccessException {
		return getSqlSessionTemplate().insert("UserSubscribeMessageRelation.insert", userMessageRelation);

	}

	public int update(UserMessageRelation userMessageRelation) throws DataAccessException {
		return getSqlSessionTemplate().update("UserSubscribeMessageRelation.update", userMessageRelation);
	}

	public int delete(int ugid) throws DataAccessException {
		return getSqlSessionTemplate().delete("UserSubscribeMessageRelation.delete", new Integer(ugid));
	}

	

	public UserMessageRelation select(int ugid) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("UserSubscribeMessageRelation.select", new Integer(ugid));
	}

	public List<UserMessageRelation> list(UserSubscribeMessageRelationCriteria userSubscribeMessageRelationCriteria) throws DataAccessException {
		Assert.notNull(userSubscribeMessageRelationCriteria, "userSubscribeMessageRelationCriteria must not be null");
		return getSqlSessionTemplate().selectList("UserSubscribeMessageRelation.list", userSubscribeMessageRelationCriteria);
	}

	public List<UserMessageRelation> listOnPage(UserSubscribeMessageRelationCriteria userSubscribeMessageRelationCriteria) throws DataAccessException {
		Assert.notNull(userSubscribeMessageRelationCriteria, "userSubscribeMessageRelationCriteria must not be null");
		Assert.notNull(userSubscribeMessageRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(userSubscribeMessageRelationCriteria);
		Paging paging = userSubscribeMessageRelationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("UserSubscribeMessageRelation.list", userSubscribeMessageRelationCriteria, rowBounds);
	}
	
	public int count(UserSubscribeMessageRelationCriteria userSubscribeMessageRelationCriteria) throws DataAccessException {
		Assert.notNull(userSubscribeMessageRelationCriteria, "userSubscribeMessageRelationCriteria must not be null");		
		return ((Integer) getSqlSessionTemplate().selectOne("UserSubscribeMessageRelation.count", userSubscribeMessageRelationCriteria)).intValue();
	}
}

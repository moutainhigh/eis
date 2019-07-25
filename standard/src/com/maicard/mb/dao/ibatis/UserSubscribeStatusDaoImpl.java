package com.maicard.mb.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.mb.criteria.UserSubscribeStatusCriteria;
import com.maicard.mb.dao.UserSubscribeStatusDao;
import com.maicard.mb.domain.UserSubscribeStatus;

@Repository
public class UserSubscribeStatusDaoImpl extends BaseDao implements UserSubscribeStatusDao {

	public int insert(UserSubscribeStatus userSubscribeStatus) throws DataAccessException {
		int ugid = Integer.parseInt("" + getSqlSessionTemplate().insert("UserSubscribeStatus.insert", userSubscribeStatus));
		return ugid;

	}

	public int update(UserSubscribeStatus userSubscribeStatus) throws DataAccessException {
		return getSqlSessionTemplate().update("UserSubscribeStatus.update", userSubscribeStatus);
	}

	public int delete(int ugid) throws DataAccessException {
		return getSqlSessionTemplate().delete("UserSubscribeStatus.delete", new Integer(ugid));
	}

	

	public UserSubscribeStatus select(int ugid) throws DataAccessException {
		return (UserSubscribeStatus) getSqlSessionTemplate().selectOne("UserSubscribeStatus.select", new Integer(ugid));
	}

	public List<UserSubscribeStatus> list(UserSubscribeStatusCriteria userSubscribeStatusCriteria) throws DataAccessException {
		Assert.notNull(userSubscribeStatusCriteria, "userSubscribeStatusCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("UserSubscribeStatus.list", userSubscribeStatusCriteria);
	}

	public List<UserSubscribeStatus> listOnPage(UserSubscribeStatusCriteria userSubscribeStatusCriteria) throws DataAccessException {
		Assert.notNull(userSubscribeStatusCriteria, "userSubscribeStatusCriteria must not be null");
		Assert.notNull(userSubscribeStatusCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(userSubscribeStatusCriteria);
		Paging paging = userSubscribeStatusCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("UserSubscribeStatus.list", userSubscribeStatusCriteria, rowBounds);
	}
	
	public int count(UserSubscribeStatusCriteria userSubscribeStatusCriteria) throws DataAccessException {
		Assert.notNull(userSubscribeStatusCriteria, "userSubscribeStatusCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("UserSubscribeStatus.count", userSubscribeStatusCriteria)).intValue();
	}
}

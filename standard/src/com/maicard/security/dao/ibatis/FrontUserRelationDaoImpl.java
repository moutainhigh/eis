package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.dao.FrontUserRelationDao;
import com.maicard.security.domain.UserRelation;

@Repository
public class FrontUserRelationDaoImpl extends BaseDao implements FrontUserRelationDao {

	public int insert(UserRelation userRelation) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("FrontUserRelation.insert", userRelation)).intValue();
	}

	public int update(UserRelation userRelation) throws DataAccessException {
		return getSqlSessionTemplate().update("FrontUserRelation.update", userRelation);
	}

	public int delete(int front_user_focus_id) throws DataAccessException {
		return getSqlSessionTemplate().delete("FrontUserRelation.delete", new Integer(front_user_focus_id));
	}
	
	public int delete(UserRelationCriteria userRelationCriteria) throws DataAccessException {
		Assert.notNull(userRelationCriteria, "frontUserFocusCriteria must not be null");
		return getSqlSessionTemplate().delete("FrontUserRelation.deleteByCriteria", userRelationCriteria);
	}
	

	public UserRelation select(long front_user_focus_id) throws DataAccessException {
		return (UserRelation) getSqlSessionTemplate().selectOne("FrontUserRelation.select", front_user_focus_id);
	}

	public List<UserRelation> list(UserRelationCriteria userRelationCriteria) throws DataAccessException {
		Assert.notNull(userRelationCriteria, "frontUserFocusCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("FrontUserRelation.list", userRelationCriteria);
	}

	public List<UserRelation> listOnPage(UserRelationCriteria userRelationCriteria) throws DataAccessException {
		Assert.notNull(userRelationCriteria, "frontUserFocusCriteria must not be null");
		Assert.notNull(userRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(userRelationCriteria);
		Paging paging = userRelationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("FrontUserRelation.list", userRelationCriteria, rowBounds);
	}

	public int count(UserRelationCriteria userRelationCriteria) throws DataAccessException {
		Assert.notNull(userRelationCriteria, "frontUserFocusCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("FrontUserRelation.count", userRelationCriteria)).intValue();
	}
	
	


}

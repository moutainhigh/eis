package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.UserRoleRelationCriteria;
import com.maicard.security.dao.FrontUserRoleRelationDao;
import com.maicard.security.domain.UserRoleRelation;

@Repository
public class FrontUserRoleRelationDaoImpl extends BaseDao implements FrontUserRoleRelationDao {

	public int insert(UserRoleRelation userRoleRelation) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("FrontUserRoleRelation.insert", userRoleRelation)).intValue();
	}

	public int update(UserRoleRelation userRoleRelation) throws DataAccessException {
		return getSqlSessionTemplate().update("FrontUserRoleRelation.update", userRoleRelation);
	}

	public int delete(int frontUserRoleRelationId) throws DataAccessException {
		return getSqlSessionTemplate().delete("FrontUserRoleRelation.delete", new Integer(frontUserRoleRelationId));


	}

	public UserRoleRelation select(int frontUserRoleRelationId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("FrontUserRoleRelation.select", new Integer(frontUserRoleRelationId));
	}

	public List<UserRoleRelation> list(UserRoleRelationCriteria frontUserRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(frontUserRoleRelationCriteria, "frontUserRoleRelationCriteria must not be null");
		return getSqlSessionTemplate().selectList("FrontUserRoleRelation.list", frontUserRoleRelationCriteria);
	}

	public List<UserRoleRelation> listOnPage(UserRoleRelationCriteria frontUserRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(frontUserRoleRelationCriteria, "frontUserRoleRelationCriteria must not be null");
		Assert.notNull(frontUserRoleRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(frontUserRoleRelationCriteria);
		Paging paging = frontUserRoleRelationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("FrontUserRoleRelation.list", frontUserRoleRelationCriteria, rowBounds);
	}

	public int count(UserRoleRelationCriteria frontUserRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(frontUserRoleRelationCriteria, "frontUserRoleRelationCriteria must not be null");	
		return getSqlSessionTemplate().selectOne("FrontUserRoleRelation.count", frontUserRoleRelationCriteria);
	}
	
	public 	void deleteByUuid(long uuid) throws DataAccessException{
		getSqlSessionTemplate().delete("FrontUserRoleRelation.deleteByUuid", uuid);
	
	}

}

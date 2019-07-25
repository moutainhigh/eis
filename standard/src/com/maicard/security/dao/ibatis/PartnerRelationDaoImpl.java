package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.dao.PartnerRelationDao;
import com.maicard.security.domain.UserRelation;

@Repository
public class PartnerRelationDaoImpl extends BaseDao implements PartnerRelationDao {

	
	public int insert(UserRelation partnerRelation) throws DataAccessException {
		return getSqlSessionTemplate().insert("PartnerRelation.insert", partnerRelation);
	}

	public int update(UserRelation partnerRelation) throws DataAccessException {


		return getSqlSessionTemplate().update("PartnerRelation.update", partnerRelation);


	}

	public int delete(int partnerRelationId) throws DataAccessException {


		return getSqlSessionTemplate().delete("PartnerRelation.delete", new Integer(partnerRelationId));


	}

	public UserRelation select(int partnerRelationId) throws DataAccessException {
		return (UserRelation) getSqlSessionTemplate().selectOne("PartnerRelation.select", new Integer(partnerRelationId));
	}


	public List<UserRelation> list(UserRelationCriteria partnerRelationCriteria) throws DataAccessException {
		Assert.notNull(partnerRelationCriteria, "partnerRelationCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("PartnerRelation.list", partnerRelationCriteria);
	}


	public List<UserRelation> listOnPage(UserRelationCriteria partnerRelationCriteria) throws DataAccessException {
		Assert.notNull(partnerRelationCriteria, "partnerRelationCriteria must not be null");
		Assert.notNull(partnerRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(partnerRelationCriteria);
		Paging paging = partnerRelationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("PartnerRelation.list", partnerRelationCriteria, rowBounds);
	}

	public int count(UserRelationCriteria partnerRelationCriteria) throws DataAccessException {
		Assert.notNull(partnerRelationCriteria, "partnerRelationCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("PartnerRelation.count", partnerRelationCriteria)).intValue();
	}

}

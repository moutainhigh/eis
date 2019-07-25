package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.UserRoleRelationCriteria;
import com.maicard.security.dao.PartnerRoleRelationDao;
import com.maicard.security.domain.UserRoleRelation;

@Repository
public class PartnerRoleRelationDaoImpl extends BaseDao implements PartnerRoleRelationDao {

	public int insert(UserRoleRelation partnerRoleRelation) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.security.sql.PartnerRoleRelation.insert", partnerRoleRelation);
	}

	public int update(UserRoleRelation partnerRoleRelation) throws DataAccessException {


		return getSqlSessionTemplate().update("com.maicard.security.sql.PartnerRoleRelation.update", partnerRoleRelation);


	}

	public int delete(int partnerRoleRelationId) throws DataAccessException {


		return getSqlSessionTemplate().delete("com.maicard.security.sql.PartnerRoleRelation.delete", new Integer(partnerRoleRelationId));


	}

	public UserRoleRelation select(int partnerRoleRelationId) throws DataAccessException {
		return (UserRoleRelation) getSqlSessionTemplate().selectOne("com.maicard.security.sql.PartnerRoleRelation.select", new Integer(partnerRoleRelationId));
	}


	public List<UserRoleRelation> list(UserRoleRelationCriteria partnerRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(partnerRoleRelationCriteria, "partnerRoleRelationCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("com.maicard.security.sql.PartnerRoleRelation.list", partnerRoleRelationCriteria);
	}


	public List<UserRoleRelation> listOnPage(UserRoleRelationCriteria partnerRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(partnerRoleRelationCriteria, "partnerRoleRelationCriteria must not be null");
		Assert.notNull(partnerRoleRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(partnerRoleRelationCriteria);
		Paging paging = partnerRoleRelationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("com.maicard.security.sql.PartnerRoleRelation.list", partnerRoleRelationCriteria, rowBounds);
	}

	public int count(UserRoleRelationCriteria partnerRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(partnerRoleRelationCriteria, "partnerRoleRelationCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.security.sql.PartnerRoleRelation.count", partnerRoleRelationCriteria)).intValue();
	}

	@Override
	public void deleteByUuid(long uuid) {
		getSqlSessionTemplate().delete("com.maicard.security.sql.PartnerRoleRelation.deleteByUuid", uuid);
		
	}

}

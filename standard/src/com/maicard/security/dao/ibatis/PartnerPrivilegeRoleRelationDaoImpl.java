package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.PrivilegeRoleRelationCriteria;
import com.maicard.security.dao.PartnerPrivilegeRoleRelationDao;
import com.maicard.security.domain.PrivilegeRoleRelation;

@Repository
public class PartnerPrivilegeRoleRelationDaoImpl extends BaseDao implements PartnerPrivilegeRoleRelationDao {


	
	public void insert(PrivilegeRoleRelation partnerPrivilegeRoleRelation) throws DataAccessException {
		getSqlSessionTemplate().insert("PartnerPrivilegeRoleRelation.insert", partnerPrivilegeRoleRelation);
	}

	public int update(PrivilegeRoleRelation partnerPrivilegeRoleRelation) throws DataAccessException {


		return getSqlSessionTemplate().update("PartnerPrivilegeRoleRelation.update", partnerPrivilegeRoleRelation);


	}

	public int delete(int partnerPrivilegeRoleRelationId) throws DataAccessException {


		return getSqlSessionTemplate().delete("PartnerPrivilegeRoleRelation.delete", new Integer(partnerPrivilegeRoleRelationId));


	}

	public PrivilegeRoleRelation select(int partnerPrivilegeRoleRelationId) throws DataAccessException {
		return (PrivilegeRoleRelation) getSqlSessionTemplate().selectOne("PartnerPrivilegeRoleRelation.select", new Integer(partnerPrivilegeRoleRelationId));
	}


	public List<PrivilegeRoleRelation> list(PrivilegeRoleRelationCriteria partnerPrivilegeRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(partnerPrivilegeRoleRelationCriteria, "partnerPrivilegeRoleRelationCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("PartnerPrivilegeRoleRelation.list", partnerPrivilegeRoleRelationCriteria);
	}


	public List<PrivilegeRoleRelation> listOnPage(PrivilegeRoleRelationCriteria partnerPrivilegeRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(partnerPrivilegeRoleRelationCriteria, "partnerPrivilegeRoleRelationCriteria must not be null");
		Assert.notNull(partnerPrivilegeRoleRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(partnerPrivilegeRoleRelationCriteria);
		Paging paging = partnerPrivilegeRoleRelationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("PartnerPrivilegeRoleRelation.list", partnerPrivilegeRoleRelationCriteria, rowBounds);
	}

	public int count(PrivilegeRoleRelationCriteria partnerPrivilegeRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(partnerPrivilegeRoleRelationCriteria, "partnerPrivilegeRoleRelationCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("PartnerPrivilegeRoleRelation.count", partnerPrivilegeRoleRelationCriteria)).intValue();
	}

}

package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.PrivilegeRelationCriteria;
import com.maicard.security.dao.PartnerPrivilegeRelationDao;
import com.maicard.security.domain.PrivilegeRelation;

@Repository
public class PartnerPrivilegeRelationDaoImpl extends BaseDao implements PartnerPrivilegeRelationDao {

	
	public void insert(PrivilegeRelation partnerPrivilegeRelation) throws DataAccessException {
		getSqlSessionTemplate().insert("PartnerPrivilegeRelation.insert", partnerPrivilegeRelation);
	}

	public int update(PrivilegeRelation partnerPrivilegeRelation) throws DataAccessException {


		return getSqlSessionTemplate().update("PartnerPrivilegeRelation.update", partnerPrivilegeRelation);


	}

	public int delete(int privilegeRelationId) throws DataAccessException {


		return getSqlSessionTemplate().delete("PartnerPrivilegeRelation.delete", new Integer(privilegeRelationId));


	}

	public PrivilegeRelation select(int privilegeRelationId) throws DataAccessException {
		return (PrivilegeRelation) getSqlSessionTemplate().selectOne("PartnerPrivilegeRelation.select", new Integer(privilegeRelationId));
	}


	public List<PrivilegeRelation> list(PrivilegeRelationCriteria partnerPrivilegeRelationCriteria) throws DataAccessException {
		Assert.notNull(partnerPrivilegeRelationCriteria, "partnerPrivilegeRelationCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("PartnerPrivilegeRelation.list", partnerPrivilegeRelationCriteria);
	}


	public List<PrivilegeRelation> listOnPage(PrivilegeRelationCriteria partnerPrivilegeRelationCriteria) throws DataAccessException {
		Assert.notNull(partnerPrivilegeRelationCriteria, "partnerPrivilegeRelationCriteria must not be null");
		Assert.notNull(partnerPrivilegeRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(partnerPrivilegeRelationCriteria);
		Paging paging = partnerPrivilegeRelationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("PartnerPrivilegeRelation.list", partnerPrivilegeRelationCriteria, rowBounds);
	}

	public int count(PrivilegeRelationCriteria partnerPrivilegeRelationCriteria) throws DataAccessException {
		Assert.notNull(partnerPrivilegeRelationCriteria, "partnerPrivilegeRelationCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("PartnerPrivilegeRelation.count", partnerPrivilegeRelationCriteria)).intValue();
	}

}

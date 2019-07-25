package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.PrivilegeRoleRelationCriteria;
import com.maicard.security.dao.FrontPrivilegeRoleRelationDao;
import com.maicard.security.domain.PrivilegeRoleRelation;

@Repository
public class FrontPrivilegeRoleRelationDaoImpl extends BaseDao implements FrontPrivilegeRoleRelationDao {

	public int insert(PrivilegeRoleRelation privilegeRoleRelation) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("FrontPrivilegeRoleRelation.insert", privilegeRoleRelation)).intValue();
	}

	public int update(PrivilegeRoleRelation privilegeRoleRelation) throws DataAccessException {
		return getSqlSessionTemplate().update("FrontPrivilegeRoleRelation.update", privilegeRoleRelation);
	}

	public int delete(int frontPrivilegeRoleRelationId) throws DataAccessException {

		return getSqlSessionTemplate().delete("FrontPrivilegeRoleRelation.delete", new Integer(frontPrivilegeRoleRelationId));

	}

	public PrivilegeRoleRelation select(int frontPrivilegeRoleRelationId) throws DataAccessException {
		return (PrivilegeRoleRelation) getSqlSessionTemplate().selectOne("FrontPrivilegeRoleRelation.select", new Integer(frontPrivilegeRoleRelationId));
	}

	public List<PrivilegeRoleRelation> list(PrivilegeRoleRelationCriteria frontPrivilegeRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(frontPrivilegeRoleRelationCriteria, "frontPrivilegeRoleRelationCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("FrontPrivilegeRoleRelation.list", frontPrivilegeRoleRelationCriteria);
	}

	public List<PrivilegeRoleRelation> listOnPage(PrivilegeRoleRelationCriteria frontPrivilegeRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(frontPrivilegeRoleRelationCriteria, "frontPrivilegeRoleRelationCriteria must not be null");
		Assert.notNull(frontPrivilegeRoleRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(frontPrivilegeRoleRelationCriteria);
		Paging paging = frontPrivilegeRoleRelationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("FrontPrivilegeRoleRelation.list", frontPrivilegeRoleRelationCriteria, rowBounds);
	}

	public int count(PrivilegeRoleRelationCriteria frontPrivilegeRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(frontPrivilegeRoleRelationCriteria, "frontPrivilegeRoleRelationCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("FrontPrivilegeRoleRelation.count", frontPrivilegeRoleRelationCriteria)).intValue();
	}
	
	public void deleteByFrontRoleId(int frontRoleId) throws DataAccessException{
		getSqlSessionTemplate().delete("FrontPrivilegeRoleRelation.deleteByFrontRoleId", new Integer(frontRoleId));
		
	}


}

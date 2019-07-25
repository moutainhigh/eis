package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.PrivilegeRelationCriteria;
import com.maicard.security.dao.FrontPrivilegeRelationDao;
import com.maicard.security.domain.PrivilegeRelation;

@Repository
public class FrontPrivilegeRelationDaoImpl extends BaseDao implements FrontPrivilegeRelationDao {

	public int insert(PrivilegeRelation frontPrivilegeRelation) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("FrontPrivilegeRelation.insert", frontPrivilegeRelation)).intValue();
	}

	public int update(PrivilegeRelation frontPrivilegeRelation) throws DataAccessException {
		return getSqlSessionTemplate().update("FrontPrivilegeRelation.update", frontPrivilegeRelation);
	}

	public int delete(int privilegeId) throws DataAccessException {
		return getSqlSessionTemplate().delete("FrontPrivilegeRelation.delete", new Integer(privilegeId));
	}
	
	public void deleteByFrontPrivilegeId(int frontPrivilegeId)throws DataAccessException {
		getSqlSessionTemplate().delete("FrontPrivilegeRelation.deleteByFrontPrivilegeId", new Integer(frontPrivilegeId));

	}


	public PrivilegeRelation select(int privilegeId) throws DataAccessException {
		return (PrivilegeRelation) getSqlSessionTemplate().selectOne("FrontPrivilegeRelation.select", new Integer(privilegeId));
	}

	public List<PrivilegeRelation> list(PrivilegeRelationCriteria frontPrivilegeRelationCriteria) throws DataAccessException {
		Assert.notNull(frontPrivilegeRelationCriteria, "frontPrivilegeRelationCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("FrontPrivilegeRelation.list", frontPrivilegeRelationCriteria);
	}

	public List<PrivilegeRelation> listOnPage(PrivilegeRelationCriteria frontPrivilegeRelationCriteria) throws DataAccessException {
		Assert.notNull(frontPrivilegeRelationCriteria, "frontPrivilegeRelationCriteria must not be null");
		Assert.notNull(frontPrivilegeRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(frontPrivilegeRelationCriteria);
		Paging paging = frontPrivilegeRelationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("FrontPrivilegeRelation.list", frontPrivilegeRelationCriteria, rowBounds);
	}

	public int count(PrivilegeRelationCriteria frontPrivilegeRelationCriteria) throws DataAccessException {
		Assert.notNull(frontPrivilegeRelationCriteria, "frontPrivilegeRelationCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("FrontPrivilegeRelation.count", frontPrivilegeRelationCriteria)).intValue();
	}

}

package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.PrivilegeRelationCriteria;
import com.maicard.security.dao.SysPrivilegeRelationDao;
import com.maicard.security.domain.PrivilegeRelation;

@Repository
public class SysPrivilegeRelationDaoImpl extends BaseDao implements SysPrivilegeRelationDao {

	public int insert(PrivilegeRelation privilegeRelation) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("SysPrivilegeRelation.insert", privilegeRelation)).intValue();
	}

	public int update(PrivilegeRelation privilegeRelation) throws DataAccessException {


		return getSqlSessionTemplate().update("SysPrivilegeRelation.update", privilegeRelation);


	}

	public int delete(int privilegeId) throws DataAccessException {


		return getSqlSessionTemplate().delete("SysPrivilegeRelation.delete", new Integer(privilegeId));


	}
	
	public void deleteByCriteria(PrivilegeRelationCriteria privilegeRelationCriteria) throws DataAccessException {
		getSqlSessionTemplate().delete("SysPrivilegeRelation.deleteByCriteria", privilegeRelationCriteria);
	}


	public PrivilegeRelation select(int privilegeId) throws DataAccessException {
		return (PrivilegeRelation) getSqlSessionTemplate().selectOne("SysPrivilegeRelation.select", new Integer(privilegeId));
	}


	public List<PrivilegeRelation> list(PrivilegeRelationCriteria privilegeRelationCriteria) throws DataAccessException {
		Assert.notNull(privilegeRelationCriteria, "sysPrivilegeRelationCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("SysPrivilegeRelation.list", privilegeRelationCriteria);
	}


	public List<PrivilegeRelation> listOnPage(PrivilegeRelationCriteria privilegeRelationCriteria) throws DataAccessException {
		Assert.notNull(privilegeRelationCriteria, "sysPrivilegeRelationCriteria must not be null");
		Assert.notNull(privilegeRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(privilegeRelationCriteria);
		Paging paging = privilegeRelationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("SysPrivilegeRelation.list", privilegeRelationCriteria, rowBounds);
	}

	public int count(PrivilegeRelationCriteria privilegeRelationCriteria) throws DataAccessException {
		Assert.notNull(privilegeRelationCriteria, "sysPrivilegeRelationCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("SysPrivilegeRelation.count", privilegeRelationCriteria)).intValue();
	}

}

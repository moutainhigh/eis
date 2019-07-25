package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.PrivilegeRoleRelationCriteria;
import com.maicard.security.dao.SysPrivilegeRoleRelationDao;
import com.maicard.security.domain.PrivilegeRoleRelation;

@Repository
public class SysPrivilegeRoleRelationDaoImpl extends BaseDao implements SysPrivilegeRoleRelationDao {

	public int insert(PrivilegeRoleRelation sysPrivilegeGroupRelation) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("SysPrivilegeRoleRelation.insert", sysPrivilegeGroupRelation)).intValue();
	}

	public int update(PrivilegeRoleRelation sysPrivilegeGroupRelation) throws DataAccessException {


		return getSqlSessionTemplate().update("SysPrivilegeRoleRelation.update", sysPrivilegeGroupRelation);


	}

	public int delete(int sysPrivilegeGroupRelationId) throws DataAccessException {


		return getSqlSessionTemplate().delete("SysPrivilegeRoleRelation.delete", new Integer(sysPrivilegeGroupRelationId));


	}
	
	public void  deleteAllByGroupId(int groupId) throws DataAccessException{
		getSqlSessionTemplate().delete("SysPrivilegeRoleRelation.deleteAllByGroupId", new Integer(groupId));		
	}

	public PrivilegeRoleRelation select(int sysPrivilegeGroupRelationId) throws DataAccessException {
		return (PrivilegeRoleRelation) getSqlSessionTemplate().selectOne("SysPrivilegeRoleRelation.select", new Integer(sysPrivilegeGroupRelationId));
	}


	public List<PrivilegeRoleRelation> list(PrivilegeRoleRelationCriteria privilegeRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(privilegeRoleRelationCriteria, "sysPrivilegeGroupRelationCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("SysPrivilegeRoleRelation.list", privilegeRoleRelationCriteria);
	}


	public List<PrivilegeRoleRelation> listOnPage(PrivilegeRoleRelationCriteria privilegeRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(privilegeRoleRelationCriteria, "sysPrivilegeGroupRelationCriteria must not be null");
		Assert.notNull(privilegeRoleRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(privilegeRoleRelationCriteria);
		Paging paging = privilegeRoleRelationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("SysPrivilegeRoleRelation.list", privilegeRoleRelationCriteria, rowBounds);
	}

	public int count(PrivilegeRoleRelationCriteria privilegeRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(privilegeRoleRelationCriteria, "sysPrivilegeGroupRelationCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("SysPrivilegeRoleRelation.count", privilegeRoleRelationCriteria)).intValue();
	}

}

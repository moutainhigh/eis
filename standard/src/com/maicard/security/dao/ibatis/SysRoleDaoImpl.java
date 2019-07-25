package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.RoleCriteria;
import com.maicard.security.dao.SysRoleDao;
import com.maicard.security.domain.Role;

@Repository
public class SysRoleDaoImpl extends BaseDao implements SysRoleDao {

	public int insert(Role sysRole) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("SysRole.insert", sysRole);
	}

	public int update(Role sysRole) throws DataAccessException {


		return getSqlSessionTemplate().update("SysRole.update", sysRole);


	}

	public int delete(int roleId) throws DataAccessException {


		return getSqlSessionTemplate().delete("SysRole.delete", new Integer(roleId));


	}

	public Role select(int roleId) throws DataAccessException {
		return (Role) getSqlSessionTemplate().selectOne("SysRole.select", new Integer(roleId));
	}


	public List<Role> list(RoleCriteria roleCriteria) throws DataAccessException {
		Assert.notNull(roleCriteria, "sysRoleCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("SysRole.list", roleCriteria);
	}


	public List<Role> listOnPage(RoleCriteria roleCriteria) throws DataAccessException {
		Assert.notNull(roleCriteria, "sysGroupCriteria must not be null");
		Assert.notNull(roleCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(roleCriteria);
		Paging paging = roleCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("SysRole.list", roleCriteria, rowBounds);
	}

	public int count(RoleCriteria roleCriteria) throws DataAccessException {
		Assert.notNull(roleCriteria, "sysGroupCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("SysRole.count", roleCriteria)).intValue();
	}
	
	public int maxId() throws DataAccessException {
		return ((Integer) getSqlSessionTemplate().selectOne("SysRole.maxId")).intValue();
		
	}
	
	public int maxGroupLevel() throws DataAccessException{
		return ((Integer) getSqlSessionTemplate().selectOne("SysRole.maxGroupLevel")).intValue();
	}
}

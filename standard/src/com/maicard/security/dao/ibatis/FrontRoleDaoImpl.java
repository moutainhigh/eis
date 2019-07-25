package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.RoleCriteria;
import com.maicard.security.dao.FrontRoleDao;
import com.maicard.security.domain.Role;

@Repository
public class FrontRoleDaoImpl extends BaseDao implements FrontRoleDao {

	public int insert(Role frontRole) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("FrontRole.insert", frontRole)).intValue();
	}

	public int update(Role frontRole) throws DataAccessException {
		return getSqlSessionTemplate().update("FrontRole.update", frontRole);
	}

	public int delete(int frontRoleId) throws DataAccessException {
		return getSqlSessionTemplate().delete("FrontRole.delete", new Integer(frontRoleId));

	}

	public Role select(int frontRoleId) throws DataAccessException {
		return (Role) getSqlSessionTemplate().selectOne("FrontRole.select", new Integer(frontRoleId));
	}

	public List<Role> list(RoleCriteria frontRoleCriteria) throws DataAccessException {
		Assert.notNull(frontRoleCriteria, "frontRoleCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("FrontRole.list", frontRoleCriteria);
	}

	public List<Role> listOnPage(RoleCriteria frontRoleCriteria) throws DataAccessException {
		Assert.notNull(frontRoleCriteria, "frontRoleCriteria must not be null");
		Assert.notNull(frontRoleCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(frontRoleCriteria);
		Paging paging = frontRoleCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("FrontRole.list", frontRoleCriteria, rowBounds);
	}

	public int count(RoleCriteria frontRoleCriteria) throws DataAccessException {
		Assert.notNull(frontRoleCriteria, "frontRoleCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("FrontRole.count", frontRoleCriteria)).intValue();
	}

}

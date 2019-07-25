package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.dao.SysPrivilegeDao;
import com.maicard.security.domain.Privilege;
import com.maicard.security.domain.User;

@Repository
public class SysPrivilegeDaoImpl extends BaseDao implements SysPrivilegeDao {

	public int insert(Privilege sysPrivilege) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("SysPrivilege.insert", sysPrivilege);
	}

	public int update(Privilege sysPrivilege) throws DataAccessException {


		return getSqlSessionTemplate().update("SysPrivilege.update", sysPrivilege);


	}

	public int delete(int privilegeId) throws DataAccessException {


		return getSqlSessionTemplate().delete("SysPrivilege.delete", new Integer(privilegeId));


	}

	public Privilege select(int privilegeId) throws DataAccessException {
		return (Privilege) getSqlSessionTemplate().selectOne("SysPrivilege.select", new Integer(privilegeId));
	}
	

	public List<User> getSysUserByPrivilege(PrivilegeCriteria privilegeCriteria) throws DataAccessException{
		Assert.notNull(privilegeCriteria, "sysPrivilegeCriteria must not be null");
		return getSqlSessionTemplate().selectList("SysPrivilege.getSysUserByPrivilege",privilegeCriteria);
		
	}



	public List<Privilege> list(PrivilegeCriteria privilegeCriteria) throws DataAccessException {
		Assert.notNull(privilegeCriteria, "sysPrivilegeCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("SysPrivilege.list", privilegeCriteria);
	}


	public List<Privilege> listOnPage(PrivilegeCriteria privilegeCriteria) throws DataAccessException {
		Assert.notNull(privilegeCriteria, "sysPrivilegeCriteria must not be null");
		Assert.notNull(privilegeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(privilegeCriteria);
		Paging paging = privilegeCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("SysPrivilege.list", privilegeCriteria, rowBounds);
	}

	public int count(PrivilegeCriteria privilegeCriteria) throws DataAccessException {
		Assert.notNull(privilegeCriteria, "sysPrivilegeCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("SysPrivilege.count", privilegeCriteria)).intValue();
	}
	
	public int maxId() throws DataAccessException {
		return ((Integer) getSqlSessionTemplate().selectOne("SysPrivilege.maxId")).intValue();
	}

}

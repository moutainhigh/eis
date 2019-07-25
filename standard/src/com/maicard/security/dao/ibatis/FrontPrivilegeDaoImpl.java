package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.dao.FrontPrivilegeDao;
import com.maicard.security.domain.Privilege;
import com.maicard.security.domain.User;

@Repository
public class FrontPrivilegeDaoImpl extends BaseDao implements FrontPrivilegeDao {

	public int insert(Privilege frontPrivilege) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("FrontPrivilege.insert", frontPrivilege)).intValue();
	}

	public int update(Privilege frontPrivilege) throws DataAccessException {
		return getSqlSessionTemplate().update("FrontPrivilege.update", frontPrivilege);
	}

	public int delete(int privilegeId) throws DataAccessException {
		return getSqlSessionTemplate().delete("FrontPrivilege.delete", new Integer(privilegeId));
	}

	public Privilege select(int privilegeId) throws DataAccessException {
		return (Privilege) getSqlSessionTemplate().selectOne("FrontPrivilege.select", new Integer(privilegeId));
	}

	public List<Privilege> list(PrivilegeCriteria frontPrivilegeCriteria) throws DataAccessException {
		Assert.notNull(frontPrivilegeCriteria, "frontPrivilegeCriteria must not be null");		
		return getSqlSessionTemplate().selectList("FrontPrivilege.list", frontPrivilegeCriteria);
	}

	public List<Privilege> listOnPage(PrivilegeCriteria frontPrivilegeCriteria) throws DataAccessException {
		Assert.notNull(frontPrivilegeCriteria, "frontPrivilegeCriteria must not be null");
		Assert.notNull(frontPrivilegeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(frontPrivilegeCriteria);
		Paging paging = frontPrivilegeCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("FrontPrivilege.list", frontPrivilegeCriteria,rowBounds);
	}
	
	public List<User> getFrontUserByPrivilege(PrivilegeCriteria frontPrivilegeCriteria){
		return getSqlSessionTemplate().selectList("FrontPrivilege.getFrontUserByPrivilege", frontPrivilegeCriteria);
		
	}

	public int count(PrivilegeCriteria frontPrivilegeCriteria) throws DataAccessException {
		Assert.notNull(frontPrivilegeCriteria, "frontPrivilegeCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("FrontPrivilege.count", frontPrivilegeCriteria)).intValue();
	}

}

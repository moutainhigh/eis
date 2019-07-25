package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.dao.PartnerPrivilegeDao;
import com.maicard.security.domain.Privilege;
import com.maicard.security.domain.User;

@Repository
public class PartnerPrivilegeDaoImpl extends BaseDao implements PartnerPrivilegeDao {

	
	public int insert(Privilege partnerPrivilege) throws DataAccessException {
		return getSqlSessionTemplate().insert("PartnerPrivilege.insert", partnerPrivilege);
	}

	public int update(Privilege partnerPrivilege) throws DataAccessException {


		return getSqlSessionTemplate().update("PartnerPrivilege.update", partnerPrivilege);


	}

	public int delete(int privilegeId) throws DataAccessException {


		return getSqlSessionTemplate().delete("PartnerPrivilege.delete", new Integer(privilegeId));


	}

	public Privilege select(int privilegeId) throws DataAccessException {
		return (Privilege) getSqlSessionTemplate().selectOne("PartnerPrivilege.select", new Integer(privilegeId));
	}


	public List<Privilege> list(PrivilegeCriteria partnerPrivilegeCriteria) throws DataAccessException {
		Assert.notNull(partnerPrivilegeCriteria, "partnerPrivilegeCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("PartnerPrivilege.list", partnerPrivilegeCriteria);
	}


	public List<Privilege> listOnPage(PrivilegeCriteria partnerPrivilegeCriteria) throws DataAccessException {
		Assert.notNull(partnerPrivilegeCriteria, "partnerPrivilegeCriteria must not be null");
		Assert.notNull(partnerPrivilegeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(partnerPrivilegeCriteria);
		Paging paging = partnerPrivilegeCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("PartnerPrivilege.list", partnerPrivilegeCriteria, rowBounds);
	}

	public int count(PrivilegeCriteria partnerPrivilegeCriteria) throws DataAccessException {
		Assert.notNull(partnerPrivilegeCriteria, "partnerPrivilegeCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("PartnerPrivilege.count", partnerPrivilegeCriteria)).intValue();
	}
	

	public List<User> getUserByPrivilege(PrivilegeCriteria partnerPrivilegeCriteria) throws DataAccessException{
		Assert.notNull(partnerPrivilegeCriteria, "partnerPrivilegeCriteria must not be null");
		return getSqlSessionTemplate().selectList("PartnerPrivilege.getUserByPrivilege",partnerPrivilegeCriteria);
		
	}

	@Override
	public List<Privilege> listByRole(PrivilegeCriteria partnerPrivilegeCriteria) {

		Assert.notNull(partnerPrivilegeCriteria, "partnerPrivilegeCriteria must not be null");
		return getSqlSessionTemplate().selectList("PartnerPrivilege.listByRole",partnerPrivilegeCriteria);
	}

	

}

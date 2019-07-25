package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.RoleCriteria;
import com.maicard.security.dao.PartnerRoleDao;
import com.maicard.security.domain.Role;

@Repository
public class PartnerRoleDaoImpl extends BaseDao implements PartnerRoleDao {

	
	public int insert(Role partnerRole) throws DataAccessException {
		return getSqlSessionTemplate().insert("PartnerRole.insert", partnerRole);
	}

	public int update(Role partnerRole) throws DataAccessException {


		return getSqlSessionTemplate().update("PartnerRole.update", partnerRole);


	}

	public int delete(int roleId) throws DataAccessException {


		return getSqlSessionTemplate().delete("PartnerRole.delete", new Integer(roleId));


	}

	public Role select(int roleId) throws DataAccessException {
		return  getSqlSessionTemplate().selectOne("PartnerRole.select", new Integer(roleId));
	}


	public List<Role> list(RoleCriteria partnerRoleCriteria) throws DataAccessException {
		Assert.notNull(partnerRoleCriteria, "partnerRoleCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("PartnerRole.list", partnerRoleCriteria);
	}


	public List<Role> listOnPage(RoleCriteria partnerRoleCriteria) throws DataAccessException {
		Assert.notNull(partnerRoleCriteria, "partnerRoleCriteria must not be null");
		Assert.notNull(partnerRoleCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(partnerRoleCriteria);
		Paging paging = partnerRoleCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("PartnerRole.list", partnerRoleCriteria, rowBounds);
	}

	public int count(RoleCriteria partnerRoleCriteria) throws DataAccessException {
		Assert.notNull(partnerRoleCriteria, "partnerRoleCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("PartnerRole.count", partnerRoleCriteria)).intValue();
	}

}

package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.MenuRoleRelationCriteria;
import com.maicard.security.dao.PartnerMenuRoleRelationDao;
import com.maicard.security.domain.MenuRoleRelation;

@Repository
public class PartnerMenuRoleRelationDaoImpl extends BaseDao implements PartnerMenuRoleRelationDao {

	
	public int insert(MenuRoleRelation partnerMenuRoleRelation) throws DataAccessException {
		return getSqlSessionTemplate().insert("PartnerMenuRoleRelation.insert", partnerMenuRoleRelation);
	}

	public int update(MenuRoleRelation partnerMenuRoleRelation) throws DataAccessException {


		return getSqlSessionTemplate().update("PartnerMenuRoleRelation.update", partnerMenuRoleRelation);


	}

	public int delete(int partnerMenuRoleRelationId) throws DataAccessException {


		return getSqlSessionTemplate().delete("PartnerMenuRoleRelation.delete", new Integer(partnerMenuRoleRelationId));


	}
	public 	void deleteByGroupId(int groupId) throws DataAccessException{
		
		getSqlSessionTemplate().delete("PartnerMenuRoleRelation.deleteByGroupId", new Integer(groupId));
		
	}
	public MenuRoleRelation select(int partnerMenuRoleRelationId) throws DataAccessException {
		return (MenuRoleRelation) getSqlSessionTemplate().selectOne("PartnerMenuRoleRelation.select", new Integer(partnerMenuRoleRelationId));
	}


	public List<MenuRoleRelation> list(MenuRoleRelationCriteria partnerMenuRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(partnerMenuRoleRelationCriteria, "partnerMenuRoleRelationCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("PartnerMenuRoleRelation.list", partnerMenuRoleRelationCriteria);
	}


	public List<MenuRoleRelation> listOnPage(MenuRoleRelationCriteria partnerMenuRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(partnerMenuRoleRelationCriteria, "partnerMenuRoleRelationCriteria must not be null");
		Assert.notNull(partnerMenuRoleRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(partnerMenuRoleRelationCriteria);
		Paging paging = partnerMenuRoleRelationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("PartnerMenuRoleRelation.list", partnerMenuRoleRelationCriteria, rowBounds);
	}

	public int count(MenuRoleRelationCriteria partnerMenuRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(partnerMenuRoleRelationCriteria, "partnerMenuRoleRelationCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("PartnerMenuRoleRelation.count", partnerMenuRoleRelationCriteria)).intValue();
	}

}

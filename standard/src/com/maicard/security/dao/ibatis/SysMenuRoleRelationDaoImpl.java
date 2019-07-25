package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.MenuRoleRelationCriteria;
import com.maicard.security.dao.SysMenuRoleRelationDao;
import com.maicard.security.domain.MenuRoleRelation;

@Repository
public class SysMenuRoleRelationDaoImpl extends BaseDao implements SysMenuRoleRelationDao {

	public int insert(MenuRoleRelation menuRoleRelation) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("SysMenuRoleRelation.insert", menuRoleRelation)).intValue();
	}

	public int update(MenuRoleRelation menuRoleRelation) throws DataAccessException {


		return getSqlSessionTemplate().update("SysMenuRoleRelation.update", menuRoleRelation);


	}

	public int delete(int id) throws DataAccessException {


		return getSqlSessionTemplate().delete("SysMenuRoleRelation.delete", new Integer(id));


	}
	
	public 	void deleteByGroupId(int groupId) throws DataAccessException{
		
		getSqlSessionTemplate().delete("SysMenuRoleRelation.deleteByGroupId", new Integer(groupId));
		
	}

	public MenuRoleRelation select(int id) throws DataAccessException {
		return (MenuRoleRelation) getSqlSessionTemplate().selectOne("SysMenuRoleRelation.select", new Integer(id));
	}


	public List<MenuRoleRelation> list(MenuRoleRelationCriteria menuRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(menuRoleRelationCriteria, "sysMenuPositionRelationCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("SysMenuRoleRelation.list", menuRoleRelationCriteria);
	}


	public List<MenuRoleRelation> listOnPage(MenuRoleRelationCriteria menuRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(menuRoleRelationCriteria, "sysMenuPositionRelationCriteria must not be null");
		Assert.notNull(menuRoleRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(menuRoleRelationCriteria);
		Paging paging = menuRoleRelationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("SysMenuRoleRelation.list", menuRoleRelationCriteria, rowBounds);
	}

	public int count(MenuRoleRelationCriteria menuRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(menuRoleRelationCriteria, "sysMenuPositionRelationCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("SysMenuRoleRelation.count", menuRoleRelationCriteria)).intValue();
	}

}

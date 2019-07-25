package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.MenuCriteria;
import com.maicard.security.dao.PartnerMenuDao;
import com.maicard.security.domain.Menu;

@Repository
public class PartnerMenuDaoImpl extends BaseDao implements PartnerMenuDao {

	
	public int insert(Menu partnerMenu) throws DataAccessException {
		return getSqlSessionTemplate().insert("PartnerMenu.insert", partnerMenu);
	}

	public int update(Menu partnerMenu) throws DataAccessException {
		return getSqlSessionTemplate().update("PartnerMenu.update", partnerMenu);
	}

	public int delete(int menuId) throws DataAccessException {
		return getSqlSessionTemplate().delete("PartnerMenu.delete", new Integer(menuId));
	}

	public Menu select(int menuId) throws DataAccessException {
		return (Menu) getSqlSessionTemplate().selectOne("PartnerMenu.select", new Integer(menuId));
	}


	public List<Menu> list(MenuCriteria partnerMenuCriteria) throws DataAccessException {
		Assert.notNull(partnerMenuCriteria, "partnerMenuCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("PartnerMenu.list", partnerMenuCriteria);
	}


	public List<Menu> listOnPage(MenuCriteria partnerMenuCriteria) throws DataAccessException {
		Assert.notNull(partnerMenuCriteria, "partnerMenuCriteria must not be null");
		Assert.notNull(partnerMenuCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(partnerMenuCriteria);
		Paging paging = partnerMenuCriteria.getPaging();
		paging.setTotalResults(totalResults);RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("PartnerMenu.list", partnerMenuCriteria, rowBounds);
	}

	public int count(MenuCriteria partnerMenuCriteria) throws DataAccessException {
		Assert.notNull(partnerMenuCriteria, "partnerMenuCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("PartnerMenu.count", partnerMenuCriteria)).intValue();
	}

}

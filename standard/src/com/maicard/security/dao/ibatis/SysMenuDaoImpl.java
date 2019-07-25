package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.MenuCriteria;
import com.maicard.security.dao.SysMenuDao;
import com.maicard.security.domain.Menu;

@Repository
public class SysMenuDaoImpl extends BaseDao implements SysMenuDao {

	public int insert(Menu menu) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("SysMenu.insert", menu)).intValue();
	}

	public int update(Menu menu) throws DataAccessException {


		return getSqlSessionTemplate().update("SysMenu.update", menu);


	}

	public int delete(int menuId) throws DataAccessException {


		return getSqlSessionTemplate().delete("SysMenu.delete", new Integer(menuId));


	}

	public Menu select(int menuId) throws DataAccessException {
		return (Menu) getSqlSessionTemplate().selectOne("SysMenu.select", new Integer(menuId));
	}


	public List<Menu> list(MenuCriteria menuCriteria) throws DataAccessException {
		Assert.notNull(menuCriteria, "sysMenuCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("SysMenu.list", menuCriteria);
	}


	public List<Menu> listOnPage(MenuCriteria menuCriteria) throws DataAccessException {
		Assert.notNull(menuCriteria, "sysMenuCriteria must not be null");
		Assert.notNull(menuCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(menuCriteria);
		Paging paging = menuCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("SysMenu.list", menuCriteria, rowBounds);
	}

	public int count(MenuCriteria menuCriteria) throws DataAccessException {
		Assert.notNull(menuCriteria, "sysMenuCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("SysMenu.count", menuCriteria)).intValue();
	}

}

package com.maicard.wpt.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.wpt.criteria.WeixinMenuCriteria;
import com.maicard.wpt.dao.WeixinMenuDao;
import com.maicard.wpt.domain.WeixinMenu;

public class WeixinMenuDaoImpl extends BaseDao implements WeixinMenuDao {
	@Override
	public int insert(WeixinMenu weixinMenu) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.wpt.sql.WeixinMenu.insert", weixinMenu);
	}

	@Override
	public int update(WeixinMenu weixinMenu) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.wpt.sql.WeixinMenu.update", weixinMenu);
	}

	@Override
	public int delete(long weixinMenuId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.wpt.sql.WeixinMenu.delete", weixinMenuId);
	}

	@Override
	public WeixinMenu select(long weixinMenuId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("com.maicard.wpt.sql.WeixinMenu.select",weixinMenuId);
	}

	@Override
	public List<WeixinMenu> list(WeixinMenuCriteria weixinMenuCriteria) throws DataAccessException {
		Assert.notNull(weixinMenuCriteria, "weixinMenuCriteria must not be null");
		return getSqlSessionTemplate().selectList("com.maicard.wpt.sql.WeixinMenu.list", weixinMenuCriteria);
	}

	@Override
	public List<WeixinMenu> listOnPage(WeixinMenuCriteria weixinMenuCriteria) throws DataAccessException {
		Assert.notNull(weixinMenuCriteria, "weixinMenuCriteria must not be null");
		Assert.notNull(weixinMenuCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(weixinMenuCriteria);
		Paging paging = weixinMenuCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.wpt.sql.WeixinMenu.list", weixinMenuCriteria, rowBounds);
	}

	@Override
	public int count(WeixinMenuCriteria weixinMenuCriteria) throws DataAccessException {
		Assert.notNull(weixinMenuCriteria, "weixinMenuCriteria must not be null");
		return ((Integer)getSqlSessionTemplate().selectOne("com.maicard.wpt.sql.WeixinMenu.count",weixinMenuCriteria)).intValue();
	}
}

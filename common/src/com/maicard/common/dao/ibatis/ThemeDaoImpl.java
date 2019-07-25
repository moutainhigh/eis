package com.maicard.common.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.ThemeCriteria;
import com.maicard.common.dao.ThemeDao;
import com.maicard.common.domain.Theme;
import com.maicard.common.util.Paging;
import com.maicard.standard.CommonStandard;

@Repository
public class ThemeDaoImpl extends BaseDao implements ThemeDao {

	public int insert(Theme theme) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("com.maicard.common.sql.Theme.insert", theme)).intValue();
	}
	
	public  void updateWrong(Theme theme) throws DataAccessException {
		getSqlSessionTemplate().insert("Theme.insertwrong", theme);
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'Theme#' + #theme.themeId")
	public int update(Theme theme) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.common.sql.Theme.update", theme);
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'Theme#' + #themeId")
	public int delete(int themeId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.common.sql.Theme.delete", themeId);
	}

	@Cacheable(value = CommonStandard.cacheNameSupport, key = "'Theme#' + #themeId")
	public Theme select(int themeId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("com.maicard.common.sql.Theme.select",themeId);
	}
	
	public List<Integer> listPk(ThemeCriteria themeCriteria) throws DataAccessException {
		Assert.notNull(themeCriteria, "themeCriteria must not be null");		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.Theme.listPk", themeCriteria);
	}

	public List<Integer> listPkOnPage(ThemeCriteria themeCriteria) throws DataAccessException {
		Assert.notNull(themeCriteria, "themeCriteria must not be null");
		Assert.notNull(themeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(themeCriteria);
		Paging paging = themeCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.Theme.listPk", themeCriteria, rowBounds);
	}

	public List<Theme> list(ThemeCriteria themeCriteria) throws DataAccessException {
		Assert.notNull(themeCriteria, "themeCriteria must not be null");		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.Theme.list", themeCriteria);
	}

	public List<Theme> listOnPage(ThemeCriteria themeCriteria) throws DataAccessException {
		Assert.notNull(themeCriteria, "themeCriteria must not be null");
		Assert.notNull(themeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(themeCriteria);
		Paging paging = themeCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.Theme.list", themeCriteria, rowBounds);
	}

	public int count(ThemeCriteria themeCriteria) throws DataAccessException {
		Assert.notNull(themeCriteria, "themeCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.common.sql.Theme.count", themeCriteria)).intValue();
	}

}

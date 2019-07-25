package com.maicard.site.dao.ibatis;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.site.criteria.TemplateCriteria;
import com.maicard.site.dao.TemplateDao;
import com.maicard.site.domain.Template;
import com.maicard.standard.CommonStandard;

import org.apache.ibatis.session.RowBounds;

@Repository
public class TemplateDaoImpl extends BaseDao implements TemplateDao {

	private final String cacheName = CommonStandard.cacheNameDocument;

	public int insert(Template template) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("Template.insert", template)).intValue();
	}

	@CacheEvict(value = cacheName, key = "'Template#' + #template.templateId")
	public int update(Template template) throws DataAccessException {
		return getSqlSessionTemplate().update("Template.update", template);
	}

	@CacheEvict(value = cacheName, key = "'Template#' + #templateId")
	public int delete(int templateId) throws DataAccessException {
		return getSqlSessionTemplate().delete("Template.delete", templateId);

	}

	@Cacheable(value = cacheName, key = "'Template#' + #templateId")
	public Template select(int templateId) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("Template.select", templateId);
	}
	@Override
	public List<Integer> listPk(TemplateCriteria templateCriteria) throws DataAccessException {
		Assert.notNull(templateCriteria, "templateCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("Template.listPk", templateCriteria);
	}

	@Override
	public List<Integer> listPkOnPage(TemplateCriteria templateCriteria) throws DataAccessException {
		Assert.notNull(templateCriteria, "templateCriteria must not be null");
		Assert.notNull(templateCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(templateCriteria);
		Paging paging = templateCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("Template.listPk", templateCriteria, rowBounds);
	}

	@Override
	public List<Template> list(TemplateCriteria templateCriteria) throws DataAccessException {
		Assert.notNull(templateCriteria, "templateCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("Template.list", templateCriteria);
	}

	@Override
	public List<Template> listOnPage(TemplateCriteria templateCriteria) throws DataAccessException {
		Assert.notNull(templateCriteria, "templateCriteria must not be null");
		Assert.notNull(templateCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(templateCriteria);
		Paging paging = templateCriteria.getPaging();
		paging.setTotalResults(totalResults); RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("Template.list", templateCriteria, rowBounds);
	}

	@Override
	public int count(TemplateCriteria templateCriteria) throws DataAccessException {
		Assert.notNull(templateCriteria, "templateCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("Template.count", templateCriteria)).intValue();
	}

}

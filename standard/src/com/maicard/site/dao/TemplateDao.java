package com.maicard.site.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.site.criteria.TemplateCriteria;
import com.maicard.site.domain.Template;

public interface TemplateDao {

	int insert(Template template) throws DataAccessException;

	int update(Template template) throws DataAccessException;

	int delete(int templateId) throws DataAccessException;

	Template select(int templateId) throws DataAccessException;

	List<Template> list(TemplateCriteria templateCriteria) throws DataAccessException;
	
	List<Template> listOnPage(TemplateCriteria templateCriteria) throws DataAccessException;
	
	int count(TemplateCriteria templateCriteria) throws DataAccessException;

	List<Integer> listPkOnPage(TemplateCriteria templateCriteria)
			throws DataAccessException;

	List<Integer> listPk(TemplateCriteria templateCriteria)
			throws DataAccessException;

}

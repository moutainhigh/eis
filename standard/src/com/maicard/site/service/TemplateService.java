package com.maicard.site.service;

import java.util.List;

import com.maicard.site.criteria.TemplateCriteria;
import com.maicard.site.domain.Template;

public interface TemplateService {

	int insert(Template template);

	int update(Template template);

	int delete(int templateId);
	
	Template select(int templateId);

	List<Template> list(TemplateCriteria templateCriteria);

	List<Template> listOnPage(TemplateCriteria templateCriteria);
	
	int count (TemplateCriteria templateCriteria);
	
}

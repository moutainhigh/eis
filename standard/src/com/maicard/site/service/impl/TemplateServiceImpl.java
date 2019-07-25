package com.maicard.site.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.LanguageService;
import com.maicard.site.criteria.TemplateCriteria;
import com.maicard.site.dao.TemplateDao;
import com.maicard.site.domain.Template;
import com.maicard.site.service.TemplateService;

@Service
public class TemplateServiceImpl extends BaseService implements TemplateService {

	@Resource
	private TemplateDao templateDao;
	@Resource
	private LanguageService languageService;

	public int insert(Template template) {
		Assert.notNull(template,"新增模版对象不能为空");
		Assert.notNull(template.getTemplateName(),"新增模版名称不能为空");
		return  templateDao.insert(template);
		}

	public int update(Template template) {
		try{
			return  templateDao.update(template);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int delete(int templateId) {
		try{
			return  templateDao.delete(templateId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}
	
	public Template select(int templateId) {
		Template template =  templateDao.select(templateId);
		return template;

	}

	public List<Template> list(TemplateCriteria templateCriteria) {
		List<Integer> idList = templateDao.listPk(templateCriteria);
		if(idList != null && idList.size() > 0){
			List<Template> templateList =  new ArrayList<Template> ();		
			for(int i = 0; i < idList.size(); i++){
				Template template = templateDao.select(idList.get(i));
				if(template != null){
					template.setId(template.getTemplateId());
					template.setIndex(i+1);
					templateList.add(template);
				}
			}
			idList = null;
			return templateList;
		}
		return Collections.emptyList();
		/*
		List<Template> templateList =  templateDao.list(templateCriteria);
		if(templateList == null){
			return null;
		}
		for(int i = 0; i < templateList.size(); i++){
			templateList.get(i).setId(templateList.get(i).getTemplateId());
			templateList.get(i).setIndex(i+1);
			templateList.get(i).setStatusName(BasicStatus.normal.findById(templateList.get(i).getCurrentStatus()).getName());
			templateList.get(i).setLanguageName(languageService.select(templateList.get(i).getLanguageId()).getLanguageName());

		}
		return templateList;
		*/
	}
	
	public List<Template> listOnPage(TemplateCriteria templateCriteria) {
		List<Integer> pkList = templateDao.listPkOnPage(templateCriteria);
		if(pkList != null && pkList.size() > 0){
			List<Template> dictList =  new ArrayList<Template> ();		
			for(int i = 0; i < pkList.size(); i++){
				Template template = templateDao.select(pkList.get(i));
				if(template != null){
					template.setId(template.getTemplateId());
					template.setIndex(i+1);
					dictList.add(template);
				}
			}
			pkList = null;
			return dictList;
		}
		return Collections.emptyList();
		/*List<Template> templateList =  templateDao.listOnPage(templateCriteria);
		if(templateList == null){
			return null;
		}
		for(int i = 0; i < templateList.size(); i++){
			templateList.get(i).setId(templateList.get(i).getTemplateId());
			templateList.get(i).setIndex(i+1);
			templateList.get(i).setStatusName(BasicStatus.normal.findById(templateList.get(i).getCurrentStatus()).getName());
			templateList.get(i).setLanguageName(languageService.select(templateList.get(i).getLanguageId()).getLanguageName());

		}
		return templateList;
		*/
	}
	
	public int count(TemplateCriteria templateCriteria) {
		return templateDao.count(templateCriteria);
	}


}

package com.maicard.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.LanguageCriteria;
import com.maicard.common.dao.LanguageDao;
import com.maicard.common.domain.Language;
import com.maicard.common.service.LanguageService;

@Service
public class LanguageServiceImpl extends BaseService implements LanguageService{

	@Resource
	private LanguageDao languageDao;

	
	public int insert(Language language) {
		try{
			return languageDao.insert(language);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
		
	}


	public int update(Language language) {
		try{
			return  languageDao.update(language);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
	}

	public int delete(int languageId) {
		try{
			return  languageDao.delete(languageId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}
	
	public Language select(int languageId) {
		Language language =  languageDao.select(languageId);
		//language.setCurrentStatusName(BasicStatus.disable.findById(language.getCurrentStatus()).getName());	
		return language;
	}

	public List<Language> list(LanguageCriteria languageCriteria) {
		List<Integer> idList = languageDao.listPk(languageCriteria);
		if(idList != null && idList.size() > 0){
			List<Language> dictList =  new ArrayList<Language> ();		
			for(int i = 0; i < idList.size(); i++){
				Language language = languageDao.select(idList.get(i));
				if(language != null){
					language.setId(language.getLanguageId());
					language.setIndex(i+1);
					//language.setCurrentStatusName(BasicStatus.unknown.findById(language.getCurrentStatus()).getName());	
					dictList.add(language);
				}
			}
			idList = null;
			return dictList;
		}
		return null;
		/*
		List<Language> languageList =  languageDao.list(languageCriteria);
		if(languageList == null){
			return null;
		}
		for(int i = 0; i < languageList.size(); i++){
			languageList.get(i).setId(languageList.get(i).getLanguageId());
			languageList.get(i).setIndex(i+1);
			languageList.get(i).setStatusName(CommonStandard.BasicStatus.disable.findById(languageList.get(i).getCurrentStatus()).getName());	
		}
		return languageList;
		*/
	}
	
	public List<Language> listOnPage(LanguageCriteria languageCriteria) {
		List<Integer> idList = languageDao.listPkOnPage(languageCriteria);
		if(idList != null && idList.size() > 0){
			List<Language> dictList =  new ArrayList<Language> ();		
			for(int i = 0; i < idList.size(); i++){
				Language language = languageDao.select(idList.get(i));
				if(language != null){
					language.setId(language.getLanguageId());
					language.setIndex(i+1);
					//language.setCurrentStatusName(BasicStatus.unknown.findById(language.getCurrentStatus()).getName());	
					dictList.add(language);
				}
			}
			idList = null;
			return dictList;
		}
		return null;
		/*
		List<Language> languageList =  languageDao.listOnPage(languageCriteria);
		if(languageList == null){
			return null;
		}
		for(int i = 0; i < languageList.size(); i++){
			languageList.get(i).setId(languageList.get(i).getLanguageId());
			languageList.get(i).setIndex(i+1);
			languageList.get(i).setStatusName(CommonStandard.BasicStatus.disable.findById(languageList.get(i).getCurrentStatus()).getName());	
		}
		return languageList;
		*/
	}
	
	public int count(LanguageCriteria languageCriteria){
		return languageDao.count(languageCriteria);
	}

}

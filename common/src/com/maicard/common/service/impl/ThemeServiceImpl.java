package com.maicard.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.SiteThemeRelationCriteria;
import com.maicard.common.criteria.ThemeCriteria;
import com.maicard.common.dao.ThemeDao;
import com.maicard.common.domain.SiteThemeRelation;
import com.maicard.common.domain.Theme;
import com.maicard.common.service.SiteThemeRelationService;
import com.maicard.common.service.ThemeService;

@Service
public class ThemeServiceImpl extends BaseService implements ThemeService{

	@Resource
	private ThemeDao themeDao;
	
	@Resource
	private SiteThemeRelationService siteThemeRelationService;

	
	public int insert(Theme theme) {
		try{
			return themeDao.insert(theme);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
		
	}


	public int update(Theme theme) {
		try{
			return  themeDao.update(theme);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
	}

	public int delete(int themeId) {
		try{
			return  themeDao.delete(themeId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}
	
	public Theme select(int themeId) {
		Theme theme =  themeDao.select(themeId);
		return theme;
	}

	public List<Theme> list(ThemeCriteria themeCriteria) {
		List<Integer> idList = themeDao.listPk(themeCriteria);
		if(idList != null && idList.size() > 0){
			List<Theme> dictList =  new ArrayList<Theme> ();		
			for(int i = 0; i < idList.size(); i++){
				Theme theme = themeDao.select(idList.get(i));
				if(theme != null){
					theme.setId(theme.getThemeId());
					theme.setIndex(i+1);
				//	theme.setCurrentStatusName(BasicStatus.unknown.findById(theme.getCurrentStatus()).getName());	
					dictList.add(theme);
				}
			}
			idList = null;
			return dictList;
		}
		return null;
		/*
		List<Theme> themeList =  themeDao.list(themeCriteria);
		if(themeList == null){
			return null;
		}
		for(int i = 0; i < themeList.size(); i++){
			themeList.get(i).setId(themeList.get(i).getThemeId());
			themeList.get(i).setIndex(i+1);
			themeList.get(i).setStatusName(CommonStandard.BasicStatus.disable.findById(themeList.get(i).getCurrentStatus()).getName());	
		}
		return themeList;
		*/
	}
	
	public List<Theme> listOnPage(ThemeCriteria themeCriteria) {
		List<Integer> idList = themeDao.listPkOnPage(themeCriteria);
		if(idList != null && idList.size() > 0){
			List<Theme> dictList =  new ArrayList<Theme> ();		
			for(int i = 0; i < idList.size(); i++){
				Theme theme = themeDao.select(idList.get(i));
				if(theme != null){
					theme.setId(theme.getThemeId());
					theme.setIndex(i+1);
			//		theme.setCurrentStatusName(BasicStatus.unknown.findById(theme.getCurrentStatus()).getName());	
					dictList.add(theme);
				}
			}
			idList = null;
			return dictList;
		}
		return null;
		/*
		List<Theme> themeList =  themeDao.listOnPage(themeCriteria);
		if(themeList == null){
			return null;
		}
		for(int i = 0; i < themeList.size(); i++){
			themeList.get(i).setId(themeList.get(i).getThemeId());
			themeList.get(i).setIndex(i+1);
			themeList.get(i).setStatusName(CommonStandard.BasicStatus.disable.findById(themeList.get(i).getCurrentStatus()).getName());	
		}
		return themeList;
		*/
	}
	
	public int count(ThemeCriteria themeCriteria){
		return themeDao.count(themeCriteria);
	}


	@Override
	public Theme selectByUser(long uuid) {

		SiteThemeRelationCriteria siteThemeRelationCriteria = new SiteThemeRelationCriteria();
		siteThemeRelationCriteria.setUuid(uuid);
		SiteThemeRelation siteThemeRelation = siteThemeRelationService.select(siteThemeRelationCriteria);
		logger.debug("根据UUID=" + uuid + "得到的主题是:" + siteThemeRelation);
		if(siteThemeRelation == null){
			return null;
		}
		return select(siteThemeRelation.getThemeId());
	}

}

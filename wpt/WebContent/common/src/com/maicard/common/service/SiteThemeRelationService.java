package com.maicard.common.service;

import java.util.List;

import com.maicard.common.criteria.SiteThemeRelationCriteria;
import com.maicard.common.domain.SiteThemeRelation;

public interface SiteThemeRelationService {

	int insert(SiteThemeRelation siteThemeRelation);

	int update(SiteThemeRelation siteThemeRelation);

	int delete(int siteThemeRelationId);
	
	SiteThemeRelation select(int siteThemeRelationId);

	List<SiteThemeRelation> list(SiteThemeRelationCriteria siteThemeRelationCriteria);

	List<SiteThemeRelation> listOnPage(SiteThemeRelationCriteria siteThemeRelationCriteria);

	
	int count(SiteThemeRelationCriteria siteThemeRelationCriteria);

	SiteThemeRelation select(SiteThemeRelationCriteria siteThemeRelationCriteria);

	/**
	 * 对某个用户的主题对应关系进行更新<br/>
	 * 如果没有已存在的关系就新增<br/>
	 * 如果有则更新<br/>
	 * 一个用户，只允许存在一个主题和站点对应关系
	 * @param siteThemeRelation
	 * @return
	 */
	int updateForUuid(SiteThemeRelation siteThemeRelation);


}

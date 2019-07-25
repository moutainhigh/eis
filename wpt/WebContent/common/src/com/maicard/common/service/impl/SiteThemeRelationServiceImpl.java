package com.maicard.common.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.SiteThemeRelationCriteria;
import com.maicard.common.dao.SiteThemeRelationDao;
import com.maicard.common.domain.SiteThemeRelation;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.SiteThemeRelationService;

@Service
public class SiteThemeRelationServiceImpl extends BaseService implements SiteThemeRelationService {

	@Resource
	private SiteThemeRelationDao siteThemeRelationDao;

	@Resource
	private ConfigService configService;

	//	private final String cacheName = CommonStandard.cacheNameSupport;
	//	private final String cachePrefix = "SiteThemeRelation";

	//private int isInited = 0;



	public int insert(SiteThemeRelation siteThemeRelation) {
		try{
			return siteThemeRelationDao.insert(siteThemeRelation);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(SiteThemeRelation siteThemeRelation) {
		try{
			return  siteThemeRelationDao.update(siteThemeRelation);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	}
	
	@Override
	public int updateForUuid(SiteThemeRelation siteThemeRelation) {
		int rs = 0;
		try{
			rs =  siteThemeRelationDao.updateForUuid(siteThemeRelation);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		//ON DUPLICATE SQL可能会返回大于1的结果，这里将把所有大于0的结果转换为1，以便于切面同步数据
		if(rs > 0){
			return 1;
		}
		return rs;
	}

	public int delete(int siteThemeRelationId) {
		try{
			return  siteThemeRelationDao.delete(siteThemeRelationId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}

	public SiteThemeRelation select(int siteThemeRelationId) {
		return siteThemeRelationDao.select(siteThemeRelationId);
	}

	public List<SiteThemeRelation> list(SiteThemeRelationCriteria siteThemeRelationCriteria) {
		List<Integer> idList = siteThemeRelationDao.listPk(siteThemeRelationCriteria);
		if(idList != null && idList.size() > 0){
			List<SiteThemeRelation> siteThemeRelationList =  new ArrayList<SiteThemeRelation> ();		
			for(Integer id : idList){
				SiteThemeRelation siteThemeRelation = siteThemeRelationDao.select(id);
				//logger.debug("获取到id=" + id + "]的siteThemeRelation=" + siteThemeRelation);
				if(siteThemeRelation != null){
					siteThemeRelationList.add(siteThemeRelation);
				}
			}
			idList = null;
			return siteThemeRelationList;
		}
		return null;
		//return siteThemeRelationDao.list(siteThemeRelationCriteria);
	}

	public List<SiteThemeRelation> listOnPage(SiteThemeRelationCriteria siteThemeRelationCriteria) {
		List<Integer> idList = siteThemeRelationDao.listPkOnPage(siteThemeRelationCriteria);
		if(idList != null && idList.size() > 0){
			List<SiteThemeRelation> siteThemeRelationList =  new ArrayList<SiteThemeRelation> ();		
			for(Integer id : idList){
				SiteThemeRelation dict = siteThemeRelationDao.select(id);
				if(dict != null){
					siteThemeRelationList.add(dict);
				}
			}
			idList = null;
			return siteThemeRelationList;
		}
		return null;
		//return siteThemeRelationDao.listOnPage(siteThemeRelationCriteria);
	}

	@Override
	public int count(SiteThemeRelationCriteria siteThemeRelationCriteria) {
		return siteThemeRelationDao.count(siteThemeRelationCriteria);
	}

	@Override
	public SiteThemeRelation select(SiteThemeRelationCriteria siteThemeRelationCriteria) {
		List<SiteThemeRelation> siteThemeRelationList = list(siteThemeRelationCriteria);
		if(siteThemeRelationList == null || siteThemeRelationList.size() < 1){
			return null;
		}
		return siteThemeRelationList.get(0);
	}



	
}

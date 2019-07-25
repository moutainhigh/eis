package com.maicard.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.cache.CachedAreaService;
import com.maicard.common.criteria.AreaCriteria;
import com.maicard.common.dao.AreaDao;
import com.maicard.common.domain.Area;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.AreaService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.UuidService;


import static com.maicard.common.criteria.AreaCriteria.CACHE_NAME;


@Service
public class AreaServiceImpl extends BaseService implements AreaService {

	@Resource
	private AreaDao areaDao;

	@Resource
	private ApplicationContextService applicationContextService;

	@Resource
	private ConfigService configService;

	@Resource
	private UuidService uuidService;

	@Resource
	private CachedAreaService cachedAreaService;




	@Override
	public int insert(Area area) {

		Assert.notNull(area, "尝试新增的评论不能为空");
		int rs = 0;
		try{
			rs = areaDao.insert(area);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		if(rs != 1){
			logger.error("新增评论失败,数据操作未返回1");
			return -1;
		}

		return 1;
	}

	@CacheEvict(value = CACHE_NAME, key = "'Area#' + #area.areaId")
	public int update(Area area) {
		int actualRowsAffected = 0;

		long areaId = area.getAreaId();

		Area _oldArea = areaDao.select(areaId);

		if (_oldArea == null) {
			return 0;
		}
		try{
			actualRowsAffected = areaDao.update(area);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());

		}
		return actualRowsAffected;
	}

	public int delete(long areaId) {
		int actualRowsAffected = 0;

		Area _oldArea = areaDao.select(areaId);

		if (_oldArea != null) {
			actualRowsAffected = areaDao.delete(areaId);
		}
		return actualRowsAffected;
	}


	public Area select(long areaId){
		return cachedAreaService.select(areaId);
	}

	public List<Area> list(AreaCriteria areaCriteria) {
		List<Long> idList = areaDao.listPk(areaCriteria);
		if(idList != null && idList.size() > 0){
			List<Area> areaList =  new ArrayList<Area> ();		
			for(Long id : idList){
				Area area = cachedAreaService.select(id);
				if(area != null){
					areaList.add(area);
				}
			}
			idList = null;
			return areaList;
		}
		return null;
		/*
		List<Area> areaList = areaDao.list(areaCriteria);
		if(areaList == null){
			return null;
		}
		for(int i = 0; i < areaList.size(); i ++){
			areaList.get(i).setIndex(i+1);		
			afterFetch(areaList.get(i));
		}
		return areaList;
		 */
	}

	public List<Area> listOnPage(AreaCriteria areaCriteria) {
		List<Long> idList = areaDao.listPkOnPage(areaCriteria);
		if(idList != null && idList.size() > 0){
			List<Area> areaList =  new ArrayList<Area> ();		
			for(Long id : idList){
				Area area = cachedAreaService.select(id);
				if(area != null){
					areaList.add(area);
				}
			}
			idList = null;
			return areaList;
		}
		return null;
		/*
		List<Area> areaList = areaDao.listOnPage(areaCriteria);
		if(areaList == null){
			return null;
		}
		for(int i = 0; i < areaList.size(); i ++){
			areaList.get(i).setIndex(i+1);
			afterFetch(areaList.get(i));
		}
		return areaList;
		 */
	}

	public int count(AreaCriteria areaCriteria){
		return areaDao.count(areaCriteria);
	}



}

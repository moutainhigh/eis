package com.maicard.common.cache.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.cache.CachedAreaService;
import com.maicard.common.dao.AreaDao;
import com.maicard.common.domain.Area;


import static com.maicard.common.criteria.AreaCriteria.CACHE_NAME;


import javax.annotation.Resource;

@Service
public class CachedAreaServiceImpl extends BaseService implements CachedAreaService{

	@Resource
	private AreaDao areaDao;
	
	
	@Override
	@Cacheable(value = CACHE_NAME, key = "'Area#' + #areaId")
	public Area select(long areaId) {
		Area area = areaDao.select(areaId);
		
		return area;
	}
	
	
}

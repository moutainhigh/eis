package com.maicard.common.service.impl;

import javax.annotation.Resource;

import com.maicard.annotation.IgnoreJmsDataSync;
import com.maicard.common.base.BaseService;
import com.maicard.common.dao.UuidDao;
import com.maicard.common.domain.Uuid;
import com.maicard.common.service.CacheService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.UuidService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.KeyConstants;

public class UuidServiceImpl extends BaseService implements UuidService {
	@Resource
	private UuidDao uuidDao;

	@Resource
	private CacheService cacheService;

	@Resource
	private CenterDataService centerDataService; 

	private final String cachePrefix = KeyConstants.UUID_PREFIX;


	@Override
	@IgnoreJmsDataSync
	public long insert(Uuid uuid) {
		try{
			long uuidResult = uuidDao.insert(uuid);
			logger.debug("生成新的UUID数据:" + uuidResult);
			return uuidResult;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public long createById(long id, long liveSec){
		String key = new StringBuffer().append(cachePrefix).append(id).toString();
		if(liveSec == 0){
			liveSec = CommonStandard.CACHE_MAX_TTL;
		}
		return centerDataService.increaseBy(key, 1, 1, liveSec);
	}

	@Override
	public long createById(long id, long minId, long liveSec){
		String key = new StringBuffer().append(cachePrefix).append(id).toString();
		if(liveSec == 0){
			liveSec = CommonStandard.CACHE_MAX_TTL;
		}
		long rs = centerDataService.increaseBy(key, 1, 1, liveSec);
		if(rs < minId){
			centerDataService.setForce(key, String.valueOf(minId), liveSec);
			rs = minId;
		}
		return rs;
	}
}

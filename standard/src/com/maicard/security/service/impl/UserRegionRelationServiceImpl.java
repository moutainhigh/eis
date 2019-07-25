package com.maicard.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.Region;
import com.maicard.common.service.RegionService;
import com.maicard.security.criteria.UserRegionRelationCriteria;
import com.maicard.security.dao.UserRegionRelationDao;
import com.maicard.security.domain.UserRegionRelation;
import com.maicard.security.service.UserRegionRelationService;

@Service
public class UserRegionRelationServiceImpl extends BaseService implements UserRegionRelationService {

	@Resource
	private UserRegionRelationDao userRegionRelationDao;
	
	@Resource
	private RegionService regionService;

	@Override
	public List<UserRegionRelation> list(UserRegionRelationCriteria userRegionRelationCriteria){
		return userRegionRelationDao.list(userRegionRelationCriteria);
	}

	@Override
	public Region getRegionByUuid(long uuid) {
		UserRegionRelationCriteria userRegionRelationCriteria = new UserRegionRelationCriteria();
		userRegionRelationCriteria.setUuid(uuid);
		List<UserRegionRelation> userRegionRelationList = list(userRegionRelationCriteria);
		if(userRegionRelationList == null || userRegionRelationList.size() < 1){
			logger.error("找不到用户[" + uuid + "]对应的区域关系");
			return null;
		}
		if(userRegionRelationList.size() != 1){
			logger.warn("用户[" + uuid + "]对应的区域关系数量异常:" + userRegionRelationList.size());
		}
		Region region = regionService.select(userRegionRelationList.get(0).getRegionId());
		if(region == null){
			logger.error("找不到用户[" + uuid + "]指定的区域:" + userRegionRelationList.get(0).getRegionId());
			return null;
		}
		logger.info("为用户[" + uuid + "]找到的的区域是:" + region);
		return region;
	}
}

package com.maicard.wpt.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.product.service.ActivityService;
import com.maicard.wpt.criteria.WeixinGroupCriteria;
import com.maicard.wpt.dao.WeixinGroupDao;
import com.maicard.wpt.domain.WeixinGroup;
import com.maicard.wpt.service.WeixinGroupService;

@Service
public class WeixinGroupServiceImpl extends BaseService implements WeixinGroupService {
	
	@Resource
	private WeixinGroupDao weixinGroupDao;

	@Resource
	private ActivityService activityService;
	
	
	private static final HashMap<Long, WeixinGroup> localCache = new HashMap<Long, WeixinGroup>();

	@Override
	public int insert(WeixinGroup weixinGroup) {
		try{
			return weixinGroupDao.insert(weixinGroup);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	@Override
	public int update(WeixinGroup weixinGroup) {
		try{
			return  weixinGroupDao.update(weixinGroup);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
			}

	@Override
	public int delete(long weixinGroupId) {
		try{
			return  weixinGroupDao.delete(weixinGroupId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;	
	}
	
	@Override
	public WeixinGroup select(long weixinGroupId) {
		WeixinGroup weixinGroup =  weixinGroupDao.select(weixinGroupId);
		if(weixinGroup == null){
			weixinGroup = new WeixinGroup();
		}
		return weixinGroup;
	}
	
	


	@Override
	public List<WeixinGroup> list(WeixinGroupCriteria weixinGroupCriteria) {
		return weixinGroupDao.list(weixinGroupCriteria);
		
	}
	
	@Override
	public List<WeixinGroup> listOnPage(WeixinGroupCriteria weixinGroupCriteria) {
		return weixinGroupDao.listOnPage(weixinGroupCriteria);

	}
	
	@Override
	public int count(WeixinGroupCriteria weixinGroupCriteria) {
		return weixinGroupDao.count(weixinGroupCriteria);
	}

	@Override
	public WeixinGroup findGroupByIdentify(String identify) {
		if(localCache == null || localCache.size() < 1){
			initCache();
		}
		logger.debug("本地缓存了" +localCache + "个微信分组" );
		for(WeixinGroup weixinGroup : localCache.values()){
			if(weixinGroup.getGroupIdentify() == null){
				logger.debug("微信分组[" + weixinGroup.getGroupId() + "]没有任何识别数据");
				continue;
			}
			String[] data = weixinGroup.getGroupIdentify().split(",");
			for(String id : data){
				if(id.equalsIgnoreCase(identify)){
					return weixinGroup;
				}
			}
		}
		return null;
	}

	private void initCache() {
		WeixinGroupCriteria weixinGroupCriteria = new WeixinGroupCriteria();
		List<WeixinGroup> weixinGroupList = list(weixinGroupCriteria);
		if(weixinGroupList == null || weixinGroupList.size() <1){
			logger.info("列出微信分组没有任何数据");
			return;
		}
		for(WeixinGroup weixinGroup : weixinGroupList){
			localCache.put(weixinGroup.getGroupId(), weixinGroup);
		}
		logger.debug("初始化了" + weixinGroupList.size() + "个微信分组");
		
	}
	

}

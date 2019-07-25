package com.maicard.wpt.service.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.product.service.ActivityService;
import com.maicard.wpt.criteria.AutoResponseModelCriteria;
import com.maicard.wpt.dao.AutoResponseModelDao;
import com.maicard.wpt.domain.AutoResponseModel;
import com.maicard.wpt.service.AutoResponseModelService;

@Service
public class AutoResponseModelServiceImpl extends BaseService implements AutoResponseModelService {
	
	@Resource
	private AutoResponseModelDao autoResponseModelDao;
	

	@Resource
	private ActivityService activityService;

	@Resource
	private ApplicationContextService applicationContextService;

	@Override
	public int insert(AutoResponseModel autoResponseModel) {
		try{
			return autoResponseModelDao.insert(autoResponseModel);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	@Override
	public int update(AutoResponseModel autoResponseModel) {
		try{
			return  autoResponseModelDao.update(autoResponseModel);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
			}

	@Override
	public int delete(long autoResponseModelId) {
		try{
			return  autoResponseModelDao.delete(autoResponseModelId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;	
	}
	
	@Override
	public AutoResponseModel select(long autoResponseModelId) {
		AutoResponseModel autoResponseModel =  autoResponseModelDao.select(autoResponseModelId);
		if(autoResponseModel == null){
			autoResponseModel = new AutoResponseModel();
		}
		return autoResponseModel;
	}

	@Override
	public List<AutoResponseModel> list(AutoResponseModelCriteria autoResponseModelCriteria) {
		return autoResponseModelDao.list(autoResponseModelCriteria);
		
	}
	
	@Override
	public List<AutoResponseModel> listOnPage(AutoResponseModelCriteria autoResponseModelCriteria) {
		List<AutoResponseModel> list =  autoResponseModelDao.listOnPage(autoResponseModelCriteria);
		if(list == null){
			return Collections.emptyList();
		} else {
			return list;
		}

	}
	
	@Override
	public int count(AutoResponseModelCriteria autoResponseModelCriteria) {
		return autoResponseModelDao.count(autoResponseModelCriteria);
	}

}

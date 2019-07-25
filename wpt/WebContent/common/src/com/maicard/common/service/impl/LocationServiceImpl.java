package com.maicard.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.annotation.IgnoreJmsDataSync;
import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.LocationCriteria;
import com.maicard.common.dao.LocationDao;
import com.maicard.common.domain.Location;
import com.maicard.common.service.LocationService;

@Service
public class LocationServiceImpl extends BaseService implements LocationService {

	@Resource
	private LocationDao locationDao;


	public int insert(Location location) {
		int rs = 0;
		try{
			rs = locationDao.insert(location);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return rs;

	}

	public int update(Location location) {
		try{
			return locationDao.update(location);
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	public int delete(LocationCriteria locationCriteria) {
		return 		locationDao.delete(locationCriteria);
	}
	
	public Location select(LocationCriteria locationCriteria) {
		List<Location> locationList = list(locationCriteria);
		
		if(locationList == null || locationList.size() < 1){
			return null;
		}		
		return locationList.get(0);
	}

	

	public List<Location> list(LocationCriteria locationCriteria) {
		List<Location> locationList = null;
		try {
			locationList = locationDao.list(locationCriteria);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(locationList == null){
			return null;
		}
		
		return locationList;
	}

	public List<Location> listOnPage(LocationCriteria locationCriteria) {
		List<Location> locationList = null;
		try {
			locationList = locationDao.listOnPage(locationCriteria);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(locationList == null){
			return null;
		}
		
		return locationList;
	}

	public 	int count(LocationCriteria locationCriteria){
		try {
			return locationDao.count(locationCriteria);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	@IgnoreJmsDataSync
	public int updateBatch(List<Location> flushLocationList) {
		if(flushLocationList == null || flushLocationList.size() < 1){
			return 0;
		}
		
		return locationDao.updateBatch(flushLocationList);
	}

	

}

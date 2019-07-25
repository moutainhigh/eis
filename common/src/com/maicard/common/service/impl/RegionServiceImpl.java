package com.maicard.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.RegionCriteria;
import com.maicard.common.dao.RegionDao;
import com.maicard.common.domain.Region;
import com.maicard.common.service.RegionService;

@Service
public class RegionServiceImpl extends BaseService implements RegionService {

	@Resource
	private RegionDao regionDao;
	


	public int insert(Region region) {
		try{
			return regionDao.insert(region);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(Region region) {
		try{
			return  regionDao.update(region);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
		
	}

	public int delete(int regionId) {
		try{
			return  regionDao.delete(regionId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
		
	}
	
	public Region select(int regionId) {
		return regionDao.select(regionId);
	}
	
	@Override
	public HashMap<String,Region> mapInTree(RegionCriteria regionCriteria){
		List<Region> plateNodeList = list(regionCriteria);
		if(plateNodeList == null){
			return null;
		}
		for(int i = 0; i< plateNodeList.size(); i++){		
			for(int j = 0; j< plateNodeList.size(); j++){
				if(plateNodeList.get(j).getParentRegionId() == plateNodeList.get(i).getRegionId()){
					if(plateNodeList.get(i).getSubRegionMap() == null){
						plateNodeList.get(i).setSubRegionMap(new HashMap<String, Region>());
					}
					plateNodeList.get(i).getSubRegionMap().put(String.valueOf(plateNodeList.get(j).getRegionId()),plateNodeList.get(j));
				}
			}

		}
		HashMap<String,Region> topNodeList = new HashMap<String, Region>();

		for(int i = 0; i< plateNodeList.size(); i++){	
			if(plateNodeList.get(i).getParentRegionId() == 0){
				topNodeList.put(String.valueOf(plateNodeList.get(i).getRegionId()),plateNodeList.get(i));
			}
		}
		return topNodeList;
	}

	public List<Region> list(RegionCriteria regionCriteria) {
		List<Integer> idList = regionDao.listPk(regionCriteria);
		if(idList != null && idList.size() > 0){
			List<Region> regionList =  new ArrayList<Region> ();		
			for(int i = 0; i < idList.size(); i++){
				Region region = regionDao.select(idList.get(i));
				if(region != null){
					region.setIndex(i+1);
				//	region.setCurrentStatusName(BasicStatus.unknown.findById(region.getCurrentStatus()).getName());	
					/*//检查是否有子区域
					RegionCriteria subRegionCriteria = regionCriteria.clone();
					subRegionCriteria.setParentRegionId(region.getRegionId());
					List<Region> subRegion = list(subRegionCriteria);
					if(subRegion != null){
						regionList.addAll(subRegion);
					}*/
					regionList.add(region);
				}
			}
			idList = null;
			return regionList;
		}
		return null;
		/*
		List<Region> regionList = regionDao.list(regionCriteria);
		if(regionList != null){
			for(int i = 0; i < regionList.size(); i++){
				regionList.get(i).setIndex(i+1);
			}
		}
		return regionList;
		*/
	}
	@Override
	public List<Region> listOnPage(RegionCriteria regionCriteria) {
		List<Integer> idList = regionDao.listPkOnPage(regionCriteria);
		if(idList != null && idList.size() > 0){
			List<Region> regionList =  new ArrayList<Region> ();		
			for(int i = 0; i < idList.size(); i++){
				Region region = regionDao.select(idList.get(i));
				if(region != null){
					region.setIndex(i+1);
			//		region.setCurrentStatusName(BasicStatus.unknown.findById(region.getCurrentStatus()).getName());	
					regionList.add(region);
				}
			}
			idList = null;
			return regionList;
		}
		return null;
		/*
		return regionDao.listOnPage(regionCriteria);
		*/
	}
	

	@Override
	public int count(RegionCriteria regionCriteria) {
		return regionDao.count(regionCriteria);
	}

	
	

}

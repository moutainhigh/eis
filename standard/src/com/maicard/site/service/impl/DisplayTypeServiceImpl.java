package com.maicard.site.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.site.criteria.*;
import com.maicard.site.dao.*;
import com.maicard.site.domain.DisplayType;
import com.maicard.site.service.DisplayTypeService;
import com.maicard.standard.BasicStatus;


@Service
public class DisplayTypeServiceImpl extends BaseService implements DisplayTypeService {

	@Resource
	private DisplayTypeDao displayTypeDao;	


	public int insert(DisplayType displayType) {
		try{
			return displayTypeDao.insert(displayType);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(DisplayType displayType) {
		try{
			return  displayTypeDao.update(displayType);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int delete(int displayTypeId) {
		try{
			return  displayTypeDao.delete(displayTypeId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}

	public DisplayType select(int displayTypeId){
		DisplayType displayType =  displayTypeDao.select(displayTypeId);
		
		return displayType;
	}

	public List<DisplayType> list(DisplayTypeCriteria displayTypeCriteria) {
		List<Integer> idList = displayTypeDao.listPk(displayTypeCriteria);
		if(idList != null && idList.size() > 0){
			List<DisplayType> displayTypeList =  new ArrayList<DisplayType> ();		
			for(Integer id : idList){
				DisplayType displayType = displayTypeDao.select(id);
				if(displayType != null){
					displayTypeList.add(displayType);
				}
			}
			idList = null;
			return displayTypeList;
		}
		return null;
		/*List<DisplayType> displayTypeList =  displayTypeDao.list(displayTypeCriteria);
		if(displayTypeList == null){
			return null;
		}
		for(int i = 0; i < displayTypeList.size(); i++){
			displayTypeList.get(i).setIndex(i+1);
			displayTypeList.get(i).setId(displayTypeList.get(i).getDisplayTypeId());
			displayTypeList.get(i).setStatusName(CommonStandard.BasicStatus.normal.findById(displayTypeList.get(i).getCurrentStatus()).getName());
		}
		return displayTypeList;*/
	}

	public List<DisplayType> listOnPage(DisplayTypeCriteria displayTypeCriteria) {
		List<Integer> idList = displayTypeDao.listPkOnPage(displayTypeCriteria);
		if(idList != null && idList.size() > 0){
			List<DisplayType> displayTypeList =  new ArrayList<DisplayType> ();		
			for(Integer id : idList){
				DisplayType displayType = displayTypeDao.select(id);
				if(displayType != null){
					displayTypeList.add(displayType);
				}
			}
			idList = null;
			return displayTypeList;
		}
		return null;
		
		/*List<DisplayType> displayTypeList =  displayTypeDao.listOnPage(displayTypeCriteria);
		if(displayTypeList == null){
			return null;
		}
		for(int i = 0; i < displayTypeList.size(); i++){
			displayTypeList.get(i).setIndex(i+1);
			displayTypeList.get(i).setId(displayTypeList.get(i).getDisplayTypeId());
			displayTypeList.get(i).setStatusName(CommonStandard.BasicStatus.normal.findById(displayTypeList.get(i).getCurrentStatus()).getName());
		}
		return displayTypeList;*/
	}

	
}

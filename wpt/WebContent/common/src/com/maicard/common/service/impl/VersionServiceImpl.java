package com.maicard.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.VersionCriteria;
import com.maicard.common.dao.VersionDao;
import com.maicard.common.domain.Version;
import com.maicard.common.service.VersionService;

@Service
public class VersionServiceImpl extends BaseService implements VersionService{

	@Resource
	private VersionDao versionDao;
	
	
	@Override
	public Version getLastVersion(long  partnerId){
		VersionCriteria versionCriteria = new VersionCriteria();
		versionCriteria.setPartnerId(partnerId);
		List<Version> versionList = listOnPage(versionCriteria);
		if(versionList == null || versionList.size() < 1){
			return null;
		}
		return versionList.get(0);
	}

	
	public int insert(Version version) {
		try{
			return versionDao.insert(version);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
		
	}


	public int update(Version version) {
		try{
			return  versionDao.update(version);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
	}

	public int delete(int versionId) {
		try{
			return  versionDao.delete(versionId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}
	
	public Version select(int versionId) {
		Version version =  versionDao.select(versionId);
		return version;
	}

	public List<Version> list(VersionCriteria versionCriteria) {
		List<Integer> idList = versionDao.listPk(versionCriteria);
		if(idList != null && idList.size() > 0){
			List<Version> dictList =  new ArrayList<Version> ();		
			for(int i = 0; i < idList.size(); i++){
				Version version = versionDao.select(idList.get(i));
				if(version != null){
					version.setId(version.getVersionId());
					version.setIndex(i+1);
					dictList.add(version);
				}
			}
			idList = null;
			return dictList;
		}
		return null;
		/*
		List<Version> versionList =  versionDao.list(versionCriteria);
		if(versionList == null){
			return null;
		}
		for(int i = 0; i < versionList.size(); i++){
			versionList.get(i).setId(versionList.get(i).getVersionId());
			versionList.get(i).setIndex(i+1);
			versionList.get(i).setStatusName(CommonStandard.BasicStatus.disable.findById(versionList.get(i).getCurrentStatus()).getName());	
		}
		return versionList;
		*/
	}
	
	public List<Version> listOnPage(VersionCriteria versionCriteria) {
		List<Integer> idList = versionDao.listPkOnPage(versionCriteria);
		if(idList != null && idList.size() > 0){
			List<Version> dictList =  new ArrayList<Version> ();		
			for(int i = 0; i < idList.size(); i++){
				Version version = versionDao.select(idList.get(i));
				if(version != null){
					version.setId(version.getVersionId());
					version.setIndex(i+1);
					dictList.add(version);
				}
			}
			idList = null;
			return dictList;
		}
		return null;
		/*
		List<Version> versionList =  versionDao.listOnPage(versionCriteria);
		if(versionList == null){
			return null;
		}
		for(int i = 0; i < versionList.size(); i++){
			versionList.get(i).setId(versionList.get(i).getVersionId());
			versionList.get(i).setIndex(i+1);
			versionList.get(i).setStatusName(CommonStandard.BasicStatus.disable.findById(versionList.get(i).getCurrentStatus()).getName());	
		}
		return versionList;
		*/
	}
	
	public int count(VersionCriteria versionCriteria){
		return versionDao.count(versionCriteria);
	}

}

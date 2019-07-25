package com.maicard.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.DictCriteria;
import com.maicard.common.dao.DictDao;
import com.maicard.common.domain.Dict;
import com.maicard.common.service.DictService;

@Service
public class DictServiceImpl extends BaseService implements DictService {

	@Resource
	private DictDao dictDao;
	


	public int insert(Dict dict) {
		try{
			return dictDao.insert(dict);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(Dict dict) {
		try{
			return  dictDao.update(dict);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
		
	}

	public int delete(int dictId) {
		try{
			return  dictDao.delete(dictId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
		
	}
	
	public Dict select(int dictId) {
		return dictDao.select(dictId);
	}

	public List<Dict> list(DictCriteria dictCriteria) {
		List<Integer> idList = dictDao.listPk(dictCriteria);
		if(idList != null && idList.size() > 0){
			List<Dict> dictList =  new ArrayList<Dict> ();		
			for(Integer id : idList){
				Dict dict = dictDao.select(id);
				if(dict != null){
					dictList.add(dict);
				}
			}
			idList = null;
			return dictList;
		}
		return null;
		/* 非缓存模式
		List<Dict> dictList = dictDao.list(dictCriteria);
		if(dictList != null){
			for(int i = 0; i < dictList.size(); i++){
				dictList.get(i).setIndex(i+1);
			}
		}
		return dictList;*/
	}
	
	@Override
	public List<Dict> listOnPage(DictCriteria dictCriteria) {
		List<Integer> idList = dictDao.listPkOnPage(dictCriteria);
		if(idList != null && idList.size() > 0){
			List<Dict> dictList =  new ArrayList<Dict> ();		
			for(Integer id : idList){
				Dict dict = dictDao.select(id);
				if(dict != null){
					dictList.add(dict);
				}
			}
			idList = null;
			return dictList;
		}
		return null;
		//非缓存模式 
		//return dictDao.listOnPage(dictCriteria);
	}


	@Override
	public int count(DictCriteria dictCriteria) {
		return dictDao.count(dictCriteria);
	}

	
	@Override
	public Dict select(String dictData) {
		DictCriteria dictCriteria = new DictCriteria();
		dictCriteria.setDictData(dictData);
		List<Dict> dictList = list(dictCriteria);
		if(dictList != null && dictList.size() == 1){
			return dictList.get(0);
		}
		return null;
	}
	
	

}

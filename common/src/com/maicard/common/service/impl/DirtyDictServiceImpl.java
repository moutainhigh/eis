package com.maicard.common.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.dao.DirtyDictDao;
import com.maicard.common.service.DirtyDictService;

@Service
public class DirtyDictServiceImpl extends BaseService implements DirtyDictService {

	@Resource
	private DirtyDictDao dirtyDictDao;

	@Override
	public boolean isDirty(String word) {
		if(StringUtils.isBlank(word)){
			return false;
		}
		try{
			return dirtyDictDao.exist(word);
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String replace(String word) {
		return dirtyDictDao.replace(word);
	}
	
	@Override
	public String check(String sentence) {
		return dirtyDictDao.check(sentence);
	}

}

package com.maicard.site.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.site.criteria.TagStatCriteria;
import com.maicard.site.dao.TagStatDao;
import com.maicard.site.domain.TagStat;
import com.maicard.site.service.TagStatService;

@Service
public class TagStatServiceImpl extends BaseService implements TagStatService {

	@Resource
	private TagStatDao tagStatDao;


	public void insert(TagStat tagStat) {
		tagStatDao.insert(tagStat);
	}

	public int update(TagStat tagStat) {
		int actualRowsAffected = 0;
		
		int tagStatId = tagStat.getTagStatId();

		TagStat _oldTagStat = tagStatDao.select(tagStatId);
		
		if (_oldTagStat != null) {
			actualRowsAffected = tagStatDao.update(tagStat);
		}
		
		return actualRowsAffected;
	}

	public int delete(int tagStatId) {
		int actualRowsAffected = 0;
		
		TagStat _oldTagStat = tagStatDao.select(tagStatId);
		
		if (_oldTagStat != null) {
			actualRowsAffected = tagStatDao.delete(tagStatId);
		}
		
		return actualRowsAffected;
	}
	
	public TagStat select(int tagStatId) {
		return tagStatDao.select(tagStatId);
	}

	public List<TagStat> list(TagStatCriteria tagStatCriteria) {
		return tagStatDao.list(tagStatCriteria);
	}
	
	public List<TagStat> listOnPage(TagStatCriteria tagStatCriteria) {
		return tagStatDao.listOnPage(tagStatCriteria);
	}

}

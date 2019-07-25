package com.maicard.site.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.site.criteria.TagCriteria;
import com.maicard.site.criteria.TagObjectRelationCriteria;
import com.maicard.site.dao.TagDao;
import com.maicard.site.domain.Tag;
import com.maicard.site.service.TagObjectRelationService;
import com.maicard.site.service.TagService;
import com.maicard.standard.CommonStandard;

@Service
public class TagServiceImpl extends BaseService implements TagService {

	@Resource
	private TagDao tagDao;
	
	@Resource
	private TagObjectRelationService tagObjectRelationService;

	public int insert(Tag tag) {
		return tagDao.insert(tag);

	}

	public int update(Tag tag) {
		return  tagDao.update(tag);

	}

	public int delete(long tagId) {
		//先删除跟本标签的关联关系
		TagObjectRelationCriteria tagObjectRelationCriteria = new TagObjectRelationCriteria();
		tagObjectRelationCriteria.setTagIds(tagId);
		int rs = tagObjectRelationService.delete(tagObjectRelationCriteria);
		logger.debug("删除跟tagId:" + tagId + "相关联的关系:" + rs);
		return  tagDao.delete(tagId);

	}

	public Tag select(long tagId) {
		return tagDao.select(tagId);
	}

	public List<Tag> list(TagCriteria tagCriteria) {
		List<Long> idList = tagDao.listPk(tagCriteria);
		if(idList == null || idList.size() < 1){
			return Collections.emptyList();
		}
		List<Tag> tagList =  new ArrayList<Tag> ();		
		for(long id : idList){
			Tag dict = tagDao.select(id);
			if(dict != null){
				tagList.add(dict);
			}
		}
		idList = null;
		return tagList;
	}

	public List<Tag> listOnPage(TagCriteria tagCriteria) {
		List<Long> idList = tagDao.listPkOnPage(tagCriteria);
		if(idList == null || idList.size() < 1){
			return Collections.emptyList();
		}
		List<Tag> tagList =  new ArrayList<Tag> ();		
		for(long id : idList){
			Tag dict = tagDao.select(id);
			if(dict != null){
				tagList.add(dict);
			}
		}
		idList = null;
		return tagList;

	}

	@Override
	public Tag select(String tagName, long ownerId) {
		TagCriteria tagCriteria = new TagCriteria();
		tagCriteria.setTagName(tagName);
		List<Tag> tagList = list(tagCriteria);
		logger.debug("根据名字[" + tagName + "]找到的标签:" + (tagList == null ? -1 : tagList.size()));
		if(tagList == null || tagList.size() != 1){
			return null;
		}
		return tagList.get(0);
	}

	@Override
	public String parseTag(String src){
		return src.replaceAll(CommonStandard.tagSplit, " ");
	}

	@Override
	public int count(TagCriteria tagCriteria) {
		return tagDao.count(tagCriteria);
	}


}

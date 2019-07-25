package com.maicard.site.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisObject;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.ClassUtils;
import com.maicard.common.util.JsonUtils;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.mb.service.MessageService;
import com.maicard.site.criteria.TagObjectRelationCriteria;
import com.maicard.site.dao.TagObjectRelationDao;
import com.maicard.site.domain.Tag;
import com.maicard.site.domain.TagObjectRelation;
import com.maicard.site.service.TagObjectRelationService;
import com.maicard.site.service.TagService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.ObjectType;

@Service
public class TagObjectRelationServiceImpl extends BaseService implements TagObjectRelationService {

	@Resource
	private TagObjectRelationDao tagObjectRelationDao;

	@Resource
	private ConfigService configService;

	@Resource
	private MessageService messageService;

	@Resource
	private TagService tagService;


	public int insert(TagObjectRelation tagObjectRelation) {
		try{
			int rs = tagObjectRelationDao.insert(tagObjectRelation);
			logger.debug("新增一个对tag:" + tagObjectRelation.getTagId() + "的关联，关联对象是:" + tagObjectRelation.getObjectType() + "#" + tagObjectRelation.getObjectId() + ",新增后的主键是:" + tagObjectRelation.getTagObjectRelationId() + ",新增结果是:" + rs);
			return rs;
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(TagObjectRelation tagObjectRelation) {
		try{
			return  tagObjectRelationDao.update(tagObjectRelation);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int delete(long tagObjectRelationId) {
		try{
			return  tagObjectRelationDao.delete(tagObjectRelationId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}

	public TagObjectRelation select(long tagObjectRelationId) {
		return tagObjectRelationDao.select(tagObjectRelationId);
	}

	public List<TagObjectRelation> list(TagObjectRelationCriteria tagObjectRelationCriteria) {
		return tagObjectRelationDao.list(tagObjectRelationCriteria);
	}

	public List<TagObjectRelation> listOnPage(TagObjectRelationCriteria tagObjectRelationCriteria) {
		return tagObjectRelationDao.listOnPage(tagObjectRelationCriteria);
	}



	@Override
	public int delete(TagObjectRelationCriteria tagObjectRelationCriteria) {
		Assert.notNull(tagObjectRelationCriteria,"尝试条件删除的tagObjectRelationCriteria不能为空");
		if(tagObjectRelationCriteria.getTagIds() != null && tagObjectRelationCriteria.getTagIds().length > 0){
			logger.debug("准备删除跟标签:" + Arrays.toString(tagObjectRelationCriteria.getTagIds()) + "的所有关联关系");
		} else {
			if(StringUtils.isBlank(tagObjectRelationCriteria.getObjectType())){
				logger.error("准备删除标签:" + tagObjectRelationCriteria.getObjectType() + "关联但是objectType为空");
				return -1;
			}
			if(tagObjectRelationCriteria.getObjectId() < 1){
				logger.error("准备删除标签:" + tagObjectRelationCriteria.getObjectId() + "关联但是objectId为空");
				return -1;
			}
			logger.debug("准备删除跟对象:" + tagObjectRelationCriteria.getObjectType() + "#" + tagObjectRelationCriteria.getObjectId() + "的所有关联关系");

		}
		int deleted = 0;
		try{
			deleted = tagObjectRelationDao.delete(tagObjectRelationCriteria);
			logger.info("根据条件:" + tagObjectRelationCriteria + "删除的标签关联是:" + deleted);
		}catch(Exception e){
			e.printStackTrace();
		}
		return 1;
	}

	@Override
	public int sync(long ownerId, String objectType, long objectId, String... tags) {
		if(objectType == null || objectType.equals("")){
			return 0;
		}
		if(objectId < 1){
			return 0;
		}

		if(tags == null || tags.length < 1){
			logger.debug("请求同步的数据没有tags，直接删除");
			TagObjectRelationCriteria tagObjectRelationCriteria = new TagObjectRelationCriteria();
			tagObjectRelationCriteria.setObjectType(objectType);
			tagObjectRelationCriteria.setObjectId(objectId);
			return delete(tagObjectRelationCriteria);
		}
		Set<String> multiTags = new HashSet<String>();
		String[] t = null;
		if(tags.length == 1){
			String t2 = tags[0];
			if(t2 != null){
				t = t2.split(CommonStandard.tagSplit);			
			}
		} else {
			t = tags;
		}
		if(t != null && t.length > 0){
			for(String data : t){
				if(StringUtils.isBlank(data)){
					continue;
				}
				multiTags.add(data);
			}
		}
		logger.debug("当前需要为对象" + objectType + "#" + objectId + "处理[" + multiTags.size() + "]个tag");
		//处理删除标签关联
		List<Tag> _oldTagList = new ArrayList<Tag>();
		TagObjectRelationCriteria tagObjectRelationCriteria = new TagObjectRelationCriteria();
		tagObjectRelationCriteria.setObjectType(objectType);
		tagObjectRelationCriteria.setObjectId(objectId);
		List<TagObjectRelation> _oldTagObjectRelationList = list(tagObjectRelationCriteria);
		if(_oldTagObjectRelationList != null){
			for(TagObjectRelation tor : _oldTagObjectRelationList){
				if(tor != null){
					Tag _oldTag = tagService.select(tor.getTagId());
					if(_oldTag != null){
						_oldTagList.add(_oldTag);
					}
				}
			}
		}

		if(_oldTagList != null && _oldTagList.size() > 0){//根据相同的objectType和objectId得到了旧的数据
			for(Tag tag : _oldTagList ){
				boolean remove = true;
				for(String tagName : multiTags){
					if(tag.getTagName().equals(tagName)){
						remove = false;
						break;
					}
				}
				if(remove){
					tagObjectRelationCriteria = new TagObjectRelationCriteria();
					tagObjectRelationCriteria.setTagIds(tag.getTagId());
					logger.debug("删除未提交的标签关系[" + tag.getTagName() + "]");
					//FIXME 此处应处理标签的标签关联关系
					tagObjectRelationCriteria.setTagIds(tag.getTagId());

					delete(tagObjectRelationCriteria);
					//将指定tag的关联关系-1
					tag.setObjectCount(tag.getObjectCount()-1);
					tagService.update(tag);
				}
			}
		}


		int actualRowsAffected = 0;
		for(String tag : multiTags){
			logger.debug("处理tag:" + tag);
			Tag existTag = tagService.select(tag, ownerId);
			long tagId = 0;
			if(existTag == null){
				Tag newTag = new Tag();
				newTag.setTagName(tag);
				newTag.setObjectCount(1);
				newTag.setCurrentStatus(BasicStatus.normal.getId());
				if(tagService.insert(newTag) != 1){
					throw new DataWriteErrorException("无法插入新的tag:" + tag);
				}
				tagId = newTag.getTagId();
				logger.debug("插入新的tag:" + tag + "/" + tagId);
			} else {
				tagId = existTag.getTagId();				
				logger.debug("找到已存在的tag:" + tag + "/" + tagId);
			}
			if(tagId == 0){
				throw new RequiredObjectIsNullException("得不到需要的tagId");
			}
			tagObjectRelationCriteria = new TagObjectRelationCriteria(); 
			tagObjectRelationCriteria.setObjectType(objectType);
			tagObjectRelationCriteria.setObjectId(objectId);
			tagObjectRelationCriteria.setTagIds(tagId);
			if(tagObjectRelationDao.count(tagObjectRelationCriteria) > 0){
				logger.debug("tag:" + tag + "与对象:" + objectType + "#" + objectId + "的关联已存在");
				continue;
			}
			if(existTag != null){//说明是找到的旧tag，应当添加一个引用count
				existTag.setObjectCount(existTag.getObjectCount()+1);
				tagService.update(existTag);
			}
			TagObjectRelation tagObjectRelation = new TagObjectRelation();
			tagObjectRelation.setObjectType(objectType);
			tagObjectRelation.setObjectId(objectId);
			tagObjectRelation.setTagId(tagId);
			tagObjectRelation.setCreateTime(new Date());
			tagObjectRelation.setCurrentStatus(BasicStatus.normal.getId());
			if(insert(tagObjectRelation) > 0){
				actualRowsAffected++;
			} else {
				throw new DataWriteErrorException("无法写入新的tagObjectRelation");
			}

		}

		return actualRowsAffected;
	}

	@Override
	public List<Tag> listTags(TagObjectRelationCriteria tagObjectRelationCriteria) {
		List<TagObjectRelation> tagObjectRelationList = list(tagObjectRelationCriteria);
		if(tagObjectRelationList == null || tagObjectRelationList.size() < 1){
			return Collections.emptyList();
		}
		List<Tag> tagList = new ArrayList<Tag>();
		for(TagObjectRelation tagObjectRelation : tagObjectRelationList){
			Tag tag = tagService.select(tagObjectRelation.getTagId());
			if(tag != null && !tagList.contains(tag)){
				tagList.add(tag);
			}
		}
		return tagList;
	}

	@Override
	public void processTagForTag(EisObject target) throws Exception{
		
		long ownerId = target.getOwnerId();
		Map<String,KeyValue> tagForTagMap = new HashMap<String,KeyValue>();

		String tagTagConfigString = configService.getValue(DataName.tagtagConfig.toString(), target.getOwnerId());
		Map<String,String> tagTagConfigMap = new HashMap<String,String>();
		if(StringUtils.isNotBlank(tagTagConfigString)){
			logger.debug("系统定义标签的标签配置是:" + tagTagConfigString);

			try{
				tagTagConfigMap = JsonUtils.getInstance().readValue(tagTagConfigString, new TypeReference<HashMap<String,String>>(){});
			}catch(Exception e){
				e.printStackTrace();
			}
			if(tagTagConfigMap == null || tagTagConfigMap.size() < 1){
				logger.error("无法解析系统定义的标签的标签配置:" + tagTagConfigString);
				return;
			}
		} else {
			logger.debug("系统未定义标签的标签配置");
			return;

		}
		
		for(String sourceTagCode : tagTagConfigMap.keySet()){
			String sourceTagValue = ClassUtils.getValue(target, sourceTagCode, null);
			logger.debug("根据系统配置获取标签的标签源:" + sourceTagCode + "=>" + sourceTagValue);
			if(sourceTagValue == null){
				logger.debug("找不到标签的标签源:" + sourceTagCode + "的对应数据");
				continue;
			}

			String destTagCode = tagTagConfigMap.get(sourceTagCode);
			if(destTagCode == null){
				logger.debug("找不到标签的标签源:" + sourceTagCode + "的目标标签代码");
				continue;
			}
			String destTagValue = ClassUtils.getValue(target, destTagCode, null);
			logger.debug("获取标签的标签源:" + sourceTagCode + "对应的目标标签[" + destTagCode + "]的数据:" + destTagValue);
			if(sourceTagValue != null && destTagValue != null){
				logger.debug("把数据[" + sourceTagCode + "]放入标签的标签源:" + sourceTagValue);
				tagForTagMap.put(sourceTagCode, new DefaultKeyValue(sourceTagValue,destTagValue));
			}
		}
		
		if(tagForTagMap.size() < 1){
			logger.info("未能找到任何标签的标签数据对");
			return;
		}
		String targetObjectType = StringUtils.uncapitalize(target.getClass().getSimpleName()); 
		for(String key : tagForTagMap.keySet()){

			KeyValue kv = tagForTagMap.get(key);
			String sourceTagName = kv.getKey().toString();
			String destTagName = kv.getValue().toString();

			Tag sourceTag = tagService.select(sourceTagName, ownerId);
			if(sourceTag == null){
				logger.info("在系统中找不到标签:" + sourceTagName);
				sourceTag = new Tag(sourceTagName, ownerId);
				int rs = tagService.insert(sourceTag);
				logger.debug("新增标签[" + sourceTagName + "]结果:" + rs);
				if(rs != 1){
					continue;
				} 				
			}
			//为文档或产品自身生成对应的标签关联关系
			if(target.getId() <= 0){
				throw new Exception("处理tagForTag的对象必须有主键并且已赋值给id属性");
			}
			TagObjectRelation tagObjectRelation = new TagObjectRelation();
			tagObjectRelation.setOwnerId(ownerId);
			
			tagObjectRelation.setObjectType(targetObjectType);
			tagObjectRelation.setObjectId(target.getId());
			tagObjectRelation.setTagId(sourceTag.getTagId());
			tagObjectRelation.setCreateTime(new Date());
			tagObjectRelation.setCurrentStatus(BasicStatus.normal.getId());
			int rs = insert(tagObjectRelation);
			if(rs == 1){
				messageService.sendJmsDataSyncMessage(null, "tagObjectRelationService", "insert", tagObjectRelation);
			}

			Tag destTag = tagService.select(destTagName, ownerId);
			if(destTag == null){
				logger.info("在系统中找不到标签:" + destTagName);
				destTag = new Tag(destTagName, ownerId);
				rs = tagService.insert(destTag);
				logger.debug("新增标签[" + destTagName + "]结果:" + rs);
				if(rs != 1){
					continue;
				} 				
			}
			tagObjectRelation = new TagObjectRelation();
			tagObjectRelation.setOwnerId(ownerId);
			tagObjectRelation.setObjectType(ObjectType.tag.name());
			tagObjectRelation.setObjectId(destTag.getTagId());
			tagObjectRelation.setTagId(sourceTag.getTagId());
			tagObjectRelation.setCreateTime(new Date());
			tagObjectRelation.setCurrentStatus(BasicStatus.normal.getId());
			rs = insert(tagObjectRelation);
			logger.debug("向系统中增加标签的标签关系:" + tagObjectRelation + ",结果:" + rs);
			if(rs == 1){
				messageService.sendJmsDataSyncMessage(null, "tagObjectRelationService", "insert", tagObjectRelation);
			}
		}

	}

	@Override
	public int count(TagObjectRelationCriteria tagObjectRelationCriteria) {
		return tagObjectRelationDao.count(tagObjectRelationCriteria);
	}

}

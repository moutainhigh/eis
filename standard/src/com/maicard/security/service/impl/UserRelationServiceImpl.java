package com.maicard.security.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisObject;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.UuidService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.util.NumericUtils;
import com.maicard.method.ExtraValueAccess;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.dao.UserRelationDao;
import com.maicard.security.domain.UserRelation;
import com.maicard.security.service.UserRelationService;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectUid;

import static com.maicard.standard.KeyConstants.USER_RELATION_COUNT_KEY_PREFIX;

@Service
public class UserRelationServiceImpl extends BaseService implements UserRelationService {

	@Resource
	private UserRelationDao userRelationDao;

	@Resource
	private DataDefineService dataDefineService;

	@Resource
	private CenterDataService centerDataService;
	
	@Resource
	private UuidService uuidService;

	final String[] needCachedDynamicType = new String[]{UserRelationCriteria.RELATION_TYPE_FAVORITE,UserRelationCriteria.RELATION_TYPE_PRAISE, UserRelationCriteria.RELATION_TYPE_READ};



	/**
	 * 异步添加一个对象关联<br/>
	 * 并把该对象的关联总数放入REDIS
	 * 主要用于文章阅读
	 */
	@Override
	@Async
	public void insertAsync(UserRelation userRelation){
		int rs = insert(userRelation);
		if(rs != 1){
			return;
		}
/*		String key = USER_RELATION_COUNT_KEY_PREFIX + "#" + userRelation.getObjectType() + "#" + (userRelation.getRelationType() == null ? "" : userRelation.getRelationType() + "#") + userRelation.getObjectId();
		long relationCount = centerDataService.increaseBy(key, 1, 1);
		logger.debug("对对象[" + userRelation.getObjectType() + "#" + userRelation.getObjectId() + "]增加一个关联数量，最新关联数量是:" + relationCount);		
*/
	}

	@Override
	public int getRelationCount(UserRelationCriteria userRelationCriteria){
		Assert.notNull(userRelationCriteria,"统计关联对象的条件不能为空");
		Assert.notNull(userRelationCriteria.getObjectType(),"统计关联对象的对象类型不能为空");		
		String key = USER_RELATION_COUNT_KEY_PREFIX + "#" + userRelationCriteria.getObjectType() + "#" + (userRelationCriteria.getRelationType() == null ? "" : userRelationCriteria.getRelationType() + "#") + userRelationCriteria.getObjectId();
		String cachedCount = centerDataService.get(key);
		logger.debug("统计对象[" + key + "]在中央缓存中的总数是:" + cachedCount );
		int count = 0;
		if(cachedCount == null || !NumericUtils.isNumeric(cachedCount)){
			count = count(userRelationCriteria);
			cachedCount = String.valueOf(count);
			centerDataService.setForce(key, cachedCount, -1);
		}
		return Integer.parseInt(cachedCount);

	}

	public int insert(UserRelation userRelation) {
		if(StringUtils.isBlank(userRelation.getRelationLimit())){
			userRelation.setRelationLimit(UserRelationCriteria.RELATION_LIMIT_UNIQUE);
		}
		if(userRelation.getRelationLimit().equalsIgnoreCase(UserRelationCriteria.RELATION_LIMIT_UNIQUE) 
				|| userRelation.getRelationLimit().equalsIgnoreCase(UserRelationCriteria.RELATION_LIMIT_GLOBAL_UNIQUE)){
			UserRelationCriteria userRelationCriteria = new UserRelationCriteria(userRelation.getOwnerId());
			userRelationCriteria.setUuid(userRelation.getUuid());
			userRelationCriteria.setObjectType(userRelation.getObjectType());
			userRelationCriteria.setRelationType(userRelation.getRelationType());
			userRelationCriteria.setObjectId(userRelation.getObjectId());

			int alreadyRelatedCount = count(userRelationCriteria);
			if(alreadyRelatedCount > 0){
				logger.error("用户[" + userRelation.getUuid() + "]已关联" + userRelation.getObjectType() + "对象#" + userRelation.getObjectId() + ",关联类型为:" + userRelation.getRelationType() + ",且关联限制为:" + userRelation.getRelationLimit() + ",不再关注");
				return -1;
			}
		}
		if(userRelation.getCreateTime() == null){
			userRelation.setCreateTime(new Date());
		}
		if(userRelation.getUserRelationId() < 1){
			//通过REDIS去获取一个主键而不是使用数据库的自增ID，来保证insert的幂等性,NetSnake,2018-5-7.
			long minId = userRelationDao.getMaxId();
			userRelation.setUserRelationId(uuidService.createById(ObjectUid.USER_RELATION.id, minId+1, -1));
		}
		int rs =  userRelationDao.insert(userRelation);
		logger.debug("插入对象[" + userRelation + "]返回结果:" + rs);
		if(rs != 1){
			return rs;
		}
		//检查是否需要更新缓存
		for(String relationType : needCachedDynamicType){
			if(userRelation.getRelationType() != null && userRelation.getRelationType().equals(relationType)){
				String key = USER_RELATION_COUNT_KEY_PREFIX + "#" + userRelation.getObjectType() + "#" + relationType + "#" + userRelation.getObjectId();
				logger.debug("新写入的UserRelation需要在中央缓存中+1:" + key);
				centerDataService.increaseBy(key, 1, 1, 0);
				break;
			}
		}

		return rs;
	}

	public int update(UserRelation userRelation) {
		int actualRowsAffected = 0;

		long id = userRelation.getUserRelationId();

		UserRelation _oldUserRelation = userRelationDao.select(id);

		if (_oldUserRelation != null) {
			actualRowsAffected = userRelationDao.update(userRelation);
		}

		return actualRowsAffected;
	}

	public int delete(UserRelationCriteria userRelationCriteria) {

		if(userRelationCriteria == null){
			return -1;
		}
		if(StringUtils.isBlank(userRelationCriteria.getObjectType())){
			logger.error("条件删除关联未提供对象类型objectType");
			return -EisError.REQUIRED_PARAMETER.id;
		}
		if(userRelationCriteria.getUuid() <= 0 && userRelationCriteria.getObjectId() <= 0){
			logger.error("条件删除关联没有提供用户ID也没有提供对象ID");
			return -EisError.REQUIRED_PARAMETER.id;
		}	
		if(StringUtils.isBlank(userRelationCriteria.getRelationType())){
			logger.error("条件删除关联未提供关联的类型relationType");
			return -EisError.REQUIRED_PARAMETER.id;
		}
		int rs = userRelationDao.delete(userRelationCriteria);
		logger.debug("删除关联[" + userRelationCriteria + "]的结果是:" + rs);
		if(rs != 1){
			return rs;
		}
		//检查是否需要更新缓存
		for(String relationType : needCachedDynamicType){
			if(userRelationCriteria.getRelationType() != null && userRelationCriteria.getRelationType().equals(relationType)){
				String key = USER_RELATION_COUNT_KEY_PREFIX + "#" + userRelationCriteria.getObjectType() + "#" + relationType + "#" + userRelationCriteria.getObjectId();
				logger.debug("新删除的UserRelation需要在中央缓存中-1:" + key);
				centerDataService.increaseBy(key, -1, 1, 0);
				break;
			}
		}
		return rs;

	}



	public List<UserRelation> list(UserRelationCriteria userRelationCriteria) {
		List<UserRelation> userRelationList = userRelationDao.list(userRelationCriteria);
		if(userRelationList == null){
			return Collections.emptyList();
		} else {
			return userRelationList;
		}
	}

	public List<UserRelation> listOnPage(UserRelationCriteria userRelationCriteria) {
		List<UserRelation> userRelationList = userRelationDao.listOnPage(userRelationCriteria);
		if(userRelationList != null){
			for(int i = 0; i < userRelationList.size(); i++){
				userRelationList.get(i).setIndex(i+1);
			}
		}
		return userRelationList;	
	}

	@Override
	public int count(UserRelationCriteria userRelationCriteria) {
		return userRelationDao.count(userRelationCriteria);
	}



	@Override
	public UserRelation select(long userRelationId) {
		UserRelation userRelation =  userRelationDao.select(userRelationId);	
		if(userRelation != null){
		}
		return userRelation;
	}

	@Override
	public int delete(long userRelationId) {
		UserRelation _oldUserRelation = select(userRelationId);
		int actualRowsAffected = 0;
		if(_oldUserRelation != null){
			actualRowsAffected = userRelationDao.delete(userRelationId);
		}
		logger.debug("删除关联[" + _oldUserRelation + "]的结果是:" + actualRowsAffected);
		if(actualRowsAffected != 1){
			return actualRowsAffected;
		}
		//检查是否需要更新缓存
		for(String relationType : needCachedDynamicType){
			if(_oldUserRelation.getRelationType() != null && _oldUserRelation.getRelationType().equals(relationType)){
				String key = USER_RELATION_COUNT_KEY_PREFIX + "#" + _oldUserRelation.getObjectType() + "#" + relationType + "#" + _oldUserRelation.getObjectId();
				logger.debug("新删除的UserRelation需要在中央缓存中-1:" + key);
				centerDataService.increaseBy(key, -1, 1, 0);
				break;
			}
		}
		return actualRowsAffected;
	}

	//在中央缓存中增加一个关联数
	@Override
	@Async
	public void plusCachedRelationCount(UserRelation userRelation) {
		String key = USER_RELATION_COUNT_KEY_PREFIX + "#" + userRelation.getObjectType() + "#" + (userRelation.getRelationType() == null ? "" : userRelation.getRelationType() + "#") + userRelation.getObjectId();
		long relationCount = centerDataService.increaseBy(key, 1, 1, 0);
		logger.debug("对对象[" + userRelation.getObjectType() + "#" + userRelation.getObjectId() + "]强行增加一个关联数量，最新关联数量是:" + relationCount);				
	}

	@Override
	public void setDynamicData(EisObject object){

		if(!(object instanceof ExtraValueAccess)){
			logger.error("系统无法处理不是ExtraValueAccess类型的动态关联数据");
			return;
		}

		ExtraValueAccess extraValueAccess = (ExtraValueAccess)object;

		String objectType = StringUtils.uncapitalize(object.getClass().getSimpleName());
		//获取阅读数
		for(String relationType : needCachedDynamicType){
			//从缓存中读取对应的KEY
			String key = USER_RELATION_COUNT_KEY_PREFIX + "#" + objectType + "#" + relationType + "#" + object.getId();
			String value = centerDataService.get(key);
			String dataCode = relationType + "Count";
			if(value == null){
				UserRelationCriteria userRelationCriteria = new UserRelationCriteria(object.getOwnerId());
				userRelationCriteria.setObjectType(objectType);
				userRelationCriteria.setRelationType(relationType);
				userRelationCriteria.setObjectId(object.getId());

				//从数据库统计
				int count = count(userRelationCriteria);
				logger.debug("从数据库中统计[" + key + "]的数量是:" + count);
				value = String.valueOf(count);				
				centerDataService.setForce(key, value, -1);
			} else {
				logger.debug("从中央缓存中统计[" + key + "]的数量是:" + value);

			}
			logger.debug("为对象[" + objectType + "#" + object.getId() + "]写入最新的数据:" + dataCode + "=>" + value);
			extraValueAccess.setExtraValue(dataCode, value);


		}


	}


}

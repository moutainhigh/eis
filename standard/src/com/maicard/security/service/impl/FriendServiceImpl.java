package com.maicard.security.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRelation;
import com.maicard.security.service.FriendService;
import com.maicard.security.service.FrontUserService;
import com.maicard.security.service.UserLocationService;
import com.maicard.security.service.UserRelationService;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserStatus;

public class FriendServiceImpl extends BaseService implements FriendService {

	@Resource
	private UserRelationService userRelationService;

	@Resource
	private UserLocationService userLocationService;

	@Resource
	private FrontUserService frontUserService;

	@Override
	public List<Long> listUuidOnPage(UserCriteria userCriteria){
		Assert.notNull(userCriteria,"列出好友的查询条件不能为空");

		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(userCriteria.getOwnerId());
		userRelationCriteria.setObjectType(ObjectType.friend.name());
		userRelationCriteria.setUuid(userCriteria.getUuid());
		userRelationCriteria.setCurrentStatus(userCriteria.getCurrentStatus());
		userRelationCriteria.setPaging(userCriteria.getPaging());
		List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
		if(userRelationList == null || userRelationList.size() < 1){
			return null;
		}
		List<Long> friendUuidList = new ArrayList<Long>();
		for(UserRelation userRelation : userRelationList){
			friendUuidList.add(userRelation.getObjectId());
		}
		return friendUuidList;
	}



	@Override
	public List<Long> listWaitConfirmUuidOnPage(UserCriteria userCriteria){
		Assert.notNull(userCriteria,"列出好友的查询条件不能为空");

		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(userCriteria.getOwnerId());
		userRelationCriteria.setObjectType(ObjectType.friend.name());
		userRelationCriteria.setObjectId(userCriteria.getUuid());
		userRelationCriteria.setCurrentStatus(userCriteria.getCurrentStatus());
		userRelationCriteria.setPaging(userCriteria.getPaging());
		List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
		if(userRelationList == null || userRelationList.size() < 1){
			return null;
		}
		List<Long> friendUuidList = new ArrayList<Long>();
		for(UserRelation userRelation : userRelationList){
			friendUuidList.add(userRelation.getUuid());
		}
		return friendUuidList;
	}

	@Override
	public List<User> listOnPage(UserCriteria userCriteria){
		Assert.notNull(userCriteria,"列出好友的查询条件不能为空");

		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(userCriteria.getOwnerId());
		userRelationCriteria.setObjectType(ObjectType.friend.name());
		userRelationCriteria.setUuid(userCriteria.getUuid());
		userRelationCriteria.setCurrentStatus(userCriteria.getCurrentStatus());
		userRelationCriteria.setPaging(userCriteria.getPaging());
		List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
		if(userRelationList == null || userRelationList.size() < 1){
			return null;
		}
		List<User> friendList = new ArrayList<User>();
		for(UserRelation userRelation : userRelationList){
			User user = frontUserService.select(userRelation.getObjectId());
			if(user == null){
				logger.warn("找不到用户[" + userCriteria.getUuid() + "]查询的玩家:" + userRelation.getObjectId());
				continue;
			}
			friendList.add(user);
		}
		return friendList;
	}

	@Override
	public int isFriend(long myUuid, long friendUuid, long ownerId) {
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(ownerId);
		userRelationCriteria.setObjectType(ObjectType.friend.name());
		userRelationCriteria.setUuid(myUuid);
		userRelationCriteria.setObjectId(friendUuid);
		List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
		logger.debug("根据用户[" + myUuid + "]获取对应用户[" + friendUuid + "]的好友关系，返回结果数量:" + (userRelationList == null ? "空" : userRelationList.size()));
		if(userRelationList == null || userRelationList.size() < 1){
			//不是好友
			return 0;
		}
		UserRelation userRelation = userRelationList.get(0);
		logger.debug("根据用户[" + myUuid + "]获取对应用户[" + friendUuid + "]的好友关系状态是:" + userRelation.getCurrentStatus());
		if(userRelation.getCurrentStatus() == UserStatus.authorized.id){
			return 1;
		}
		return 0;


	}

	@Override
	public int addFriend(long myUuid, long friendUuid, long ownerId) {

		if(myUuid == friendUuid){
			logger.info("用户[" + myUuid + "]尝试添加自身为好友");
			return EisError.duplicateOperate.id;
		}
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(ownerId);
		userRelationCriteria.setObjectType(ObjectType.friend.name());
		userRelationCriteria.setUuid(myUuid);
		userRelationCriteria.setObjectId(friendUuid);
		List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
		logger.debug("根据用户[" + myUuid + "]获取对应用户[" + friendUuid + "]的好友关系，返回结果数量:" + (userRelationList == null ? "空" : userRelationList.size()));
		if(userRelationList == null || userRelationList.size() < 1){
			//不是好友，添加请求
			UserRelation userRelation = new UserRelation(ownerId);
			userRelation.setObjectType(ObjectType.friend.name());
			userRelation.setUuid(myUuid);
			userRelation.setObjectId(friendUuid);
			userRelation.setCurrentStatus(UserStatus.inQuery.getId());
			userRelation.setRelationType(UserRelationCriteria.RELATION_LIMIT_UNIQUE);
			int rs = userRelationService.insert(userRelation);
			if(rs != 1){
				logger.error("无法添加好友请求，新增数据返回:" + rs);
				return OperateResult.failed.id;
			}
			logger.debug("添加用户[" + myUuid + "]针对用户[" + friendUuid + "]加为好友的请求，返回等待确认状态:" + userRelation.getCurrentStatus());
			return userRelation.getCurrentStatus();
		}
		//已经是好友或等待状态
		UserRelation userRelation = userRelationList.get(0);
		logger.debug("用户[" + myUuid + "]与对应用户[" + friendUuid + "]已经有好友关系，状态是:" + userRelation.getCurrentStatus());
		return userRelation.getCurrentStatus();
	}

	@Override
	public int deleteFriend(long myUuid, long friendUuid, long ownerId) {
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(ownerId);
		userRelationCriteria.setObjectType(ObjectType.friend.name());
		userRelationCriteria.setUuid(myUuid);
		userRelationCriteria.setObjectId(friendUuid);
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_LIMIT_UNIQUE);
		int rs = userRelationService.delete(userRelationCriteria);
		logger.debug("删除用户[" + myUuid + "=>" + friendUuid + "]的好友关系结果:" + rs);

		userRelationCriteria = new UserRelationCriteria(ownerId);
		userRelationCriteria.setObjectType(ObjectType.friend.name());
		userRelationCriteria.setUuid(friendUuid);
		userRelationCriteria.setObjectId(myUuid);
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_LIMIT_UNIQUE);
		rs = userRelationService.delete(userRelationCriteria);
		logger.debug("删除用户[" + myUuid + "<=" + friendUuid + "]的好友关系结果:" + rs);

		return rs;
	}

	@Override
	public int confirmFriend(long myUuid, long friendUuid, long ownerId, boolean deny){
		//先找出对方请求添加自己的那条记录
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(ownerId);
		userRelationCriteria.setObjectType(ObjectType.friend.name());
		userRelationCriteria.setUuid(friendUuid);
		userRelationCriteria.setObjectId(myUuid);
		List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
		logger.debug("根据用户[" + myUuid + "]获取对应用户[" + friendUuid + "]的好友关系，返回结果数量:" + (userRelationList == null ? "空" : userRelationList.size()));
		if(userRelationList == null || userRelationList.size() < 1){
			//没有找到添加好友的请求
			return EisError.dataNotFoundInSystem.id;
		}
		UserRelation userRelation = userRelationList.get(0);
		userRelation.setCurrentStatus(UserStatus.authorized.id);
		int rs = userRelationService.update(userRelation);
		logger.debug("确认好友添加请求[" + friendUuid + "=>" + myUuid + "]的修改结果:" + rs);

		//增加一条自己对该玩家的好友记录

		UserRelation userRelation2 = new UserRelation(ownerId);
		userRelation2.setObjectType(ObjectType.friend.name());
		userRelation2.setUuid(myUuid);
		userRelation2.setObjectId(friendUuid);
		userRelation2.setCurrentStatus(UserStatus.authorized.id);
		userRelation2.setRelationType(UserRelationCriteria.RELATION_LIMIT_UNIQUE);
		rs = userRelationService.insert(userRelation2);
		logger.debug("添加确认好友的我方数据请求[" + myUuid + "=>" + friendUuid + "]的添加结果:" + rs);
		if(rs != 1){
			//查找该记录
			userRelationCriteria = new UserRelationCriteria(ownerId);
			userRelationCriteria.setObjectType(ObjectType.friend.name());
			userRelationCriteria.setUuid(myUuid);
			userRelationCriteria.setObjectId(friendUuid);
			userRelationList = userRelationService.list(userRelationCriteria);
			if(userRelationList == null || userRelationList.size() < 1){
				logger.error("无法添加确认好友的我方数据[" + myUuid + "=>" + friendUuid + "]，也找不到已存在的该数据");
				return OperateResult.failed.id;
			}
			userRelation = userRelationList.get(0);
			userRelation.setCurrentStatus(UserStatus.authorized.id);
			rs = userRelationService.update(userRelation);
			return rs;
		}
		return OperateResult.success.id;
	}



	@Override
	public int count(UserCriteria userCriteria) {
		return 0;
	}





}

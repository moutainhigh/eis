package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;

public interface FriendService {
	/**
	 * 检查两个用户是不是好友，0不是，1是，2曾经是，3黑名单
	 */
	int isFriend(long myUuid, long friendUuid, long ownerId);

	/**
	 * 请求添加一个用户为好友，返回成功、失败、待确认等状态
	 */
	int addFriend(long myUuid, long friendUuid, long ownerId);

	/**
	 * 确认一个添加好友的请求，返回成功、拒绝等
	 * @param deny 
	 */
	int confirmFriend(long myUuid, long friendId, long ownerId, boolean deny);

	/**
	 * 列出自己的好友
	 */
	List<User> listOnPage(UserCriteria userCriteria);

	/**
	 * 列出好友的UUID，如果是游戏平台，则可能为roleId
	 */
	List<Long> listUuidOnPage(UserCriteria userCriteria);
	
	/**
	 * 列出等待我确认的好友请求
	 * @param userCriteria
	 * @return
	 */
	List<Long> listWaitConfirmUuidOnPage(UserCriteria userCriteria);


	/**
	 * 删除好友
	 */
	int deleteFriend(long myUuid, long friendUuid, long ownerId);

	/**
	 * 计算好友个数
	 * @param userCriteria
	 * @return
	 */
	int count(UserCriteria userCriteria);





}

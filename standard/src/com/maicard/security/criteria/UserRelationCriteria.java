package com.maicard.security.criteria;

import java.util.Date;

import org.springframework.util.Assert;

import com.maicard.common.base.Criteria;

public class UserRelationCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	public static final String RELATION_LIMIT_BUILTIN = "buildin";
	public static final String RELATION_LIMIT_CUSTOM = "custom";
	public static final String RELATION_LIMIT_UNIQUE = "unique";
	public static final String RELATION_LIMIT_MULTI = "multi";
	public static final String RELATION_LIMIT_GLOBAL_UNIQUE = "globalUnique";
	
	public static final String RELATION_TYPE_READ = "read";			//阅读了该文章
	public static final String RELATION_TYPE_FAVORITE = "favorite";	//收藏了该文章
	public static final String RELATION_TYPE_PRAISE = "praise";			//点赞了该对象
	public static final String RELATION_TYPE_SUBSCRIBE = "subscribe";			//订阅了该对象

	/**
	 * 赞助了该对象
	 */
	public static final String RELATION_TYPE_DONATE = "donate";
	
	
	
	private int userRelationId;

	private long uuid;

	private String objectType;

	private long objectId;
	
	private String relationLimit;		//关联限制，如是否只能唯一关注	
	
	private String relationType;
	
	private Date beginTime;
	
	private Date endTime;
	
	private String data;
	

	public UserRelationCriteria() {
	}

	public UserRelationCriteria(long ownerId) {
		Assert.isTrue(ownerId > 0, "新建立的UserRelationCriteria不能指定ownerId=0");
		this.ownerId = ownerId;
	}

	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getUserRelationId() {
		return userRelationId;
	}
	public void setUserRelationId(int userRelationId) {
		this.userRelationId = userRelationId;
	}
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public long getObjectId() {
		return objectId;
	}
	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}
	public String getRelationType() {
		return relationType;
	}
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	public String getRelationLimit() {
		return relationLimit;
	}

	public void setRelationLimit(String relationLimit) {
		this.relationLimit = relationLimit;
	}
	

}

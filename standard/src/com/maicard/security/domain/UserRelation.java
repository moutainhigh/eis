package com.maicard.security.domain;

import java.util.Date;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maicard.annotation.NeedJmsDataSyncP2P;
import com.maicard.common.domain.EVEisObject;
/**
 * 用户与对象的关联关系，所有没有由专用对象来标记的，全部使用这个关联关系。
 * 例如后台用户与发布节点、前台用户与与之关联的业务
 * 
 * 
 * @author NetSnake
 * @date 2012-3-29
 */


@JsonIgnoreProperties(ignoreUnknown = true)
@NeedJmsDataSyncP2P
public class UserRelation extends EVEisObject {

	private static final long serialVersionUID = 3974816927478246004L;


	private long userRelationId;

	private long uuid;
	
	/**
	 * 关联限制，如是否只能唯一关注	
	 */
	private String relationLimit;		

	/**
	 * 要关联的对象ID
	 */
	private long objectId;

	/**
	 * 与该对象的关联类型，如一个文档的关联，可能是关注，也可能是阅读
	 */
	private String relationType;		

	private long activity;

	private Date createTime;
	
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"userRelationId='" + userRelationId + "'" +
				"uuid=" + "'" + uuid + "'" + 
				"objectType=" + "'" + objectType + "'" + 
				"relationType=" + "'" + relationType + "'" + 
				"objectId=" + "'" + objectId + "'" + 
				"relationLimit=" + "'" + relationLimit + "'" + 
				")";
	}

	public UserRelation(){}

	public UserRelation(long ownerId){
		Assert.isTrue(ownerId > 0);
		this.ownerId = ownerId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
		this.lastUse = createTime;
	}

	private Date lastUse;




	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public long getActivity() {
		return activity;
	}

	public void setActivity(long activity) {
		this.activity = activity;
	}

	public Date getLastUse() {
		return lastUse;
	}

	public void setLastUse(Date lastUse) {
		this.lastUse = lastUse;
	}

	public long getUserRelationId() {
		return userRelationId;
	}

	public void setUserRelationId(long userRelationId) {
		this.userRelationId = userRelationId;
	}


	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	

	


	public String getRelationLimit() {
		return relationLimit;
	}

	public void setRelationLimit(String relationLimit) {
		this.relationLimit = relationLimit;
	}
}

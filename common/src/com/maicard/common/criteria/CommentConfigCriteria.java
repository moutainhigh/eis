package com.maicard.common.criteria;


import com.maicard.common.base.Criteria;
import com.maicard.standard.CommonStandard;

public class CommentConfigCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	public static final String CACHE_NAME = CommonStandard.cacheNameDocument;


	private String objectType;		//评论类型
	private long objectId;		//评论的对象ID
	private int initStatus;	//评论初始状态
	private boolean withGlobalConfig;		//是否查询ID=0的配置

	public CommentConfigCriteria() {
	}
	

	public CommentConfigCriteria(long ownerId) {
		this.ownerId = ownerId;
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

	public int getInitStatus() {
		return initStatus;
	}

	public void setInitStatus(int initStatus) {
		this.initStatus = initStatus;
	}


	public boolean isWithGlobalConfig() {
		return withGlobalConfig;
	}


	public void setWithGlobalConfig(boolean withGlobalConfig) {
		this.withGlobalConfig = withGlobalConfig;
	}

	

	

}

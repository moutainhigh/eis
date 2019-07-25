package com.maicard.common.domain;


import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maicard.common.domain.EisObject;

/**
 * 某个对象是否允许评论
 *
 *
 * @author NetSnake
 * @date 2016年4月23日
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentConfig extends EisObject{

	private static final long serialVersionUID = 6438383722224308401L;
	
	private long commentConfigId;		//主键
	
	//继承自父类的objectType		//评论类型
	
	private long objectId;		//评论的对象ID
	
	private int initStatus;		//针对某个或某种对象的评论初始值，如是直接通过还是作为草稿被审批
	
	private boolean unique;			//某个用户针对该对象是否只能评论一次
	
	private boolean canDelete;			//该用户能否删除自己的评论
	
	private boolean canEdit;			//该用户能否编辑自己的评论
	
	private String commentProcessor;	//自定义评论处理器
	/*
	
	private boolean subscribeOnly;		//该用户必须订购了该产品才能评论
	
    @JsonView({JsonFilterView.Partner.class})
	private String subscribeCheckBean;		//检查是否订购该产品的服务名称
	
    @JsonView({JsonFilterView.Partner.class})
	private String subscribeCheckMethod;		//检查是否订购该产品的服务方法，一般是count
	
    @JsonView({JsonFilterView.Partner.class})
	private String subscribeCheckParameter;		//检查是否订购该产品的参数
	
    @JsonView({JsonFilterView.Partner.class})
	private String subscribeCheckParameterClassName;		//检查是否订购该产品的参数类
*/	
	private Map<String,String> extraDataDefine;			//评论中允许用户添加的附加数据，比如产品的自拍图，则可以定义为一组<productGallery,gallery类型>
	
	public CommentConfig(){
		
	}
	
	public CommentConfig(long ownerId){
		this.ownerId = ownerId;
	}

	public long getCommentConfigId() {
		return commentConfigId;
	}

	public void setCommentConfigId(long commentConfigId) {
		this.commentConfigId = commentConfigId;
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

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"commentConfigId=" + "'" + commentConfigId + "'," + 
			"objectType=" + "'" + objectType + "'," + 
			"objectId=" + "'" + objectId + "'," + 
			"initStatus=" + "'" + initStatus + "'," + 
			"ownerId=" + "'" + ownerId + "'" + 
			")";
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}


	public Map<String, String> getExtraDataDefine() {
		return extraDataDefine;
	}

	public void setExtraDataDefine(Map<String, String> extraDataDefine) {
		this.extraDataDefine = extraDataDefine;
	}

	public String getCommentProcessor() {
		return commentProcessor;
	}

	public void setCommentProcessor(String commentProcessor) {
		this.commentProcessor = commentProcessor;
	}

}

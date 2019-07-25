package com.maicard.common.criteria;

import java.util.Date;

import com.maicard.common.base.Criteria;
import com.maicard.standard.CommonStandard;

public class CommentCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	public static final String CACHE_NAME = CommonStandard.cacheNameDocument;

	public static final int RANK_GOOD = 3;	
	public static final int RANK_NORMAL= 2;
	public static final int RANK_BAD = 1;
	
	public static final int STATUS_WAIT_EXAMINE = 141001;		//等待审批状态
	public static final int STATUS_PUBLISHED = 141002;		//已正常发布
	public static final int STATUS_HIDDEN = 141003;			//处于隐藏状态
	


	private long rootCommentId;		//第一条评论ID

	private long uuid;			//评论用户
	private String objectType;		//评论类型
	private long objectId;		//评论的对象ID
	private Date createTimeBegin;	//评论时间起始
	private Date createTimeEnd;		//评论时间结束	
	
	private String title;		//评论标题
	private String content;		//评论内容
	private int rank;			//评级
	private int rate;			//评价

	public CommentCriteria() {
	}

	public CommentCriteria(long ownerId) {
		this.ownerId = ownerId;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}



	public long getRootCommentId() {
		return rootCommentId;
	}



	public void setRootCommentId(long rootCommentId) {
		this.rootCommentId = rootCommentId;
	}



	public int getRank() {
		return rank;
	}



	public void setRank(int rank) {
		this.rank = rank;
	}

	public Date getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	

}

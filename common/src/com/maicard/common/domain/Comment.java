package com.maicard.common.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maicard.annotation.NeedJmsDataSyncP2P;
import com.maicard.standard.KeyConstants;

/**
 * 用户评论
 *
 *
 * @author NetSnake
 * @date 2016年4月19日
 *
 */

@NeedJmsDataSyncP2P
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment extends EVEisObject implements Cloneable{
	
	

	private static final long serialVersionUID = 6438383722224308401L;
	private long commentId;		//主键
	private long relatedCommentId;	//关联的评论
	private long rootCommentId;		//第一条评论ID
	private long uuid;			//评论用户
	//继承自父类的objectType		//评论类型
	private long objectId;		//评论的对象ID
	private Date createTime;	//评论创建时间
	private Date publishTime;	//评论发布时间
	private String title;		//评论标题
	private String content;		//评论内容
	private int rank;			//评级，如好评、中评、差评
	private int rate;			//评分
	private int readCount;		//阅读次数
	private int replyCount;		//跟帖数
	private int praiseCount;	//点赞数
	
	private long commentConfigId;			//评论对应的评论配置ID

	
	private HashMap<String,String> data;	//其他数据

	
	public Comment(){	
	}
	
	@Override
	public Comment clone() {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(this);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);

			return (Comment)in.readObject();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	public void applyCommentConfig(CommentConfig commentConfig){
		this.currentStatus = commentConfig.getInitStatus();
		if(this.data == null){
			this.data = new HashMap<String,String>();
		}
		if(commentConfig.isUnique()){
			this.data.put("unique", "true");
		}
		if(commentConfig.isCanDelete()){
			this.data.put("canDelete","true");
		}
	}
	
	public Comment(long ownerId){
		this.ownerId = ownerId;
	}
	
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	
	public long getCommentId() {
		return commentId;
	}
	public void setCommentId(long commentId) {
		this.commentId = commentId;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	public long getRelatedCommentId() {
		return relatedCommentId;
	}
	public void setRelatedCommentId(long relatedCommentId) {
		this.relatedCommentId = relatedCommentId;
	}
	public int getReadCount() {
		return readCount;
	}
	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}
	public int getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
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

	
	
	public long getCommentConfigId() {
		return commentConfigId;
	}

	public void setCommentConfigId(long commentConfigId) {
		this.commentConfigId = commentConfigId;
	}
	
	

	public int getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	public String getLockKey(){
		return  KeyConstants.COMMENT_LOCK_PREFIX + "#" + this.getObjectType() + "#" + this.getObjectId() + "#" + this.getUuid();
	}
}

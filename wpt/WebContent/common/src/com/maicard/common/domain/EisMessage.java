package com.maicard.common.domain;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.maicard.standard.MessageStandard.MessageLevel;

/**
 * 系统消息
 * @author NetSnake
 * @date 2012-4-23
 */
@JsonInclude(Include.NON_EMPTY)
public class EisMessage extends JmsObject{

	private static final long serialVersionUID = -5801454686645571501L;

	@JsonIgnore
	private String messageLevel = MessageLevel.system.getCode();

	private String messageId;
	private String replyMessageId;
	private Integer topicId = 0;
	private Integer senderId = 0;
	private Integer receiverId = 0;
	private String title;
	private String content;
	private String message;
	private Date sendTime;
	private Date receiveTime;
	private Date validTime;
	private Integer currentStatus = 0;
	private String objectType;
	private Integer operateCode = 0;
	private Integer operateResult = 0;
	private long timestamp = 0;

	@JsonIgnore
	private boolean needReply;

	private String[] perferMethod; //首选优先发送方式


	private Integer id = 0;
	private HashMap<String,Object> attachment;

	public EisMessage(){
		//this.messageId = UUID.randomUUID().toString();
	}
	
	public EisMessage(int operateCode){
		this.operateCode = operateCode;
	}

	public EisMessage(int operateCode, String message){
		this.operateCode = operateCode;
		this.message = message;
	}

	public EisMessage(int operateCode, String message, String content){
		this.operateCode = operateCode;
		this.message = message;
		this.content = content;
	}

	public EisMessage(String messageLevel, String objectType, int operateCode){
		this.messageLevel = messageLevel;
		this.objectType = objectType;
		this.operateCode = operateCode;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"operateCode=" + "'" + operateCode + "'," + 
				"message=" + "'" + message + "'," + 
				")";
	}


	public Integer getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(Integer currentStatus) {
		this.currentStatus = currentStatus;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSenderId() {
		return senderId;
	}
	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}
	public Integer getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}
	public HashMap<String, Object> getAttachment() {
		if(attachment == null){
			attachment = new HashMap<String,Object>();
		}
		return attachment;
	}
	public void setAttachment(HashMap<String, Object> attachment) {
		this.attachment = attachment;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public Date getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	public Integer getTopicId() {
		return topicId;
	}
	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}
	public Date getValidTime() {
		return validTime;
	}
	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}
	public Integer getOperateCode() {
		return operateCode;
	}
	public void setOperateCode(Integer operateCode) {
		this.operateCode = operateCode;
	}
	public String getMessageLevel() {
		return messageLevel;
	}
	public void setMessageLevel(String messageLevel) {
		this.messageLevel = messageLevel == null ? null : messageLevel.trim();
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId == null ? null : messageId.trim();
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType == null ? null : objectType.trim();
	}

	public boolean isNeedReply() {
		return needReply;
	}

	public void setNeedReply(boolean needReply) {
		this.needReply = needReply;
	}

	public String[] getPerferMethod() {
		return perferMethod;
	}

	public void setPerferMethod(String[] perferMethod) {
		this.perferMethod = perferMethod;
	}

	public String getReplyMessageId() {
		return replyMessageId;
	}

	public void setReplyMessageId(String replyMessageId) {
		this.replyMessageId = replyMessageId == null ? null : replyMessageId.trim();
	}

	public Integer getOperateResult() {
		return operateResult;
	}

	public void setOperateResult(Integer operateResult) {
		this.operateResult = operateResult;
	}

	@SuppressWarnings("unchecked")
	public <T>T getAttachmentData(String key) {
		if(this.attachment == null || this.attachment.size() < 1){
			return null;
		}
		if(this.attachment.get(key) == null){
			return null;
		}
		return (T) this.attachment.get(key);
	}

	public void setAttachmentData(String key, Object value) {
		if(this.attachment == null){
			this.attachment = new HashMap<String,Object>();
		}		
		this.attachment.put(key, value);
	}
}

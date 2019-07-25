package com.maicard.mb.criteria;

import java.util.Date;

import com.maicard.common.base.Criteria;

public class MessageCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private String messageId;
	private String messageType;
	private String messageExtraType;
	private long receiverId;
	private long senderId;
	private String originalType;
	private Date sendTimeBegin;

	private Date sendTimeEnd;
	private String identify;
	
	private int[] senderStatus;
	private int[] receiverStatus;

	private long inviter;


	public MessageCriteria() {
	}
	

	public MessageCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(long receiverId) {
		this.receiverId = receiverId;
	}

	public long getSenderId() {
		return senderId;
	}

	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	public String getOriginalType() {
		return originalType;
	}

	public void setOriginalType(String originalType) {
		if(originalType == null || originalType.trim().equals("")){
			return;
		}
		this.originalType = originalType;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		if(identify == null || identify.trim().equals("")){
			return;
		}
		this.identify = identify;
	}

	public Date getSendTimeBegin() {
		return sendTimeBegin;
	}

	public void setSendTimeBegin(Date sendTimeBegin) {
		this.sendTimeBegin = sendTimeBegin;
	}

	public Date getSendTimeEnd() {
		return sendTimeEnd;
	}

	public void setSendTimeEnd(Date sendTimeEnd) {
		this.sendTimeEnd = sendTimeEnd;
	}

	public String getMessageExtraType() {
		return messageExtraType;
	}

	public void setMessageExtraType(String messageExtraType) {
		this.messageExtraType = messageExtraType;
	}

	public long getInviter() {
		return inviter;
	}

	public void setInviter(long inviter) {
		this.inviter = inviter;
	}

	public int[] getSenderStatus() {
		return senderStatus;
	}

	public void setSenderStatus(int... senderStatus) {
		this.senderStatus = senderStatus;
	}

	public int[] getReceiverStatus() {
		return receiverStatus;
	}

	public void setReceiverStatus(int... receiverStatus) {
		this.receiverStatus = receiverStatus;
	}


}

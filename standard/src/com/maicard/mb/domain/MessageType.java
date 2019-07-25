package com.maicard.mb.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maicard.common.domain.EisObject;

/**
 * 消息类型
 *
 * @author NetSnake
 * @date 2015-11-03
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageType extends EisObject{
	

	private static final long serialVersionUID = 4899700750238749755L;

	private int messageTypeId;
	private String messageTypeName;
	private String messageTypeDescription;
	private int charLimit = 0;			//一条信息最多的字数
	private String templateContent;
	private String processClass;		//发送该信息的类
	
	
	public int getCharLimit() {
		return charLimit;
	}
	public void setCharLimit(int charLimit) {
		this.charLimit = charLimit;
	}
	public String getTemplateContent() {
		return templateContent;
	}
	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}
	
	public String getProcessClass() {
		return processClass;
	}
	public void setProcessClass(String processClass) {
		this.processClass = processClass;
	}
	public int getMessageTypeId() {
		return messageTypeId;
	}
	public void setMessageTypeId(int messageTypeId) {
		this.messageTypeId = messageTypeId;
	}
	public String getMessageTypeName() {
		return messageTypeName;
	}
	public void setMessageTypeName(String messageTypeName) {
		this.messageTypeName = messageTypeName;
	}
	public String getMessageTypeDescription() {
		return messageTypeDescription;
	}
	public void setMessageTypeDescription(String messageTypeDescription) {
		this.messageTypeDescription = messageTypeDescription;
	}
	

	
	
}

package com.maicard.wpt.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.common.domain.EVEisObject;
import com.maicard.views.JsonFilterView;

/**
 * 用于确定微信用户的自动应答
 *可以根据应答类型和ID，去获取某一个富媒体消息
 *
 * @author NetSnake
 * @date 2016年1月14日
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoResponseModel extends EVEisObject  {

	
	private static final long serialVersionUID = -5890727473391214423L;


	@JsonView({JsonFilterView.Full.class})
	private long autoResponseModelId;			//PK
		
	
    @JsonView({JsonFilterView.Partner.class})
	private String question;		//问题
	
	private Object response;		//应答
	
	private String responseType;	//应答的类型
	
	private long responseId;		//应答的对应的ID
	

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public long getResponseId() {
		return responseId;
	}

	public void setResponseId(long responseId) {
		this.responseId = responseId;
	}

	public long getAutoResponseModelId() {
		return autoResponseModelId;
	}

	public void setAutoResponseModelId(long autoResponseModelId) {
		this.autoResponseModelId = autoResponseModelId;
	}

	
	
	

	
}

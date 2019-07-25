package com.maicard.wpt.criteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.common.base.Criteria;
import com.maicard.views.JsonFilterView;


@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoResponseModelCriteria extends Criteria  {



	private static final long serialVersionUID = -2100770793093064346L;


	@JsonView({JsonFilterView.Partner.class})
	private String question;		//问题

	private Object response;		//应答

	private String responseType;	//应答的类型

	private long responseId;		//应答的对应的ID

	public AutoResponseModelCriteria() {
	}

	public AutoResponseModelCriteria(long ownerId) {
		this.ownerId = ownerId;
	}

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


}

package com.maicard.site.criteria;

import com.maicard.common.base.Criteria;

public class TemplateCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int templateId;
	
	public TemplateCriteria() {
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

}

package com.maicard.site.domain;

import com.maicard.common.domain.EisObject;

public class Template extends  EisObject implements Cloneable {

	private static final long serialVersionUID = 1L;

	private int templateId;

	private String templateName;

	private int suggestLevel;

	private int languageId;

	private int templateSuiteId;

	private String templateLocation;

	private String previewUrl;

	private String previewPicUrl;


	//非持久性属性	
	private String languageName;


	public Template() {
	}

	@Override
	public Template clone(){
		return (Template)super.clone();
	}


	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public int getSuggestLevel() {
		return suggestLevel;
	}

	public void setSuggestLevel(int suggestLevel) {
		this.suggestLevel = suggestLevel;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public int getTemplateSuiteId() {
		return templateSuiteId;
	}

	public void setTemplateSuiteId(int templateSuiteId) {
		this.templateSuiteId = templateSuiteId;
	}

	public String getTemplateLocation() {
		return templateLocation;
	}

	public void setTemplateLocation(String templateLocation) {
		this.templateLocation = templateLocation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + templateId;

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Template other = (Template) obj;
		if (templateId != other.templateId)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"templateId=" + "'" + templateId + "'," + 
				"templateName=" + "'" + templateName + "'," + 
				"currentStatus=" + "'" + currentStatus + "'," + 
				"ownerId=" + "'" + ownerId + "'" + 
				")";
	}



	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	public String getPreviewPicUrl() {
		return previewPicUrl;
	}

	public void setPreviewPicUrl(String previewPicUrl) {
		this.previewPicUrl = previewPicUrl;
	}

}

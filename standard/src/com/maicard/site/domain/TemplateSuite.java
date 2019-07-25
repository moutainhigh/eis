package com.maicard.site.domain;

import com.maicard.common.domain.EisObject;

/**
 * 模版组
 * 用于将多个模版组合为一个模版套装供用户选择
 *
 *
 * @author NetSnake
 * @date 2015年11月8日
 *
 */
public class TemplateSuite extends  EisObject {


	private static final long serialVersionUID = 5831018058880810565L;

	private int templateSuiteId;

	private String templateSuiteName;

	private String templateSuiteDesc;

	private int languageId;

	private String templateSuffix;

	
	//非持久性属性	
	private String languageName;


	public TemplateSuite() {
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"templateSuiteId=" + "'" + templateSuiteId + "'" + 
			")";
	}

	

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public int getTemplateSuiteId() {
		return templateSuiteId;
	}

	public void setTemplateSuiteId(int templateSuiteId) {
		this.templateSuiteId = templateSuiteId;
	}

	public String getTemplateSuiteName() {
		return templateSuiteName;
	}

	public void setTemplateSuiteName(String templateSuiteName) {
		this.templateSuiteName = templateSuiteName;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public String getTemplateSuffix() {
		return templateSuffix;
	}

	public void setTemplateSuffix(String templateSuffix) {
		this.templateSuffix = templateSuffix;
	}

	public String getTemplateSuiteDesc() {
		return templateSuiteDesc;
	}

	public void setTemplateSuiteDesc(String templateSuiteDesc) {
		this.templateSuiteDesc = templateSuiteDesc;
	}
	
}

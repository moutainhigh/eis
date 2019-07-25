package com.maicard.common.domain;

public class Language extends EisObject{

	private static final long serialVersionUID = 1L;

	private Integer languageId;

	private String languageName;

	private String languageDescription;

	
	public Language() {
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName == null ? null : languageName.trim();
	}

	public String getLanguageDescription() {
		return languageDescription;
	}

	public void setLanguageDescription(String languageDescription) {
		this.languageDescription = languageDescription == null ? null : languageDescription.trim();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + languageId;

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
		final Language other = (Language) obj;
		if (languageId != other.languageId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"languageId=" + "'" + languageId + "'" + 
			")";
	}

	
	
}

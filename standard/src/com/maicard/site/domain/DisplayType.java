package com.maicard.site.domain;

import com.maicard.common.domain.EisObject;

public class DisplayType extends EisObject {

	private static final long serialVersionUID = 1L;

	private int displayTypeId;

	private String displayTypeName;

	private String displayTypeDescription;


	private int flag;
	
	
		
	public DisplayType() {
	}

	public int getDisplayTypeId() {
		return displayTypeId;
	}

	public void setDisplayTypeId(int displayTypeId) {
		this.displayTypeId = displayTypeId;
	}

	public String getDisplayTypeName() {
		return displayTypeName;
	}

	public void setDisplayTypeName(String displayTypeName) {
		this.displayTypeName = displayTypeName;
	}

	public String getDisplayTypeDescription() {
		return displayTypeDescription;
	}

	public void setDisplayTypeDescription(String displayTypeDescription) {
		this.displayTypeDescription = displayTypeDescription;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + displayTypeId;

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
		final DisplayType other = (DisplayType) obj;
		if (displayTypeId != other.displayTypeId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"displayTypeId=" + "'" + displayTypeId + "'" + 
			")";
	}


	public void setId(int id) {
		this.id = id;
	}

	

	

}

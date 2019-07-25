package com.maicard.common.domain;


public class GlobalUnique extends EisObject {

	private static final long serialVersionUID = 1L;

	private String data;
	private boolean needSave;

	public GlobalUnique() {
	}
	
	public GlobalUnique(String data, long ownerId) {
		this.data = data;
		this.ownerId = ownerId;
		this.needSave = true;
	}
	
	public GlobalUnique(String data, long ownerId, boolean needSave) {
		this.data = data;
		this.ownerId = ownerId;
		this.needSave = needSave;
	}
	
	
	

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data == null ? null : data.trim();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());

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
		final GlobalUnique other = (GlobalUnique) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"data=" + "'" + data + "'," + 
			"ownerId=" + "'" + ownerId + "'" + 
			")";
	}

	public boolean isNeedSave() {
		return needSave;
	}

	public void setNeedSave(boolean needSave) {
		this.needSave = needSave;
	}

	
}

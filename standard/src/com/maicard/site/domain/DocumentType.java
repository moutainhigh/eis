package com.maicard.site.domain;

import java.util.HashMap;

import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EVEisObject;

public class DocumentType extends EVEisObject {

	private static final long serialVersionUID = 1L;

	private int documentTypeId;
	
	private String documentTypeCode;

	private String documentTypeName;

	private String documentTypeDescription;
	
	private int flag;
	

	//非持久化属性	
	private HashMap<String,DataDefine> dataDefineMap;
		
	
	private String statusName;

	public DocumentType() {
	}

	public int getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(int documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getDocumentTypeName() {
		return documentTypeName;
	}

	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}

	public String getDocumentTypeDescription() {
		return documentTypeDescription;
	}

	public void setDocumentTypeDescription(String documentTypeDescription) {
		this.documentTypeDescription = documentTypeDescription;
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
		result = prime * result + documentTypeId;

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
		final DocumentType other = (DocumentType) obj;
		if (documentTypeId != other.documentTypeId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"documentTypeId=" + "'" + documentTypeId + "'" + 
			")";
	}
	
	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public HashMap<String, DataDefine> getDataDefineMap() {
		return dataDefineMap;
	}

	public void setDataDefineMap(
			HashMap<String, DataDefine> documentDataDefineMap) {
		this.dataDefineMap = documentDataDefineMap;
	}

	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

}

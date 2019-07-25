package com.maicard.site.criteria;

import com.maicard.common.base.Criteria;

public class DocumentTypeCriteria extends Criteria {

	private static final long serialVersionUID = 1L;

	private int documentTypeId;
	private String documentTypeCode;

	public DocumentTypeCriteria() {
	}

	public int getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(int documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		if(documentTypeCode != null && !documentTypeCode.trim().equals("")){
			this.documentTypeCode = documentTypeCode;
		}
	}

}

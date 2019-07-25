package com.maicard.site.criteria;

import com.maicard.common.base.Criteria;

public class DocumentDataCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int documentDataId;
	
	private int udid;
	
	private int dataDefineId;
			
	private String content;
	
	private int documentTypeId;
	
	private String dataValue;
	
	private int anonymousReturnDocumentDataCutCount;
	
	public DocumentDataCriteria() {
	}

	public int getDocumentDataId() {
		return documentDataId;
	}

	public void setDocumentDataId(int documentDataId) {
		this.documentDataId = documentDataId;
	}

	public int getUdid() {
		return udid;
	}

	public void setUdid(int udid) {
		this.udid = udid;
	}

	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(int documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public int getDataDefineId() {
		return dataDefineId;
	}

	public void setDataDefineId(int dataDefineId) {
		this.dataDefineId = dataDefineId;
	}

	public int getAnonymousReturnDocumentDataCutCount() {
		return anonymousReturnDocumentDataCutCount;
	}

	public void setAnonymousReturnDocumentDataCutCount(
			int anonymousReturnDocumentDataCutCount) {
		this.anonymousReturnDocumentDataCutCount = anonymousReturnDocumentDataCutCount;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

}

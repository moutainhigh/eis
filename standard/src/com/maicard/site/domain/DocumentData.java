package com.maicard.site.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonView;
import com.maicard.common.domain.ExtraData;
import com.maicard.standard.BasicStatus;
import com.maicard.views.JsonFilterView;

public class DocumentData  extends ExtraData  implements Serializable{

	private static final long serialVersionUID = 1L;


	private int documentDataId;

	private int udid;

	private String dataValue;

    @JsonView({JsonFilterView.Full.class})
	private int index;

	
	public DocumentData(){}
	
	public DocumentData(String dataCode, String dataValue) {
		this.dataCode = dataCode;
		this.dataValue = dataValue;
		this.currentStatus = BasicStatus.dynamic.getId();
	}

	@Override
	public DocumentData clone(){
		return (DocumentData)super.clone();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + documentDataId;

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
		final DocumentData other = (DocumentData) obj;
		if (documentDataId != other.documentDataId)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"documentDataId=" + "'" + documentDataId + "'" + 
			"dataDefineId=" + "'" + dataDefineId + "'" + 
			"dataCode=" + "'" + dataCode + "'" + 
			"dataValue=" + "'" + dataValue + "'" + 
			")";
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

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		if(dataValue != null && !dataValue.trim().equals("")){
			this.dataValue = dataValue.trim().replaceAll("\t", "");

		}
	}



}

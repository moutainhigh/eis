package com.maicard.site.domain;

import java.io.Serializable;

public class DocumentNodeRelation implements Serializable {

	private static final long serialVersionUID = 1L;

	private int documentNodeRelationId;

	private int udid;

	private int nodeId;

	private int currentStatus;

	//非持久化属性	
	private int id;
	private int index;
	private String statusName;
	
	public DocumentNodeRelation() {
	}

	public int getDocumentNodeRelationId() {
		return documentNodeRelationId;
	}

	public void setDocumentNodeRelationId(int documentNodeRelationId) {
		this.documentNodeRelationId = documentNodeRelationId;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public int getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(int currentStatus) {
		this.currentStatus = currentStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + documentNodeRelationId;

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
		final DocumentNodeRelation other = (DocumentNodeRelation) obj;
		if (documentNodeRelationId != other.documentNodeRelationId)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"documentNodeRelationId=" + "'" + documentNodeRelationId + "'" + 
			")";
	}

	public int getUdid() {
		return udid;
	}

	public void setUdid(int udid) {
		this.udid = udid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
}

package com.maicard.site.criteria;

import com.maicard.common.base.Criteria;

public class DocumentNodeRelationCriteria extends Criteria {

	private static final long serialVersionUID = 1L;
	
	private int documentNodeRelationId;
	
	private int udid;
	
	private int nodeId;
	
	public DocumentNodeRelationCriteria() {
	}

	public int getDocumentNodeRelationId() {
		return documentNodeRelationId;
	}

	public void setDocumentNodeRelationId(int documentNodeRelationId) {
		this.documentNodeRelationId = documentNodeRelationId;
	}

	public int getUdid() {
		return udid;
	}

	public void setUdid(int udid) {
		this.udid = udid;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

}

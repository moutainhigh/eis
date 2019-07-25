package com.maicard.flow.domain;

import java.util.Date;
import java.util.List;

import com.maicard.common.domain.EisObject;

public class WorkflowInstance extends EisObject {

	private static final long serialVersionUID = 1L;

	private int workflowInstanceId = 0;

	private int workflowId = 0;

	private int currentStep = 0;

	private String targetObjectType;

	private int targetObjectTypeId;


	private long objectId = 0;

	private Date startTime;

	private Date endTime;

	////////////////////////////
	private List<Route> routeList;

	private Route previewsRoute;
	private List<Route> nextRouteList;

	public WorkflowInstance() {
	}

	public WorkflowInstance(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getWorkflowInstanceId() {
		return workflowInstanceId;
	}

	public void setWorkflowInstanceId(int workflowInstanceId) {
		this.workflowInstanceId = workflowInstanceId;
	}

	public int getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(int workflowId) {
		this.workflowId = workflowId;
	}

	public String getTargetObjectType() {
		return targetObjectType;
	}

	public void setTargetObjectType(String targetObjectType) {
		this.targetObjectType = targetObjectType == null ? null : targetObjectType.trim();
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + workflowInstanceId;

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
		final WorkflowInstance other = (WorkflowInstance) obj;
		if (workflowInstanceId != other.workflowInstanceId)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
				"(" + 
				"workflowInstanceId=" + "'" + workflowInstanceId + "'" + 
				",workflowId=" + "'" + workflowId + "'" +
				",currentStep=" + "'" + currentStep + "'" + 
				",object=" + "'" + objectType + ":" + objectId + "'" +
				",currentStatus=" + currentStatus + "'" +
				")";
	}

	public List<Route> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<Route> routeList) {
		this.routeList = routeList;
	}

	public Route getPreviewsRoute() {
		return previewsRoute;
	}

	public void setPreviewsRoute(Route previewsRoute) {
		this.previewsRoute = previewsRoute;
	}

	public List<Route> getNextRouteList() {
		return nextRouteList;
	}

	public void setNextRouteList(List<Route> nextRouteList) {
		this.nextRouteList = nextRouteList;
	}

	public int getTargetObjectTypeId() {
		return targetObjectTypeId;
	}

	public void setTargetObjectTypeId(int targetObjectTypeId) {
		this.targetObjectTypeId = targetObjectTypeId;
	}

	public Route getCurrentRoute() {
		if(this.routeList == null || this.routeList.size() < 1){
			return null;
		}
		if(this.currentStep < 1){
			return this.routeList.get(0);
		}
		for(Route r : this.routeList){
			if(r.getStep() == this.currentStep){
				return r;
			}
		}
		return null;
	}

	public int getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
	}

}

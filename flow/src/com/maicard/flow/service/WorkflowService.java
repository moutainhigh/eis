package com.maicard.flow.service;

import java.util.List;

import com.maicard.flow.criteria.WorkflowCriteria;
import com.maicard.flow.domain.Workflow;

public interface WorkflowService {

	int insert(Workflow workflow);

	int update(Workflow workflow);

	int delete(int workflowId);
	
	Workflow select(int workflowId);

	List<Workflow> list(WorkflowCriteria workflowCriteria);

	List<Workflow> listOnPage(WorkflowCriteria workflowCriteria);

	int count(WorkflowCriteria workflowCriteria);
	
	boolean requiredWorkflow(String objectType, String action, long ownerId);

}
